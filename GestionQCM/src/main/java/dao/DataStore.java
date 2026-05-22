package dao;

import java.util.Arrays;
import java.util.List;

/**
 * Utilitaire léger pour le projet.
 * Cette classe ne stocke plus de données en mémoire, elle fournit uniquement des constantes métier et des helpers.
 */
public class DataStore {

    public static List<String> getAllNiveaux() {
        return Arrays.asList("L1", "L2", "L3", "M1", "M2");
    }

    public static void sendEmailSimulation(String adresse, String message) {
        System.out.println("[Simulation d'envoi d'email] à : " + adresse);
        System.out.println("Contenu : " + message);
    }
}
