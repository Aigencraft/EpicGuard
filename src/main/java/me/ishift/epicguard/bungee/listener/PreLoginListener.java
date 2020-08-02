/*
 * EpicGuard is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EpicGuard is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

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
        String address = connection.getVirtualHost().getAddress().getHostAddress();
        String nickname = connection.getName();

        CheckResult result = this.handle(address, nickname);
        if (result.isDetected()) {
            event.setCancelled(true);
            event.setCancelReason(TextComponent.fromLegacyText(result.getKickMessage()));
        }
    }
}
