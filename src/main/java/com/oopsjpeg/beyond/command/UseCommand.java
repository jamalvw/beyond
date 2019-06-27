package com.oopsjpeg.beyond.command;

import com.oopsjpeg.beyond.Beyond;
import com.oopsjpeg.beyond.Command;
import com.oopsjpeg.beyond.Util;
import com.oopsjpeg.beyond.object.Item;
import com.oopsjpeg.beyond.object.discord.UserData;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;

public class UseCommand implements Command {
    @Override
    public void execute(Message message, String alias, String[] args) {
        MessageChannel channel = message.getChannel().block();
        User author = message.getAuthor().orElse(null);
        UserData data = Beyond.getInstance().getUser(author);

        if (data.getItems().isEmpty())
            Util.sendFailure(channel, author, "You don't have any items.");
        else if (args.length == 0)
            Util.sendFailure(channel, author, "You must select an item by ID.");
        else {
            try {
                Item item = data.getItemById(Integer.parseInt(args[0]));
                if (item == null)
                    Util.sendFailure(channel, author, "Invalid item ID.\n"
                            + "Check your item IDs with `" + Beyond.getInstance().getPrefix() + "items`.");
                else if (!item.canUse()) {
                    Util.sendFailure(channel, author, "This item can't be used.");
                } else {
                    item.onUse(data);
                    if (item.removeOnUse())
                        data.getItems().remove(item);
                    Util.sendSuccess(channel, author, item.getName() + " used.");
                    Beyond.getInstance().getMongo().saveUser(data);
                }
            } catch (NumberFormatException error) {
                Util.sendFailure(channel, author, "Invalid item ID.");
            }
        }
    }

    @Override
    public String getName() {
        return "use";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"equip"};
    }

    @Override
    public boolean isRegisteredOnly() {
        return true;
    }
}
