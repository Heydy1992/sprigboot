# sprigboot
markdown
# Gestión de Productos Reactivo
Proyecto básico para aprender **Spring Boot**, **Spring Security**, **programación
reactiva con WebFlux**, **internacionalización (i18n)** en 3 idiomas (español, inglés,
francés), y conexión con **MongoDB** usando operaciones CRUD.
## Tecnologías Usadas
- Java 17
- Spring Boot 3.x
- Spring WebFlux (reactivo)
- Spring Security
- Spring Data Reactive MongoDB
- Internacionalización (i18n)
- Postman (para pruebas)
- Lombok
-Tests unitarios con JUnit y Mockito.
-Tests de integración con WebTestClient y Mongo real / remoto.
-Docker (para contenedores)
---
## Internacionalización
Endpoint de prueba:
GET /api/productos/mensaje?lang=es | en | fr
yaml
---
## Seguridad
- Autenticación básica (HTTP Basic)
- Usuario: `usuario`
- Contraseña: `clave123`
---
## Endpoints CRUD (Protegidos)
Método Endpoint Descripción
GET `/api/productos` Lista todos los productos
POST `/api/productos/{id}` Crear un nuevo producto
GET `/api/productos/{id}` Buscar producto por ID
PUT `/api/productos/{id}` Actualizar producto por ID
DELETE `/api/productos/{id}` Eliminar producto por ID
---
## Pruebas con Postman

1. Usa autenticación básica con el usuario y clave.
2. Prueba el CRUD y el endpoint `/mensaje?lang=fr`.
---


## Requisitos para ejecutar
- Java 17
- Maven 3.8+
- MongoDB local (en `localhost:27017`)
---


## Cómo correr el proyecto
```bash
# Clonar el repositorio
git clone https://github.com/tuusuario/gestion-productos-reactivo.git
# Entrar al proyecto
cd gestion-productos-reactivo
# Ejecutar el proyecto
./mvnw spring-boot:run
Contribuciones
¡Este proyecto fue creado para que puedas ayudarte para la primera prueba de Lenguaje
de Programación avanzado 2 y las puedas aprender y nivelarte en este tema. Siéntete libre
de mejorarlo o usarlo como base para tus propios proyectos.
pgsql
---
## 2. Archivo `.gitignore`
```gitignore
# Compiled class file
*.class
# Maven
target/
!.mvn/wrapper/maven-wrapper.jar
**/target/
# Logs
*.log
# IntelliJ IDEA
.idea/
*.iml
# Eclipse
.project
.classpath
.c9/
.settings/
# VS Code
.vscode/
# OS
.DS_Store
Thumbs.db


# Lombok
lombok.config

```
## Explicación de las Pruebas y para qué Sirven

Este proyecto incluye **tests unitarios** y **tests de integración**:

### 1. Tests Unitarios

- **Ubicación:** `src/test/java/com/example/productos/service` (o en controladores con mocks).
- **Tecnologías:** **JUnit 5** y **Mockito**.
- **Propósito:**
  - Verificar la **lógica de negocio** y **controladores** **sin** dependencia real de la base de datos.
  - Simulan (`mock`) el repositorio para aislar la funcionalidad.
- **Cómo correrlos:**
  ```bash
  mvn test

### 2. Tests de Integración

- **Ubicación**: `src/test/java/com/example/productos/controller`
- **Tecnología**: **WebTestClient**, que realiza llamadas reales a la aplicación levantada en un puerto aleatorio.
- **Propósito**:
  - Probar el **API REST** en condiciones **similares a producción**, incluyendo conexión a MongoDB (local, remoto o Docker).
  - Revisar la **seguridad** (HTTP Basic), endpoints CRUD y la internacionalización.
- **Cómo correrlos**:
  ```bash
  mvn test

### 3. Docker y el Pipeline

**instalé Docker** para integrar un pipeline (por ejemplo, en **GitHub Actions**), puedo **correr contenedores** de Mongo y la aplicación, o usar **Testcontainers**.

Ajusto mi archivo `docker-compose.yml` si deseo **levantar mongo y la aplicación** simultáneamente en contenedores.

En **GitHub Actions**, defino un archivo `.github/workflows/test.yml` que ejecuta:
```bash
./mvnw test
