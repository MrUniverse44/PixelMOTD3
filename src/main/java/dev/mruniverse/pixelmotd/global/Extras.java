package dev.mruniverse.pixelmotd.global;

public interface Extras {

    String getVariables(String message,int customOnline,int customMax);

    String getEvents(String message);

    String getCentered(String message);

}
