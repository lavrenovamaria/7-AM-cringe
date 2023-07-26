package core.domain.services;

import com.google.api.services.drive.model.File;
import core.domain.models.DriveApiClient;
import core.domain.models.TelegramBot;

import java.io.IOException;
import java.util.List;

public class ImageService {
    private TelegramBot telegramBot;
    private DriveApiClient driveApiClient;

    public ImageService(TelegramBot telegramBot, DriveApiClient driveApiClient) {
        this.telegramBot = telegramBot;
        this.driveApiClient = driveApiClient;
    }

    public void sendImagesToTelegram(List<String> chatIds) {
        try {
            List<File> imageFiles = driveApiClient.getImagesFromDrive();

            for (int i = 0; i < imageFiles.size(); i++) {
                String chatId = chatIds.get(i);
                String message = "Here is a picture for you!";
                telegramBot.sendPhoto(chatId, message, imageFiles.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
