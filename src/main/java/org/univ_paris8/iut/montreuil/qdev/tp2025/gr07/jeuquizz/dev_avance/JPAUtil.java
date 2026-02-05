package org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory emf;

    static {
        try {
            // Le nom doit correspondre exactement à celui dans persistence.xml ("MasterAnnoncePU")
            emf = Persistence.createEntityManagerFactory("MasterAnnoncePU");
        } catch (Throwable ex) {
            System.err.println("Initialisation de l'EntityManagerFactory échouée." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}