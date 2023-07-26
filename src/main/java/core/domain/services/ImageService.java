package core.domain.services;

import com.google.api.services.drive.model.File;
import core.domain.models.DriveApiClient;
import core.domain.models.TelegramBot;

import java.io.IOException;
import java.util.List;

public class ImageService {
    private TelegramBot telegramBot;
    private DriveApiClient driveApiClient;

    private int imageIndex = 1;

    public ImageService(TelegramBot telegramBot, DriveApiClient driveApiClient) {
        this.telegramBot = telegramBot;
        this.driveApiClient = driveApiClient;
    }

    public void sendImagesToTelegram(List<String> chatIds) {
        try {
            List<File> imageFiles = driveApiClient.getImagesFromDrive();

            // Send one image per day at 7 AM using imageIndex.
            if (imageIndex < imageFiles.size()) {
                String message = "Here is a picture for you!";
                for (String chatId : chatIds) {
                    telegramBot.sendPhoto(chatId, message, imageFiles.get(imageIndex));
                }
                imageIndex++;
            }

            if (imageIndex >= imageFiles.size()) {
                imageIndex = 0; // Reset imageIndex if it exceeds the number of images.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
