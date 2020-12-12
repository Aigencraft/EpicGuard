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

package me.xneox.epicguard.core.util;

import org.diorite.libs.org.apache.commons.lang3.Validate;
import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.Logger;

public final class URLUtils {
    private static final Logger LOGGER = Logger.getLogger(URLUtils.class.getName());

    @Nullable
    public static String readString(String url) {
        Validate.notNull(url, "URL cannot be null!");

        try {
            URLConnection connection = new URL(url).openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/4.0");
            try (Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8.toString())) {
                scanner.useDelimiter("\\A");
                return scanner.hasNext() ? scanner.next() : "";
            }
        } catch (IOException e) {
            LOGGER.warning("Couldn't read the content of: " + url + " (" + e.getMessage() + "). Please check your internet connection.");
        }
        return null;
    }

    private URLUtils() {}
}
