package com.oopsjpeg.beyond.command;

import com.oopsjpeg.beyond.Beyond;
import com.oopsjpeg.beyond.Command;
import com.oopsjpeg.beyond.Util;
import com.oopsjpeg.beyond.object.Item;
import com.oopsjpeg.beyond.object.discord.UserData;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;

public class SellCommand implements Command {
    @Override
    public void execute(Message message, String alias, String[] args) {
        MessageChannel channel = message.getChannel().block();
        User author = message.getAuthor().orElse(null);
        UserData data = Beyond.getInstance().getUser(author);

        if (data.hasJourney())
            Util.sendFailure(channel, author, "You can't sell items while on a journey.");
        else if (data.getItems().isEmpty())
            Util.sendFailure(channel, author, "You don't have any items.");
        else if (args.length == 0)
            Util.sendFailure(channel, author, "You must select an item by ID.");
        else {
            try {
                Item item = data.getItemById(Integer.parseInt(args[0]));
                if (item == null)
                    Util.sendFailure(channel, author, "Invalid item ID.\n"
                            + "Check your item IDs with `" + Beyond.getInstance().getPrefix() + "items`.");
                else if (!item.canSell()) {
                    Util.sendFailure(channel, author, "This item can't be sold.");
                } else {
                    int amount = item.sell();
                    data.getItems().remove(item);
                    Util.sendSuccess(channel, author, item.getName() + " sold for **" + amount + "** gold.");
                    Beyond.getInstance().getMongo().saveUser(data);
                }
            } catch (NumberFormatException error) {
                Util.sendFailure(channel, author, "Invalid item ID.");
            }
        }
    }

    @Override
    public String getName() {
        return "sell";
    }

    @Override
    public boolean isRegisteredOnly() {
        return true;
    }
}
