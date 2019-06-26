package com.oopsjpeg.beyond.listener;

import com.oopsjpeg.beyond.Beyond;
import com.oopsjpeg.beyond.Listener;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;

public class ReadyListener implements Listener {
    @Override
    public void register(DiscordClient client) {
        client.getEventDispatcher().on(ReadyEvent.class).subscribe(this::onReady);
    }

    private void onReady(ReadyEvent event) {
        DiscordClient client = event.getClient();
        Beyond.LOGGER.info("Ready on " + client.getGuilds().count().block() + " guild(s) with " + client.getUsers().count().block() + " user(s).");
    }
}
