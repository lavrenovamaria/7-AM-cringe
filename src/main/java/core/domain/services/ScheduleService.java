package core.domain.services;

import core.domain.models.DayTime;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduleService {
    public static void scheduleTaskAt(Runnable task, DayTime scheduledTime) {
        // Get the current date and time.
        Calendar now = Calendar.getInstance();

        // Set the scheduled hour and minute.
        Calendar scheduled = Calendar.getInstance();
        scheduled.set(Calendar.HOUR_OF_DAY, scheduledTime.getHour());
        scheduled.set(Calendar.MINUTE, scheduledTime.getMinute());
        scheduled.set(Calendar.SECOND, 0);

        // If the scheduled time is before the current time, add one day.
        if (scheduled.before(now)) {
            scheduled.add(Calendar.DATE, 1);
        }

        // Calculate the delay until the scheduled time.
        long delayInMillis = scheduled.getTimeInMillis() - now.getTimeInMillis();

        // Schedule the task to run at the scheduled time.
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                task.run();
            }
        }, new Date(System.currentTimeMillis() + delayInMillis));
    }
}
