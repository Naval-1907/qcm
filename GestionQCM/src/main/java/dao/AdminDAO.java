package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

/**
 * DAO pour la table ADMIN. Fournit une méthode pour vérifier les identifiants.
 *
 * Sécurité :
 * - Utilise PreparedStatement pour éviter les injections SQL : les paramètres
 *   sont transmis séparément et ne sont pas concaténés dans la requête.
 * - Les mots de passe doivent être stockés sous forme hachée (BCrypt / Argon2).
 *   Ici on utilise BCrypt pour vérifier le mot de passe fourni par l'utilisateur.
 *
 * Pourquoi BCrypt + salt ?
 * - BCrypt génère automatiquement un salt unique pour chaque mot de passe et
 *   intègre ce salt dans le hash renvoyé. Cela signifie que deux utilisateurs
 *   avec le même mot de passe auront des hash différents.
 * - Le salage empêche les attaques par table de correspondance (rainbow tables)
 *   et rend les attaques par dictionnaire beaucoup plus coûteuses.
 * - BCrypt est conçu pour être lent (paramètre cost) afin de ralentir les attaques
 *   par force brute.
 */
public class AdminDAO {

    /**
     * Vérifie si les identifiants fournis correspondent à un administrateur.
     *
     * Implémentation :
     * - Récupère le hash stocké en base pour le username avec PreparedStatement.
     * - Utilise BCrypt.checkpw(passwordPlain, storedHash) pour valider.
     *
     * Retourne true si authentification réussie, false sinon.
     */
    public boolean verifierIdentifiants(String username, String password) {
        String sql = "SELECT password FROM ADMIN WHERE username = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    if (storedHash == null) return false;
                    // Vérifie le mot de passe en clair contre le hash bcrypt stocké.
                    return BCrypt.checkpw(password, storedHash);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification des identifiants.", e);
        }
        return false;
    }

    // Vous pouvez ajouter d'autres méthodes utiles : création d'admin (avec hash),
    // changement de mot de passe (re-hash), récupération d'un objet Admin, etc.
}
