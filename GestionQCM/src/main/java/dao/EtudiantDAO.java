package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.Etudiant;

/**
 * DAO pour la table ETUDIANT.
 * Toutes les opérations JDBC sont réalisées avec try-with-resources pour fermer automatiquement les ressources.
 */
public class EtudiantDAO {

    public List<String> getAllNiveaux() {
        return Arrays.asList("L1", "L2", "L3", "M1", "M2");
    }

    public List<Etudiant> findEtudiants(String recherche) {
        List<Etudiant> etudiants = new ArrayList<>();
        String sql = "SELECT num_etudiant, nom, prenoms, niveau, adr_email FROM ETUDIANT";

        if (recherche != null && !recherche.trim().isEmpty()) {
            sql += " WHERE LOWER(num_etudiant) LIKE ? OR LOWER(nom) LIKE ? OR LOWER(prenoms) LIKE ?";
        }

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (recherche != null && !recherche.trim().isEmpty()) {
                String pattern = "%" + recherche.trim().toLowerCase() + "%";
                stmt.setString(1, pattern);
                stmt.setString(2, pattern);
                stmt.setString(3, pattern);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    etudiants.add(mapEtudiant(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche d'étudiants.", e);
        }
        return etudiants;
    }

    public List<Etudiant> getAllEtudiants() {
        return findEtudiants(null);
    }

    public Etudiant getEtudiantByNumero(String numEtudiant) {
        if (numEtudiant == null) {
            return null;
        }
        String sql = "SELECT num_etudiant, nom, prenoms, niveau, adr_email FROM ETUDIANT WHERE num_etudiant = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numEtudiant);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapEtudiant(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'étudiant.", e);
        }
        return null;
    }

    public void saveOrUpdateEtudiant(Etudiant etudiant) {
        // PostgreSQL upsert
        String sql = "INSERT INTO ETUDIANT (num_etudiant, nom, prenoms, niveau, adr_email) VALUES (?, ?, ?, ?, ?) " +
                 "ON CONFLICT (num_etudiant) DO UPDATE SET nom = EXCLUDED.nom, prenoms = EXCLUDED.prenoms, niveau = EXCLUDED.niveau, adr_email = EXCLUDED.adr_email";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, etudiant.getNumEtudiant());
            stmt.setString(2, etudiant.getNom());
            stmt.setString(3, etudiant.getPrenoms());
            stmt.setString(4, etudiant.getNiveau());
            stmt.setString(5, etudiant.getAdrEmail());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement de l'étudiant.", e);
        }
    }

    public void deleteEtudiant(String numEtudiant) {
        String sql = "DELETE FROM ETUDIANT WHERE num_etudiant = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numEtudiant);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'étudiant.", e);
        }
    }

    /**
     * Met à jour un étudiant existant. Si le numéro d'étudiant (PK) change,
     * la méthode mettra à jour la clé primaire en utilisant la valeur originale
     * passée en paramètre `originalNumEtudiant`.
     */
    public void updateEtudiant(String originalNumEtudiant, Etudiant etudiant) {
        String sql = "UPDATE ETUDIANT SET num_etudiant = ?, nom = ?, prenoms = ?, niveau = ?, adr_email = ? WHERE num_etudiant = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, etudiant.getNumEtudiant());
            stmt.setString(2, etudiant.getNom());
            stmt.setString(3, etudiant.getPrenoms());
            stmt.setString(4, etudiant.getNiveau());
            stmt.setString(5, etudiant.getAdrEmail());
            stmt.setString(6, originalNumEtudiant);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'étudiant.", e);
        }
    }

    public List<Etudiant> filterEtudiantsParNiveau(String niveau) {
        List<Etudiant> etudiants = new ArrayList<>();
        String sql = "SELECT num_etudiant, nom, prenoms, niveau, adr_email FROM ETUDIANT WHERE niveau = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, niveau);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    etudiants.add(mapEtudiant(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du filtrage des étudiants par niveau.", e);
        }
        return etudiants;
    }

    public int getTotalEtudiants() {
        String sql = "SELECT COUNT(*) AS total FROM ETUDIANT";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du calcul du total des étudiants.", e);
        }
        return 0;
    }

    private Etudiant mapEtudiant(ResultSet rs) throws SQLException {
        Etudiant etudiant = new Etudiant();
        etudiant.setNumEtudiant(rs.getString("num_etudiant"));
        etudiant.setNom(rs.getString("nom"));
        etudiant.setPrenoms(rs.getString("prenoms"));
        etudiant.setNiveau(rs.getString("niveau"));
        etudiant.setAdrEmail(rs.getString("adr_email"));
        return etudiant;
    }
}
