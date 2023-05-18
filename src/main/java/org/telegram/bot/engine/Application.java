package org.telegram.bot.engine;

public class Application {

    public static void main(String[] args) {
        ImageSender sender = new ImageSenderBot();
        ImageSource imageSource = new GoogleDriveAdapter();

        Scheduler.scheduleTaskAt(
                () -> sender.send(imageSource.getNext()),
                Scheduler.calendarTime(7, 0)
        );
    }

}

interface ImageSender {
    void send(String uri);
}

interface ImageSource {
    String getNext();
}
