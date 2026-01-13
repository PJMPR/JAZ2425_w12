# 1️⃣ Wprowadzenie – po co cache?

Ta część wykładu ma na celu **zbudowanie intuicji**, dlaczego cache jest jednym z kluczowych mechanizmów w nowoczesnych aplikacjach backendowych oraz jakie problemy realnie rozwiązuje.

Czas trwania: **ok. 10 minut**

---

## 🚦 Problemy wydajnościowe w aplikacjach backendowych

Typowa aplikacja backendowa obsługuje wiele żądań, które:

* odczytują dane z bazy danych,
* komunikują się z zewnętrznymi API,
* wykonują kosztowne obliczenia.

Bez cache każde żądanie:

* przechodzi pełną ścieżkę logiczną,
* generuje obciążenie infrastruktury,
* zwiększa czas odpowiedzi dla użytkownika.

Wraz ze wzrostem ruchu (RPS/RPM) problemy te eskalują:

* wolniejsze odpowiedzi,
* większe koszty infrastruktury,
* ryzyko timeoutów i awarii kaskadowych.

---

## ⏱️ Latencja – główny wróg wydajności

Najczęstsze źródła opóźnień:

### 🗄️ Baza danych

* zapytania JOIN,
* brak indeksów,
* blokady i konkurencja,
* sieć (szczególnie w cloudzie).

### 🌐 Zewnętrzne API

* niekontrolowana latencja,
* limity (rate limits),
* błędy i retry,
* brak SLA.

### 🧮 Złożone obliczenia

* raporty,
* agregacje,
* algorytmy biznesowe,
* serializacja / deserializacja dużych obiektów.

➡️ **Cache pozwala ominąć te koszty dla powtarzalnych żądań.**

---

## ⚖️ Cache jako trade-off: pamięć vs spójność danych

Cache **nie jest darmowy** – wprowadza kompromisy.

### Co zyskujemy?

* krótszy czas odpowiedzi,
* mniejsze obciążenie DB i usług zewnętrznych,
* lepszą skalowalność aplikacji.

### Co tracimy?

* pełną aktualność danych,
* prostotę architektury,
* deterministyczność zachowania systemu.

➡️ Kluczowe pytanie brzmi:

> *Jak bardzo możemy pozwolić sobie na nieaktualne dane?*

Odpowiedź zależy od:

* domeny biznesowej,
* rodzaju danych,
* oczekiwań użytkownika.

---

## 🧩 Pojęcia kluczowe

### ✅ Cache hit / ❌ Cache miss

* **Cache hit** – dane zostały znalezione w cache i zwrócone bez wykonania kosztownej operacji.
* **Cache miss** – dane nie istnieją w cache, więc aplikacja musi je pobrać z oryginalnego źródła.

Wysoki *hit ratio* = dobrze zaprojektowany cache.

---

### ⏳ TTL (Time To Live)

TTL określa **jak długo dane mogą przebywać w cache**.

* po upływie TTL wpis jest usuwany lub uznawany za nieważny,
* krótszy TTL → lepsza aktualność,
* dłuższy TTL → lepsza wydajność.

TTL jest jednym z najprostszych i najbezpieczniejszych mechanizmów invalidacji cache.

---

### 🧹 Eviction

Eviction to proces **usuwania danych z cache**, który może nastąpić:

* po upływie TTL,
* po przekroczeniu limitu pamięci,
* ręcznie (np. po aktualizacji danych).

Popularne strategie:

* LRU (Least Recently Used)
* LFU (Least Frequently Used)
* FIFO

---

### ❄️ Cold cache vs 🔥 Warm cache

* **Cold cache** – cache jest pusty (np. po restarcie aplikacji).
* **Warm cache** – cache jest już wypełniony danymi.

Cold cache:

* wolniejszy start aplikacji,
* nagłe obciążenie DB.

Warm cache:

* stabilna wydajność,
* przewidywalne czasy odpowiedzi.

➡️ W systemach produkcyjnych często stosuje się *cache warming*.

---
