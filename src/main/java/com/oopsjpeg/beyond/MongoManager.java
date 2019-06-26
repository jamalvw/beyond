package com.oopsjpeg.beyond;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.util.JSON;
import com.oopsjpeg.beyond.object.discord.UserData;
import org.bson.Document;

import java.util.function.Consumer;

public class MongoManager extends MongoClient {
    private MongoCollection<Document> users;

    public MongoManager(String host, String database) {
        super(host);
        users = getDatabase(database).getCollection("users");
    }

    public MongoCollection<Document> getUsers() {
        return users;
    }

    public void loadUsers() {
        users.find().forEach((Consumer<Document>) this::loadUser);
    }

    public void saveUsers() {
        Beyond.getInstance().getUsers().values().forEach(this::saveUser);
    }

    public void loadUser(Document d) {
        Beyond.getInstance().getUsers().put(d.getLong("_id"), Beyond.GSON.fromJson(JSON.serialize(d), UserData.class));
    }

    public void saveUser(UserData ud) {
        users.replaceOne(Filters.eq(ud.getId()), Document.parse(Beyond.GSON.toJson(ud)), new ReplaceOptions().upsert(true));
    }
}
