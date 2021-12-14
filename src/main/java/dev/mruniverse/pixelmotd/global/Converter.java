package dev.mruniverse.pixelmotd.global;

import java.util.List;

public class Converter {
    public static String ListToString(List<String> list) {
        StringBuilder builder = new StringBuilder();
        int line = 0;
        int maxLine = list.size();
        for (String lines : list) {
            line++;
            if(line != maxLine) {
                builder.append(lines).append("\n");
            } else {
                builder.append(lines);
            }
        }
        return builder.toString();
    }

    public static String ListToStringText(List<String> list) {
        StringBuilder builder = new StringBuilder();
        int line = 0;
        int maxLine = list.size();
        for (String lines : list) {
            line++;
            builder.append(" ").append(lines);
        }
        return builder.toString();
    }
}
