package model;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * durationDelay is defined as String in the format %H:%M 13:56
 */
public class TimeManager {

    private static final Logger logger = LoggerFactory.getLogger(TimeManager.class);

    private static final String DEFAULT_DURATION_DELAY = "00:00";

    private Instant whenElapsedPointInTime;

    public TimeManager() {
        this(DEFAULT_DURATION_DELAY);
    }

    public TimeManager(@NotNull String durationDelay) {
        setWhenElapsedUsingDurationDelay(durationDelay);
    }

    public TimeManager(@NotNull Duration durationDelay) {
        whenElapsedPointInTime = addDurationDelayToNow(durationDelay);
    }

    public void setWhenElapsedUsingDurationDelay(@NotNull String durationDelay) {
        try {
            Duration duration = convertStringDurationDelayIntoDuration(durationDelay);
            whenElapsedPointInTime = addDurationDelayToNow(duration);
        } catch (DateTimeException ex) {
            logger.error("Failed to convert <{}> into whenElapsed parameter. Setting to default now(). Message: <{}>", durationDelay, ex.toString());
            setWhenElapsedToNow();
        }
    }

    public static Duration convertStringDurationDelayIntoDuration(@NotNull String durationDelay) throws DateTimeException {
        return Duration.between(
                LocalTime.MIN, // 00:00:00
                LocalTime.parse(durationDelay) // 02:00 or 00:02:00
        );
    }

    public static Instant addDurationDelayToNow(@NotNull Duration durationDelay) {
        return Instant.now().plus(durationDelay);
    }

    private void setWhenElapsedToNow() {
        whenElapsedPointInTime = Instant.now();
    }

    public static boolean isValidDurationDelta(@NotNull String durationDelta) {
        try {
            convertStringDurationDelayIntoDuration(durationDelta);
            return true;
        } catch(DateTimeException ex) {
            return false;
        }
    }

    public Instant getWhenElapsedPointInTime() {
        return whenElapsedPointInTime;
    }

    public Duration getRemainingDuration() {
        return howLongBeforeItElapses(whenElapsedPointInTime);
    }

    public static Duration howLongBeforeItElapses(@NotNull Instant pointInTime) {
        return Duration.between(
                Instant.now(),
                pointInTime
        );
    }

    public boolean hasElapsed() {
        return whenElapsedPointInTime.isBefore(Instant.now());
    }
}
