# 🧠 Cache w Java Spring 

Ten wykład jest praktycznym wprowadzeniem do **mechanizmów cache w aplikacjach Java opartych o Spring Framework / Spring Boot**. Jego celem jest pokazanie *dlaczego*, *kiedy* i *jak* stosować cache w realnych projektach – od prostych rozwiązań in-memory po cache rozproszony w środowiskach produkcyjnych.


---

### 1️⃣ Wprowadzenie – po co cache?

* Problemy wydajnościowe w aplikacjach backendowych
* Latencja: DB, API zewnętrzne, złożone obliczenia
* Cache jako trade-off: **pamięć vs spójność danych**

**Pojęcia kluczowe:**

* cache hit / miss
* TTL (Time To Live)
* eviction
* cold vs warm cache

---

### 2️⃣ Rodzaje cache

**Podział techniczny:**

* In-memory cache
* Distributed cache

**In-memory:**

* `ConcurrentHashMap`
* Caffeine
* Ehcache

**Distributed:**

* Redis
* Hazelcast
* Memcached

---

### 3️⃣ Spring Cache Abstraction

Spring oferuje **abstrakcję cache**, która pozwala:

* oddzielić logikę biznesową od mechanizmu cache,
* łatwo wymieniać implementacje (Caffeine → Redis),
* korzystać z deklaratywnego cache poprzez adnotacje.

**Kluczowe elementy:**

* `CacheManager`
* `Cache`
* Proxy + AOP

**Najważniejsze adnotacje:**

* `@EnableCaching`
* `@Cacheable`
* `@CachePut`
* `@CacheEvict`
* `@Caching`

**Ograniczenia, o których trzeba wiedzieć:**

* self-invocation (wywołania wewnątrz klasy)
* tylko metody `public`
* proxy-based mechanizm

---

### 4️⃣ Demo – Cacheable + Caffeine

Scenariusz demo:

* proste REST API w Spring Boot,
* metoda symulująca kosztowną operację (DB / `Thread.sleep`),
* porównanie czasu odpowiedzi **z cache i bez cache**.

Zakres demo:

* konfiguracja `CaffeineCacheManager`,
* TTL i limit rozmiaru cache,
* obserwacja cache hit / miss.

---

### 5️⃣ Cache invalidation – najtrudniejszy problem

> "There are only two hard things in Computer Science: cache invalidation and naming things"

**Problemy:**

* nieaktualne dane (stale cache),
* nadmierne czyszczenie cache,
* niespójność między instancjami.

**Strategie:**

* TTL
* `@CacheEvict`
* cache-aside
* event-driven invalidation

---

### 6️⃣ Distributed cache – Redis w Springu (ok. 10 min)

**Kiedy potrzebujesz Redis?**

* wiele instancji aplikacji,
* Kubernetes / autoscaling,
* stateless services.

Omówienie:

* `RedisCacheManager`
* serializacja danych (JSON vs JDK)
* podstawowe problemy produkcyjne (latencja, sieć, rozmiar danych)

---

