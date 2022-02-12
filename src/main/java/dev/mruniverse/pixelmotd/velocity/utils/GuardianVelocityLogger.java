package dev.mruniverse.pixelmotd.velocity.utils;


import com.velocitypowered.api.proxy.ProxyServer;
import dev.mruniverse.pixelmotd.commons.GLogger;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class GuardianVelocityLogger implements GLogger {
    private final String hidePackage;
    private final ProxyServer server;
    private String pluginName = "PixelMOTD";
    private String containIdentifier = "mruniverse";

    /**
     * Call the External Logger
     *
     * @param pluginName this is the name of your plugin.
     * @param hidePackage hide package example: dev.mruniverse.guardianrftb.
     */
    public GuardianVelocityLogger(ProxyServer server, String pluginName, String hidePackage) {
        this.hidePackage = hidePackage;
        this.server = server;
        if (pluginName != null) this.pluginName = pluginName;
    }

    /**
     * Call the External Logger
     *
     * @param pluginName this is the name of your plugin.
     * @param hidePackage hide package example: dev.mruniverse.guardianrftb.
     * @param containIdentifier when a package contain this word this package will show in Internal - StackTrace
     */
    public GuardianVelocityLogger(ProxyServer server,String pluginName,String hidePackage, String containIdentifier) {
        this.hidePackage = hidePackage;
        this.server = server;
        if (pluginName != null) this.pluginName = pluginName;
        if (containIdentifier == null) return;
        this.containIdentifier = containIdentifier;
    }

    /**
     * Send an error message to console.
     * @param message message to send.
     */
    public void error(String message) {
        sendMessage("&f[&cERROR &7| &f" + pluginName + "] " + message);
    }
    /**
     * Send an error message to console.
     * @param throwable throwable to send.
     */
    public void error(Exception throwable) {
        String location = throwable.getClass().getName();
        String error = throwable.getClass().getSimpleName();
        sendMessage("&f[&cERROR &7| &f" + pluginName + "] -------------------------");
        sendMessage("&f[&cERROR &7| &f" + pluginName + "] Location: " + location.replace("." + error,""));
        sendMessage("&f[&cERROR &7| &f" + pluginName + "] Error: " + error);
        if (throwable.getStackTrace() != null) {
            sendMessage("&f[&cERROR &7| &f" + pluginName + "] Internal - StackTrace: ");
            List<StackTraceElement> other = new ArrayList<>();
            for(StackTraceElement line : throwable.getStackTrace()) {
                if (line.toString().contains(containIdentifier)) {
                    sendMessage("&f[&cERROR &7| &f" + pluginName + "] (Line: " + line.getLineNumber() + ") " + line.toString().replace("(" + line.getFileName() + ":" + line.getLineNumber() + ")","").replace(hidePackage,""));
                } else {
                    other.add(line);
                }
            }
            sendMessage("&f[&cERROR &7| &f" + pluginName + "]  -------------------------");
            sendMessage("&f[&cERROR &7| &f" + pluginName + "] External - StackTrace: ");
            for(StackTraceElement line : other) {
                sendMessage("&f[&cERROR &7| &f" + pluginName + "] (Line: " + line.getLineNumber() + ") (Class: " + line.getFileName() + ") (Method: " + line.getMethodName() + ")".replace(".java",""));
            }

        }
        sendMessage("&f[&cERROR &7| &f" + pluginName + "]  -------------------------");
    }

    /**
     * Send a warm message to console.
     * @param message message to send.
     */
    public void warn(String message) {
        sendMessage("&f[&eWARN &7| &f" + pluginName + "] " + message);
    }

    /**
     * Send a debug message to console.
     * @param message message to send.
     */
    public void debug(String message) {
        sendMessage("&f[&9DEBUG &7| &f" + pluginName + "] " + message);
    }

    /**
     * Send an info message to console.
     * @param message message to send.
     */
    public void info(String message) {
        sendMessage("&f[&bINFO &7| &f" + pluginName + "] " + message);
    }



    /**
     * Used to other methods and prevent this copy pasta
     * to those methods.
     *
     * @param message Provided message
     */
    public void sendMessage(String message) {
        server.getConsoleCommandSource().sendMessage(LegacyComponentSerializer.builder().character('&').build().deserialize(message));
    }
}