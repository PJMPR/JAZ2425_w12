# 5️⃣ Cache invalidation – najtrudniejszy problem

> "There are only two hard things in Computer Science: cache invalidation and naming things"

Ta część wykładu skupia się na **najtrudniejszym aspekcie cache** – utrzymaniu poprawności danych w czasie.

---

## 🧠 Dlaczego invalidation jest trudne?

Cache działa dobrze tylko wtedy, gdy:

* dane są **wystarczająco aktualne**,
* system zachowuje **przewidywalność**,
* nie pojawiają się trudne do debugowania błędy.

Problem polega na tym, że:

* dane w systemie **ciągle się zmieniają**,
* cache z definicji przechowuje **kopię** danych,
* im większy system, tym więcej miejsc do synchronizacji.

➡️ Invalidation to kompromis pomiędzy **wydajnością**, **spójnością** i **złożonością**.

---

## ❌ Typowe problemy

### 🕰️ Nieaktualne dane (stale cache)

* cache zwraca dane, które nie odzwierciedlają stanu źródła (np. DB),
* użytkownik widzi stare informacje,
* trudne do wykrycia w testach.

Przykłady:

* zmieniony profil użytkownika,
* zaktualizowana cena produktu,
* zmiana statusu zamówienia.

---

### 🧹 Nadmierne czyszczenie cache

* zbyt agresywne `@CacheEvict(allEntries = true)`,
* czyszczenie cache „na wszelki wypadek",
* nagłe skoki obciążenia DB (cache stampede).

➡️ Efekt: cache istnieje, ale **nie daje realnych korzyści**.

---

### 🔀 Niespójność między instancjami

Problem typowy dla:

* aplikacji skalowanych horyzontalnie,
* in-memory cache,
* środowisk chmurowych.

Objawy:

* jedna instancja ma świeże dane,
* inna nadal korzysta ze starego cache.

➡️ To jeden z głównych powodów użycia **distributed cache**.

---

## 🛠️ Strategie invalidacji cache

Nie istnieje jedno idealne rozwiązanie – strategie często się **łączy**.

---

### ⏳ TTL (Time To Live)

Najprostsza i najbezpieczniejsza strategia.

* dane wygasają po określonym czasie,
* brak potrzeby ręcznej invalidacji,
* przewidywalne zachowanie.

**Trade-off:**

* krótszy TTL → świeższe dane,
* dłuższy TTL → lepsza wydajność.

➡️ TTL jest często pierwszą linią obrony.

---

### 🧹 `@CacheEvict`

Ręczne usuwanie danych z cache w odpowiednich momentach.

Typowe użycie:

* `update`,
* `delete`,
* operacje modyfikujące stan.

Zalety:

* pełna kontrola,
* natychmiastowa spójność.

Wady:

* łatwo zapomnieć o evict,
* rosnąca złożoność wraz z systemem.

---

### 🔁 Cache-aside pattern

Najczęściej stosowany wzorzec pracy z cache.

Schemat:

1. aplikacja sprawdza cache,
2. jeśli brak danych → pobiera z DB,
3. zapisuje wynik do cache,
4. przy aktualizacji danych → usuwa cache.

Zalety:

* prostota,
* pełna kontrola po stronie aplikacji.

Wady:

* odpowiedzialność spoczywa na developerze.

➡️ Spring Cache domyślnie realizuje ten wzorzec.

---

### 📣 Event-driven invalidation

Invalidacja oparta o zdarzenia w systemie.

Przykłady:

* event po zapisie do DB,
* komunikaty Kafka / RabbitMQ,
* pub/sub w Redisie.

Zalety:

* dobra skalowalność,
* spójność w systemach rozproszonych.

Wady:

* większa złożoność,
* opóźnienia propagacji zdarzeń.

---

