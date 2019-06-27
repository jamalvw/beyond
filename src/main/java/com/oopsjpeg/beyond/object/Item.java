package com.oopsjpeg.beyond.object;

import com.oopsjpeg.beyond.object.discord.UserData;

public interface Item {
    int getId();

    String getName();

    default void onUse(UserData data) {

    }

    default boolean canUse() {
        return false;
    }

    default boolean removeOnUse() {
        return false;
    }

    default boolean canSell() {
        return true;
    }

    default int sell() {
        return 20;
    }
}
