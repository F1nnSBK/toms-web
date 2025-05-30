# Toms - Website

![Spring Boot Logo](https://img.shields.io/badge/Spring%20Boot-3.3.0-brightgreen?logo=spring-boot&style=for-the-badge)
![Java Version](https://img.shields.io/badge/Java-17+-blue?logo=openjdk&style=for-the-badge)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-E.g.%203.1-orange?logo=thymeleaf&style=for-the-badge)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-red?logo=spring-security&style=for-the-badge)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-purple?logo=bootstrap&style=for-the-badge)
![Font Awesome](https://img.shields.io/badge/Font%20Awesome-6.x-blueviolet?logo=fontawesome&style=for-the-badge)

---

## 🚀 Projektübersicht

Dieses Projekt ist eine einfache, aber robuste Webanwendung, die mit **Spring Boot** entwickelt wurde und auf **Thymeleaf** für das Frontend setzt. Es dient als praktische Übung und Showcase für grundlegende Webentwicklungskonzepte, insbesondere im Bereich **Benutzerauthentifizierung und -verwaltung** mit **Spring Security**.

Es beinhaltet ein einfaches **Item-Management** mit einem REST-API und einem darauf aufbauenden Admin-Panel.

### Kernfeatures:

* **Benutzerauthentifizierung:** Sichere Anmeldung und Abmeldung mit Spring Security (klassischer, serverseitiger Login-Flow).
* **Rollenbasierte Autorisierung:** Unterscheidung zwischen `USER` und `ADMIN`-Rollen für den Zugriff auf geschützte Bereiche.
* **Admin-Panel:** Ein geschützter Bereich für Administratoren.
    * **Item-Verwaltung:** CRUD-Operationen (Erstellen, Lesen, Aktualisieren, Löschen) für Items über eine Web-Oberfläche.
    * **Datei-Upload:** Eine Funktion zum sicheren Hochladen von Dateien (z.B. Bilder für Items) mit Spring Security Absicherung.
* **Öffentliche Seiten:** Grundlegende statische Seiten (Produkte, Kontakt, Über mich).
* **RESTful API:** Eine einfache API für den Zugriff auf Item-Daten.
* **Thymeleaf Frontend:** Dynamische HTML-Seiten für eine benutzerfreundliche Oberfläche.
* **Bootstrap 5:** Modernes, responsives Design.

## ✨ Technologien

* **Backend:**
    * Java 17+
    * Spring Boot [Dein Spring Boot Version, z.B. 3.2.0]
    * Spring Security [Deine Spring Security Version, z.B. 6.2.0]
    * Spring Data JPA (Optional, falls eine DB verwendet wird, sonst In-Memory-User/Data)
    * Maven [oder Gradle]
* **Frontend:**
    * Thymeleaf
    * HTML5, CSS3
    * Bootstrap 5.3
    * Font Awesome 6
* **Datenbank (optional):**
    * [z.B. H2 Database (für lokale Entwicklung/Tests) oder PostgreSQL/MySQL, etc.]

## 🚀 Erste Schritte

Diese Anleitung hilft dir, das Projekt lokal aufzusetzen und zu starten.

### Voraussetzungen

Stelle sicher, dass folgende Software auf deinem System installiert ist:

* Java Development Kit (JDK) 17 oder höher
* Maven [oder Gradle]
* Eine geeignete IDE (z.B. IntelliJ IDEA, VS Code, Eclipse)

### Installation

1.  **Repository klonen:**
    ```bash
    git clone [https://github.com/](https://github.com/)[DeinGitHubBenutzername]/[DeinRepositoryName].git
    cd [DeinRepositoryName]
    ```

2.  **Abhängigkeiten installieren:**
    * **Maven:**
        ```bash
        mvn clean install
        ```
    * **Gradle:**
        ```bash
        ./gradlew build
        ```

3.  **Anwendung starten:**
    * **Maven:**
        ```bash
        mvn spring-boot:run
        ```
    * **Gradle:**
        ```bash
        ./gradlew bootRun
        ```
    * **Aus der IDE:** Führe die `main`-Methode in der Hauptanwendungsklasse (z.B. `YourApplicationName.java`) aus.

Die Anwendung sollte nun auf `http://localhost:8080` verfügbar sein.

## 🔑 Anmeldeinformationen

Für die lokale Entwicklung sind Standardbenutzer in der `DevSecurityConfig` konfiguriert:

| Benutzername | Passwort | Rolle   |
| :----------- | :------- | :------ |
| `admin`      | `admin`  | `ADMIN` |
| `user`       | `password` | `USER`  |

* **Login-Seite:** `http://localhost:8080/login`
* **Admin-Bereich:** `http://localhost:8080/admin` (Zugriff nur für `ADMIN`)
* **Datei-Upload:** `http://localhost:8080/admin/upload` (Zugriff nur für `ADMIN`)

## 📄 Lizenz

Dieses Projekt steht unter der [MIT Lizenz](LICENSE).

---

## 📞 Kontakt

Dein Name / Dein GitHub-Profil
[Optional: Deine E-Mail-Adresse oder LinkedIn-Profil]

---