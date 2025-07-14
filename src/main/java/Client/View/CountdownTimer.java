package Client.View;

import java.util.logging.Logger;

import static java.lang.Math.ceil;

/**
 * CountdownTimer is a thread-based timer that counts down from a specified number of minutes.
 * It supports pausing, resuming, and logging the time spent.
 */
public class CountdownTimer extends Thread{
    private static final Logger logger = Logger.getLogger(CountdownTimer.class.getName());
    private long remainingTime;
    private long totalTimeSpent;
    private volatile boolean running;
    private volatile boolean paused;
    private String timeString;
    private Runnable onTimerEnd;
    private Runnable onTick;

    /**
     * Constructs a CountdownTimer with the specified initial time in minutes.
     *
     * @param minutes The initial time in minutes.
     */
    public CountdownTimer(int minutes) {
        this.remainingTime = minutes * 60 * 1000;
        this.totalTimeSpent = 0;
        this.running = true;
        this.paused = false;
        this.timeString = formatTime(remainingTime);
    }

    /**
     * Formats the given time in milliseconds to a string in mm:ss format.
     *
     * @param time The time in milliseconds.
     * @return A string representing the formatted time.
     */
    public String formatTime(long time) {
        long seconds = (time / 1000) % 60;
        long minutes = (time / (1000 * 60)) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void setOnTimerEnd(Runnable onTimerEnd) {
        this.onTimerEnd = onTimerEnd;
    }
    public void setOnTick(Runnable onTick) {
        this.onTick = onTick;
    }
    public String getTimeString() {
        return timeString;
    }

    /**
     * Stops the timer and logs the total time spent from starting new timer to stop.
     */
    public void stopAndLogTime() {
        running = false;
        logger.info("Time spent: " + formatTime((long)ceil(totalTimeSpent)));
    }

    /**
     * Pauses the timer and logs the time spent before pausing.
     */
    public synchronized void pauseTimer() {
            paused = true;
            logger.info("Time spent before save: " + formatTime((long)ceil(totalTimeSpent)));
    }

    /**
     * The main run method of the thread, which handles the countdown logic.
     */
    @Override
    public void run() {
        long lastTime = System.currentTimeMillis();
        while (running && remainingTime > 0) {
            if(!paused) {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - lastTime;
                remainingTime -= elapsedTime;
                totalTimeSpent += elapsedTime;
                lastTime = currentTime;

                timeString = formatTime(remainingTime);

                // If a tick action is set, execute it
                if (onTick != null) {
                    onTick.run();
                }

                // Check if the remaining time has reached zero
                // If a timer end action is set (normally turn exchange), execute it
                if (remainingTime <= 0) {
                    timeString = "00:00";
                    if (onTimerEnd != null) {
                        onTimerEnd.run();
                    }
                }

                try {
                    // Sleep the thread to reduce CPU usage, adjusting for any time spent in processing.
                    // Sleeping 997 to correctly update string for the timer
                    Thread.sleep(Math.max(0, 997 - (System.currentTimeMillis() - currentTime)));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }else{
                lastTime = System.currentTimeMillis();
            }
        }
    }
    public long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
        this.timeString = formatTime(remainingTime);
    }
}
