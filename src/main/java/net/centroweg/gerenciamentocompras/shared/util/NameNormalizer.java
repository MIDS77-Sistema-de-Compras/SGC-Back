package net.centroweg.gerenciamentocompras.shared.util;

import java.util.regex.Pattern;

/**
 * Normalizes names used as business identifiers while keeping the user's capitalization.
 */
public final class NameNormalizer {

    private static final Pattern CONSECUTIVE_WHITESPACE = Pattern.compile("\\s+");

    private NameNormalizer() {
    }

    public static String normalize(String value) {
        if (value == null) {
            return null;
        }
        return CONSECUTIVE_WHITESPACE.matcher(value.trim()).replaceAll(" ");
    }
}
