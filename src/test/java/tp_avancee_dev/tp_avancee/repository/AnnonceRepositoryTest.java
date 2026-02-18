package tp_avancee_dev.tp_avancee.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import tp_avancee_dev.tp_avancee.model.Annonce;
import tp_avancee_dev.tp_avancee.model.Category;
import tp_avancee_dev.tp_avancee.model.Status;
import tp_avancee_dev.tp_avancee.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AnnonceRepositoryTest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private AnnonceRepository repository;

    @BeforeAll
    public void init() {
        // On charge la configuration "testPU" définie dans src/test/resources/...
        emf = Persistence.createEntityManagerFactory("testPU");
        repository = new AnnonceRepository();
    }

    @AfterAll
    public void tearDown() {
        if (emf != null) emf.close();
    }

    @BeforeEach
    public void setup() {
        em = emf.createEntityManager();
        // Nettoyage et chargement des données avant chaque test
        loadDataSet();
    }

    @AfterEach
    public void cleanup() {
        if (em != null) em.close();
    }


    private void loadDataSet() {
        em.getTransaction().begin();

        // 1. Nettoyage préalable (ordre inverse des dépendances)
        em.createQuery("DELETE FROM Annonce").executeUpdate();
        em.createQuery("DELETE FROM Category").executeUpdate();
        em.createQuery("DELETE FROM User").executeUpdate();

        // 2. Création User
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@test.com");
        user.setPassword("password");
        em.persist(user);

        // 3. Création Catégorie
        Category cat = new Category();
        cat.setLabel("Informatique");
        em.persist(cat);

        // 4. Création de 15 Annonces pour tester la pagination
        for (int i = 1; i <= 15; i++) {
            Annonce a = new Annonce();
            a.setTitle("Annonce " + i);
            a.setDescription("Description " + i);
            a.setAuthor(user);
            a.setCategory(cat);
            a.setStatus(Status.PUBLISHED);
            em.persist(a);
        }

        em.getTransaction().commit();
    }

    @Test
    public void testPagination() {
        // Exercice 8.4 : Tester la pagination

        // Page 1, taille 10 => doit retourner les 10 premières (les plus récentes d'abord)
        List<Annonce> page1 = repository.findAllPaginated(em, 1, 10);
        assertEquals(10, page1.size(), "La page 1 doit contenir 10 éléments");

        // Page 2, taille 10 => doit retourner les 5 restantes (car 15 au total)
        List<Annonce> page2 = repository.findAllPaginated(em, 2, 10);
        assertEquals(5, page2.size(), "La page 2 doit contenir 5 éléments");
    }

    @Test
    public void testSearchByKeyword() {
        // Test bonus pour vérifier ta méthode de recherche
        List<Annonce> results = repository.searchByKeywordPaginated(em, "Annonce 1", 1, 10);
        // "Annonce 1", "Annonce 10", "Annonce 11"... "Annonce 15" => Ça fait pas mal de résultats
        assertFalse(results.isEmpty());
        assertTrue(results.get(0).getTitle().contains("Annonce"));
    }
}

