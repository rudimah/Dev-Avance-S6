package org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Entity
@Table(name = "annonces")
public class Annonce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false)
    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 64, message = "Le titre ne doit pas dépasser 64 caractères")
    private String title;

    @Column(length = 256)
    @Size(max = 256, message = "La description ne doit pas dépasser 256 caractères")
    private String description;

    @Column(length = 64)
    @Size(max = 64, message = "L'adresse ne doit pas dépasser 64 caractères")
    private String address;

    @Column(length = 64)
    @Email(message = "Le format de l'email de contact est invalide")
    @Size(max = 64, message = "L'email ne doit pas dépasser 64 caractères")
    private String mail;

    @Column(nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    @NotNull
    private AnnonceStatus status;

    // Relation ManyToOne vers User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @NotNull(message = "Une annonce doit avoir un auteur")
    private User author;

    // Relation ManyToOne vers Category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "Une annonce doit appartenir à une catégorie")
    private Category category;

    public Annonce() {
    }

    @PrePersist
    protected void onCreate() {
        if (this.date == null) {
            this.date = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = AnnonceStatus.DRAFT;
        }
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public AnnonceStatus getStatus() { return status; }
    public void setStatus(AnnonceStatus status) { this.status = status; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}