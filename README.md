# Adventure Book Backend

A REST API for interactive "Choose Your Own Adventure" books. Players browse a library of adventure books, start a game, make choices at each section, and face consequences that affect their health.

## Prerequisites

- **Java 17** or higher
- **Maven 3.9+** (or use the included Maven wrapper)

## Build

```bash
./mvnw clean package
```

To skip tests:

```bash
./mvnw clean package -DskipTests
```

## Run

```bash
./mvnw spring-boot:run
```

The application starts on **http://localhost:8080**.

## Database

The project uses an **H2 in-memory database** that resets on every restart. Three sample books are loaded automatically on startup from `src/main/resources/books/`.

The H2 console is available at **http://localhost:8080/h2-console** with:

| Setting  | Value                    |
|----------|--------------------------|
| JDBC URL | `jdbc:h2:mem:adventuredb` |
| Username | `sa`                     |
| Password | *(empty)*                |

## API Endpoints

### Objective 1 -- Browse & Search Books

```
GET /v1/api/books
```
Returns all books in the library.

```
GET /v1/api/books/search?title=crystal&author=&difficulty=EASY&category=
```
Search books by title, author, difficulty (`EASY`, `MEDIUM`, `HARD`), or category. All parameters are optional.

### Objective 2 -- Book Details & Categories

```
GET /v1/api/books/{id}
```
Returns a book's details (title, author, difficulty, categories, section count).

```
POST /v1/api/books/{id}/categories
Content-Type: application/json

{ "category": "ADVENTURE" }
```
Adds a category to a book.

```
DELETE /v1/api/books/{id}/categories/{category}
```
Removes a category from a book.

### Objective 3 -- Read a Book

```
POST /v1/api/game/start
Content-Type: application/json

{ "bookId": 1 }
```
Starts a new game on the given book. Returns the opening section with available choices.

```
GET /v1/api/game/{playerId}
```
Returns the current game state (section text, options, health).

### Objective 4 -- Make Choices & Consequences

```
POST /v1/api/game/{playerId}/choose
Content-Type: application/json

{ "gotoId": 100 }
```
Makes a choice. The `gotoId` must be one of the available options from the current section. If the chosen option has a consequence, the player's health is affected. The player starts with **10 HP** and dies if health reaches **0**.

## Example Walkthrough

```bash
# List all books
curl http://localhost:8080/v1/api/books

# Start playing "The Crystal Caverns"
curl -X POST http://localhost:8080/v1/api/game/start \
  -H "Content-Type: application/json" \
  -d '{"bookId": 1}'

# Choose to cross the rope bridge (gotoId: 100)
curl -X POST http://localhost:8080/v1/api/game/1/choose \
  -H "Content-Type: application/json" \
  -d '{"gotoId": 100}'

# Check current game state
curl http://localhost:8080/v1/api/game/1
```

## Book Validation

Books loaded on startup are validated against these rules. Invalid books are skipped:

- Must have exactly **one** `BEGIN` section
- Must have at least **one** `END` section
- All `gotoId` references must point to existing sections
- Non-ending sections must have at least one option

## Tech Stack

- **Spring Boot 4.0.3**
- **Spring Data JPA** with Hibernate
- **H2** in-memory database
- **Lombok** for boilerplate reduction
- **Jackson** for JSON parsing
- **Maven** build system
