# ig-clone-jsf

### Projekt na zajęcia **JPDSI2**, **AWA** - *Uniwersytet Śląski*
### Instagram clone; made for educational purposes
- UI based on 

### Funkcjonalności:
- **logowanie, rejestracja, edycja profilu**
- **role**: użytkownik, moderator
- **CRUD** postów
- dodawanie i usuwanie **polubień**
- dodawanie i usuwanie **komentarzy**
- **polecani** użytkownicy
- **wyszukiwanie** użytkowników
- **filtrowanie** postów po treści
- osobny "**feed**" dla strony głównej, użytkownika i panelu moderatora
- **upload zdjęć**
- **internacjonalizacja** *(języki: polski, angielski, francuski)*
- **walidacja** wszystkich danych wejściowych
- "**infinite scroll**" z paginacją (na głównej stronie)

## Zrzuty ekranu:

### Strona główna
<img src="https://github.com/azizko1337/ig-clone-jsf/blob/main/screenshots/index.png?raw=true" width="600">

### Logowanie
<img src="https://github.com/azizko1337/ig-clone-jsf/blob/main/screenshots/login.png?raw=true" width="600">

### Rejestracja
<img src="https://github.com/azizko1337/ig-clone-jsf/blob/main/screenshots/register.png?raw=true" width="600">

### Polubienia&komentarze (ajax)
<img src="https://github.com/azizko1337/ig-clone-jsf/blob/main/screenshots/post.png?raw=true" width="600">

### Dodawanie posta
<img src="https://github.com/azizko1337/ig-clone-jsf/blob/main/screenshots/addpost.png?raw=true" width="600">

## Stack:
### JSF2 + XHTML + CSS + JavaScript

## Schemat bazy danych:
<img src="https://raw.githubusercontent.com/azizko1337/ig-clone/refs/heads/main/db-scheme/ig-clone.svg" width="600">

## Plik bazy danych
<a target="_blank" href="https://github.com/azizko1337/ig-clone/tree/main/db-scheme">Link (analogiczny projekt w technologii PHP)</a>

## Deploment na serwerze `Wildfly 33.0.2 final`
### Konfiguracja bazy danych
- Import podanej wyżej bazy danych (`MariaDB`), nazwa bazy: `ig-clone`
- Dodanie `datasource` - `<datasource jndi-name="java:/ig-cloneDS" pool-name="ig-cloneDS">
                    <connection-url>jdbc:mariadb://localhost:3306/ig-clone</connection-url>
                    <driver-class>org.mariadb.jdbc.Driver</driver-class>
                    <driver>mariadb</driver>
                    <security user-name="root"/>
                    <validation>
                        <valid-connection-checker class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLValidConnectionChecker"/>
                        <validate-on-match>true</validate-on-match>
                        <exception-sorter class-name="org.jboss.jca.adapters.jdbc.extensions.mysql.MySQLExceptionSorter"/>
                    </validation>
                </datasource>` do `<datasources></datasources>` w `standalone.xml`
### Konfiguracja uploadu zdjęć
- Utworzenie folderu `ig-clone-uploads`, w głównym folderze `Wildfly`
- Dodanie `<file name="ig-clone-uploads" path="${jboss.home.dir}/ig-clone-uploads"/>` do `<handlers></handlers>` w pliku `standalone.xml`.
- Dodanie `<location name="/ig-clone-uploads" handler="ig-clone-uploads" />` do `<host name="default-host" alias="localhost"></host>`, również w pliku `standalone.xml`

### Konfiguracja projektu
- Ustawienie parametru kontekstowego `FILE_UPLOAD` w `web.xml`: `	<context-param>
		<param-name>UPLOAD_PATH</param-name>
		<param-value>A:/servers/wildfly-33.0.2.Final/ig-clone-uploads</param-value>
	</context-param>`. Ścieżka musi się zgadzać z ustawioną wyżej lokalizacją (tamta służy do odczytywania zdjęć, ta do zapisywania).
- Eksport projektu `EAR` do pliku `.war` i umiejscowienie go w podfolderze `/standalone/deployments`