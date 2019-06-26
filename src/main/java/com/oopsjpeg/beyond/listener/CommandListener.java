package com.oopsjpeg.beyond.listener;

import com.oopsjpeg.beyond.*;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;

import java.util.Arrays;

public class CommandListener implements Listener {
    @Override
    public void register(DiscordClient client) {
        client.getEventDispatcher().on(MessageCreateEvent.class).subscribe(this::onMessage);
    }

    private void onMessage(MessageCreateEvent event) {
        DiscordClient client = event.getClient();
        Message message = event.getMessage();
        User author = message.getAuthor().orElse(null);
        String content = message.getContent().orElse(null);
        MessageChannel channel = message.getChannel().block();
        String prefix = Beyond.getInstance().getSettings().get(Settings.PREFIX);

        if (author != null && content != null && channel != null
                && !author.equals(client.getSelf().block())
                && content.toLowerCase().startsWith(prefix.toLowerCase())) {
            String[] split = content.replaceFirst(prefix, "").split(" ");
            String alias = split[0];
            String[] args = Arrays.copyOfRange(split, 1, split.length);
            Command command = Beyond.getInstance().getCommand(alias);

            if (command != null) {
                if (command.isRegisteredOnly() && !Beyond.getInstance().hasUser(author))
                    Util.sendFailure(channel, author, "You must be registered to use this command.");
                else
                    command.execute(message, alias, args);
            }
        }

    }
}
