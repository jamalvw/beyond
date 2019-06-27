package com.oopsjpeg.beyond.object.item;

import com.oopsjpeg.beyond.object.Item;
import com.oopsjpeg.beyond.object.discord.UserData;

public class HealthPotion implements Item {
    private int id;

    public HealthPotion(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return "Health Potion";
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public void onUse(UserData data) {
        data.addHealth(Math.round(data.getHealth() * 0.15f));
    }

    @Override
    public boolean removeOnUse() {
        return true;
    }
}
