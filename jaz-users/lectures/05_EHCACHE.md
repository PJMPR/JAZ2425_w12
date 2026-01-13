# 6️⃣ Wstęp do Ehcache

Ta część wykładu wprowadza **Ehcache** jako klasyczne i dojrzałe rozwiązanie cache w ekosystemie Java oraz pokazuje, **gdzie Ehcache ma sens w porównaniu do Caffeine i Redis**.

---

## 🧠 Czym jest Ehcache?

**Ehcache** to:

* dojrzała biblioteka cache dla JVM,
* projekt rozwijany od wielu lat w ekosystemie Java,
* rozwiązanie często spotykane w starszych, ale też nadal utrzymywanych systemach.

Ehcache może działać jako:

* **in-memory cache**,
* **cache z persistencją na dysku**,
* (w wersjach enterprise) cache rozproszony.

➡️ Historycznie był jednym z *domyślnych wyborów* w świecie Springa.

---

## 🕰️ Dlaczego warto znać Ehcache?

* Ehcache występuje w **wielu istniejących systemach**,
* często jest zintegrowany z:

  * Hibernate (2nd level cache),
  * starszymi wersjami Springa,
* jego konfiguracja dobrze pokazuje **jak działa cache „od środka”**.

➡️ Znajomość Ehcache to duży plus przy pracy z legacy code.

---

## 🔧 Kluczowe cechy Ehcache

### ✅ TTL i eviction

Ehcache oferuje:

* TTL (time-to-live),
* TTI (time-to-idle),
* strategie eviction (LRU, LFU).

➡️ W przeciwieństwie do `ConcurrentMapCache`, są to mechanizmy **wbudowane**.

---

### 💾 Persistencja na dysku

Ehcache może:

* przechowywać dane na dysku,
* przetrwać restart aplikacji,
* działać jako cache „pół-trwały”.

To cecha, której **nie ma Caffeine**.

---

### 🧩 Integracja ze Spring Cache

Ehcache bardzo dobrze integruje się z:

* **Spring Cache Abstraction**,
* konfiguracją XML lub Java Config,
* cache opartym o nazwy (`cache names`).

Dla Springa Ehcache to po prostu kolejna implementacja `CacheManager`.

---

## ⚖️ Ehcache vs Caffeine vs Redis (wysoki poziom)

| Cecha        | Ehcache          | Caffeine  | Redis       |
| ------------ | ---------------- | --------- | ----------- |
| Typ          | In-memory / disk | In-memory | Distributed |
| TTL          | ✅                | ✅         | ✅           |
| Eviction     | ✅                | ✅         | ✅           |
| Persistencja | ✅                | ❌         | ✅           |
| Sieć         | ❌                | ❌         | ✅           |
| Złożoność    | średnia          | niska     | wysoka      |

➡️ Wybór zależy od **wymagań, a nie popularności**.

---

## 🚧 Kiedy Ehcache ma sens?

Ehcache jest dobrym wyborem, gdy:

* aplikacja działa na **pojedynczej instancji**,
* potrzebujesz **TTL + eviction + opcjonalnej persistencji**,
* pracujesz z istniejącym systemem,
* używasz Hibernate 2nd level cache.

Ehcache **nie jest najlepszym wyborem**, gdy:

* system jest skalowany horyzontalnie,
* wymagany jest wspólny cache między instancjami,
* potrzebujesz bardzo niskiej latencji (Caffeine).

---

