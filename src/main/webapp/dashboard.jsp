<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard - Music Collaboration Platform</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <header>
        <h1>Music Collab Dashboard</h1>
        <nav>
            <ul>
                <li><a href="dashboard.jsp">Dashboard</a></li>
                <li><a href="index.jsp">Logout</a></li>
            </ul>
        </nav>
    </header>
    <main>
        <h2>Welcome, <%= request.getParameter("username") %></h2>
        <section id="projects">
            <h3>Your Projects</h3>
            <ul>
                <li><a href="project.jsp?id=1">Project 1</a></li>
                <li><a href="project.jsp?id=2">Project 2</a></li>
            </ul>
        </section>
        <section id="portfolio">
            <h3>Your Portfolio</h3>
            <!-- Add portfolio items here -->
        </section>
    </main>
    <footer>
        <p>&copy; 2023 Music Collab. All rights reserved.</p>
    </footer>
    <script src="script.js"></script>
</body>
</html>