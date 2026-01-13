# 2️⃣ Rodzaje cache

Ta część wykładu porządkuje **rodzaje cache stosowane w aplikacjach backendowych** oraz pomaga zrozumieć, **kiedy i dlaczego wybrać konkretne rozwiązanie**.


---

## 🧩 Podział techniczny cache

Z perspektywy architektury aplikacji backendowych cache można podzielić na:

1. **In-memory cache**
2. **Distributed cache**
3. **Cache na poziomie HTTP / CDN** (krótkie omówienie)

Każdy z tych typów rozwiązuje **inne problemy** i ma **inne konsekwencje architektoniczne**.

---

## 🧠 In-memory cache

**In-memory cache** przechowuje dane w pamięci RAM **pojedynczej instancji aplikacji**.

### 🔧 Charakterystyka

* bardzo niska latencja (nanosekundy / mikrosekundy),
* brak komunikacji sieciowej,
* cache żyje razem z instancją aplikacji,
* utrata danych przy restarcie aplikacji.

### 🧰 Przykłady implementacji

* `ConcurrentHashMap` (najprostsze rozwiązanie)
* **Caffeine** (najczęściej rekomendowane w Spring)
* Ehcache

### ✅ Zalety

* najwyższa wydajność,
* prosta konfiguracja,
* brak zależności zewnętrznych,
* idealny do lokalnych optymalizacji.

### ❌ Wady

* brak współdzielenia danych między instancjami,
* problemy przy skalowaniu horyzontalnym,
* każda instancja ma własny cache.

### 📌 Kiedy stosować?

* aplikacja działa na jednej instancji,
* dane są read-heavy i mogą być lokalne,
* cache pełni rolę optymalizacji, a nie źródła prawdy.

---

## 🌐 Distributed cache

**Distributed cache** jest współdzielony pomiędzy wieloma instancjami aplikacji i działa jako **zewnętrzna usługa**.

### 🔧 Charakterystyka

* dostęp przez sieć,
* wspólny cache dla całego klastra,
* niezależny cykl życia od aplikacji,
* możliwość replikacji i wysokiej dostępności.

### 🧰 Przykłady implementacji

* **Redis** (najczęstszy wybór)
* Hazelcast
* Memcached

### ✅ Zalety

* spójny cache dla wielu instancji,
* dobre wsparcie dla skalowania,
* możliwość centralnego zarządzania TTL i eviction,
* często używany w Kubernetes.

### ❌ Wady

* dodatkowa latencja sieciowa,
* koszty utrzymania infrastruktury,
* serializacja / deserializacja danych,
* większa złożoność operacyjna.

### 📌 Kiedy stosować?

* aplikacja jest skalowana horyzontalnie,
* wiele instancji musi widzieć te same dane,
* cache jest elementem architektury systemu.

---

## 🔄 Porównanie: In-memory vs Distributed

| Cecha      | In-memory    | Distributed      |
| ---------- | ------------ | ---------------- |
| Latencja   | bardzo niska | niska            |
| Skalowanie | słabe        | dobre            |
| Spójność   | lokalna      | globalna         |
| Złożoność  | niska        | średnia / wysoka |
| Koszt      | niski        | wyższy           |

➡️ **Najczęstszy błąd:** wybór distributed cache tam, gdzie wystarczy in-memory.

---

