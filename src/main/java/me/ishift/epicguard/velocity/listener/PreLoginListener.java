package me.ishift.epicguard.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import me.ishift.epicguard.core.EpicGuard;
import me.ishift.epicguard.core.check.DetectionService;
import me.ishift.epicguard.velocity.util.VelocityUtils;
import net.kyori.text.Component;

public class PreLoginListener extends DetectionService {
    public PreLoginListener(EpicGuard epicGuard) {
        super(epicGuard);
    }

    @Subscribe
    public void onPreLogin(PreLoginEvent event) {
        String address = event.getConnection().getRemoteAddress().getAddress().getHostAddress();
        String nickname = event.getUsername();

        if (this.performCheck(address, nickname)) {
            Component reason = VelocityUtils.getTextComponent(this.getKickMessage());
            event.setResult(PreLoginEvent.PreLoginComponentResult.denied(reason));
        }
    }
}