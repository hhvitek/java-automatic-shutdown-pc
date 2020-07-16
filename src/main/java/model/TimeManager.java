package model;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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

    public TimeManager(@NotNull TimeManager other) {
        whenElapsedPointInTime = Instant.from(other.whenElapsedPointInTime);
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

    public static TimeManager fromNow() {
        return new TimeManager("00:00");
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TimeManager that = (TimeManager) o;
        return Objects.equals(whenElapsedPointInTime, that.whenElapsedPointInTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(whenElapsedPointInTime);
    }

    @Override
    public String toString() {
        return "TimeManager{" +
                "whenElapsedPointInTime=" + whenElapsedPointInTime +
                '}';
    }

    public String getRemainingDurationInHHMMSS_ifElapsedZeros() {
        if (hasElapsed()) {
            return "00:00:00";
        } else {
            Duration delay = getRemainingDuration();
            return convertDurationToHHMMSSString(delay);
        }

    }

    /**
     * Converts Javas Duration parameter into Javas String in the format HH:mm:SS (16:30:30)
     * @param duration
     * @return
     */
    private String convertDurationToHHMMSSString(@NotNull Duration duration) {
        return String.format("%02d:%02d:%02d",
                duration.toHoursPart(),
                duration.toMinutesPart(),
                duration.toSecondsPart()
        );
    }

    public String getWhenElapsedInHHMM() {
        LocalDateTime dateTime = LocalDateTime.now().plus(getRemainingDuration());
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Override
    public TimeManager clone() {
        TimeManager clone = new TimeManager(this);
        return clone;
    }


}
