package dev.mruniverse.pixelmotd.commons;

import dev.mruniverse.pixelmotd.commons.players.PlayersDatabase;

public interface Ping {
    PlayersDatabase database = new PlayersDatabase();

    default PlayersDatabase getPlayerDatabase() {
        return database;
    }

    void update();

    void setWhitelist(boolean status);
}
