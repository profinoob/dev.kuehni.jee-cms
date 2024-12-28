package dev.kuehni.jeecms.util.text;

import jakarta.annotation.Nonnull;

import java.util.Objects;

public class StringUtils {
    private StringUtils() {}

    /// Remove some common accents from text. Example:`ö` &rarr; `o`.
    @Nonnull
    public static String stripAccents(@Nonnull String text) {
        return Objects.requireNonNull(text, "text")
                .replace("ß", "ss")
                .replace("à", "a")
                .replace("â", "a")
                .replace("ä", "a")
                .replace("ç", "c")
                .replace("é", "e")
                .replace("è", "e")
                .replace("ê", "e")
                .replace("ë", "e")
                .replace("î", "i")
                .replace("ï", "i")
                .replace("ô", "o")
                .replace("ö", "o")
                .replace("ù", "u")
                .replace("û", "u")
                .replace("ü", "u")
                .replace("À", "A")
                .replace("Â", "A")
                .replace("Ä", "A")
                .replace("Ç", "C")
                .replace("É", "E")
                .replace("È", "E")
                .replace("Ê", "E")
                .replace("Ë", "E")
                .replace("Î", "I")
                .replace("Ï", "I")
                .replace("Ô", "O")
                .replace("Ö", "O")
                .replace("Ù", "U")
                .replace("Û", "U")
                .replace("Ü", "U");
    }
}
