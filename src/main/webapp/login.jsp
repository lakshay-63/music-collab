<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Music Collaboration Platform</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <header>
        <h1>Music Collab</h1>
    </header>
    <main>
        <h2>Login</h2>
        <form action="dashboard.jsp" method="post">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
            <input type="submit" value="Login">
        </form>
    </main>
    <footer>
        <p>&copy; 2023 Music Collab. All rights reserved.</p>
    </footer>
    <script src="script.js"></script>
</body>
</html>