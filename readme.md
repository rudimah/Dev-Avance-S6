# 📢 MasterAnnonce Backend API

## 📝 Présentation
Ce projet transforme l'application **MasterAnnonce** en une API REST Java professionnelle, sécurisée et industrialisée. Conformément aux consignes, aucune dépendance à **Spring** n'est utilisée, au profit de technologies standard **Jakarta EE** [cite: 20-22].

## 🏗️ Architecture
L'application adopte une architecture en couches pour une séparation nette des responsabilités :
* **Client** : Postman ou Front JS communiquant en JSON .
* **API REST (JAX-RS)** : Points d'entrée `/api` gérant les ressources de manière stateless.
* **Service** : Couche contenant la logique métier et la gestion des transactions
* **Repository (JPA/Hibernate)** : Abstraction de l'accès aux données PostgreSQL.



## 🔐 Sécurité & Flux d'Authentification (JAAS)
Le système de sécurité est entièrement **stateless** et repose sur **JAAS** (Java Authentication and Authorization Service):

1.  **Authentification Initiale** : L'utilisateur envoie ses identifiants au endpoint `/api/login` Le `DbLoginModule` vérifie les credentials en base de données via `UserRepository`[cite: 156].
2.  **Génération de Token** : En cas de succès, un token opaque est généré et renvoyé au client .
3.  **Appels Protégés** : Le client envoie le token dans le header `Authorization: Bearer <token>` pour chaque requête.
4.  **Reconstruction de l'Identité** : Un filtre JAX-RS intercepte la requête et utilise le `TokenLoginModule` pour valider le token et peupler le `Subject` avec les `UserPrincipal` et `RolePrincipal` sans utiliser de session HTTP [cite: 181-186, 189-200].

## 🛠️ Règles Métier Avancées 
Le service applique des règles strictes pour garantir l'intégrité des données:
* **Propriété** : Seul l'auteur peut modifier ou supprimer son annonce.
* **Statut** : Une annonce `PUBLISHED` est verrouillée et ne peut plus être modifiée
* **Suppression** : Une annonce doit impérativement être passée en statut `ARCHIVED` avant d'être supprimée
* **Concurrence** : Utilisation de l'annotation `@Version` pour gérer les accès concurrents (Optimistic Locking)

## 📈 Industrialisation & Qualité
* **Documentation** : Spécification OpenAPI disponible via Swagger à l'adresse `/api/openapi.json`
* **Logging** : Utilisation de **Logback** pour un logging structuré (Date, Niveau, Thread, Message)

## ⚠️ Problèmes rencontrés & Solutions
* **Problème : Conflits de versions Maven.** *
* **Problème : Erreur 400 lors des tests REST.** * *Solution* : Activation de `JacksonFeature` dans la configuration du `JerseyTest` pour permettre la désérialisation correcte des payloads JSON.
