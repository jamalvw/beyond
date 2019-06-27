package com.oopsjpeg.beyond.object.item;

import com.oopsjpeg.beyond.object.Item;

public class Armor implements Item {
    private int health;

    @Override
    public String getName() {
        return "Armor";
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
