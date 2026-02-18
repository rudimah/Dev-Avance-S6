package tp_avancee_dev.tp_avancee.service;

import jakarta.persistence.EntityManager;
import jakarta.ws.rs.ForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tp_avancee_dev.tp_avancee.api.exceptions.BusinessConflictException;
import tp_avancee_dev.tp_avancee.db.EntityManagerUtil;
import tp_avancee_dev.tp_avancee.model.Annonce;
import tp_avancee_dev.tp_avancee.model.Category;
import tp_avancee_dev.tp_avancee.model.Status;
import tp_avancee_dev.tp_avancee.model.User;
import tp_avancee_dev.tp_avancee.repository.AnnonceRepository;
import tp_avancee_dev.tp_avancee.repository.CategoryRepository;

import java.util.List;

public class AnnonceService {

    private final AnnonceRepository annonceRepository;
    private static final Logger logger = LoggerFactory.getLogger(AnnonceService.class);
    private final CategoryRepository categoryRepository;

    public AnnonceService() {
        this.annonceRepository = new AnnonceRepository();
        this.categoryRepository = new CategoryRepository();
    }

    public Annonce getAnnonceById(Long id) {
        logger.info("test ");
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            return annonceRepository.findByIdWithRelations(em, id);
        } finally {
            em.close();
        }
    }

    public Annonce createAnnonce(String title, String description, String adress, String mail, Long authorId, Long categoryId) {
        logger.info("Tentative de création d'annonce par l'auteur ID: {}", authorId);
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            em.getTransaction().begin();

            User author = em.find(User.class, authorId);
            Category category = em.find(Category.class, categoryId);

            if (author == null) throw new IllegalArgumentException("Auteur introuvable");
            if (category == null) throw new IllegalArgumentException("Catégorie introuvable");

            Annonce annonce = new Annonce(title, description, adress, mail, author, category);

            annonceRepository.create(em, annonce);
            em.getTransaction().commit();
            logger.info("Annonce créée avec succès : ID={}", annonce.getId());
            return annonce;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            logger.error("Erreur lors de la création de l'annonce", e);
            throw e;
        } finally {
            em.close();
        }
    }

    public Annonce updateAnnonce(Long id, String title, String description, String adress, String mail, Long categoryId, Long requesterId) {
        logger.info("Tentative de mis a jour de  l'annonce : {}", title);
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            em.getTransaction().begin();

            Annonce annonce = annonceRepository.findById(em, id);
            if (annonce == null) {
                em.getTransaction().rollback();
                return null;
            }

            if (!annonce.getAuthor().getId().equals(requesterId)) {
                em.getTransaction().rollback();
                throw new ForbiddenException("Vous n'êtes pas l'auteur de cette annonce.");
            }

            if (annonce.getStatus() == Status.PUBLISHED) {
                em.getTransaction().rollback();
                throw new BusinessConflictException("Impossible de modifier une annonce publiée.");
            }

            annonce.setTitle(title);
            annonce.setDescription(description);
            annonce.setAdress(adress);
            annonce.setMail(mail);

            if (categoryId != null) {
                Category cat = categoryRepository.findById(em, categoryId);
                if (cat != null) annonce.setCategory(cat);
            }

            em.getTransaction().commit();
            logger.info("Annonce mise a joru avec succès : ID={}", id);
            return annonce;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            logger.error("Erreur lors de la mis a jour de l'annonce : {}", id, e);
            throw e;
        } finally {
            em.close();
        }
    }

    public boolean deleteAnnonce(Long id, Long requesterId) {
        logger.info("Tentative de suppression de  l'annonce : {}", id);
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            em.getTransaction().begin();

            Annonce annonce = annonceRepository.findById(em, id);
            if (annonce == null) {
                em.getTransaction().rollback();
                return false;
            }

            if (!annonce.getAuthor().getId().equals(requesterId)) {
                em.getTransaction().rollback();
                throw new ForbiddenException("Vous n'êtes pas l'auteur.");
            }

            if (annonce.getStatus() != Status.ARCHIVED) {
                em.getTransaction().rollback();
                throw new BusinessConflictException("L'annonce doit être archivée avant suppression.");
            }

            annonceRepository.delete(em, id);
            em.getTransaction().commit();
            logger.info("suppression de  l'annonce : {} reussie", id);
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            logger.info("Erreur lors de suppression de  l'annonce : {}", id, e);
            throw e;
        } finally {
            em.close();
        }
    }


    public List<Annonce> listAnnoncesPaginated(int page, int size) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            return annonceRepository.findAllPaginated(em, page, size);
        } finally {
            em.close();
        }
    }

    public List<Annonce> searchAnnonces(String keyword, int page, int size) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            return annonceRepository.searchByKeywordPaginated(em, keyword, page, size);
        } finally {
            em.close();
        }
    }

    public Annonce changeStatusTo(Long annonceId, Status newStatus) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            Annonce annonce = annonceRepository.findById(em, annonceId);
            if(annonce != null) {
                annonce.setStatus(newStatus);
            }
            em.getTransaction().commit();
            return annonce;
        } finally {
            em.close();
        }
    }
}