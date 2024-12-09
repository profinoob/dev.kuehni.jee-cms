package dev.kuehni.jeecms.util.html;

import jakarta.annotation.Nonnull;

public class HtmlUtils {
    private HtmlUtils() {}

    @Nonnull
    public static String escape(@Nonnull String rawText) {
        final var out = new StringBuilder(rawText.length());
        for (int i = 0; i < rawText.length(); i++) {
            var c = rawText.charAt(i);
            if (c > 127 || c == '"' || c == '\'' || c == '<' || c == '>' || c == '&') {
                out.append("&#");
                out.append((int) c);
                out.append(';');
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }
}
