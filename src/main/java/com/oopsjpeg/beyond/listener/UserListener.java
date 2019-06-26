package com.oopsjpeg.beyond.listener;

import com.oopsjpeg.beyond.Beyond;
import com.oopsjpeg.beyond.Listener;
import com.oopsjpeg.beyond.Util;
import com.oopsjpeg.beyond.object.discord.UserData;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class UserListener implements Listener {
    @Override
    public void register(DiscordClient client) {
        client.getEventDispatcher().on(ReadyEvent.class).subscribe(this::onReady);
    }

    private void onReady(ReadyEvent event) {
        Beyond.SCHEDULER.scheduleAtFixedRate(() -> Beyond.getInstance().getUsers().values().stream()
                .filter(UserData::canHeal)
                .forEach(data -> data.addHealth(Math.round(data.getMaxHealth() * 0.2f))), 5, 5, TimeUnit.MINUTES);
        Beyond.SCHEDULER.scheduleAtFixedRate(() -> Beyond.getInstance().getUsers().values().stream()
                .filter(UserData::hasJourney)
                .filter(data -> LocalDateTime.now().isAfter(data.getJourney().getStartTIme().plusSeconds(data.getJourney().getDuration())))
                .forEach(data -> {
                    int gold = Util.nextInt(60, 100) * data.getJourney().getLevel();
                    int xp = Util.nextInt(120, 200) * data.getJourney().getLevel();
                    data.addGold(gold);
                    data.addXp(xp);
                    data.setJourney(null);
                    Util.sendSuccess(data.getUser().getPrivateChannel().block(), data.getUser(), "Journey Success",
                            "Gold: **" + gold + "**\n"
                                    + "XP: **" + xp + "**");
                    Beyond.getInstance().getMongo().saveUser(data);
                }), 0, 10, TimeUnit.SECONDS);
        Beyond.LOGGER.info("Started user watcher.");
    }
}
