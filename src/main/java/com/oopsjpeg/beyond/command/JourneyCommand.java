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
            Util.sendFailure(channel, author, "You are already on a journey.");
        } else if (data.getHealth() <= data.getMaxHealth() * 0.1f) {
            Util.sendFailure(channel, author, "You're too low on health to start a journey.\nWait a while to regenerate health or use an item.");
        } else {
            int level = args.length >= 1 ? Math.max(0, Math.min(data.getLevel(), Integer.parseInt(args[0]))) : data.getLevel();
            Journey journey = new Journey(level, Math.round(Math.max(1, data.getLevel() * 0.5f)));
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
