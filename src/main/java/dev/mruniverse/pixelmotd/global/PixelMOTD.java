package dev.mruniverse.pixelmotd.global;

public abstract class PixelMOTD {
    private FileStorage storage;
    private GLogger logs;
    private InputManager inputManager;

    public void setLogs(GLogger logs) {
        this.logs = logs;
    }

    public GLogger getLogs() {
        return logs;
    }

    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public void setStorage(FileStorage storage) {
        this.storage = storage;
    }

    public FileStorage getFiles() {
        return storage;
    }
}
