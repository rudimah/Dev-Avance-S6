package org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Le label de la catégorie ne peut pas être vide")
    private String label;

    public Category() {
    }

    public Category(String label) {
        this.label = label;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) && Objects.equals(label, category.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label);
    }
}