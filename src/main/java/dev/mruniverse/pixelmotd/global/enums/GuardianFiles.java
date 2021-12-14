package dev.mruniverse.pixelmotd.global.enums;

import dev.mruniverse.pixelmotd.global.utils.GuardianFile;

public enum GuardianFiles implements GuardianFile {
    SETTINGS {
        @Override
        public String getFileName() {
            return "settings.yml";
        }
        @Override
        public String getFolderName() {
            return "";
        }
        @Override
        public boolean isInDifferentFolder() {
            return false;
        }
    },
    MOTDS {
        @Override
        public String getFileName() {
            return "motds.yml";
        }
        @Override
        public String getFolderName() {
            return "";
        }
        @Override
        public boolean isInDifferentFolder() {
            return false;
        }
    },
    WHITELIST {
        @Override
        public String getFileName() {
            return "whitelist.yml";
        }
        @Override
        public String getFolderName() {
            return "modes";
        }
        @Override
        public boolean isInDifferentFolder() {
            return true;
        }
    },
    BLACKLIST {
        @Override
        public String getFileName() {
            return "blacklist.yml";
        }
        @Override
        public String getFolderName() {
            return "modes";
        }
        @Override
        public boolean isInDifferentFolder() {
            return true;
        }
    },
    EVENTS {
        @Override
        public String getFileName() {
            return "events.yml";
        }
        @Override
        public String getFolderName() {
            return "";
        }
        @Override
        public boolean isInDifferentFolder() {
            return false;
        }
    },
    MESSAGES_EN {
        @Override
        public String getFileName() {
            return "messages_en.yml";
        }
        @Override
        public String getFolderName() {
            return "translations";
        }
        @Override
        public boolean isInDifferentFolder() {
            return true;
        }
    },
    MESSAGES_ES {
        @Override
        public String getFileName() {
            return "messages_es.yml";
        }
        @Override
        public String getFolderName() {
            return "translations";
        }
        @Override
        public boolean isInDifferentFolder() {
            return true;
        }
    },
    MESSAGES_PL{
        @Override
        public String getFileName() {
            return "messages_pl.yml";
        }
        @Override
        public String getFolderName() {
            return "translations";
        }
        @Override
        public boolean isInDifferentFolder() {
            return true;
        }
    },
    MESSAGES_JP {
        @Override
        public String getFileName() {
            return "messages_jp.yml";
        }
        @Override
        public String getFolderName() {
            return "translations";
        }
        @Override
        public boolean isInDifferentFolder() {
            return true;
        }
    },
    MESSAGES {
        @Override
        public String getFileName() {
            return "messages_en.yml";
        }
        @Override
        public String getFolderName() {
            return "translations";
        }
        @Override
        public boolean isInDifferentFolder() {
            return true;
        }
    },
    EMERGENCY {
        @Override
        public String getFileName() {
            return "emergency.yml";
        }
        @Override
        public String getFolderName() {
            return "";
        }
        @Override
        public boolean isInDifferentFolder() {
            return false;
        }
    }
}
