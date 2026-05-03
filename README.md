# 📋 Todo App

Konsolowy menedżer zadań napisany w Javie. Pozwala tworzyć, przeglądać, ukańczać i usuwać zadania — z automatycznym zapisem do pliku JSON.

## Wygląd

```
╔══════════════════════════════════════════════════════════╗
│ MENADZER ZADAN                                           │
╠══════════════════════════════════════════════════════════╣
│ 0     kupic samochod        MEDIUM     ○ TODO            │
│ 1     napisac raport        HIGH       ✓ DONE            │
╠══════════════════════════════════════════════════════════╣
│ [1] Dodaj [2] Ukoncz [3] Usun [0] Wyjscie                │
╚══════════════════════════════════════════════════════════╝
```

## Funkcje

- Dodawanie zadań z nazwą i poziomem ważności (LOW / MEDIUM / HIGH)
- Oznaczanie zadań jako ukończone
- Usuwanie zadań
- Automatyczny zapis do pliku `tasks.json`
- Dane zachowywane między uruchomieniami

## Uruchomienie

Wymagana Java 11+.

```bash
java -jar todo-app-jar-with-dependencies.jar
```

## Budowanie ze źródeł

```bash
git clone https://github.com/Marcin-P-Partyka/todo-app
cd todo-app
./mvnw package
java -jar target/todo-app-jar-with-dependencies.jar
```

## Technologie

- Java 17
- Maven
- Gson (zapis/odczyt JSON)

## Struktura projektu

```
src/main/java/todo/
├── Main.java
├── model/
│   ├── Task.java
│   └── Priority.java
├── service/
│   └── TaskManager.java
├── storage/
│   └── FileStorage.java
└── ui/
    └── ConsoleUI.java
```
