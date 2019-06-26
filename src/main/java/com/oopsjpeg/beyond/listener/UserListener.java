package com.oopsjpeg.beyond.listener;

import com.oopsjpeg.beyond.Beyond;
import com.oopsjpeg.beyond.Listener;
import com.oopsjpeg.beyond.Util;
import com.oopsjpeg.beyond.object.Journey;
import com.oopsjpeg.beyond.object.discord.UserData;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.MessageChannel;

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
                .filter(data -> LocalDateTime.now().isAfter(data.getJourney().getStartTIme().plusMinutes(data.getJourney().getDuration())))
                .forEach(data -> {
                    Journey journey = data.getJourney();
                    MessageChannel channel = data.getUser().getPrivateChannel().block();
                    int xp;

                    data.removeHealth(journey.getLevel() * 30);

                    if (data.getHealth() > 0) {
                        int gold = Util.nextInt(60, 100) * journey.getLevel();
                        xp = Util.nextInt(120, 180) * journey.getLevel();
                        Util.sendSuccess(channel, data.getUser(), "Journey Success",
                                "Gold: **" + Util.comma(gold) + "**\n"
                                        + "XP: **" + Util.comma(xp) + "**");
                        data.addGold(gold);
                    } else {
                        xp = Util.nextInt(40, 60) * journey.getLevel();
                        Util.sendFailure(channel, data.getUser(), "Journey Failure",
                                "XP: **" + Util.comma(xp) + "**");
                    }

                    data.addXp(xp);
                    data.setJourney(null);

                    Beyond.getInstance().getMongo().saveUser(data);
                }), 0, 1, TimeUnit.MINUTES);
        Beyond.LOGGER.info("Started user watcher.");
    }
}
