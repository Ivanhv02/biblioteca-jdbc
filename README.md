# Biblioteca JDBC — Arquitectura por Capas y Transacciones

Proyecto en Java con Maven y PostgreSQL que implementa un sistema de gestión de biblioteca aplicando principios de arquitectura limpia y control transaccional mediante JDBC puro.

## Arquitectura del Proyecto

El código está estructurado siguiendo el flujo unidireccional **App → Service → Repository → Base de Datos**:

* **`com.dam.ad.model`**: Entidades del dominio (`Libro`).
* **`com.dam.ad.repository`**: Interfaces que definen el contrato de persistencia (`LibroRepository`).
* **`com.dam.ad.repository.jdbc`**: Implementación concreta con SQL, `PreparedStatement` y mapeo de `ResultSet` (`JdbcLibroRepository`).
* **`com.dam.ad.service`**: Lógica de negocio, validaciones e inyección de dependencias por constructor (`LibroService`, `TxLibroService`).
* **`com.dam.ad.db`**: Gestión de conexiones JDBC (`Db`).
* **`com.dam.ad.app`**: Puntos de entrada y demostraciones ejecutables (`CrudDemo`, `TxDemo`).

## Características Técnicas

* **CRUD completo**: Operaciones `SELECT`, `INSERT` (con recuperación de claves autogeneradas), `UPDATE` y `DELETE` usando `try-with-resources`.
* **Desacoplamiento**: Uso de interfaces e inyección de dependencias para aislar la lógica de negocio de la tecnología de base de datos.
* **Control Transaccional (`commit` / `rollback`)**: Coordinación de múltiples operaciones compartiendo una misma `Connection` con `setAutoCommit(false)` para garantizar la atomicidad ("todo o nada").

## Configuración y Ejecución

1. Crear una base de datos en **PostgreSQL** y ejecutar el script incluido en [`schema.sql`](./schema.sql).
2. Configurar las credenciales de conexión (URL, usuario y contraseña) en la clase `com.dam.ad.db.Db`.
3. Ejecutar `CrudDemo` para probar el flujo CRUD básico o `TxDemo` para verificar el comportamiento ante errores con `rollback()`.