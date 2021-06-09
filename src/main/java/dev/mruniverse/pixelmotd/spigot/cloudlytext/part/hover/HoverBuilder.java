package dev.mruniverse.pixelmotd.spigot.cloudlytext.part.hover;

import net.md_5.bungee.api.ChatColor;

import java.util.List;

public class HoverBuilder {

    private final StringBuilder hover;
    public HoverBuilder(String text){
        this();
        append(text);
    }

    public HoverBuilder(){
        hover = new StringBuilder();
    }

    public HoverBuilder append(String text){
        hover.append(ChatColor.translateAlternateColorCodes('&',text)
        ).append("\n");
        return this;
    }

    public HoverBuilder append(List<String> stringList){
        stringList.forEach(this::append);
        return this;
    }

    public String create(){
        hover.delete(hover.length()-1,hover.length());
        return hover.toString();
    }

}
