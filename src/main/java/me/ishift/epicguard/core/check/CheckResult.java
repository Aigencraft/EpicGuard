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

package me.ishift.epicguard.core.check;

import me.ishift.epicguard.core.util.ChatUtils;

import java.util.List;

public class CheckResult {
    private final boolean detected;
    private final List<String> kickMessage;

    public CheckResult(boolean detected, List<String> kickMessage) {
        this.detected = detected;
        this.kickMessage = kickMessage;
    }

    public boolean isDetected() {
        return this.detected;
    }

    public String getKickMessage() {
        StringBuilder reason = new StringBuilder();
        for (String string : this.kickMessage) {
            reason.append(ChatUtils.colored(string)).append("\n");
        }
        return reason.toString();
    }

    public static CheckResult undetected() {
        return new CheckResult(false, null);
    }
}