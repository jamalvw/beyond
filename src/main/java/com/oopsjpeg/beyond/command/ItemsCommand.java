package com.oopsjpeg.beyond.command;

import com.oopsjpeg.beyond.Beyond;
import com.oopsjpeg.beyond.Command;
import com.oopsjpeg.beyond.Util;
import com.oopsjpeg.beyond.object.Item;
import com.oopsjpeg.beyond.object.discord.UserData;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemsCommand implements Command {
    @Override
    public void execute(Message message, String alias, String[] args) {
        MessageChannel channel = message.getChannel().block();
        User author = message.getAuthor().orElse(null);
        UserData data = Beyond.getInstance().getUser(author);

        if (data.getItems().isEmpty())
            Util.sendFailure(channel, author, "You don't have any items.");
        else {
            channel.createMessage(m -> m.setEmbed(e -> {
                e.setColor(Color.CYAN);
                e.setAuthor(author.getUsername() + "#" + author.getDiscriminator(), null, author.getAvatarUrl());
                e.setDescription("Use/equip an item with `" + Beyond.getInstance().getPrefix() + "use <id>`.");

                List<Item> items = new ArrayList<>(data.getItems());
                items.removeIf(i -> i.equals(data.getArmor()) || i.equals(data.getWeapon()));

                e.addField("Armor", data.hasArmor() ? "[`" + data.getArmor().getId() + "`] " + data.getArmor().getHealth() + " Health" : "None", true);
                e.addField("Weapon", data.hasWeapon() ? "[`" + data.getWeapon().getId() + "`] " + data.getWeapon().getDamage() + " Damage" : "None", true);
                e.addField("Item(s)", !items.isEmpty() ? items.stream()
                        .map(i -> "[`" + i.getId() + "`] " + i.getName())
                        .collect(Collectors.joining("\n")) : "None", false);
            })).block();
        }
    }

    @Override
    public String getName() {
        return "items";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"equipment"};
    }

    @Override
    public boolean isRegisteredOnly() {
        return true;
    }
}
