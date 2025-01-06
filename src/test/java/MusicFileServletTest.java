package com.musiccollab.servlets;

import com.musiccollab.dao.MusicFileDAO;
import com.musiccollab.dao.UserDAO;
import com.musiccollab.models.MusicFile;
import com.musiccollab.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MusicFileServletTest {

    private MusicFileServlet musicFileServlet;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;
    private HttpSession mockSession;
    private Part mockFilePart;
    private UserDAO mockUserDAO;
    private MusicFileDAO mockMusicFileDAO;

    @BeforeEach
    void setUp() {
        musicFileServlet = new MusicFileServlet();
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockSession = mock(HttpSession.class);
        mockFilePart = mock(Part.class);

        mockUserDAO = mock(UserDAO.class);
        mockMusicFileDAO = mock(MusicFileDAO.class);

        // Inject mock DAOs
        musicFileServlet = new MusicFileServlet() {
            @Override
            protected UserDAO getUserDAO() {
                return mockUserDAO;
            }

            @Override
            protected MusicFileDAO getMusicFileDAO() {
                return mockMusicFileDAO;
            }
        };
    }

    @Test
    void testDoPost_FileUploadSuccess() throws Exception {
        // Mock request parameters and parts
        String fileName = "test-song.mp3";
        Long uploadedById = 1L;
        String filePath = "uploads" + File.separator + fileName;

        when(mockRequest.getPart("file")).thenReturn(mockFilePart);
        when(mockFilePart.getSubmittedFileName()).thenReturn(fileName);
        when(mockFilePart.getSize()).thenReturn(1024L);
        when(mockRequest.getParameter("uploadedBy")).thenReturn(uploadedById.toString());

        User mockUser = new User();
        mockUser.setId(uploadedById);
        when(mockUserDAO.getUserById(uploadedById)).thenReturn(mockUser);

        // Simulate writing file
        doNothing().when(mockFilePart).write(anyString());

        // Run the servlet's doPost method
        musicFileServlet.doPost(mockRequest, mockResponse);

        // Verify DAO interactions
        verify(mockUserDAO, times(1)).getUserById(uploadedById);
        verify(mockMusicFileDAO, times(1)).addMusicFile(any(MusicFile.class));

        // Verify redirection
        verify(mockResponse, times(1)).sendRedirect("music-files");
    }

    @Test
    void testDoPost_FileUploadFailure_SQLException() throws Exception {
        // Mock request parameters and parts
        String fileName = "test-song.mp3";
        Long uploadedById = 1L;

        when(mockRequest.getPart("file")).thenReturn(mockFilePart);
        when(mockFilePart.getSubmittedFileName()).thenReturn(fileName);
        when(mockFilePart.getSize()).thenReturn(1024L);
        when(mockRequest.getParameter("uploadedBy")).thenReturn(uploadedById.toString());

        User mockUser = new User();
        mockUser.setId(uploadedById);
        when(mockUserDAO.getUserById(uploadedById)).thenReturn(mockUser);

        // Simulate a SQLException when adding the music file
        doThrow(new SQLException("Database error")).when(mockMusicFileDAO).addMusicFile(any(MusicFile.class));

        // Run the servlet's doPost method and assert exception
        ServletException exception = assertThrows(ServletException.class, () -> {
            musicFileServlet.doPost(mockRequest, mockResponse);
        });

        assertTrue(exception.getMessage().contains("Error uploading music file"));

        // Verify DAO interactions
        verify(mockUserDAO, times(1)).getUserById(uploadedById);
        verify(mockMusicFileDAO, times(1)).addMusicFile(any(MusicFile.class));
    }
}
