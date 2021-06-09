package dev.mruniverse.pixelmotd.spigot.cloudlytext.part;

import net.md_5.bungee.api.chat.BaseComponent;

public interface TextPart {

    BaseComponent getPart();

    static TextPartBuilder of(String text){
        return new TextPartBuilder(text);
    }

}
