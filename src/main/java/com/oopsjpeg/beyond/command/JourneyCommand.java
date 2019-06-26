package com.oopsjpeg.beyond.command;

import com.oopsjpeg.beyond.Beyond;
import com.oopsjpeg.beyond.Command;
import com.oopsjpeg.beyond.Util;
import com.oopsjpeg.beyond.object.Journey;
import com.oopsjpeg.beyond.object.discord.UserData;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;

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
            Util.send(channel, author, "Your journey has begun.",
                    "Duration: About **" + Util.timeDiff(journey.getStartTIme(), journey.getStartTIme().plusMinutes(journey.getDuration())) + "**\n" +
                            "Level: **" + journey.getLevel() + "**");
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
