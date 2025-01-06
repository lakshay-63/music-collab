package com.musiccollab.servlets;

import com.musiccollab.dao.UserDAO;
import com.musiccollab.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServletTest {

    private UserServlet userServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private RequestDispatcher mockDispatcher;
    private UserDAO mockUserDAO;

    @BeforeEach
    void setUp() {
        userServlet = new UserServlet();

        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockDispatcher = mock(RequestDispatcher.class);

        mockUserDAO = mock(UserDAO.class);

        userServlet = new UserServlet() {
            @Override
            protected UserDAO getUserDAO() {
                return mockUserDAO;
            }
        };
    }

    @Test
    void testDoGet_Success() throws Exception {
        List<User> mockUsers = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("JohnDoe");
        mockUsers.add(user1);

        when(mockUserDAO.getAllUsers()).thenReturn(mockUsers);
        when(mockRequest.getRequestDispatcher("/users.jsp")).thenReturn(mockDispatcher);

        userServlet.doGet(mockRequest, mockResponse);

        verify(mockRequest, times(1)).setAttribute("users", mockUsers);
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    void testDoGet_SQLException() throws Exception {
        when(mockUserDAO.getAllUsers()).thenThrow(new SQLException("Database error"));

        ServletException exception = assertThrows(ServletException.class, () -> {
            userServlet.doGet(mockRequest, mockResponse);
        });

        assertTrue(exception.getMessage().contains("Error retrieving users"));
    }

    @Test
    void testDoPost_Success() throws Exception {
        String username = "JaneDoe";
        String email = "jane@example.com";
        String password = "password123";
        String role = "user";

        when(mockRequest.getParameter("username")).thenReturn(username);
        when(mockRequest.getParameter("email")).thenReturn(email);
        when(mockRequest.getParameter("password")).thenReturn(password);
        when(mockRequest.getParameter("role")).thenReturn(role);

        userServlet.doPost(mockRequest, mockResponse);

        verify(mockUserDAO, times(1)).addUser(any(User.class));
        verify(mockResponse, times(1)).sendRedirect("users");
    }

    @Test
    void testDoPost_SQLException() throws Exception {
        String username = "JaneDoe";
        String email = "jane@example.com";
        String password = "password123";
        String role = "user";

        when(mockRequest.getParameter("username")).thenReturn(username);
        when(mockRequest.getParameter("email")).thenReturn(email);
        when(mockRequest.getParameter("password")).thenReturn(password);
        when(mockRequest.getParameter("role")).thenReturn(role);
        doThrow(new SQLException("Database error")).when(mockUserDAO).addUser(any(User.class));

        ServletException exception = assertThrows(ServletException.class, () -> {
            userServlet.doPost(mockRequest, mockResponse);
        });

        assertTrue(exception.getMessage().contains("Error adding user"));
    }
}
