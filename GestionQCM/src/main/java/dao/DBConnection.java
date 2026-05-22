package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestionnaire de connexion JDBC utilisant le pattern Singleton.
 * Il expose une méthode getConnection() que les DAO doivent utiliser.
 */
public class DBConnection {
    // Defaults for local PostgreSQL. Can be overridden with environment variables.
    private static final String DB_URL = System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:postgresql://localhost:5432/gestion_qcm");
    private static final String DB_USER = System.getenv().getOrDefault("JDBC_DATABASE_USERNAME", "postgres");
    private static final String DB_PASSWORD = System.getenv().getOrDefault("JDBC_DATABASE_PASSWORD", "postgres");
    private static DBConnection instance;

    private DBConnection() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Pilote PostgreSQL introuvable. Ajoutez le driver JDBC au classpath.", e);
        }
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
