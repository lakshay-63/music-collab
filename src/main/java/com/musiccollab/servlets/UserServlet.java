package com.musiccollab.servlets;

import com.musiccollab.dao.UserDAO;
import com.musiccollab.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<User> users = userDAO.getAllUsers();
            req.setAttribute("users", users);
            req.getRequestDispatcher("/users.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving users", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String username = req.getParameter("username");
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            String role = req.getParameter("role");

            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(role);

            userDAO.addUser(user);
            resp.sendRedirect("users");
        } catch (SQLException e) {
            throw new ServletException("Error adding user", e);
        }
    }
}
