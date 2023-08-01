package core.domain.services;

import com.google.api.services.drive.model.File;

import core.domain.models.DriveApiClient;
import core.domain.models.DriveFile;
import core.domain.models.TelegramBot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;

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
            if (imageIndex < imageFiles.size()) {
                String message = getMessageOfTheDay();
                DriveFile driveFile = downloadImageFile(imageFiles.get(imageIndex));
                for (String chatId : chatIds) {
                    telegramBot.sendPhoto(chatId, message, driveFile.getFileName(), driveFile.getInputStream());
                }
                imageIndex++;
            }
            if (imageIndex >= imageFiles.size()) {
                imageIndex = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DriveFile downloadImageFile(File imageFile) throws IOException {
        String fileName = imageFile.getName();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        driveApiClient.downloadFileContent(imageFile.getId(), outputStream);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        return new DriveFile(fileName, inputStream);
    }

    private String getMessageOfTheDay() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        return switch (dayOfWeek) {
            case Calendar.MONDAY -> "Понедельничек! Доброе утро! Хорошего настроения!";
            case Calendar.TUESDAY -> "Прекрасного вторничного дня! Пусть тебя окружает любовь и позитив.!";
            case Calendar.WEDNESDAY -> "Среда, мои чуваки! Посылаю вам теплые объятия и хорошее настроение.";
            case Calendar.THURSDAY -> "Четвергговые пожелания вам дня, наполненного улыбками и моментами чистого счастья.";
            case Calendar.FRIDAY -> "Пятничка, ура! Посылаю вам позитивную энергию и мысли для яркого и прекрасного дня.";
            case Calendar.SATURDAY -> "Желаю вам хорошего выходного дня!";
            case Calendar.SUNDAY -> "Добречкого утречка воскресенья";
            default -> "Доброе утро сладушки-оладушки!";
        };
    }
}
