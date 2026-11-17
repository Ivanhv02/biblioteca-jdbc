-- Creación de la tabla autor (si la utilizas como clave foránea)
CREATE TABLE IF NOT EXISTS autor (
                                     id SERIAL PRIMARY KEY,
                                     nombre VARCHAR(100) NOT NULL
    );

-- Creación de la tabla libro
CREATE TABLE IF NOT EXISTS libro (
                                     id SERIAL PRIMARY KEY,
                                     titulo VARCHAR(200) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    anio INTEGER,
    disponible BOOLEAN NOT NULL DEFAULT TRUE,
    autor_id INTEGER REFERENCES autor(id) ON DELETE SET NULL
    );

-- Datos de ejemplo iniciales para pruebas
INSERT INTO autor (nombre) VALUES
                               ('Robert C. Martin'),
                               ('Joshua Bloch');

INSERT INTO libro (titulo, isbn, anio, disponible, autor_id) VALUES
                                                                 ('Clean Architecture', '9780134494166', 2017, TRUE, 1),
                                                                 ('Effective Java', '9780134685991', 2018, TRUE, 2);