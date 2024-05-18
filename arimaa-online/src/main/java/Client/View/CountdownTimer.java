package Client.View;

import java.util.concurrent.*;
import java.util.logging.Logger;

import static java.lang.Math.ceil;

public class CountdownTimer extends Thread{
    private static final Logger logger = Logger.getLogger(CountdownTimer.class.getName());
    private long remainingTime;
    private long totalTimeSpent;
    private volatile boolean running;
    private volatile boolean paused;
    private long pauseTime;
    private String timeString;
    private Runnable onTimerEnd;
    private Runnable onTick;

    public CountdownTimer(int minutes) {
        this.remainingTime = minutes * 60 * 1000;
        this.totalTimeSpent = 0;
        this.running = true;
        this.paused = false;
        this.timeString = formatTime(remainingTime);
    }

    private String formatTime(long time) {
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

    public void stopAndLogTime() {
        running = false;
        logger.info("Time spent: " + formatTime((long)ceil(totalTimeSpent)));
    }


    public synchronized void pauseTimer() {
        if (!paused) {
            pauseTime = System.currentTimeMillis();
            paused = true;
        }
    }

    public synchronized void resumeTimer() {
        if (paused) {
            long timeSpentPaused = System.currentTimeMillis() - pauseTime;
            remainingTime -= timeSpentPaused;
            paused = false;
        }
    }

    @Override
    public void run() {
        long lastTime = System.currentTimeMillis();
        while (running && remainingTime > 0) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - lastTime;
            remainingTime -= elapsedTime;
            totalTimeSpent += elapsedTime;
            lastTime = currentTime;

            timeString = formatTime(remainingTime);

            if (onTick != null) {
                onTick.run();
            }

            if (remainingTime <= 0) {
                timeString = "00:00";
                if (onTimerEnd != null) {
                    onTimerEnd.run();
                }
            }

            try {
                Thread.sleep(Math.max(0, 997 - (System.currentTimeMillis() - currentTime)));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
