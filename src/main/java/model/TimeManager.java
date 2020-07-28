package model;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * durationDelay is defined as String in the format %H:%M 13:56
 * <p>
 * Represents time remaining to the point in time. Specifically used to store Java's Instant time in future.
 * This point in time in future is determined by duration from now() to this point in time.
 * <p>
 * Minimum string value is 00:00
 */
public final class TimeManager {

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

    public TimeManager(@NotNull Instant pointInTime) {
        whenElapsedPointInTime = pointInTime;
    }

    public TimeManager(@NotNull TimeManager other) {
        whenElapsedPointInTime = Instant.from(other.whenElapsedPointInTime);
    }

    void setWhenElapsedUsingDurationDelay(@NotNull String durationDelay) {
        try {
            Duration duration = convertStringDurationDelayIntoDuration(durationDelay);
            whenElapsedPointInTime = addDurationDelayToNow(duration);
        } catch (DateTimeException ex) {
            logger.error("Failed to convert <{}> into whenElapsed parameter. Setting to default now(). Message: <{}>", durationDelay, ex.toString());
            setWhenElapsedToNow();
        }
    }

    public static @NotNull TimeManager fromNow() {
        return new TimeManager("00:00");
    }

    public static @NotNull Duration convertStringDurationDelayIntoDuration(@NotNull String durationDelay) throws DateTimeException {
        return Duration.between(
                LocalTime.MIN, // 00:00:00
                LocalTime.parse(durationDelay) // 02:00 or 00:02:00
        );
    }

    public static @NotNull Instant addDurationDelayToNow(@NotNull Duration durationDelay) {
        return Instant.now().plus(durationDelay);
    }

    private void setWhenElapsedToNow() {
        whenElapsedPointInTime = Instant.now();
    }

    public static boolean isValidDurationDelta(@NotNull String durationDelta) {
        try {
            convertStringDurationDelayIntoDuration(durationDelta);
            return true;
        } catch (DateTimeException ex) {
            return false;
        }
    }

    public @NotNull Instant getWhenElapsedPointInTime() {
        return whenElapsedPointInTime;
    }

    public @NotNull Duration getRemainingDuration() {
        return howLongBeforeItElapses(whenElapsedPointInTime);
    }


    public static @NotNull Duration howLongBeforeItElapses(@NotNull Instant pointInTime) {
        return Duration.between(
                Instant.now(),
                pointInTime
        );
    }

    public boolean hasElapsed() {
        return whenElapsedPointInTime.isBefore(Instant.now());
    }

    @Override
    public String toString() {
        return getRemainingDurationInHHMMSS_ifElapsedZeros();
    }

    public @NotNull String getRemainingDurationInHHMMSS_ifElapsedZeros() {
        if (hasElapsed()) {
            return "00:00:00";
        } else {
            Duration delay = getRemainingDuration();
            return convertDurationToHHMMSSString(delay);
        }

    }

    /**
     * Converts Javas Duration parameter into Javas String in the format HH:mm:SS (16:30:30)
     */
    private @NotNull String convertDurationToHHMMSSString(@NotNull Duration duration) {
        return String.format("%02d:%02d:%02d",
                duration.toHoursPart(),
                duration.toMinutesPart(),
                duration.toSecondsPart()
        );
    }

    public @NotNull String getWhenElapsedInHHMM() {
        LocalDateTime dateTime = LocalDateTime.now().plus(getRemainingDuration());
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeManager that = (TimeManager) o;
        return whenElapsedPointInTime.equals(that.whenElapsedPointInTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(whenElapsedPointInTime);
    }
}
