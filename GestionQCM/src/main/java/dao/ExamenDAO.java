package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Examen;

/**
 * DAO pour la table EXAMEN.
 * Il gère l'enregistrement des notes et le classement par mérite.
 */
public class ExamenDAO {

    public void registerExamen(Examen examen) {
        String sql = "INSERT INTO EXAMEN (num_exam, num_etudiant, annee_univ, note) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, examen.getNumExam());
            stmt.setString(2, examen.getNumEtudiant());
            stmt.setString(3, examen.getAnneeUniv());
            stmt.setDouble(4, examen.getNote());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement de l'examen.", e);
        }
    }

    public double getAverageExamNote() {
        String sql = "SELECT AVG(note) AS moyenne FROM EXAMEN";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return Math.round(rs.getDouble("moyenne") * 100.0) / 100.0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du calcul de la moyenne des examens.", e);
        }
        return 0.0;
    }

    public List<Examen> getExamensSortedByNoteDesc() {
        List<Examen> examens = new ArrayList<>();
        String sql = "SELECT num_exam, num_etudiant, annee_univ, note FROM EXAMEN ORDER BY note DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                examens.add(mapExamen(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des examens triés.", e);
        }
        return examens;
    }

    public List<Map<String, Object>> getClassementParMerite() {
        List<Map<String, Object>> classement = new ArrayList<>();
        String sql = "SELECT e.num_exam, e.num_etudiant, e.annee_univ, e.note, t.nom, t.prenoms, t.niveau " +
                     "FROM EXAMEN e " +
                     "LEFT JOIN ETUDIANT t ON e.num_etudiant = t.num_etudiant " +
                     "ORDER BY e.note DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> ligne = new HashMap<>();
                ligne.put("numExam", rs.getString("num_exam"));
                ligne.put("numEtudiant", rs.getString("num_etudiant"));
                ligne.put("anneeUniv", rs.getString("annee_univ"));
                ligne.put("note", rs.getDouble("note"));
                ligne.put("nom", rs.getString("nom"));
                ligne.put("prenoms", rs.getString("prenoms"));
                ligne.put("niveau", rs.getString("niveau"));
                classement.add(ligne);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la génération du classement par mérite.", e);
        }
        return classement;
    }

    public String generateNextExamId() {
        // PostgreSQL: extract numeric part and cast to integer
        String sql = "SELECT COALESCE(MAX(CAST(REGEXP_REPLACE(num_exam, '[^0-9]', '', 'g') AS INTEGER)), 0) AS max_id FROM examen";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int maxId = rs.getInt("max_id");
                return String.format("EX%03d", maxId + 1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la génération du prochain numéro d'examen.", e);
        }
        return "EX001";
    }

    private Examen mapExamen(ResultSet rs) throws SQLException {
        Examen examen = new Examen();
        examen.setNumExam(rs.getString("num_exam"));
        examen.setNumEtudiant(rs.getString("num_etudiant"));
        examen.setAnneeUniv(rs.getString("annee_univ"));
        examen.setNote(rs.getDouble("note"));
        return examen;
    }

    /**
     * Compte le nombre d'examens associés à un étudiant donné.
     * Utilisé pour empêcher la suppression destructive d'un étudiant ayant un historique.
     */
    public int countExamsByStudent(String numEtudiant) {
        String sql = "SELECT COUNT(*) AS c FROM EXAMEN WHERE num_etudiant = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numEtudiant);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("c");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du comptage des examens de l'étudiant.", e);
        }
        return 0;
    }
}
