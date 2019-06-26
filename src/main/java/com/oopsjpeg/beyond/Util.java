package com.oopsjpeg.beyond;

import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.entity.User;
import discord4j.core.spec.EmbedCreateSpec;

import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Util {
    public static final Random RANDOM = new Random();

    public static boolean isDigits(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static String nameThenId(User u) {
        return "**" + u.getUsername() + "#" + u.getDiscriminator() + "**";
    }

    public static int nextInt(int min, int max) {
        return min + RANDOM.nextInt(max - min);
    }

    public static String timeDiff(LocalDateTime date1, LocalDateTime date2) {
        Duration duration = Duration.between(date1, date2);
        Stack<String> stack = new Stack<>();

        if (duration.toDays() > 0) stack.push(duration.toDays() + "d");
        duration = duration.minusDays(duration.toDays());

        if (duration.toHours() > 0) stack.push(duration.toHours() + "h");
        duration = duration.minusHours(duration.toHours());

        if (duration.toMinutes() > 0) stack.push(duration.toMinutes() + "m");
        duration = duration.minusMinutes(duration.toMinutes());

        if (duration.getSeconds() > 0) stack.push(duration.getSeconds() + "s");

        return stack.stream().limit(3).collect(Collectors.joining(" "));
    }

    public static Consumer<? super EmbedCreateSpec> embed(User user, String title, String description, Color color) {
        return e -> e
                .setAuthor(user.getUsername() + "#" + user.getDiscriminator(), null, user.getAvatarUrl())
                .setTitle(title)
                .setDescription(description)
                .setColor(color);
    }

    public static void send(MessageChannel channel, User user, String content) {
        channel.createMessage(m -> m.setEmbed(embed(user, "", content, Color.CYAN))).block();
    }

    public static void send(MessageChannel channel, User user, String title, String content) {
        channel.createMessage(m -> m.setEmbed(embed(user, title, content, Color.CYAN))).block();
    }

    public static void sendError(MessageChannel channel, User user, String content) {
        channel.createMessage(m -> m.setEmbed(embed(user, "", content, new Color(221, 46, 68)))).block();
    }

    public static void sendSuccess(MessageChannel channel, User user, String content) {
        channel.createMessage(m -> m.setEmbed(embed(user, "", content, new Color(119, 178, 85)))).block();
    }

    public static void sendSuccess(MessageChannel channel, User user, String title, String content) {
        channel.createMessage(m -> m.setEmbed(embed(user, title, content, new Color(119, 178, 85)))).block();
    }
}
