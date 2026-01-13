# 3️⃣ Spring Cache Abstraction

Ta część wykładu pokazuje **jak Spring realizuje cache od strony technicznej** oraz dlaczego Spring Cache Abstraction jest tak często wykorzystywana w aplikacjach produkcyjnych.

---

## 🧠 Idea Spring Cache Abstraction

Spring oferuje **warstwę abstrakcji**, która pozwala korzystać z cache w sposób:

* **deklaratywny**,
* **niezależny od implementacji**,
* **minimalnie inwazyjny dla kodu biznesowego**.

Dzięki temu:

* logika biznesowa nie „wie”, *jak* działa cache,
* można łatwo zmienić mechanizm cache bez zmian w kodzie,
* cache staje się elementem konfiguracji, a nie logiki domenowej.

---

## 🎯 Co daje Spring Cache?

Spring Cache Abstraction pozwala:

* **oddzielić logikę biznesową od mechanizmu cache**

  * brak `if (cache.containsKey())` w kodzie

* **łatwo wymieniać implementacje**

  * Caffeine → Redis → Ehcache
  * zmiana w konfiguracji, nie w serwisach

* **korzystać z deklaratywnego cache poprzez adnotacje**

  * czytelny i spójny kod

➡️ Cache staje się *cross-cutting concern*, podobnie jak transakcje czy security.

---

## 🧩 Architektura pod maską

Spring Cache opiera się na kilku kluczowych elementach:

### 🔧 CacheManager

* centralny punkt dostępu do cache,
* odpowiada za tworzenie i zarządzanie instancjami `Cache`,
* jest konfigurowany jako bean Springa.

Przykłady:

* `CaffeineCacheManager`
* `RedisCacheManager`
* `ConcurrentMapCacheManager`

---

### 📦 Cache

* reprezentuje **konkretny cache** (np. `users`, `products`),
* oferuje operacje `get`, `put`, `evict`,
* jest logicznym kontenerem na dane.

Aplikacja **nigdy nie powinna** korzystać z `Cache` bezpośrednio – robi to Spring.

---

### 🪄 Proxy + AOP

Spring Cache działa poprzez:

* generowanie proxy dla beanów,
* przechwytywanie wywołań metod,
* wykonanie logiki cache **przed i po** wywołaniu metody.

Schemat:

```
Request → Proxy → Cache → Metoda → Cache → Response
```

➡️ To dokładnie ten sam mechanizm, który stoi za `@Transactional`.

---

## 🏷️ Najważniejsze adnotacje

### `@EnableCaching`

* włącza obsługę cache w kontekście Springa,
* zazwyczaj umieszczana na klasie konfiguracyjnej lub `@SpringBootApplication`.

---

### `@Cacheable`

* sprawdza cache **przed** wykonaniem metody,
* jeśli dane są w cache → metoda nie jest wywoływana.

Przykładowe użycie:

* cache wyników zapytań,
* cache odczytów z API,
* cache kosztownych obliczeń.

---

### `@CachePut`

* **zawsze wykonuje metodę**,
* zapisuje wynik do cache.

Stosowana głównie przy:

* aktualizacji danych,
* synchronizacji cache z DB.

---

### `@CacheEvict`

* usuwa dane z cache,
* może usuwać pojedynczy wpis lub cały cache.

Najczęściej używana przy:

* operacjach `update`, `delete`.

---

### `@Caching`

* pozwala łączyć wiele operacji cache w jednej metodzie,
* przydatna przy bardziej złożonych scenariuszach.

---

## ⚠️ Ograniczenia, o których trzeba wiedzieć

Spring Cache ma kilka **krytycznych ograniczeń**, które bardzo często zaskakują na produkcji.

### 🔁 Self-invocation

* wywołanie metody z adnotacją cache **wewnątrz tej samej klasy**,
* proxy Springa nie jest wtedy używane,
* cache **nie zadziała**.

➡️ Rozwiązanie: wydziel logikę do osobnego beana.

---

### 🔓 Tylko metody `public`

* Spring proxy przechwytuje tylko metody publiczne,
* `protected` / `private` → brak cache.

---

### 🧱 Proxy-based mechanizm

* cache działa tylko na beanach zarządzanych przez Springa,
* bezpośrednie tworzenie obiektów (`new`) omija cache,

---

