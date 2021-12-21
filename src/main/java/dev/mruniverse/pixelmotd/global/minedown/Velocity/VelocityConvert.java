package dev.mruniverse.pixelmotd.global.minedown.Velocity;

import dev.mruniverse.pixelmotd.global.minedown.MineDown;
import dev.mruniverse.pixelmotd.global.minedown.MineDownParser;
import dev.mruniverse.pixelmotd.global.minedown.Replacer;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.Map;

import static dev.mruniverse.pixelmotd.global.minedown.MineDown.stringify;

public class VelocityConvert {

    private final boolean replaceFirst;

    private final Replacer replacer;

    private final MineDownParser parser;

    private final String message;

    private BaseComponent[] baseComponents;


    public VelocityConvert(BaseComponent[] baseComponents,String message, boolean replaceFirst, Replacer replacer, MineDownParser parser) {
        this.baseComponents = baseComponents;
        this.message = message;
        this.replaceFirst = replaceFirst;
        this.replacer = replacer;
        this.parser = parser;
    }

    public BaseComponent[] get() {
        if (baseComponents == null) {
            if (replaceFirst) {
                Replacer componentReplacer = new Replacer();
                for (Map.Entry<String, BaseComponent[]> entry : replacer.componentReplacements().entrySet()) {
                    componentReplacer.replace(entry.getKey(), stringify(entry.getValue()));
                }
                baseComponents = parser.parse(componentReplacer.replaceIn(replacer.replaceIn(message))).create();
            } else {
                baseComponents = replacer.replaceIn(parser.parse(message).create());
            }
        }
        return baseComponents;
    }
}
