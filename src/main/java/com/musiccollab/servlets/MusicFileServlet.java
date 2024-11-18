package com.musiccollab.servlets;

import com.musiccollab.dao.MusicFileDAO;
import com.musiccollab.dao.UserDAO;
import com.musiccollab.models.MusicFile;
import com.musiccollab.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/music-files")
@MultipartConfig
public class MusicFileServlet extends HttpServlet {
    private final MusicFileDAO musicFileDAO = new MusicFileDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Part filePart = req.getPart("file");
            String fileName = filePart.getSubmittedFileName();
            String fileType = fileName.substring(fileName.lastIndexOf('.') + 1);
            Long fileSize = filePart.getSize();
            Long uploadedById = Long.parseLong(req.getParameter("uploadedBy"));

            String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            filePart.write(uploadPath + File.separator + fileName);

            User uploadedBy = userDAO.getUserById(uploadedById);
            MusicFile musicFile = new MusicFile();
            musicFile.setFileName(fileName);
            musicFile.setFileType(fileType);
            musicFile.setFileSize(fileSize);
            musicFile.setUploadedBy(uploadedBy);

            musicFileDAO.addMusicFile(musicFile);
            resp.sendRedirect("music-files");
        } catch (SQLException e) {
            throw new ServletException("Error uploading music file", e);
        }
    }
}
