package dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour AdminDAO.verifierIdentifiants
 * - Prépare un utilisateur admin de test avec mot de passe haché (BCrypt)
 * - Vérifie succès et échec d'authentification
 */
public class AdminDAOTest {

    private AdminDAO adminDAO;
    private String testUsername;
    private final String testPlainPassword = "admin123"; // mot de passe utilisé pour le test

    @BeforeEach
    void setUp() throws SQLException {
        adminDAO = new AdminDAO();
        testUsername = "test_admin_" + System.nanoTime();

        // Générer un hash bcrypt avec cost 12
        String hashed = BCrypt.hashpw(testPlainPassword, BCrypt.gensalt(12));

        // Insérer l'admin de test dans la base (PostgreSQL upsert)
        String sql = "INSERT INTO ADMIN (username, password, nom) VALUES (?, ?, ?)" +
                 " ON CONFLICT (username) DO UPDATE SET password = EXCLUDED.password, nom = EXCLUDED.nom";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, testUsername);
            ps.setString(2, hashed);
            ps.setString(3, "Test Admin");
            ps.executeUpdate();
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Supprimer l'admin de test pour ne pas polluer la base
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM ADMIN WHERE username = ?")) {
            ps.setString(1, testUsername);
            ps.executeUpdate();
        }
    }

    @Test
    void testVerifierIdentifiants_Success() {
        // Action: appeler la méthode de vérification avec identifiants corrects
        boolean result = adminDAO.verifierIdentifiants(testUsername, testPlainPassword);

        // Assertion: l'authentification doit réussir (true)
        assertTrue(result, "L'utilisateur avec le mot de passe correct doit être authentifié.");

        // Vérification supplémentaire : s'assurer que la requête n'a pas modifié le password en base
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT password FROM ADMIN WHERE username = ?")) {
            ps.setString(1, testUsername);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next(), "L'utilisateur de test doit exister en base.");
                String stored = rs.getString("password");
                // stored doit être un hash bcrypt (commence par $2a$ ou $2b$)
                assertNotNull(stored);
                assertTrue(stored.startsWith("$2"), "Le mot de passe stocké doit être un hash bcrypt.");
            }
        } catch (SQLException e) {
            fail("Erreur SQL lors de la vérification additionnelle: " + e.getMessage());
        }
    }

    @Test
    void testVerifierIdentifiants_Fail_WrongPassword() {
        // Action: utiliser un mot de passe incorrect
        boolean result = adminDAO.verifierIdentifiants(testUsername, "wrongpassword");

        // Assertion: l'authentification doit échouer
        assertFalse(result, "L'authentification doit échouer pour un mot de passe incorrect.");
    }

    @Test
    void testVerifierIdentifiants_Fail_NoUser() {
        // Action: un utilisateur inexistant
        boolean result = adminDAO.verifierIdentifiants("no_such_user_12345", "whatever");

        // Assertion: doit renvoyer false
        assertFalse(result, "L'appel pour un utilisateur inexistant doit retourner false.");
    }
}
