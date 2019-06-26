package com.oopsjpeg.beyond.object.discord;

import com.oopsjpeg.beyond.Beyond;
import com.oopsjpeg.beyond.object.Journey;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;

public class UserData {
    private final long id;

    private int level;
    private int xp;
    private int gold;
    private Journey journey;

    public UserData(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return Beyond.getInstance().getClient().getUserById(Snowflake.of(id)).block();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void addLevels(int level) {
        setLevel(getLevel() + level);
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        xp = Math.max(0, xp);

        while (xp > getMaxXp()) {
            xp -= getMaxXp();
            addLevels(1);
        }

        this.xp = xp;
    }

    public void addXp(int xp) {
        setXp(getXp() + xp);
    }

    public int getMaxXp() {
        return (int) Math.pow(65 + level * 30, 1.14);
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = Math.max(0, gold);
    }

    public void addGold(int gold) {
        setGold(getGold() + gold);
    }

    public Journey getJourney() {
        return journey;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public boolean hasJourney() {
        return journey != null;
    }
}
