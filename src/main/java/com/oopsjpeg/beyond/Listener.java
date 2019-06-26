package com.oopsjpeg.beyond;

import discord4j.core.DiscordClient;

public interface Listener {
    void register(DiscordClient client);
}
