package com.oopsjpeg.beyond.object.discord;

import com.oopsjpeg.beyond.Beyond;
import com.oopsjpeg.beyond.object.Item;
import com.oopsjpeg.beyond.object.Journey;
import com.oopsjpeg.beyond.object.item.Armor;
import com.oopsjpeg.beyond.object.item.Weapon;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    private final long id;

    private int health;
    private int level;
    private int xp;
    private int gold;

    private List<Item> items = new ArrayList<>();
    private Armor armor;
    private Weapon weapon;

    private Journey journey;

    public UserData(long id) {
        this.id = id;
    }

    public void heal() {
        health = getMaxHealth();
    }

    public boolean canHeal() {
        return health < getMaxHealth();
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return Beyond.getInstance().getClient().getUserById(Snowflake.of(id)).block();
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(getMaxHealth(), health));
    }

    public void addHealth(int health) {
        setHealth(getHealth() + health);
    }

    public void removeHealth(int health) {
        setHealth(getHealth() - health);
    }

    public int getMaxHealth() {
        return 100 + ((level - 1) * 5) + (hasArmor() ? armor.getHealth() : 0);
    }

    public int getDamage() {
        return 10 + (level - 1) + (hasWeapon() ? weapon.getDamage() : 0);
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
            heal();
        }

        this.xp = xp;
    }

    public void addXp(int xp) {
        setXp(getXp() + xp);
    }

    public int getMaxXp() {
        return (int) Math.pow(65 + (level - 1) * 70, 1.14);
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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Armor getArmor() {
        return armor;
    }

    public void setArmor(Armor armor) {
        this.armor = armor;
    }

    public boolean hasArmor() {
        return armor != null;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public boolean hasWeapon() {
        return weapon != null;
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
