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

public class AccountCommand implements Command {
    @Override
    public void execute(Message message, String alias, String[] args) {
        MessageChannel channel = message.getChannel().block();
        User author = message.getAuthor().orElse(null);
        UserData data = Beyond.getInstance().getUser(author);
        Journey journey = data.getJourney();

        channel.createMessage(m -> m.setEmbed(e -> {
            e.setColor(Color.ORANGE);
            e.setAuthor(author.getUsername() + "#" + author.getDiscriminator(), null, author.getAvatarUrl());
            e.setDescription("Level: **" + data.getLevel() + "** (" + data.getXp() + " / " + data.getMaxXp() + ")\n"
                    + "Gold: **" + data.getGold() + "**");

            e.addField("Stats", "Health: **" + data.getHealth() + " / " + data.getMaxHealth() + "**\n"
                    + "Damage: **" + data.getDamage() + "**", true);

            e.addField("Armor", (data.hasArmor() ? data.getArmor().getHealth() + " Health" : "None"), true);
            e.addField("Weapon", (data.hasWeapon() ? data.getWeapon().getDamage() + " Damage" : "None"), true);
            e.addField("Total Item(s)", String.valueOf(data.getItems().size()), true);

            if (data.hasJourney())
                e.addField("Journey", Util.timeDiff(journey.getStartTIme(), journey.getStartTIme().plusMinutes(journey.getDuration())) + " left", true);
        })).block();
    }

    @Override
    public String getName() {
        return "account";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"profile", "gold", "info"};
    }

    @Override
    public boolean isRegisteredOnly() {
        return true;
    }
}
