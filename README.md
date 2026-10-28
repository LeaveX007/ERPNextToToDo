# ERPNextToToDo

Ein leichtgewichtiger Microservice auf Basis von **Kotlin** und **Ktor (CIO)**, der als Schnittstelle dient, um To-Do-Einträge entgegenzunehmen und automatisiert in eine **ERPNext**-Instanz über deren REST-API zu übertragen.

---

## Inhaltsverzeichnis

- [Überblick & Nutzen](#überblick--nutzen)
- [Technologie-Stack](#technologie-stack)
- [Voraussetzungen](#voraussetzungen)
- [Architektur & Funktionsweise](#architektur--funktionsweise)
- [Konfiguration](#konfiguration)
- [Starten & Ausführen](#starten--ausführen)
- [API-Dokumentation](#api-dokumentation)
- [Projektstruktur](#projektstruktur)

---

## Überblick & Nutzen

Das Projekt minimiert den Integrationsaufwand zwischen Drittsystemen und ERPNext, indem es einen einfachen Endpunkt bereitstellt:
- Empfängt ToDo-Objekte im JSON-Format.
- Transformiert und leitet die Anfragen über die ERPNext-REST-API (`/api/resource/ToDo`) weiter.
- Authentifiziert Anfragen mittels API-Token an der ERPNext-Instanz.

---

## Technologie-Stack

- **Programmiersprache:** Kotlin (JVM 21)
- **Framework:** Ktor Server & Client (CIO Engine)
- **Serialisierung:** `kotlinx.serialization` (JSON)
- **Logging:** Logback / SLF4J
- **Build-Tool:** Gradle (Kotlin DSL)

---

## Voraussetzungen

- **JDK 21** oder höher
- Eine laufende **ERPNext**-Instanz mit aktivierter REST-API und passendem API-Token
- **ERPNext**-Instanz horcht auf port 80
- **ERPNext**-Instanz url lautet: site2.local
> **Hinweis zur Sicherheit für ERPNext:** Für Produktivumgebungen ist zwingend https erforderlich.

---

## Architektur & Funktionsweise

```text
[ Client / HTTP Request ]
          │  POST / {"description": "..."}
          ▼
[ ERPNextToToDo Server ] (Port 8080)
          │  Routing.kt -> ApiAddService
          │  HTTP POST /api/resource/ToDo
          │  Authorization: token <API_KEY>:<API_SECRET>
          ▼
[ ERPNext Instanz ] (z.B. site2.local)
```

1. **Routing (`Routing.kt`)**: Horcht auf `POST /`, deserialisiert den Payload in ein `ToDo`-Datenmodell.
2. **API-Service (`ApiAddService.kt`)**: Setzt einen asynchronen HTTP-POST-Request gegen die ERPNext-Ressource ab.
3. **Antwort**: Gibt bei Erfolg HTTP 200 OK mit Bestätigungsmeldung an den Aufrufer zurück.

---

## Konfiguration

### Server-Port & Module
In `src/main/resources/application.yaml`:
```yaml
ktor:
  deployment:
    port: 8080
  application:
    modules:
      - RoutingKt.configureRouting
```

### ERPNext-Verbindungsdaten
Die Verbindungsparameter für ERPNext befinden sich in `src/main/kotlin/Routing.kt`:
- **Host:** `site2.local`
- **DocType:** `ToDo`
- **API-Token:** `228cc2946f6f330:e53dd148aca809c` (im Format `<api_key>:<api_secret>`)

> **Hinweis zur Sicherheit:** Für Produktivumgebungen empfiehlt es sich, Host und API-Tokens über Umgebungsvariablen oder die `application.yaml` einzuspeisen.

---

## Starten & Ausführen

### Mit Gradle Wrapper

```bash
# Projekt bauen
./gradlew build

# Server starten
./gradlew run

# Tests ausführen
./gradlew test
```

Unter Windows PowerShell / CMD:
```powershell
.\gradlew.bat run
```

Sobald der Server gestartet ist, ist er standardmäßig unter `http://localhost:8080` erreichbar.

---

## API-Dokumentation

### ToDo erstellen

- **Endpunkt:** `POST /`
- **Headers:** `Content-Type: application/json`

#### Request Body
```json
{
  "description": "Mein Youtube Todo"
}
```

#### Response
- **Status:** `200 OK`
- **Body:** `Todo successfully added.`

#### HTTP-Testdatei
Im Projekt liegt eine fertige Request-Datei unter `request/addToDo.http`, die direkt in IntelliJ IDEA / HTTP-Clients ausgeführt werden kann:
```http
POST http://127.0.0.1:8080/
Content-Type: application/json

{"description":"Mein krasses Youtube Todo"}
```

---

## Projektstruktur

```
.
├── build.gradle.kts                # Build- und Abhängigkeiten-Konfiguration
├── gradle/
│   └── libs.versions.toml          # Versionskatalog
├── request/
│   └── addToDo.http                # HTTP-Test-Requests
└── src/
    └── main/
        ├── kotlin/
        │   ├── main.kt             # Einstiegspunkt (EngineMain)
        │   ├── Routing.kt          # HTTP-Routing & Request-Handling
        │   ├── ToDo.kt             # Datenmodell
        │   └── ApiAddService.kt    # ERPNext REST-Client-Service
        └── resources/
            ├── application.yaml    # Server-Konfiguration
            └── logback.xml         # Logging-Konfiguration
```
