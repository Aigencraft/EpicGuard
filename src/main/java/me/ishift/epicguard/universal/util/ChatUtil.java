package me.ishift.epicguard.universal.util;

import net.md_5.bungee.api.ChatColor;

public class ChatUtil {
    public static String fix(String text) {
        return ChatColor.translateAlternateColorCodes('&', text.replace(">>", "»"));
    }
}
