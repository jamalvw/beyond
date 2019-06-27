package com.oopsjpeg.beyond.listener;

import com.oopsjpeg.beyond.Beyond;
import com.oopsjpeg.beyond.Listener;
import com.oopsjpeg.beyond.Util;
import com.oopsjpeg.beyond.object.Item;
import com.oopsjpeg.beyond.object.Journey;
import com.oopsjpeg.beyond.object.discord.UserData;
import com.oopsjpeg.beyond.object.item.Armor;
import com.oopsjpeg.beyond.object.item.HealthPotion;
import com.oopsjpeg.beyond.object.item.Weapon;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.MessageChannel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class UserListener implements Listener {
    @Override
    public void register(DiscordClient client) {
        client.getEventDispatcher().on(ReadyEvent.class).subscribe(this::onReady);
    }

    private void onReady(ReadyEvent event) {
        Beyond.SCHEDULER.scheduleAtFixedRate(() -> Beyond.getInstance().getUsers().values().stream()
                .filter(UserData::canHeal)
                .forEach(data -> {
                    data.addHealth(Math.max(0, Math.round(data.getMaxHealth() * 0.1f)));
                    Beyond.getInstance().getMongo().saveUser(data);
                }), 2, 2, TimeUnit.MINUTES);
        Beyond.SCHEDULER.scheduleAtFixedRate(() -> Beyond.getInstance().getUsers().values().stream()
                .filter(UserData::hasJourney)
                .filter(data -> LocalDateTime.now().isAfter(data.getJourney().getStartTIme().plusMinutes(data.getJourney().getDuration())))
                .forEach(data -> {
                    Journey journey = data.getJourney();
                    MessageChannel channel = data.getUser().getPrivateChannel().block();
                    int xp = 60 + (journey.getLevel() * Util.nextInt(20, 40));
                    int gold = Util.nextInt(30, 60) * journey.getLevel();

                    data.removeHealth((40 + (journey.getLevel() * Util.nextInt(9, 14))) - data.getDamage());

                    if (data.getHealth() > 0) {
                        xp *= 1.4f;
                        List<Item> items = new ArrayList<>();

                        for (int i = 0; i < journey.getDuration(); i++) {
                            if (Util.RANDOM.nextFloat() <= Math.max(0.01f, 0.58f / (1 + (journey.getLevel() * 0.34f)))) {
                                int type = Util.RANDOM.nextInt(2);
                                if (type == 0) {
                                    Armor armor = new Armor(data.nextItemId());
                                    armor.setHealth(50 + (journey.getLevel() * Util.nextInt(4, 12)));
                                    items.add(armor);
                                } else if (type == 1) {
                                    Weapon weapon = new Weapon(data.nextItemId());
                                    weapon.setDamage(5 + (journey.getLevel() * Util.nextInt(2, 8)));
                                    items.add(weapon);
                                }
                            }

                            if (Util.RANDOM.nextFloat() <= Math.max(0.07f, 0.67f / (1 + (journey.getLevel() * 0.31)))) {
                                int type = Util.RANDOM.nextInt(1);
                                if (type == 0) {
                                    HealthPotion potion = new HealthPotion(data.nextItemId());
                                    items.add(potion);
                                }
                            }
                        }

                        Util.sendSuccess(channel, data.getUser(), "Journey Success",
                                "Gold: " + Util.comma(gold) + "\n"
                                        + "XP: " + Util.comma(xp) + "\n"
                                        + "Items: " + items.stream().map(Item::getName).collect(Collectors.joining(", ")));
                        data.getItems().addAll(items);
                    } else {
                        gold *= -0.6f;
                        Util.sendFailure(channel, data.getUser(), "Journey Failure", "XP: " + Util.comma(xp));
                    }

                    data.addXp(xp);
                    data.addGold(gold);
                    data.setJourney(null);

                    Beyond.getInstance().getMongo().saveUser(data);
                }), 0, 30, TimeUnit.SECONDS);
        Beyond.LOGGER.info("Started user watcher.");
    }
}
