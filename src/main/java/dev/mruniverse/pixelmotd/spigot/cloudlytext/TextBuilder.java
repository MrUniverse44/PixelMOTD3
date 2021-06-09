package dev.mruniverse.pixelmotd.spigot.cloudlytext;

import dev.mruniverse.pixelmotd.spigot.cloudlytext.part.TextPart;
import dev.mruniverse.pixelmotd.spigot.cloudlytext.part.TextPartBuilder;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@SuppressWarnings("unused")
public class TextBuilder implements Text {

    private final List<TextPart> textParts;
    private final Map<String, TextPart> partsReplace;
    private final ReplacerText replacerText;

    public TextBuilder(){
        textParts = new ArrayList<>();
        partsReplace = new HashMap<>();
        replacerText = new ReplacerText();
    }

    public TextBuilder add(TextPartBuilder partBuilder){
        textParts.add(partBuilder.create());
        return this;
    }

    public TextBuilder replace(String replacer, TextPartBuilder textPartBuilder) {
        replacerText.replace(replacer,textPartBuilder.create());
        return this;
    }

    @Override
    public BaseComponent[] create() {

        List<BaseComponent> partsWithReplaces = replacerText.getReplaces(
                textParts);

        if(partsWithReplaces.isEmpty()){
            BaseComponent[] components = new BaseComponent[textParts.size()];

            for(int i=0;i<textParts.size();i++){
                components[i] = textParts.get(i).getPart();
            }
            return components;
        }

        BaseComponent[] components = new BaseComponent[partsWithReplaces.size()];
        for(int i=0;i<partsWithReplaces.size();i++){
            components[i] = partsWithReplaces.get(i);
        }
        return components;
    }

    public Map<String, TextPart> getPartsReplace() {
        return partsReplace;
    }
}
