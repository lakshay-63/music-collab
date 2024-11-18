package com.musiccollab.models;

import java.util.List;

public class Portfolio {
    private Long id;
    private User musician;
    private String bio;
    private List<MusicFile> musicSamples;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getMusician() {
        return musician;
    }

    public void setMusician(User musician) {
        this.musician = musician;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<MusicFile> getMusicSamples() {
        return musicSamples;
    }

    public void setMusicSamples(List<MusicFile> musicSamples) {
        this.musicSamples = musicSamples;
    }
}
