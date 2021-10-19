package dev.mruniverse.pixelmotd.global.enums;

import dev.mruniverse.pixelmotd.global.Motd;

public enum MotdType implements Motd {
    NORMAL{
        @Override
        public String getPath(){
            return "motds.";
        }

        @Override
        public String getSettings(MotdSettings settings){
            if(hasMotd) return getPath() + motd + settings.getPath();
            return settings.getPath();
        }

        @Override
        public String getName() {
            return "Normal";
        }

        @Override
        public IconFolders getIconFolders(){
            return IconFolders.NORMAL;
        }

        @Override
        public boolean isHexMotd() {
            return false;
        }
    },
    NORMAL_HEX{
        @Override
        public String getPath(){
            return "motds-hex.";
        }

        @Override
        public String getName() {
            return "Normal";
        }

        @Override
        public String getSettings(MotdSettings settings){
            if(hasMotd) return getPath() + motd + settings.getPath();
            return settings.getPath();
        }

        @Override
        public IconFolders getIconFolders(){
            return IconFolders.NORMAL;
        }

        @Override
        public boolean isHexMotd() {
            return true;
        }
    },
    WHITELIST{
        @Override
        public String getPath(){
            return "whitelist.";
        }

        @Override
        public String getSettings(MotdSettings settings){
            if(hasMotd) return getPath() + motd + settings.getPath();
            return settings.getPath();
        }

        @Override
        public IconFolders getIconFolders(){
            return IconFolders.NORMAL;
        }

        @Override
        public String getName() {
            return "Whitelist";
        }

        @Override
        public boolean isHexMotd() {
            return false;
        }
    },
    WHITELIST_HEX{
        @Override
        public String getPath(){
            return "whitelist-hex.";
        }

        @Override
        public String getSettings(MotdSettings settings){
            if(hasMotd) return getPath() + motd + settings.getPath();
            return settings.getPath();
        }

        @Override
        public String getName() {
            return "Whitelist";
        }

        @Override
        public IconFolders getIconFolders(){
            return IconFolders.NORMAL;
        }

        @Override
        public boolean isHexMotd() {
            return true;
        }
    },
    BLACKLIST{
        @Override
        public String getPath(){
            return "blacklist.";
        }

        @Override
        public String getSettings(MotdSettings settings){
            if(hasMotd) return getPath() + motd + settings.getPath();
            return settings.getPath();
        }

        @Override
        public IconFolders getIconFolders(){
            return IconFolders.NORMAL;
        }

        @Override
        public String getName() {
            return "Blacklist";
        }

        @Override
        public boolean isHexMotd() {
            return false;
        }
    },
    OUTDATED_SERVER{
        @Override
        public String getPath(){
            return "outdated-server.";
        }

        @Override
        public String getSettings(MotdSettings settings){
            if(hasMotd) return getPath() + motd + settings.getPath();
            return settings.getPath();
        }

        @Override
        public String getName() {
            return "outdatedServer";
        }

        @Override
        public IconFolders getIconFolders(){
            return IconFolders.NORMAL;
        }

        @Override
        public boolean isHexMotd() {
            return false;
        }
    },
    OUTDATED_CLIENT{
        @Override
        public String getPath(){
            return "outdated-client.";
        }

        @Override
        public String getName() {
            return "outdatedClient";
        }

        @Override
        public String getSettings(MotdSettings settings){
            if(hasMotd) return getPath() + motd + settings.getPath();
            return settings.getPath();
        }

        @Override
        public IconFolders getIconFolders(){
            return IconFolders.NORMAL;
        }

        @Override
        public boolean isHexMotd() {
            return false;
        }
    };

    public String motd = "";
    public boolean hasMotd = false;

    public void setMotd(String motd) {
        this.motd = motd;
        hasMotd = true;
    }
}
