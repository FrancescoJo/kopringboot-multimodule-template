# CLAUDE.md - AI Assistant Guide

This document provides comprehensive guidance for AI assistants working with this Kotlin Spring Boot multimodule template repository.

## Project Overview

**kopringboot-multimodule-template** is an enterprise-grade Spring Boot multimodule template written in Kotlin, demonstrating clean architecture principles with strict code quality standards.

- **Language**: Kotlin 2.2.0
- **JVM Target**: Java 21
- **Framework**: Spring Boot 3.5.5
- **Build Tool**: Gradle 8.11.1
- **Architecture**: Clean Architecture with DDD principles
- **License**: Apache License 2.0

## Table of Contents

1. [Module Structure](#module-structure)
2. [Architecture Patterns](#architecture-patterns)
3. [Package Naming Conventions](#package-naming-conventions)
4. [Testing Strategy](#testing-strategy)
5. [Code Quality Standards](#code-quality-standards)
6. [Build System](#build-system)
7. [Spring Profiles](#spring-profiles)
8. [Development Workflows](#development-workflows)
9. [Common Utilities](#common-utilities)
10. [Git and CI/CD](#git-and-cicd)
11. [AI Assistant Guidelines](#ai-assistant-guidelines)

---

## Module Structure

The repository is organized into three main module groups with clear dependency boundaries:

### App-Backend (Main Application)
```
app-backend/
├── core/              # Domain models, business logic, use cases
├── infrastructure/    # Data persistence, JPA, external integrations
└── server/           # REST API, controllers, Spring Boot application
```

### Admin-Backend (Administrative Application)
```
admin-backend/
├── core/              # Admin-specific business logic
├── infrastructure/    # Admin data access
└── server/           # Admin REST API
```

### Common-Libs-Backend (Shared Libraries)
```
common-libs-backend/
├── foundation/        # Core utilities (crypto, collections, text, etc.)
├── jacksonConfig/     # Jackson JSON configuration for Kotlin
├── serverWebApi/      # Reusable API response models
└── springWebMvc/      # Spring Web MVC common configurations
```

### Dependency Hierarchy
```
server (app/admin)
  ↓ depends on
core + infrastructure + common-libs
  ↓ depends on
common-libs-backend:foundation (lowest level)
```

**Important**: Never create circular dependencies between modules. The foundation module should have no internal dependencies.

---

## Architecture Patterns

This project follows **Clean Architecture** with **Domain-Driven Design (DDD)** principles:

### Core Layer (Domain)
- **Location**: `{module}/core/src/main/kotlin`
- **Contains**:
  - Domain models with `IdentifiableObject` and `DateAuditable` interfaces
  - Value objects (e.g., `UserId`)
  - Repository interfaces (abstractions)
  - Use cases marked with `@UseCase` annotation
  - Projections for read models (e.g., `UserProjection`)
  - Domain exceptions

**Example**:
```kotlin
// app-backend/core/src/main/kotlin/com/github/francescojo/core/domain/user/usecase/CreateUserUseCase.kt
@UseCase
class CreateUserUseCase(
    private val userRepository: WritableRepository<User>
) {
    fun execute(request: CreateUserRequest): User {
        // Business logic here
    }
}
```

### Infrastructure Layer
- **Location**: `{module}/infrastructure/src/main/kotlin`
- **Contains**:
  - JPA entities and repositories
  - Database configuration (HikariCP, Hibernate)
  - External service integrations
  - Implementation of core repository interfaces

**Key Pattern**: Repositories in core are interfaces, implementations are in infrastructure.

### Server/Presentation Layer
- **Location**: `{module}/server/src/main/kotlin`
- **Contains**:
  - REST Controllers (versioned APIs: `/v1/users/*`)
  - Request/Response DTOs
  - Exception handlers (`@ControllerAdvice`)
  - Spring Boot application class
  - Configuration beans

### Design Patterns Used

1. **Repository Pattern**: Data access abstraction
   - Interface in core: `com.github.francescojo.core.repository.WritableRepository`
   - Implementation in infrastructure: JPA repositories

2. **Use Case Pattern**: Encapsulated business logic
   - Location: `core/domain/{entity}/usecase/`
   - Annotation: `@UseCase`

3. **Projection Pattern**: Separate read/write models
   - Write model: Domain entities
   - Read model: Projections with `ProjectionFinder`

4. **Value Object Pattern**: Type-safe identifiers
   - Example: `UserId`, `AdminId`

---

## Package Naming Conventions

### Root Package
```
com.github.francescojo
```

### App-Backend Structure
```
com.github.francescojo
├── core
│   ├── annotation          # @UseCase, @OpenForTesting
│   ├── domain
│   │   └── {entity}
│   │       ├── model       # Write models (User)
│   │       ├── projection  # Read models (UserProjection)
│   │       ├── repository  # Repository interfaces
│   │       ├── usecase     # Business logic
│   │       └── exception   # Domain exceptions
│   ├── exception
│   │   ├── external        # Client errors (404, 400)
│   │   └── internal        # System errors (500)
│   ├── repository          # Base repository interfaces
│   └── projection          # Base projection interfaces
├── infrastructure
│   ├── jdbc
│   │   ├── entity          # JPA entities
│   │   └── repository      # JPA repository implementations
│   └── appconfig           # Infrastructure configuration
├── endpoint.v1             # API version 1
│   ├── {entity}
│   │   ├── create          # CreateController + DTOs
│   │   ├── edit            # EditController + DTOs
│   │   ├── delete          # DeleteController
│   │   ├── get             # GetController + DTOs
│   │   └── common          # Shared Response DTOs
│   └── health              # Health check endpoint
├── appconfig
│   └── bean                # Spring bean configurations
└── advice.errorhandler     # Global exception handling
```

### Library Structure
```
com.github.francescojo.lib
├── annotation.*            # @OpenForTesting, @VisibleForTesting
├── codec.*                 # TsidCodecUtils, UuidCodecUtils
├── collection.*            # CollectionUtils, FastCollectedLruCache
├── crypto.*                # AesCipher, RsaCipher, HashUtils
├── i18n.*                  # LocaleProvider
├── lang.*                  # ReflectionUtils
├── net.*                   # UriUtils, InetAddressUtils
├── text.*                  # StringUtils, ToStringHelper
├── time.*                  # TimeUtils
└── util.*                  # UuidUtils, SemanticVersion
```

**Naming Rule**: When creating new entities, follow the existing pattern strictly.

---

## Testing Strategy

### Three-Tier Test Classification

| Test Size | Annotation | Purpose | Framework | Location |
|-----------|-----------|---------|-----------|----------|
| **Small** | `@SmallTest` | Unit tests | JUnit 5 + Kotest | `src/test/kotlin/testcase/small/` |
| **Medium** | `@MediumTest` | Component tests | JUnit 5 + TestContainers | `src/test/kotlin/testcase/medium/` |
| **Large** | `@LargeTest` | Integration tests | Spock (Groovy) | `src/test/groovy/testcase/large/` |

### Test Frameworks

- **JUnit 5** (5.12.2): Core framework for small/medium tests
- **Kotest** (5.5.4): BDD-style assertions and matchers
- **Spock** (2.4-M4): BDD integration tests with Groovy
- **Mockito & Mockito-Kotlin**: Mocking for unit tests
- **TestContainers** (1.17.3): Containerized database tests
- **JavaFaker** (1.0.2): Test data generation

### Test Structure Example

```kotlin
// Small Test (Unit)
@SmallTest
class CreateUserUseCaseSpec : StringSpec({
    "should create user successfully" {
        // Given
        val repository = mockk<WritableRepository<User>>()
        val useCase = CreateUserUseCase(repository)

        // When
        val result = useCase.execute(request)

        // Then
        result shouldNotBe null
        verify { repository.save(any()) }
    }
})
```

```kotlin
// Medium Test (Component)
@MediumTest
@ExtendWith(SpringExtension::class)
class UserRepositoryMediumTest : JdbcTemplateMediumTestBase() {
    @Test
    fun `should persist user to database`() {
        // Integration with test database
    }
}
```

```groovy
// Large Test (Integration)
@LargeTest
class UserApiLargeTest extends RestAssuredLargeTestBase {
    def "should create user via REST API"() {
        given:
        def request = [name: "John Doe", email: "john@example.com"]

        when:
        def response = given()
            .contentType(ContentType.JSON)
            .body(request)
            .post("/v1/users")

        then:
        response.statusCode() == 201
    }
}
```

### Running Tests

```bash
# All tests
./gradlew :app-backend:test

# Only small tests (unit)
./gradlew :app-backend:smallTest

# Only medium tests
./gradlew :app-backend:mediumTest

# Only large tests (integration)
./gradlew :app-backend:largeTest

# All tests with coverage
./gradlew :app-backend:testAll

# Generate coverage report
./gradlew :app-backend:jacocoTestReport
```

### Test Coverage Requirements

- **Minimum**: 25% line coverage (enforced by CI)
- **Target**: Higher coverage for critical business logic
- Coverage validated via JaCoCo in CI pipeline

### Testing Best Practices for AI Assistants

1. **Always write tests** when adding new features
2. **Use appropriate test size**:
   - Small: Pure business logic, no I/O
   - Medium: Database interactions, file I/O
   - Large: Full API integration tests
3. **Follow existing test naming**: `{ClassName}Spec` or `{ClassName}Test`
4. **Use Kotest matchers** for assertions in Kotlin tests
5. **Mock external dependencies** in small tests
6. **Use TestContainers** for database tests (medium size)

---

## Code Quality Standards

### Detekt Configuration

**Location**: `gradle/scripts/settings/detekt.yml`

**Key Rules**:
- **Max Issues**: 0 (fail-fast)
- **Warnings as Errors**: Enabled
- **Line Length**: 120 characters
- **Cognitive Complexity**: Max 15 per method
- **Cyclomatic Complexity**: Max 15 per method
- **Nested Block Depth**: Max 4
- **Long Parameter List**: Max 6 parameters (7 for constructors)
- **Large Class**: Max 600 lines
- **Long Method**: Max 60 lines
- **Functions Per Class**: Max 11

### Kotlin Compiler Settings

```kotlin
kotlin {
    compilerOptions {
        allWarningsAsErrors.set(true)  // Strict mode
        jvmTarget.set(JvmTarget.fromTarget("21"))
    }
}
```

### Running Code Quality Checks

```bash
# Run Detekt on specific module
./gradlew :app-backend:core:detekt

# Run Detekt on all app-backend modules
./gradlew :app-backend:detektAll

# Full code quality check (tests + detekt)
./gradlew :app-backend:check
```

### Code Style Guidelines for AI Assistants

1. **Kotlin Idioms**: Use Kotlin-specific features
   - Extension functions
   - Data classes
   - When expressions
   - Named arguments
   - Default parameters

2. **Immutability**: Prefer `val` over `var`

3. **Null Safety**: Leverage Kotlin's null safety
   - Avoid `!!` operator
   - Use `?.let`, `?:`, and safe calls

4. **Function Naming**: Descriptive, verb-based names
   - Good: `createUser`, `findUserById`
   - Bad: `doStuff`, `process`

5. **Class Organization**:
   ```kotlin
   class Example {
       // 1. Properties
       private val field: String

       // 2. Init blocks
       init { }

       // 3. Secondary constructors
       constructor() : this("")

       // 4. Public methods
       fun publicMethod() { }

       // 5. Private methods
       private fun privateMethod() { }

       // 6. Companion object
       companion object { }
   }
   ```

6. **Avoid Code Smells**:
   - No magic numbers (use named constants)
   - No wildcard imports
   - No unused imports
   - No empty catch blocks
   - No unnecessary null checks

---

## Build System

### Gradle Structure

```
gradle/
└── scripts/
    ├── java.gradle                  # Java compilation
    ├── kotlin.gradle                # Kotlin configuration
    ├── detekt.gradle                # Code quality
    ├── testing.gradle               # JUnit, small/medium tests
    ├── testing-large.gradle         # Spock, large tests
    ├── springBootCommon.gradle      # Spring Boot base
    ├── springBootCommonWebMvc.gradle # Web MVC dependencies
    ├── commonInfrastructure.gradle  # JPA, databases
    ├── sourceSets.gradle            # Profile-specific sources
    ├── springBootJar.gradle         # JAR packaging
    ├── packaging.gradle             # Build metadata
    ├── testFailAggregation.gradle   # Test reporting
    └── settings/
        └── detekt.yml               # Detekt configuration
```

### Build Profiles

Four environment profiles available:

| Profile | Database | Logging | Use Case |
|---------|----------|---------|----------|
| **local** | H2 in-memory | TRACE/DEBUG | Local development |
| **alpha** | Configurable | DEBUG | Early staging |
| **beta** | Configurable | INFO | Late staging |
| **release** | MariaDB | INFO | Production |

### Build Commands

```bash
# Default build (local profile)
./gradlew :app-backend:server:assemble

# Build with specific profile
./gradlew :app-backend:server:assemble -PbuildConfig=release

# Clean build
./gradlew clean :app-backend:server:assemble

# Build all modules
./gradlew build

# Run application locally
java -jar app-backend/server/build/libs/app-backend_server-1.0.0.jar

# Run with specific profile
java -jar app-backend_server-1.0.0.jar --spring.profiles.active=release
```

### Archive Naming Convention

Built JARs follow the pattern: `{module}_{submodule}-{version}.jar`

Examples:
- `app-backend_server-1.0.0.jar`
- `app-backend_core-1.0.0.jar`
- `admin-backend_server-1.0.0.jar`

### Dependencies Management

All versions are centralized in `build.gradle` under `buildscript.ext`:

```groovy
version_kotlin = "2.2.0"
version_springBoot = "3.5.5"
version_kotest = "5.5.4"
// etc.
```

**For AI Assistants**: When adding dependencies, update versions in `build.gradle` first, then reference them in module-specific build files.

---

## Spring Profiles

### Profile Configuration Files

**Location**: `{module}/server/src/{profile}/resources/application-{profile}.yml`

### Local Profile (Default)
- **Database**: H2 in-memory (MODE=MYSQL compatible)
- **JPA DDL**: `validate` (no auto-creation)
- **Logging**: TRACE for app, DEBUG for framework
- **Management**: All actuator endpoints exposed
- **Port**: 8080 (from `KOPRINGBOOT_APP_PORT` env var)

### Release Profile (Production)
- **Database**: MariaDB
- **JPA DDL**: `validate`
- **Logging**: INFO level only
- **Management**: Health and info only
- **Security**: Health details hidden
- **Log Files**: Via `KOPRINGBOOT_APP_LOG_PATH` env var

### Application Configuration

Base configuration in `server/src/main/resources/application.yml`:

```yaml
server:
  port: ${KOPRINGBOOT_APP_PORT:8080}
  error:
    include-exception: false
    include-stacktrace: never

spring:
  application:
    name: kopringboot-app
  jpa:
    open-in-view: false
    hibernate:
      ddl-auto: validate
```

**Important**: Never use `ddl-auto: create` or `update` in production profiles.

---

## Development Workflows

### Adding a New Feature

1. **Identify Module**: Determine which module (app-backend or admin-backend)
2. **Create Domain Model** (if needed):
   ```
   core/src/main/kotlin/com/github/francescojo/core/domain/{entity}/model/
   ```
3. **Define Repository Interface**:
   ```
   core/src/main/kotlin/com/github/francescojo/core/domain/{entity}/repository/
   ```
4. **Implement Use Case**:
   ```
   core/src/main/kotlin/com/github/francescojo/core/domain/{entity}/usecase/
   ```
5. **Create JPA Entity**:
   ```
   infrastructure/src/main/kotlin/com/github/francescojo/infrastructure/jdbc/entity/
   ```
6. **Implement Repository**:
   ```
   infrastructure/src/main/kotlin/com/github/francescojo/infrastructure/jdbc/repository/
   ```
7. **Create Controller**:
   ```
   server/src/main/kotlin/com/github/francescojo/endpoint/v1/{entity}/
   ```
8. **Write Tests** (all three sizes as appropriate)
9. **Run Quality Checks**:
   ```bash
   ./gradlew :app-backend:check
   ```

### Modifying Existing Code

1. **Read existing code first** - Never modify code you haven't read
2. **Understand the context** - Check related use cases and tests
3. **Make minimal changes** - Don't refactor unrelated code
4. **Update tests** - Ensure tests pass and add new ones if needed
5. **Run Detekt** - Fix any code quality issues
6. **Verify coverage** - Maintain or improve coverage

### Refactoring Guidelines

1. **Don't over-engineer**: Only refactor when explicitly requested
2. **Keep it simple**: Avoid premature abstractions
3. **One concern at a time**: Don't mix refactoring with feature additions
4. **Test coverage**: Ensure tests exist before refactoring
5. **Backward compatibility**: Avoid breaking changes unless necessary

---

## Common Utilities

### Foundation Module

**Location**: `common-libs-backend/foundation/src/main/kotlin/com/github/francescojo/lib/`

| Category | Classes | Purpose |
|----------|---------|---------|
| **Annotations** | `@OpenForTesting`, `@VisibleForTesting` | Test visibility |
| **Codec** | `TsidCodecUtils`, `UuidCodecUtils` | ID serialization |
| **Collections** | `CollectionUtils`, `FastCollectedLruCache` | Collection helpers |
| **Crypto** | `AesCipher`, `RsaCipher`, `HashUtils` | Encryption/hashing |
| **I18n** | `LocaleProvider` | Internationalization |
| **Lang** | `ReflectionUtils` | Reflection utilities |
| **Net** | `UriUtils`, `InetAddressUtils` | Network utilities |
| **Text** | `StringUtils`, `ToStringHelper` | String manipulation |
| **Time** | `TimeUtils` | Temporal utilities |
| **Util** | `UuidUtils`, `SemanticVersion` | General utilities |

### Using Common Utilities

```kotlin
// String utilities
import com.github.francescojo.lib.text.StringUtils

val trimmed = StringUtils.trimToEmpty(input)
val ellipsis = StringUtils.ellipsize(longText, 50)

// Crypto utilities
import com.github.francescojo.lib.crypto.AesCipher

val cipher = AesCipher(secretKey)
val encrypted = cipher.encrypt(plainText)

// ToStringHelper (similar to Guava)
import com.github.francescojo.lib.text.ToStringHelper

override fun toString() = ToStringHelper.of(this)
    .add("id", id)
    .add("name", name)
    .toString()
```

### Jackson Configuration

**Module**: `common-libs-backend/jacksonConfig`

Pre-configured for Kotlin with:
- `jackson-module-kotlin`: Kotlin data class support
- `jackson-datatype-jsr310`: Java 8 date/time support
- Custom serializers/deserializers

### Web API Utilities

**Module**: `common-libs-backend/serverWebApi`

Provides:
- Common response wrappers
- Error response models
- HTTP status utilities

---

## Git and CI/CD

### Branch Strategy

- **Main branch**: `main` (or as specified in repository settings)
- **Feature branches**: `feature/{description}` or `claude/{session-id}`
- **Protected branches**: Direct commits to main are discouraged

### CI/CD Pipeline

**Workflow**: `.github/workflows/code_quality.yml`

**Triggers**:
- Pull request events (opened, synchronize, reopened)
- Manual dispatch

**Jobs**:
1. **Changed Module Detection**: Identifies modified modules
2. **Detekt**: Static analysis on changed modules
3. **Tests**: Full test suite on changed modules
4. **Coverage Validation**: Ensures >25% coverage

**Environment**:
- **OS**: Ubuntu latest
- **JDK**: Oracle JDK 21
- **Gradle**: Setup via `gradle/actions/setup-gradle@v4`

### Commit Guidelines

1. **Clear messages**: Describe what and why
   ```
   Add user deletion endpoint

   Implements DELETE /v1/users/{id} with soft delete logic.
   Includes validation for user existence and permission checks.
   ```

2. **Logical commits**: One concern per commit
3. **No secrets**: Never commit credentials or sensitive data
4. **Conventional format** (optional but recommended):
   - `feat:` New feature
   - `fix:` Bug fix
   - `refactor:` Code refactoring
   - `test:` Test additions
   - `docs:` Documentation
   - `chore:` Build/tooling changes

### Pull Request Process

1. **Create feature branch** from main
2. **Make changes** with tests and documentation
3. **Run local checks**:
   ```bash
   ./gradlew :app-backend:check
   ```
4. **Commit and push**
5. **Open pull request** with description
6. **CI checks must pass** before merge
7. **Address review comments** if any

---

## AI Assistant Guidelines

### Do's

1. **Always read files before modifying them**
   - Use Read tool to understand existing code
   - Check related files for context

2. **Follow existing patterns**
   - Match the coding style
   - Use the same package structure
   - Follow naming conventions

3. **Write comprehensive tests**
   - Add tests for new features
   - Update tests for changes
   - Aim for meaningful coverage

4. **Run quality checks before committing**
   ```bash
   ./gradlew :app-backend:detektAll
   ./gradlew :app-backend:test
   ```

5. **Use appropriate tools**
   - Glob for finding files
   - Grep for searching code
   - Read for examining files
   - Edit for precise modifications
   - Task tool for complex searches

6. **Document significant changes**
   - Add KDoc comments for public APIs
   - Update relevant documentation

7. **Commit regularly with clear messages**
   - Logical, focused commits
   - Descriptive commit messages

### Don'ts

1. **Don't modify code without reading it first**
   - Never guess at implementation details

2. **Don't over-engineer**
   - Avoid unnecessary abstractions
   - Don't add features not requested
   - Keep solutions simple

3. **Don't ignore code quality**
   - Fix all Detekt violations
   - Ensure tests pass
   - Maintain coverage

4. **Don't create circular dependencies**
   - Respect module boundaries
   - Foundation should have no internal deps

5. **Don't use unsafe Kotlin patterns**
   - Avoid `!!` operator
   - Don't suppress warnings without reason
   - No wildcard imports

6. **Don't commit generated files**
   - Build artifacts (`build/`, `*.class`)
   - IDE files (`.idea/`, `.gradle/`)
   - Log files (`*.log`)

7. **Don't mix concerns in commits**
   - Separate refactoring from features
   - One logical change per commit

### Code Review Checklist

Before finalizing changes, verify:

- [ ] All files read before modification
- [ ] Tests written and passing
- [ ] Detekt checks passing
- [ ] No compiler warnings
- [ ] Coverage maintained or improved
- [ ] Documentation updated
- [ ] Commit messages clear
- [ ] No secrets in code
- [ ] Follows existing patterns
- [ ] Minimal, focused changes

### Common Tasks Reference

#### Adding a New REST Endpoint

1. Create use case in `core/domain/{entity}/usecase/`
2. Add controller in `server/endpoint/v1/{entity}/{action}/`
3. Define request/response DTOs in controller package
4. Write unit tests for use case
5. Write integration tests for endpoint
6. Document with OpenAPI annotations

#### Adding a New Domain Entity

1. Create model in `core/domain/{entity}/model/`
2. Define repository interface in `core/domain/{entity}/repository/`
3. Create JPA entity in `infrastructure/jdbc/entity/`
4. Implement repository in `infrastructure/jdbc/repository/`
5. Add projection if needed
6. Write comprehensive tests

#### Updating Dependencies

1. Update version in `build.gradle` under `buildscript.ext`
2. Run `./gradlew dependencies` to verify
3. Update documentation if major version change
4. Test thoroughly

#### Troubleshooting Build Issues

```bash
# Clean build
./gradlew clean

# Refresh dependencies
./gradlew --refresh-dependencies

# Build with debug info
./gradlew :app-backend:build --info

# Check dependency tree
./gradlew :app-backend:dependencies
```

---

## Important File Locations

### Configuration Files
- **Gradle**: `build.gradle`, `settings.gradle`
- **Detekt**: `gradle/scripts/settings/detekt.yml`
- **Application**: `{module}/server/src/{profile}/resources/application-{profile}.yml`
- **Git Ignore**: `.gitignore`

### Key Source Directories
- **Core Domain**: `app-backend/core/src/main/kotlin/com/github/francescojo/core/`
- **Infrastructure**: `app-backend/infrastructure/src/main/kotlin/com/github/francescojo/infrastructure/`
- **REST API**: `app-backend/server/src/main/kotlin/com/github/francescojo/endpoint/`
- **Common Utils**: `common-libs-backend/foundation/src/main/kotlin/com/github/francescojo/lib/`

### Test Directories
- **Small Tests**: `src/test/kotlin/testcase/small/`
- **Medium Tests**: `src/test/kotlin/testcase/medium/`
- **Large Tests**: `src/test/groovy/testcase/large/`

### Build Output
- **JAR Files**: `{module}/server/build/libs/`
- **Test Reports**: `{module}/build/reports/tests/`
- **Coverage Reports**: `{module}/build/reports/jacoco/`
- **Detekt Reports**: `{module}/build/reports/detekt/`

---

## Quick Reference Commands

### Build
```bash
./gradlew :app-backend:server:assemble                 # Build application
./gradlew :app-backend:server:assemble -PbuildConfig=release  # Production build
./gradlew clean build                                  # Clean and build all
```

### Test
```bash
./gradlew :app-backend:smallTest                       # Unit tests only
./gradlew :app-backend:mediumTest                      # Component tests
./gradlew :app-backend:largeTest                       # Integration tests
./gradlew :app-backend:testAll                         # All tests + coverage
```

### Quality
```bash
./gradlew :app-backend:detektAll                       # Run Detekt
./gradlew :app-backend:check                           # Full quality check
./gradlew :app-backend:jacocoTestReport               # Coverage report
```

### Run
```bash
./gradlew :app-backend:server:bootRun                  # Run with Gradle
java -jar app-backend_server-1.0.0.jar                # Run JAR
java -jar app-backend_server-1.0.0.jar --spring.profiles.active=release
```

---

## Version Information

- **Template Version**: 1.0.0
- **Last Updated**: 2025-11-28
- **Maintained By**: Francesco Jo (@FrancescoJo)
- **Repository**: https://github.com/FrancescoJo/kopringboot-multimodule-template

---

## Additional Resources

- **README**: `README.adoc` - Project overview and setup
- **License**: `LICENSE` - Apache License 2.0
- **GitHub Workflows**: `.github/workflows/` - CI/CD definitions
- **Gradle Scripts**: `gradle/scripts/` - Build configuration details

---

**Note for AI Assistants**: This guide should be your primary reference when working with this codebase. Always prioritize code quality, testing, and following established patterns. When in doubt, examine existing code for examples.
