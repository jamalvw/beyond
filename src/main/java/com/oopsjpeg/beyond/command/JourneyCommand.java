package com.oopsjpeg.beyond.command;

import com.oopsjpeg.beyond.Beyond;
import com.oopsjpeg.beyond.Command;
import com.oopsjpeg.beyond.Util;
import com.oopsjpeg.beyond.object.Journey;
import com.oopsjpeg.beyond.object.discord.UserData;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;

import java.awt.*;

public class JourneyCommand implements Command {
    @Override
    public void execute(Message message, String alias, String[] args) {
        MessageChannel channel = message.getChannel().block();
        User author = message.getAuthor().orElse(null);
        UserData data = Beyond.getInstance().getUser(author);

        if (data.hasJourney()) {
            Util.sendError(channel, author, "You are already on a journey.");
        } else {
            Journey journey = new Journey(1, 1);
            journey.start(data);

            channel.createMessage(m -> m.setEmbed(e -> {
                e.setColor(Color.YELLOW);
                e.setAuthor(author.getUsername() + "#" + author.getDiscriminator(), null, author.getAvatarUrl());
                e.setTitle("Journey Start");
                e.setDescription("__Good luck!__\n\n"
                        + "Journey Level: **" + journey.getLevel() + "**\n"
                        + "Duration: **" + Util.timeDiff(journey.getStartTIme(), journey.getStartTIme().plusMinutes(journey.getDuration())) + "**");

                e.addField("Stats", "Health: **" + data.getHealth() + " / " + data.getMaxHealth() + "**\n"
                        + "Damage: **" + data.getDamage() + "**", true);
                e.addField("Armor", (data.hasArmor() ? data.getArmor().getHealth() + " Health" : "None"), true);
                e.addField("Weapon", (data.hasWeapon() ? data.getWeapon().getDamage() + " Damage" : "None"), true);
            })).block();

            Beyond.getInstance().getMongo().saveUser(data);
        }
    }

    @Override
    public String getName() {
        return "journey";
    }

    @Override
    public boolean isRegisteredOnly() {
        return true;
    }
}
