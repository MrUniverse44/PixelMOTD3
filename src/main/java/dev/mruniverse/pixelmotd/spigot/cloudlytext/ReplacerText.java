package dev.mruniverse.pixelmotd.spigot.cloudlytext;

import dev.mruniverse.pixelmotd.spigot.cloudlytext.part.TextPart;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReplacerText {

    private final Map<String, TextPart> partsReplace;

    public ReplacerText(){
        partsReplace = new HashMap<>();
    }

    public void replace(String replace, TextPart textPart){
        partsReplace.put(replace,textPart);
    }

    public List<BaseComponent> getReplaces(List<TextPart> parts){
        List<BaseComponent> replaces = new ArrayList<>();
        StringBuilder stringBuilder;

        for(TextPart textPart : parts){
            stringBuilder = new StringBuilder();
            BaseComponent baseComponent = textPart
                    .getPart();

            String[] textParts = baseComponent.toLegacyText()
                    .split(" ");

            for(String word : textParts){

                if(partsReplace.containsKey(word)){

                    createPart(replaces,stringBuilder,baseComponent);
                    replaces.add(partsReplace.get(word).getPart());
                    replaces.add(TextPart.of(" ").create().getPart());

                    stringBuilder = new StringBuilder();
                    continue;
                }

                stringBuilder.append(word).append(" ");
            }

            createPart(replaces,stringBuilder,baseComponent);
        }

        return replaces;
    }

    private void createPart(List<BaseComponent> parts, StringBuilder stringBuilder, BaseComponent baseComponent) {
        TextComponent textComponent = new TextComponent(stringBuilder.toString());
        textComponent.setHoverEvent(baseComponent.getHoverEvent());
        textComponent.setColor(baseComponent.getColor());
        textComponent.setClickEvent(baseComponent.getClickEvent());

        parts.add(textComponent);
    }
}
