package me.ishift.commons;

import me.ishift.commons.util.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class PhoenixCommons extends JavaPlugin {
    private Logger logger;

    @Override
    public void onEnable() {
        this.logger = new Logger("Phoenix", "plugins/Phoenix");
    }

    public static PhoenixCommons getPlugin() {
        return JavaPlugin.getPlugin(PhoenixCommons.class);
    }

    public void log(String message) {
        this.logger.info(message);
    }
}
