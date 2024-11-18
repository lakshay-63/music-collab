package com.musiccollab.models;

import java.util.List;

public class Project {
    private Long id;
    private String name;
    private String description;
    private User createdBy;
    private List<User> collaborators;
    private List<MusicFile> musicFiles;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<User> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<User> collaborators) {
        this.collaborators = collaborators;
    }

    public List<MusicFile> getMusicFiles() {
        return musicFiles;
    }

    public void setMusicFiles(List<MusicFile> musicFiles) {
        this.musicFiles = musicFiles;
    }
}
