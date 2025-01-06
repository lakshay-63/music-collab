package com.musiccollab.servlets;

import com.musiccollab.dao.PortfolioDAO;
import com.musiccollab.dao.UserDAO;
import com.musiccollab.models.Portfolio;
import com.musiccollab.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PortfolioServletTest {

    private PortfolioServlet portfolioServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private HttpSession mockSession;
    private RequestDispatcher mockDispatcher;
    private PortfolioDAO mockPortfolioDAO;
    private UserDAO mockUserDAO;

    @BeforeEach
    void setUp() {
        portfolioServlet = new PortfolioServlet();

        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockSession = mock(HttpSession.class);
        mockDispatcher = mock(RequestDispatcher.class);

        mockPortfolioDAO = mock(PortfolioDAO.class);
        mockUserDAO = mock(UserDAO.class);

        portfolioServlet = new PortfolioServlet() {
            @Override
            protected PortfolioDAO getPortfolioDAO() {
                return mockPortfolioDAO;
            }

            @Override
            protected UserDAO getUserDAO() {
                return mockUserDAO;
            }
        };
    }

    @Test
    void testDoGet_PortfolioFound() throws Exception {
        Long musicianId = 1L;

        Portfolio mockPortfolio = new Portfolio();
        mockPortfolio.setId(1L);
        mockPortfolio.setBio("Test bio");

        when(mockRequest.getParameter("musicianId")).thenReturn(musicianId.toString());
        when(mockPortfolioDAO.getPortfolioByMusicianId(musicianId)).thenReturn(mockPortfolio);
        when(mockRequest.getRequestDispatcher("/portfolio.jsp")).thenReturn(mockDispatcher);

        portfolioServlet.doGet(mockRequest, mockResponse);

        verify(mockRequest, times(1)).setAttribute("portfolio", mockPortfolio);
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    void testDoGet_PortfolioNotFound() throws Exception {
        Long musicianId = 1L;

        when(mockRequest.getParameter("musicianId")).thenReturn(musicianId.toString());
        when(mockPortfolioDAO.getPortfolioByMusicianId(musicianId)).thenReturn(null);
        when(mockRequest.getRequestDispatcher("/portfolio.jsp")).thenReturn(mockDispatcher);

        portfolioServlet.doGet(mockRequest, mockResponse);

        verify(mockRequest, times(1)).setAttribute("portfolio", null);
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    void testDoGet_SQLException() throws Exception {
        Long musicianId = 1L;

        when(mockRequest.getParameter("musicianId")).thenReturn(musicianId.toString());
        when(mockPortfolioDAO.getPortfolioByMusicianId(musicianId)).thenThrow(new SQLException("Database error"));

        ServletException exception = assertThrows(ServletException.class, () -> {
            portfolioServlet.doGet(mockRequest, mockResponse);
        });

        assertTrue(exception.getMessage().contains("Error retrieving portfolio"));
    }

    @Test
    void testDoPost_Success() throws Exception {
        Long musicianId = 1L;
        String bio = "Test bio";

        User mockUser = new User();
        mockUser.setId(musicianId);

        when(mockRequest.getParameter("musicianId")).thenReturn(musicianId.toString());
        when(mockRequest.getParameter("bio")).thenReturn(bio);
        when(mockUserDAO.getUserById(musicianId)).thenReturn(mockUser);

        portfolioServlet.doPost(mockRequest, mockResponse);

        verify(mockUserDAO, times(1)).getUserById(musicianId);
        verify(mockPortfolioDAO, times(1)).addPortfolio(any(Portfolio.class));
        verify(mockResponse, times(1)).sendRedirect("portfolios?musicianId=" + musicianId);
    }

    @Test
    void testDoPost_SQLException() throws Exception {
        Long musicianId = 1L;
        String bio = "Test bio";

        User mockUser = new User();
        mockUser.setId(musicianId);

        when(mockRequest.getParameter("musicianId")).thenReturn(musicianId.toString());
        when(mockRequest.getParameter("bio")).thenReturn(bio);
        when(mockUserDAO.getUserById(musicianId)).thenReturn(mockUser);
        doThrow(new SQLException("Database error")).when(mockPortfolioDAO).addPortfolio(any(Portfolio.class));

        ServletException exception = assertThrows(ServletException.class, () -> {
            portfolioServlet.doPost(mockRequest, mockResponse);
        });

        assertTrue(exception.getMessage().contains("Error creating portfolio"));
    }
}
