package org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.service;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.entity.Annonce;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.entity.AnnonceStatus;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.entity.Category;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.entity.User;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.repository.AnnonceRepository;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.repository.CategoryRepository;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.repository.UserRepository;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Collections;
import java.util.List;

public class AnnonceService {

    /**
     * 1.a Création d'annonce
     * Gère la transaction et l'association avec User et Category
     */
    public void createAnnonce(Annonce annonce, Long userId, Long categoryId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Instanciation des repositories avec l'EM courant
            UserRepository userRepo = new UserRepository(em);
            CategoryRepository catRepo = new CategoryRepository(em);
            AnnonceRepository annonceRepo = new AnnonceRepository(em);

            // Récupération des entités liées
            User author = userRepo.findById(userId);
            Category category = catRepo.findById(categoryId);

            if (author == null || category == null) {
                throw new IllegalArgumentException("Auteur ou Catégorie introuvable");
            }

            // Association
            annonce.setAuthor(author);
            annonce.setCategory(category);
            // Le statut et la date sont gérés par le @PrePersist dans l'entité,
            // mais on peut forcer le statut ici si besoin
            annonce.setStatus(AnnonceStatus.DRAFT);

            // Persistance
            annonceRepo.save(annonce);

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Erreur lors de la création de l'annonce", e);
        } finally {
            em.close();
        }
    }

    /**
     * 1.b Modification d'annonce
     */
    public void updateAnnonce(Annonce annonceModifiee) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            AnnonceRepository annonceRepo = new AnnonceRepository(em);

            // On vérifie que l'annonce existe
            Annonce existing = annonceRepo.findById(annonceModifiee.getId());
            if (existing != null) {
                // Mise à jour des champs (on évite d'écraser l'auteur ou la date de création par erreur)
                existing.setTitle(annonceModifiee.getTitle());
                existing.setDescription(annonceModifiee.getDescription());
                existing.setAddress(annonceModifiee.getAddress());
                existing.setMail(annonceModifiee.getMail());
                existing.setCategory(annonceModifiee.getCategory());

                annonceRepo.update(existing);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Erreur lors de la modification", e);
        } finally {
            em.close();
        }
    }

    /**
     * 1.c Publication d'annonce (Changement de statut)
     */
    public void publishAnnonce(Long annonceId) {
        updateStatus(annonceId, AnnonceStatus.PUBLISHED);
    }

    /**
     * 1.d Archivage d'annonce (Changement de statut)
     */
    public void archiveAnnonce(Long annonceId) {
        updateStatus(annonceId, AnnonceStatus.ARCHIVED);
    }

    // Méthode utilitaire privée pour éviter la duplication de code sur les changements de statut
    private void updateStatus(Long annonceId, AnnonceStatus newStatus) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            AnnonceRepository repo = new AnnonceRepository(em);
            Annonce annonce = repo.findById(annonceId);
            if (annonce != null) {
                annonce.setStatus(newStatus);
                repo.update(annonce);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Erreur lors du changement de statut", e);
        } finally {
            em.close();
        }
    }

    /**
     * 1.e Suppression d'annonce
     */
    public void deleteAnnonce(Long annonceId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            new AnnonceRepository(em).delete(annonceId);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Erreur lors de la suppression", e);
        } finally {
            em.close();
        }
    }

    /**
     * 1.f Recherche et Listing paginé
     * Note: Même pour de la lecture, on ouvre/ferme l'EM proprement.
     */
    public List<Annonce> searchAnnonces(String keyword, AnnonceStatus status, int page, int size) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            AnnonceRepository repo = new AnnonceRepository(em);
            // Appel à la méthode "search" créée dans l'exercice précédent (bonus)
            // ou combinaison de findAllPaginated / findByKeyword selon votre implémentation
            return repo.search(keyword, status, page, size);
        } catch (Exception e) {
            e.printStackTrace(); // Log
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    /**
     * Méthode utilitaire pour récupérer une annonce par ID (pour affichage détail ou form update)
     */
    public Annonce getAnnonceById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return new AnnonceRepository(em).findById(id);
        } finally {
            em.close();
        }
    }

    /**
     * Récupérer toutes les catégories (pour peupler les listes déroulantes dans les vues)
     */
    public List<Category> getAllCategories() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return new CategoryRepository(em).findAll();
        } finally {
            em.close();
        }
    }
}