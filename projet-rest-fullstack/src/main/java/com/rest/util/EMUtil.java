package com.rest.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Classe utilitaire pour gérer l'EntityManager
 * Pattern Singleton pour EntityManagerFactory
 */
public class EMUtil {
    
    private static EntityManagerFactory emf = null;
    private static final String PERSISTENCE_UNIT_NAME = "PersonPU";

    // Constructeur privé pour empêcher l'instanciation
    private EMUtil() {
    }

    /**
     * Obtient l'EntityManagerFactory (créée une seule fois)
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
                System.out.println("EntityManagerFactory créée avec succès");
            } catch (Exception e) {
                System.err.println("Erreur lors de la création de l'EntityManagerFactory");
                e.printStackTrace();
            }
        }
        return emf;
    }

    /**
     * Crée un nouvel EntityManager
     */
    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    /**
     * Ferme l'EntityManagerFactory
     */
    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("EntityManagerFactory fermée");
        }
    }
}