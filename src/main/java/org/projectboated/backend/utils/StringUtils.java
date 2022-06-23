package org.projectboated.backend.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtils {

    public static boolean equalsOrNull(String s1, String s2) {
        if (s1 == null || s2 == null) {
            return true;
        }
        return s1.equals(s2);
    }

}
