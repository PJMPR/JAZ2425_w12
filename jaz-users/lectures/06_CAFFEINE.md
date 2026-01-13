# 7️⃣ Konfiguracja Caffeine w Spring Boot

Ten dokument pokazuje **jak skonfigurować cache Caffeine w aplikacji Spring Boot**
---

## 🧩 Krok 1: Zależności (Gradle)

W pliku `build.gradle` (lub `build.gradle.kts`) dodaj zależności:

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-cache'
    implementation 'com.github.ben-manes.caffeine:caffeine'
}
```

* `spring-boot-starter-cache` – włącza Spring Cache Abstraction
* `caffeine` – implementacja cache in-memory

Spring Boot **automatycznie wykryje Caffeine** i skonfiguruje odpowiedni `CacheManager`.

---

## ⚙️ Krok 2: Konfiguracja w `application.properties`

Spring Boot pozwala skonfigurować Caffeine **bez pisania kodu**, używając właściwości aplikacji.

### Minimalna konfiguracja

```properties
spring.cache.type=caffeine
spring.cache.cache-names=users,products
spring.cache.caffeine.spec=maximumSize=1000,expireAfterWrite=10m
```

Znaczenie parametrów:

* `spring.cache.type=caffeine` – wymusza użycie Caffeine
* `spring.cache.cache-names` – lista logicznych nazw cache
* `maximumSize=1000` – maksymalna liczba wpisów w cache
* `expireAfterWrite=10m` – TTL (10 minut od zapisu)

➡️ To **najczęściej polecany sposób konfiguracji** dla Spring Boot.

---

## 📊 (Opcjonalnie) Statystyki cache

Podczas demo i testów warto włączyć statystyki:

```properties
spring.cache.caffeine.spec=maximumSize=1000,expireAfterWrite=10m,recordStats
```

Dzięki temu Caffeine będzie zbierać informacje o:

* cache hit / miss,
* evictions,
* rozmiarze cache.

---

## 🧠 Krok 3: Włączenie cache w aplikacji

Cache w Springu musi być jawnie włączony:

```java
@EnableCaching
@SpringBootApplication
public class Application {
}
```

Bez tej adnotacji **żadne adnotacje cache nie będą działać**.

---

## 🏷️ Krok 4: Użycie `@Cacheable`

Przykład użycia cache w warstwie serwisowej:

```java
@Cacheable(cacheNames = "users", key = "#id")
public User getUser(Long id) {
    simulateSlowOperation();
    return userRepository.findById(id).orElseThrow();
}
```

Zachowanie:

* pierwsze wywołanie → **cache miss** (metoda się wykonuje),
* kolejne wywołania → **cache hit** (metoda nie jest wywoływana).

---

## ⚠️ Ważne rzeczy, o których trzeba pamiętać

### 1️⃣ Zawsze ustaw limit rozmiaru

Brak `maximumSize` lub `maximumWeight` może prowadzić do:

* niekontrolowanego zużycia pamięci,
* problemów z GC,
* crashy aplikacji.

---

### 2️⃣ TTL to nie luksus, to konieczność

Cache bez TTL:

* wcześniej czy później zwróci nieaktualne dane,
* jest trudny w utrzymaniu.

---

### 3️⃣ Caffeine to cache lokalny

* działa tylko w obrębie jednej instancji JVM,
* przy wielu instancjach cache nie jest współdzielony.

➡️ Przy skalowaniu horyzontalnym potrzebujesz Redis lub innego distributed cache.

---
