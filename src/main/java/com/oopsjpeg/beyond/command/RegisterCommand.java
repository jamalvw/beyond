package com.oopsjpeg.beyond.command;

import com.oopsjpeg.beyond.Beyond;
import com.oopsjpeg.beyond.Command;
import com.oopsjpeg.beyond.Util;
import com.oopsjpeg.beyond.object.discord.UserData;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;

public class RegisterCommand implements Command {
    @Override
    public void execute(Message message, String alias, String[] args) {
        MessageChannel channel = message.getChannel().block();
        User author = message.getAuthor().orElse(null);

        if (!Beyond.getInstance().hasUser(author)) {
            UserData data = Beyond.getInstance().registerUser(author.getId().asLong());
            data.setLevel(1);
            data.heal();
            Beyond.getInstance().getMongo().saveUser(data);
            Util.sendSuccess(channel, author, "Welcome to **Beyond**!");
        } else {
            Util.sendFailure(channel, author, "You are already registered.");
        }
    }

    @Override
    public String getName() {
        return "register";
    }
}
