package dao;

import model.Etudiant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour EtudiantDAO : insertion et lecture.
 * Les tests nettoient explicitement la base dans @AfterEach pour éviter toute pollution.
 */
public class EtudiantDAOTest {

    private EtudiantDAO etudiantDAO;
    private String testNum;

    @BeforeEach
    void setUp() {
        etudiantDAO = new EtudiantDAO();
        testNum = "TST" + System.nanoTime();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Nettoyage explicite : suppression de l'étudiant de test
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM ETUDIANT WHERE num_etudiant = ?")) {
            ps.setString(1, testNum);
            ps.executeUpdate();
        }
    }

    @Test
    void testInsertAndFindEtudiant() {
        // Préparer l'objet étudiant
        Etudiant e = new Etudiant();
        e.setNumEtudiant(testNum);
        e.setNom("TestNom");
        e.setPrenoms("TestPrenoms");
        e.setNiveau("L1");
        e.setAdrEmail(testNum + "@example.com");

        // Action: insérer l'étudiant via le DAO
        etudiantDAO.saveOrUpdateEtudiant(e);

        // Lecture depuis la base
        Etudiant fromDb = etudiantDAO.getEtudiantByNumero(testNum);

        // Assertions : vérifier que les champs insérés correspondent à ceux récupérés
        assertNotNull(fromDb, "Après insertion, getEtudiantByNumero doit retourner un objet non null.");
        assertEquals(e.getNumEtudiant(), fromDb.getNumEtudiant(), "Le numéro d'étudiant doit correspondre.");
        assertEquals(e.getNom(), fromDb.getNom(), "Le nom doit être correctement sauvegardé.");
        assertEquals(e.getPrenoms(), fromDb.getPrenoms(), "Les prénoms doivent être correctement sauvegardés.");
        assertEquals(e.getNiveau(), fromDb.getNiveau(), "Le niveau doit être correctement sauvegardé.");
        assertEquals(e.getAdrEmail(), fromDb.getAdrEmail(), "L'email doit être correctement sauvegardé.");
    }

    @Test
    void testUpdateEtudiant() {
        // Insérer d'abord
        Etudiant e = new Etudiant();
        e.setNumEtudiant(testNum);
        e.setNom("InitialNom");
        e.setPrenoms("InitialPrenoms");
        e.setNiveau("L1");
        e.setAdrEmail(testNum + "@example.com");
        etudiantDAO.saveOrUpdateEtudiant(e);

        // Modifier les champs
        e.setNom("NomModifie");
        e.setPrenoms("PrenomsModifie");
        etudiantDAO.updateEtudiant(testNum, e);

        // Lire et vérifier
        Etudiant updated = etudiantDAO.getEtudiantByNumero(testNum);
        assertNotNull(updated, "L'étudiant doit exister après update.");
        assertEquals("NomModifie", updated.getNom(), "Le nom doit refléter la mise à jour.");
        assertEquals("PrenomsModifie", updated.getPrenoms(), "Les prénoms doivent refléter la mise à jour.");
    }
}
