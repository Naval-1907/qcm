package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Qcm;

/**
 * DAO pour la table QCM.
 * Toutes les opérations utilisent des PreparedStatement pour prévenir les injections SQL.
 */
public class QcmDAO {

    public List<Qcm> getAllQcms() {
        List<Qcm> qcms = new ArrayList<>();
        String sql = "SELECT num_quest, question, reponse1, reponse2, reponse3, reponse4, bonne_reponse FROM QCM ORDER BY num_quest";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                qcms.add(mapQcm(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des QCM.", e);
        }
        return qcms;
    }

    public Qcm getQcmByNumero(String numQuest) {
        if (numQuest == null) {
            return null;
        }
        String sql = "SELECT num_quest, question, reponse1, reponse2, reponse3, reponse4, bonne_reponse FROM QCM WHERE num_quest = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numQuest);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapQcm(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du QCM.", e);
        }
        return null;
    }

    public void saveOrUpdateQcm(Qcm qcm) {
        // PostgreSQL upsert using ON CONFLICT
        String sql = "INSERT INTO QCM (num_quest, question, reponse1, reponse2, reponse3, reponse4, bonne_reponse) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                     "ON CONFLICT (num_quest) DO UPDATE SET question = EXCLUDED.question, reponse1 = EXCLUDED.reponse1, " +
                     "reponse2 = EXCLUDED.reponse2, reponse3 = EXCLUDED.reponse3, reponse4 = EXCLUDED.reponse4, bonne_reponse = EXCLUDED.bonne_reponse";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, qcm.getNumQuest());
            stmt.setString(2, qcm.getQuestion());
            stmt.setString(3, qcm.getReponse1());
            stmt.setString(4, qcm.getReponse2());
            stmt.setString(5, qcm.getReponse3());
            stmt.setString(6, qcm.getReponse4());
            stmt.setString(7, qcm.getBonneReponse());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du QCM.", e);
        }
    }

    public void deleteQcm(String numQuest) {
        String sql = "DELETE FROM QCM WHERE num_quest = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numQuest);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du QCM.", e);
        }
    }

    /**
     * Met à jour un QCM existant. Si le numéro de question change, la clé est
     * remplacée par la nouvelle valeur fournie dans l'objet `qcm`.
     */
    public void updateQcm(String originalNumQuest, Qcm qcm) {
        String sql = "UPDATE QCM SET num_quest = ?, question = ?, reponse1 = ?, reponse2 = ?, reponse3 = ?, reponse4 = ?, bonne_reponse = ? WHERE num_quest = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, qcm.getNumQuest());
            stmt.setString(2, qcm.getQuestion());
            stmt.setString(3, qcm.getReponse1());
            stmt.setString(4, qcm.getReponse2());
            stmt.setString(5, qcm.getReponse3());
            stmt.setString(6, qcm.getReponse4());
            stmt.setString(7, qcm.getBonneReponse());
            stmt.setString(8, originalNumQuest);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du QCM.", e);
        }
    }

    public List<Qcm> selectRandomQuestions(int requestedCount) {
        int totalAvailable = getTotalQcms();
        if (totalAvailable == 0) {
            // Aucune question disponible : lever une exception claire pour le contrôleur
            throw new IllegalStateException("La banque de questions est vide. Impossible de démarrer un examen.");
        }
        int count = Math.min(requestedCount, totalAvailable);
        List<Qcm> qcms = new ArrayList<>();
        // Use PostgreSQL RANDOM()
        String sql = "SELECT num_quest, question, reponse1, reponse2, reponse3, reponse4, bonne_reponse " +
                 "FROM QCM ORDER BY RANDOM() LIMIT ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, count);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    qcms.add(mapQcm(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sélection aléatoire des questions.", e);
        }
        return qcms;
    }

    public int getTotalQcms() {
        String sql = "SELECT COUNT(*) AS total FROM QCM";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du calcul du total des QCM.", e);
        }
        return 0;
    }

    private Qcm mapQcm(ResultSet rs) throws SQLException {
        Qcm qcm = new Qcm();
        qcm.setNumQuest(rs.getString("num_quest"));
        qcm.setQuestion(rs.getString("question"));
        qcm.setReponse1(rs.getString("reponse1"));
        qcm.setReponse2(rs.getString("reponse2"));
        qcm.setReponse3(rs.getString("reponse3"));
        qcm.setReponse4(rs.getString("reponse4"));
        qcm.setBonneReponse(rs.getString("bonne_reponse"));
        return qcm;
    }
}
