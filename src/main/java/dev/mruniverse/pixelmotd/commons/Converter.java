package dev.mruniverse.pixelmotd.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Converter {
    public static String ListToString(List<String> list) {
        StringBuilder builder = new StringBuilder();
        int line = 0;
        int maxLine = list.size();
        for (String lines : list) {
            line++;
            if (line != maxLine) {
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

    public static List<String> listReplacer(List<String> list, Map<String,String> replacements) {
        List<String> replaced = new ArrayList<>();
        for(String line : list) {
            replaced.add(replacedLine(line,replacements));
        }
        return replaced;
    }

    private static String replacedLine(String line,Map<String,String> replacements) {
        for(Map.Entry<String,String> entry : replacements.entrySet()) {
            line = line.replace(entry.getKey(),entry.getValue());
        }
        return line;
    }

}
