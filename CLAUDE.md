# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

Maven wrapper is included (use `mvnw.cmd` on Windows, `./mvnw` elsewhere).

- Run app: `./mvnw spring-boot:run` (serves on port **8085**)
- Build: `./mvnw clean package`
- All tests: `./mvnw test`
- Single test: `./mvnw test -Dtest=SbEcomApplicationTests#methodName`

No linter is configured. The only existing test is the default `contextLoads` test.

## Stack

Spring Boot 4.1.1, Java 25, Spring Data JPA, in-memory H2 (`jdbc:h2:mem:test`, console enabled), Bean Validation, Lombok, ModelMapper. Note Boot 4 starter names (`spring-boot-starter-webmvc`, `spring-boot-h2console`). Schema is auto-created by Hibernate on each start; data does not persist.

## Architecture

Base package `com.ecommerce.project`, classic layered structure: `controller` → `service` (interface + `*Impl`) → `repositories` (Spring Data JPA), with `model` (JPA entities) and `payload` (DTOs / response wrappers).

- **Entity vs DTO**: controllers accept entities (`Category`, `Product`) as request bodies, but services return DTOs (`CategoryDTO`, `ProductDTO`) mapped via the `ModelMapper` bean defined in `config/AppConfig`. List endpoints return wrapper payloads (`CategoryResponse`, `ProductResponse`) carrying paging metadata.
- **Pagination/sorting defaults** live in `config/AppConstants` (page 0, size 50, sort by `categoryId`, `asc`) and are used as `@RequestParam` defaults.
- **Error handling**: `exceptions/MyGlobalExceptionHandler` (`@RestControllerAdvice`) maps `ResourceNotFoundException` → 404, `APIException` → 400, and `MethodArgumentNotValidException` → 400 with a field→message map. Both custom exceptions return `payload/APIResponse(message, success)`. Throw these from services rather than building error responses by hand.
- **Routes** are prefixed `/api`; admin-style operations use `/api/admin/...` (e.g. `POST /api/admin/categories/{categoryId}/product`). No security layer is configured yet.
- **Product creation** (`ProductServiceImpl.addProduct`): looks up the `Category` (404 if missing), sets a default image `default.png`, and computes `specialPrice = price - discount% of price` server-side.

Product functionality is in progress (uncommitted work); only `addProduct` exists so far, while Category has full CRUD.
