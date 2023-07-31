package core.domain.services;

import core.domain.models.DayTime;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduleService {
    public static void scheduleTaskAt(Runnable task, DayTime scheduledTime) {

        Calendar now = Calendar.getInstance();

        Calendar scheduled = Calendar.getInstance();
        scheduled.set(Calendar.HOUR_OF_DAY, scheduledTime.getHour());
        scheduled.set(Calendar.MINUTE, scheduledTime.getMinute());
        scheduled.set(Calendar.SECOND, 0);

        if (scheduled.before(now)) {
            scheduled.add(Calendar.DATE, 1);
        }

        long delayInMillis = scheduled.getTimeInMillis() - now.getTimeInMillis();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                task.run();
            }
        }, new Date(System.currentTimeMillis() + delayInMillis));
    }
}
