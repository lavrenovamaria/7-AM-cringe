package org.telegram.bot.engine;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {

    public static void scheduleTaskAt(Runnable task, Calendar scheduledTime) {
        Calendar currentTime = Calendar.getInstance();
        if (scheduledTime.before(currentTime)) {
            scheduledTime.add(Calendar.DAY_OF_MONTH, 1);
        }
        long initialDelay = scheduledTime.getTimeInMillis() - currentTime.getTimeInMillis();

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(task, initialDelay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
    }

    public static Calendar calendarTime(int hour, int minute) {
        Calendar scheduledTime = Calendar.getInstance();
        scheduledTime.set(Calendar.HOUR_OF_DAY, hour);
        scheduledTime.set(Calendar.MINUTE, minute);
        scheduledTime.set(Calendar.SECOND, 0);

        return scheduledTime;
    }

}
