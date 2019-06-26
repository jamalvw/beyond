package com.oopsjpeg.beyond.object;

import com.oopsjpeg.beyond.object.discord.UserData;

import java.time.LocalDateTime;

public class Journey {
    private final int level;
    private final long duration; // in minutes
    private LocalDateTime startTIme;

    public Journey(int level, long duration) {
        this.level = level;
        this.duration = duration;
    }

    public void start(UserData data) {
        startTIme = LocalDateTime.now();
        data.setJourney(this);
    }

    public int getLevel() {
        return level;
    }

    public long getDuration() {
        return duration;
    }

    public LocalDateTime getStartTIme() {
        return startTIme;
    }

    public void setStartTIme(LocalDateTime startTIme) {
        this.startTIme = startTIme;
    }
}
