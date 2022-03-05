package dev.mruniverse.pixelmotd.commons;

import java.util.List;

public interface Extras {

    void update();

    String getVariables(String message, int customOnline, int customMax, String user);

    String getEvents(String message);

    String getCentered(String message);

    List<String> getConvertedLines(List<String> lines, int more);

}
