package com.oopsjpeg.beyond;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oopsjpeg.beyond.command.*;
import com.oopsjpeg.beyond.json.InterfaceAdapter;
import com.oopsjpeg.beyond.listener.CommandListener;
import com.oopsjpeg.beyond.listener.ReadyListener;
import com.oopsjpeg.beyond.listener.UserListener;
import com.oopsjpeg.beyond.object.Item;
import com.oopsjpeg.beyond.object.discord.UserData;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.object.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Beyond {
    public static final Logger LOGGER = LoggerFactory.getLogger(Beyond.class);
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Item.class, new InterfaceAdapter<Item>())
            .setPrettyPrinting().create();
    public static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    private static Beyond instance;

    private MongoManager mongo;
    private DiscordClient client;
    private Settings settings = new Settings(getSettingsFile());
    private List<Listener> listeners = new ArrayList<>();
    private List<Command> commands = new ArrayList<>();

    private Map<Long, UserData> users = new HashMap<>();

    public static Beyond getInstance() {
        return instance;
    }

    public static String getSettingsFile() {
        return "settings.ini";
    }

    public static void main(String[] args) {
        instance = new Beyond();
        instance.start();
    }

    private void start() {
        try {
            if (!new File(settings.getFile()).exists()) {
                // Create settings if it doesn't exist
                settings.save();
                LOGGER.info("Created new settings. Please configure it.");
            } else {
                // Load settings
                settings.load();
                LOGGER.info("Loaded settings.");

                // Create mongo manager
                mongo = new MongoManager(settings.get(Settings.MONGO_HOST), settings.get(Settings.MONGO_DATABASE));
                mongo.loadUsers();

                // Create discord client
                client = new DiscordClientBuilder(settings.get(Settings.TOKEN)).build();

                // Add listeners
                addListener(new ReadyListener());
                addListener(new UserListener());
                addListener(new CommandListener());

                // Add commands
                addCommand(new AccountCommand());
                addCommand(new ItemsCommand());
                addCommand(new JourneyCommand());
                addCommand(new RegisterCommand());
                addCommand(new SellCommand());
                addCommand(new UseCommand());

                // Log in client
                client.login().block();
            }
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    public String getPrefix() {
        return settings.get(Settings.PREFIX);
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
        listener.register(client);
        LOGGER.info("Added new listener of class '" + listener.getClass().getName() + "'.");
    }

    public void addCommand(Command command) {
        commands.add(command);
        LOGGER.info("Added new command of class '" + command.getClass().getName() + "'.");
    }

    public Command getCommand(String alias) {
        return commands.stream().filter(c -> c.getName().equalsIgnoreCase(alias) || Arrays.stream(c.getAliases()).anyMatch(a -> a.equalsIgnoreCase(alias))).findAny().orElse(null);
    }

    public UserData getUser(long id) {
        return users.getOrDefault(id, null);
    }

    public UserData getUser(User user) {
        return getUser(user.getId().asLong());
    }

    public UserData registerUser(long id) {
        UserData ud = new UserData(id);
        users.put(id, ud);
        return ud;
    }

    public boolean hasUser(User user) {
        return users.containsKey(user.getId().asLong());
    }

    public MongoManager getMongo() {
        return mongo;
    }

    public DiscordClient getClient() {
        return client;
    }

    public Settings getSettings() {
        return settings;
    }

    public List<Listener> getListeners() {
        return listeners;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public Map<Long, UserData> getUsers() {
        return users;
    }
}
