
package com.scrambleticket.client.cookie;

import java.util.ArrayList;
import java.util.List;

public class _embedded_cookie {

    private static final List<Pair> pairs = new ArrayList<>();
    private static final List<Pair> defaultPairs = new ArrayList<>();

    static {
        for (Cookie cookie : Cookie.values()) {
            if (cookie.isDefault()) {
                defaultPairs.add(new Pair(cookie.getPath(), cookie.getKey(), cookie.getValue()));
            } else {
                pairs.add(new Pair(cookie.getPath(), cookie.getKey()));
            }
        }
    }

    public static List<Pair> getDefaultPairs() {
        return defaultPairs;
    }

    public static List<Pair> getPairs() {
        return pairs;
    }

    public enum Cookie {

        guidesStatus("/", "guidesStatus", "off"), //
        highContrastMode("/", "highContrastMode", "defaltMode"), //
        cursorStatus("/", "cursorStatus", "off"), //

        route("/", "route"), //
        BIGipServerotn("/", "BIGipServerotn"), //
        JSESSIONID("/otn", "JSESSIONID"), //

        _passport_session("/passport", "_passport_session"), //
        BIGipServerpassport("/", "BIGipServerpassport"), //

        uamtk("/passport", "uamtk"), //
        tk("/otn", "tk"), //
        uKey("/", "uKey"), //

        ;

        private final String path;
        private final String key;
        private final String value;
        private final boolean isDefault;

        Cookie(String path, String key) {
            this(path, key, null, false);
        }

        Cookie(String path, String key, String value) {
            this(path, key, value, true);
        }

        Cookie(String path, String key, String value, boolean isDefault) {
            this.path = path;
            this.key = key;
            this.value = value;
            this.isDefault = isDefault;
        }

        public String getPath() {
            return path;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public boolean isDefault() {
            return isDefault;
        }
    }

    public static class Pair {
        private final String path;
        private final String key;
        private final String value;

        public Pair(String path, String key) {
            this(path, key, null);
        }

        public Pair(String path, String key, String value) {
            this.path = path;
            this.key = key;
            this.value = value;
        }

        public String getPath() {
            return path;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
