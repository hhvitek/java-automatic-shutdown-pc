package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class TimeManagerTest {

    @Test
    void setDurationDelayDefault_ok_Test() throws InterruptedException {
        TimeManager time = new TimeManager();

        // must wait so Instant.now() returns a new value
        Thread.sleep(1);

        Assertions.assertTrue(time.hasElapsed());

        Instant pointInTime = time.getWhenElapsedPointInTime();
        Assertions.assertTrue(pointInTime.isBefore(Instant.now()));

        Duration duration = time.getRemainingDuration();
        Assertions.assertTrue(duration.isNegative());
    }

    @Test
    void setDurationDelay_ok_Test() throws InterruptedException {

        String durationDelay = "02:30";
        TimeManager time = new TimeManager(durationDelay);

        Thread.sleep(1);

        Assertions.assertFalse(time.hasElapsed());

        Duration duration = time.getRemainingDuration();
        Assertions.assertTrue(!duration.isNegative());
        Assertions.assertEquals(2, duration.toHoursPart());
        Assertions.assertEquals(29, duration.toMinutesPart());
    }

    @Test
    void setDurationDelayCorrupted_error_setToNow_Test() throws InterruptedException {

        String durationDelay = "02:XX";
        TimeManager time = new TimeManager(durationDelay);

        Thread.sleep(1);

        Assertions.assertTrue(time.hasElapsed());

        Duration duration = time.getRemainingDuration();
        Assertions.assertTrue(duration.isNegative());
    }

}
