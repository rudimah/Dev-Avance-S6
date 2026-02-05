package org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.repository;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.entity.Category;
import javax.persistence.EntityManager;

public class CategoryRepository extends AbstractRepository<Category> {
    public CategoryRepository(EntityManager em) {
        super(em, Category.class);
    }
}