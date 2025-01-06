package com.musiccollab.servlets;

import com.musiccollab.dao.ProjectDAO;
import com.musiccollab.dao.UserDAO;
import com.musiccollab.models.Project;
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

class ProjectServletTest {

    private ProjectServlet projectServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private RequestDispatcher mockDispatcher;
    private ProjectDAO mockProjectDAO;
    private UserDAO mockUserDAO;

    @BeforeEach
    void setUp() {
        projectServlet = new ProjectServlet();

        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockDispatcher = mock(RequestDispatcher.class);

        mockProjectDAO = mock(ProjectDAO.class);
        mockUserDAO = mock(UserDAO.class);

        projectServlet = new ProjectServlet() {
            @Override
            protected ProjectDAO getProjectDAO() {
                return mockProjectDAO;
            }

            @Override
            protected UserDAO getUserDAO() {
                return mockUserDAO;
            }
        };
    }

    @Test
    void testDoGet_Success() throws Exception {
        List<Project> mockProjects = new ArrayList<>();
        Project project1 = new Project();
        project1.setName("Project 1");
        project1.setDescription("Description 1");
        mockProjects.add(project1);

        when(mockProjectDAO.getAllProjects()).thenReturn(mockProjects);
        when(mockRequest.getRequestDispatcher("/projects.jsp")).thenReturn(mockDispatcher);

        projectServlet.doGet(mockRequest, mockResponse);

        verify(mockRequest, times(1)).setAttribute("projects", mockProjects);
        verify(mockDispatcher, times(1)).forward(mockRequest, mockResponse);
    }

    @Test
    void testDoGet_SQLException() throws Exception {
        when(mockProjectDAO.getAllProjects()).thenThrow(new SQLException("Database error"));

        ServletException exception = assertThrows(ServletException.class, () -> {
            projectServlet.doGet(mockRequest, mockResponse);
        });

        assertTrue(exception.getMessage().contains("Error retrieving projects"));
    }

    @Test
    void testDoPost_Success() throws Exception {
        Long createdById = 1L;
        String name = "New Project";
        String description = "A test project";

        User mockUser = new User();
        mockUser.setId(createdById);

        when(mockRequest.getParameter("name")).thenReturn(name);
        when(mockRequest.getParameter("description")).thenReturn(description);
        when(mockRequest.getParameter("createdBy")).thenReturn(createdById.toString());
        when(mockUserDAO.getUserById(createdById)).thenReturn(mockUser);

        projectServlet.doPost(mockRequest, mockResponse);

        verify(mockUserDAO, times(1)).getUserById(createdById);
        verify(mockProjectDAO, times(1)).addProject(any(Project.class));
        verify(mockResponse, times(1)).sendRedirect("projects");
    }

    @Test
    void testDoPost_SQLException() throws Exception {
        Long createdById = 1L;
        String name = "New Project";
        String description = "A test project";

        User mockUser = new User();
        mockUser.setId(createdById);

        when(mockRequest.getParameter("name")).thenReturn(name);
        when(mockRequest.getParameter("description")).thenReturn(description);
        when(mockRequest.getParameter("createdBy")).thenReturn(createdById.toString());
        when(mockUserDAO.getUserById(createdById)).thenReturn(mockUser);
        doThrow(new SQLException("Database error")).when(mockProjectDAO).addProject(any(Project.class));

        ServletException exception = assertThrows(ServletException.class, () -> {
            projectServlet.doPost(mockRequest, mockResponse);
        });

        assertTrue(exception.getMessage().contains("Error adding project"));
    }
}
