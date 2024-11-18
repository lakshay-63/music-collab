package com.musiccollab.servlets;

import com.musiccollab.dao.PortfolioDAO;
import com.musiccollab.dao.UserDAO;
import com.musiccollab.models.Portfolio;
import com.musiccollab.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/portfolios")
public class PortfolioServlet extends HttpServlet {
    private final PortfolioDAO portfolioDAO = new PortfolioDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long musicianId = Long.parseLong(req.getParameter("musicianId"));
            Portfolio portfolio = portfolioDAO.getPortfolioByMusicianId(musicianId);
            req.setAttribute("portfolio", portfolio);
            req.getRequestDispatcher("/portfolio.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving portfolio", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long musicianId = Long.parseLong(req.getParameter("musicianId"));
            String bio = req.getParameter("bio");

            User musician = userDAO.getUserById(musicianId);
            Portfolio portfolio = new Portfolio();
            portfolio.setMusician(musician);
            portfolio.setBio(bio);

            portfolioDAO.addPortfolio(portfolio);
            resp.sendRedirect("portfolios?musicianId=" + musicianId);
        } catch (SQLException e) {
            throw new ServletException("Error creating portfolio", e);
        }
    }
}