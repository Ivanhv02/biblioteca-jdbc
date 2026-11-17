package com.dam.ad.app;

import com.dam.ad.db.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PrepararBaseDatos {

    public static void main(String[] args) {
        try (Connection con = Db.getConnection();
             Statement st = con.createStatement()) {

            // 1. Crear tabla autor si no existe
            st.execute("""
                CREATE TABLE IF NOT EXISTS autor (
                    id SERIAL PRIMARY KEY,
                    nombre VARCHAR(150) NOT NULL UNIQUE
                );
            """);

            // 2. Añadir columna autor_id a libro si no existe
            st.execute("""
                ALTER TABLE libro
                ADD COLUMN IF NOT EXISTS autor_id INTEGER REFERENCES autor(id);
            """);

            // 3. Insertar autores iniciales evitando duplicados
            String[] autores = {"J. R. R. Tolkien", "J. K. Rowling", "Ken Follett"};
            String insertAutorSql = "INSERT INTO autor (nombre) VALUES (?) ON CONFLICT (nombre) DO NOTHING;";
            try (PreparedStatement psAutor = con.prepareStatement(insertAutorSql)) {
                for (String autor : autores) {
                    psAutor.setString(1, autor);
                    psAutor.executeUpdate();
                }
            }

            // 4. Relacionar libros existentes con sus autores por ISBN
            relacionarLibroConAutor(con, "9780201633610", "J. R. R. Tolkien");     // El Hobbit
            relacionarLibroConAutor(con, "9780132350884", "J. K. Rowling");       // Harry Potter
            relacionarLibroConAutor(con, "9780134685991", "Ken Follett");        // Los Pilares de la Tierra

            System.out.println(">> Preparación del esquema y migración de datos completada con éxito.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void relacionarLibroConAutor(Connection con, String isbn, String nombreAutor) throws SQLException {
        // Obtener el id autogenerado del autor
        Integer autorId = null;
        String sqlBuscarAutor = "SELECT id FROM autor WHERE nombre = ?";
        try (PreparedStatement ps = con.prepareStatement(sqlBuscarAutor)) {
            ps.setString(1, nombreAutor);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    autorId = rs.getInt("id");
                }
            }
        }

        if (autorId == null) {
            System.out.println("Aviso: No se encontró al autor " + nombreAutor);
            return;
        }

        // Actualizar el libro con el autor_id correspondiente
        String sqlUpdate = "UPDATE libro SET autor_id = ? WHERE isbn = ?";
        try (PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
            ps.setInt(1, autorId);
            ps.setString(2, isbn);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Libro con ISBN " + isbn + " vinculado a " + nombreAutor + " (filas actualizadas: " + filas + ")");
            } else {
                System.out.println("Aviso: No se encontró ningún libro con ISBN " + isbn + " para vincular.");
            }
        }
    }
}