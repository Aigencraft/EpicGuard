package me.ishift.epicguard.bungee.listener;

import me.ishift.epicguard.core.EpicGuard;
import me.ishift.epicguard.core.check.CheckResult;
import me.ishift.epicguard.core.handler.DetectionHandler;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PreLoginListener extends DetectionHandler implements Listener {
    public PreLoginListener(EpicGuard epicGuard) {
        super(epicGuard);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPreLogin(PreLoginEvent event) {
        PendingConnection connection = event.getConnection();
        String address = connection.getAddress().getAddress().getHostAddress();
        String nickname = connection.getName();

        CheckResult result = this.handle(address, nickname);
        if (result.isDetected()) {
            event.setCancelled(true);
            event.setCancelReason(TextComponent.fromLegacyText(result.getKickMessage()));
        }
    }
}
