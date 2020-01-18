package me.ishift.epicguard.bukkit.listener;

import me.ishift.epicguard.bukkit.GuardBukkit;
import me.ishift.epicguard.bukkit.manager.*;
import me.ishift.epicguard.bukkit.object.CustomFile;
import me.ishift.epicguard.bukkit.object.User;
import me.ishift.epicguard.bukkit.util.MessagesBukkit;
import me.ishift.epicguard.bukkit.util.Notificator;
import me.ishift.epicguard.bukkit.util.Updater;
import me.ishift.epicguard.bukkit.util.nms.NMSUtil;
import me.ishift.epicguard.universal.AttackType;
import me.ishift.epicguard.universal.Config;
import me.ishift.epicguard.universal.check.GeoCheck;
import me.ishift.epicguard.universal.check.NameContainsCheck;
import me.ishift.epicguard.universal.util.ChatUtil;
import me.ishift.epicguard.universal.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        try {
            final Player p = e.getPlayer();
            final String adress = p.getAddress().getAddress().getHostAddress();

            if (NMSUtil.isOldVersion()) {
                BrandPluginMessageListener.addChannel(p, "MC|BRAND");
            }

            if (Config.antibot && !BlacklistManager.checkWhitelist(adress)) {
                if (NameContainsCheck.check(p.getName())) {
                    e.setJoinMessage("");
                    PlayerQuitListener.hiddenNames.add(p.getName());
                    p.kickPlayer(MessagesBukkit.MESSAGE_KICK_NAMECONTAINS.stream().map(s -> ChatUtil.fix(s) + "\n").collect(Collectors.joining()));
                    return;
                }

                if (BlacklistManager.check(adress)) {
                    e.setJoinMessage("");
                    PlayerQuitListener.hiddenNames.add(p.getName());
                    p.kickPlayer(MessagesBukkit.MESSAGE_KICK_BLACKLIST.stream().map(s -> ChatUtil.fix(s) + "\n").collect(Collectors.joining()));
                    return;
                }

                if (GeoCheck.check(adress)) {
                    e.setJoinMessage("");
                    PlayerQuitListener.hiddenNames.add(p.getName());
                    p.kickPlayer(MessagesBukkit.MESSAGE_KICK_COUNTRY.stream().map(s -> ChatUtil.fix(s) + "\n").collect(Collectors.joining()));
                    return;
                }
            }

            UserManager.addUser(p);
            final User u = UserManager.getUser(p);
            u.setIp(adress);
            Updater.notify(p);
            AttackManager.handleAttack(AttackType.JOIN);

            if (Config.autoWhitelist) {
                Bukkit.getScheduler().runTaskLater(GuardBukkit.getInstance(), () -> {
                    if (p.isOnline()) {
                        BlacklistManager.addWhitelist(adress);
                    }
                }, Config.autoWhitelistTime);
            }

            // IP History manager
            if (Config.ipHistoryEnable) {
                final List<String> history = DataFileManager.getDataFile().getStringList("history." + p.getName());
                if (!history.contains(adress)) {
                    if (!history.isEmpty()) {
                        Notificator.broadcast(MessagesBukkit.HISTORY_NEW.replace("{NICK}", p.getName()).replace("{IP}", adress));
                    }
                    history.add(adress);
                }
                DataFileManager.getDataFile().set("history." + p.getName(), history);
                u.setAdresses(history);
            }

            // Brand Verification
            if (NMSUtil.isOldVersion()) {
                final CustomFile customFile = FileManager.getFile(GuardBukkit.getInstance().getDataFolder() + "/brand.yml");
                Bukkit.getScheduler().runTaskLater(GuardBukkit.getInstance(), () -> {
                    if (!p.isOnline()) {
                        return;
                    }

                    if (customFile.getConfig().getBoolean("channel-verification.enabled")) {
                        if (u.getBrand().equals("none")) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), customFile.getConfig().getString(ChatUtil.fix("channel-verification.punish")).replace("{PLAYER}", p.getName()));
                            Logger.info("Exception occurred in " + p.getName() + "'s connection! If you think this is an issue go to /plugins/EpicGuard/brand.yml file, and replace every 'true' with false. Do NOT report this! This is not a bug!");
                            return;
                        }
                    }
                    if (customFile.getConfig().getBoolean("blocked-brands.enabled")) {
                        for (String string : customFile.getConfig().getStringList("blocked-brands")) {
                            if (u.getBrand().equalsIgnoreCase(string)) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), customFile.getConfig().getString(ChatUtil.fix("blocked-brands.punish")).replace("{PLAYER}", p.getName()));
                            }
                        }
                    }
                }, 50L);
            }
        } catch (Exception ex) {
            Logger.throwException(ex);
        }
    }
}