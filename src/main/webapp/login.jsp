<%--
  Created by IntelliJ IDEA.
  User: hrahman
  Date: 05/02/2026
  Time: 16:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Connexion - Gestion Annonces</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .login-container {
            background: white;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            width: 100%;
            max-width: 350px;
            text-align: center;
        }
        h2 { margin-bottom: 1.5rem; color: #333; }
        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box; /* Important pour que le padding ne dépasse pas */
        }
        button {
            width: 100%;
            padding: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 1rem;
            margin-top: 10px;
        }
        button:hover { background-color: #0056b3; }
        .error-message {
            color: #d9534f;
            background-color: #f2dede;
            border: 1px solid #ebccd1;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 15px;
            font-size: 0.9em;
        }
    </style>
</head>
<body>

<div class="login-container">
    <h2>Connexion</h2>

    <c:if test="${not empty error}">
        <div class="error-message">
                ${error}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <div>
            <input type="text" name="username" placeholder="Nom d'utilisateur" required autofocus />
        </div>
        <div>
            <input type="password" name="password" placeholder="Mot de passe" required />
        </div>
        <div>
            <button type="submit">Se connecter</button>
        </div>
    </form>
</div>

</body>
</html>