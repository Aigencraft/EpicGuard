package pl.polskistevek.guard.bukkit.util;

import org.bukkit.entity.Player;
import pl.polskistevek.guard.bukkit.GuardPluginBukkit;
import pl.polskistevek.guard.utils.ChatUtil;
import pl.polskistevek.guard.utils.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Updater {
    public static final String currentVersion = GuardPluginBukkit.getPlugin(GuardPluginBukkit.class).getDescription().getVersion();
    public static String lastestVersion;
    public static boolean updateAvaible = false;

    public static void checkForUpdates() {
        if (GuardPluginBukkit.UPDATER) {
            lastestVersion = lookup();
            updateAvaible = !lastestVersion.equals(currentVersion);
            Logger.info("[IMPORTANT - UPDATE AVAILABLE!] Your version is outdated (" + currentVersion + " -> " + lastestVersion + ") - download new version here: https://www.spigotmc.org/resources/72369", false);
        }
    }

    public static void notify(Player p) {
        if (p.hasPermission(GuardPluginBukkit.PERMISSION)) {
            if (Updater.updateAvaible) {
                p.sendMessage(ChatUtil.fix(MessagesBukkit.PREFIX + "&7Your version &8(&c" + currentVersion + "&8) &7is outdated! New version is avaible on SpigotMC &8(&6" + Updater.lastestVersion + "&8)&7."));
                p.sendMessage(ChatUtil.fix(MessagesBukkit.PREFIX + "&7Download latest version, to enjoy new features:"));
                p.sendMessage(ChatUtil.fix(MessagesBukkit.PREFIX + "&6https://www.spigotmc.org/resources/72369"));
            }
        }
    }

    private static String lookup() {
        String line = null;
        try {
            final Scanner scanner = new Scanner(new URL("https://api.spigotmc.org/legacy/update.php?resource=72369").openStream());
            line = scanner.next();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }
}
