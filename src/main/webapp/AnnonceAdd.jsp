<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Ajouter une Annonce</title>
</head>
<body>
<h3>Création d'une annonce :</h3>
<form action="AnnonceAdd" method="POST">
    <label>Titre :</label><br>
    <input type="text" name="title" placeholder="Titre de l'annonce" required><br><br>

    <label>Description :</label><br>
    <textarea name="description" placeholder="Description détaillée" required></textarea><br><br>

    <label>Adresse :</label><br>
    <input type="text" name="adress" placeholder="Ville ou adresse" required><br><br>

    <label>Email :</label><br>
    <input type="text" name="mail" placeholder="votre@email.com" required><br><br>

    <input type="submit" value="Enregistrer l'annonce">
</form>
</body>
</html>