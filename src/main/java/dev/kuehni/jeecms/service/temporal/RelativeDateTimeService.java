package dev.kuehni.jeecms.service.temporal;

import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Named
@ApplicationScoped
public class RelativeDateTimeService {
    private static final long MAX_SECONDS_NOW = 9;
    private static final long MAX_SECONDS = 59;
    private static final long MAX_MINUTES = 59;
    private static final long MAX_HOURS = 23;
    private static final long MAX_DAYS = 29;
    private static final long MAX_MONTHS = 11;


    @Nonnull
    private final Clock clock;

    private RelativeDateTimeService() {
        this(Clock.systemDefaultZone());
    }

    public RelativeDateTimeService(@Nonnull Clock clock) {
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    /// Generate a relative time string, like "3 minutes ago" for the given `instant`.\
    /// This rounds approximately to the nearest number of seconds, minutes, hours, days, months, or years.
    ///
    /// @param instant Must lie in the past.
    /// @throws IllegalArgumentException if `instant` is not in the past.
    public String format(@Nonnull LocalDateTime instant) {
        final var now = LocalDateTime.now(clock);
        if (now.isBefore(instant))
            throw new IllegalArgumentException(now + " (now) is before instant " + instant);

        final var secondsAgo = instant.until(now, ChronoUnit.SECONDS);
        if (secondsAgo <= MAX_SECONDS_NOW) {
            return "now";
        }

        if (secondsAgo <= MAX_SECONDS) {
            return secondsAgo + " seconds ago";
        }

        final var minutesAgo = instant.until(now, ChronoUnit.MINUTES);
        if (minutesAgo == 1) {
            return "1 minute ago";
        }
        if (minutesAgo <= MAX_MINUTES) {
            return minutesAgo + " minutes ago";
        }

        final var hoursAgo = instant.until(now, ChronoUnit.HOURS);
        if (hoursAgo == 1) {
            return "1 hour ago";
        }
        if (hoursAgo <= MAX_HOURS) {
            return hoursAgo + " hours ago";
        }

        final var daysAgo = instant.until(now, ChronoUnit.DAYS);
        if (daysAgo == 1) {
            return "1 day ago";
        }
        if (daysAgo <= MAX_DAYS) {
            return daysAgo + " days ago";
        }

        final var monthsAgo = instant.until(now, ChronoUnit.MONTHS);
        if (monthsAgo == 1) {
            return "1 month ago";
        }
        if (monthsAgo <= MAX_MONTHS) {
            return monthsAgo + " months ago";
        }

        final var yearsAgo = instant.until(now, ChronoUnit.YEARS);
        if (yearsAgo == 1) {
            return "1 year ago";
        }
        return yearsAgo + " years ago";
    }
}
