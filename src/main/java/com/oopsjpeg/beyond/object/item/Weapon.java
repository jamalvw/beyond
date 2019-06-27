package com.oopsjpeg.beyond.object.item;

import com.oopsjpeg.beyond.object.Item;
import com.oopsjpeg.beyond.object.discord.UserData;

public class Weapon implements Item {
    private int id;
    private int damage;

    public Weapon(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return "Weapon (+" + getDamage() + " Damage)";
    }

    @Override
    public void onUse(UserData data) {
        data.setWeapon(this);
    }

    @Override
    public boolean canUse() {
        return true;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
