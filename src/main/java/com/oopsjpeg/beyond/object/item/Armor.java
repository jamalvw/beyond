package com.oopsjpeg.beyond.object.item;

import com.oopsjpeg.beyond.object.Item;
import com.oopsjpeg.beyond.object.discord.UserData;

public class Armor implements Item {
    private int id;
    private int health;

    public Armor(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return "Armor (+" + getHealth() + " Health)";
    }

    @Override
    public void onUse(UserData data) {
        data.setArmor(this);
    }

    @Override
    public boolean canUse() {
        return true;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
