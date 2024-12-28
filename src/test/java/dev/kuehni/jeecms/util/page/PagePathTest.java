package dev.kuehni.jeecms.util.page;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PagePathTest {

    @Test
    void isRoot_withEmptyPath_returnsTrue() {
        final var path = new PagePath(List.of());
        assertTrue(path.isRoot());
    }

    @Test
    void isRoot_withNonEmptyPath_returnsFalse() {
        final var path = new PagePath(List.of("test"));
        assertFalse(path.isRoot());
    }

    @Test
    void toString_returnsTheInputOfParseWithoutLeadingSlash() {
        final var original = "/asdf/test/page";
        final var path = PagePath.parseWithoutLeadingSlash(original);
        assertEquals(original, path.toString());
    }

    @Test
    void toString_doesntReturnATrailingSlash() {
        final var original = "/asdf/test/page/";
        final var path = PagePath.parseWithoutLeadingSlash(original);
        assertEquals("/asdf/test/page", path.toString());
    }
}
