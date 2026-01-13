# 8️⃣ Konfiguracja Redis Cache w Spring Boot (Docker)

Ten dokument pokazuje **jak uruchomić Redis w Dockerze** oraz jak skonfigurować **Spring Cache Abstraction** tak, aby używała **Redis jako distributed cache**.

Założenia:

* używasz `application.properties` (nie YAML),
* zależności są zarządzane przez **Gradle**,
* Redis działa lokalnie przez **docker-compose**.

---

## 🧩 Krok 1: docker-compose.yml (Redis)

W repozytorium dodaj plik `docker-compose.yml`:

```yaml
services:
  redis:
    image: redis:7-alpine
    container_name: redis-cache
    ports:
      - "6379:6379"
    command: ["redis-server", "--appendonly", "yes"]
    volumes:
      - redis_data:/data

volumes:
  redis_data:
```

Co to daje?

* Redis dostępny lokalnie na `localhost:6379`
* włączone AOF (`appendonly yes`) → dane mogą przetrwać restart kontenera
* wolumen `redis_data` trzyma dane na dysku hosta (Docker volume)

Uruchom:

```bash
docker compose up -d
```

Sprawdź logi:

```bash
docker compose logs -f redis
```

---

## 🧰 Krok 2: Zależności (Gradle)

Dodaj zależności do `build.gradle`:

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-cache'
    implementation 'org.springframework.boot:spring-boot-starter-data-redis'
}
```

Uwagi:

* Spring Boot użyje domyślnie klienta Redis (zwykle Lettuce).
* Nie musisz dodawać ręcznie zależności na klienta, jeśli starter je wciąga.

---

## ⚙️ Krok 3: Konfiguracja w `application.properties`

Minimalna konfiguracja:

```properties
# Cache
spring.cache.type=redis
spring.cache.cache-names=users,products

# Redis connection
spring.data.redis.host=localhost
spring.data.redis.port=6379

# (opcjonalnie) prefix dla kluczy cache
spring.cache.redis.key-prefix=demo::

# (opcjonalnie) nie zapisuj nulli
spring.cache.redis.cache-null-values=false
```

### TTL w Redis Cache

Najważniejsza różnica vs Caffeine:

* TTL najlepiej ustawiać **per-cache** w `RedisCacheConfiguration` (w kodzie),
* properties mają ograniczoną elastyczność.

➡️ Poniżej jest zalecany wariant: TTL w kodzie (czytelne i kontrolowane).

---

## 🧠 Krok 4: Włączenie cache

```java
@EnableCaching
@SpringBootApplication
public class Application {
}
```

---

## 🧱 Krok 5: RedisCacheManager z TTL (zalecane)

Dodaj konfigurację (np. `RedisCacheConfig.java`). Ten przykład:

* ustawia domyślne TTL,
* pokazuje różne TTL per cache,
* ustawia serializację JSON (czytelniejsze klucze/wartości i mniej problemów przy zmianach klas).

```java
import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class RedisCacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        var jsonSerializer = new GenericJackson2JsonRedisSerializer();

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer)
            )
            .disableCachingNullValues()
            .entryTtl(Duration.ofMinutes(10));

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(defaultConfig)
            .withCacheConfiguration(
                "users",
                defaultConfig.entryTtl(Duration.ofMinutes(5))
            )
            .withCacheConfiguration(
                "products",
                defaultConfig.entryTtl(Duration.ofMinutes(30))
            )
            .build();
    }
}
```

Dlaczego JSON?

* łatwiej debugować (możesz podejrzeć wartości w Redis),
* mniejsze ryzyko problemów po refactorach niż przy domyślnej serializacji JDK.

---

## 🏷️ Krok 6: Użycie `@Cacheable`, `@CacheEvict`

Tak samo jak w Caffeine — to jest siła Spring Cache Abstraction.

```java
@Cacheable(cacheNames = "users", key = "#id")
public User getUser(Long id) {
    return userRepository.findById(id).orElseThrow();
}

@CacheEvict(cacheNames = "users", key = "#id")
public void updateUser(Long id, UserUpdateDto dto) {
    // update w DB
}
```

---

## 🔍 Szybki test: czy cache działa?

### 1) Obserwuj czas odpowiedzi

* 1. wywołanie wolniejsze (miss)
* kolejne szybsze (hit)

### 2) Podejrzyj klucze w Redis

Wejdź do kontenera:

```bash
docker exec -it redis-cache redis-cli
```

Zobacz klucze:

```redis
KEYS *
```

Sprawdź TTL:

```redis
TTL demo::users::1
```

(Uwaga: format klucza zależy od prefixu i konfiguracji.)

---


