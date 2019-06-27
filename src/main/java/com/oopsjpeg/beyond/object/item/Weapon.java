package com.oopsjpeg.beyond.object.item;

import com.oopsjpeg.beyond.object.Item;

public class Weapon implements Item {
    private int damage;

    @Override
    public String getName() {
        return "Weapon";
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
