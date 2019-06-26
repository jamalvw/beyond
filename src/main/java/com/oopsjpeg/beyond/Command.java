package com.oopsjpeg.beyond;

import discord4j.core.object.entity.Message;

public interface Command {
    void execute(Message message, String alias, String[] args);

    String getName();

    default String[] getAliases() {
        return new String[0];
    }

    default boolean isRegisteredOnly() {
        return false;
    }
}
