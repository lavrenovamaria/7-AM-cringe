package core.domain.models;

public class DayTime {
    private int hour;
    private int minute;

    public DayTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}
