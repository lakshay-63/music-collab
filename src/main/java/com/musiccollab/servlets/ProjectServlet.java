package com.musiccollab.servlets;

import com.musiccollab.dao.ProjectDAO;
import com.musiccollab.dao.UserDAO;
import com.musiccollab.models.Project;
import com.musiccollab.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/projects")
public class ProjectServlet extends HttpServlet {
    private final ProjectDAO projectDAO = new ProjectDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Project> projects = projectDAO.getAllProjects();
            req.setAttribute("projects", projects);
            req.getRequestDispatcher("/projects.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving projects", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String name = req.getParameter("name");
            String description = req.getParameter("description");
            Long createdById = Long.parseLong(req.getParameter("createdBy"));

            User createdBy = userDAO.getUserById(createdById);
            Project project = new Project();
            project.setName(name);
            project.setDescription(description);
            project.setCreatedBy(createdBy);

            projectDAO.addProject(project);
            resp.sendRedirect("projects");
        } catch (SQLException e) {
            throw new ServletException("Error adding project", e);
        }
    }
}
