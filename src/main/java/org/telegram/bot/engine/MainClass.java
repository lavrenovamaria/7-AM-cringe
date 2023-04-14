package org.telegram.bot.engine;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class MainClass extends TelegramLongPollingBot {
    private final String botToken = "Bot";

    private Set<String> chatIds = new HashSet<>();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();

            if ("/start".equalsIgnoreCase(text)) {
                chatIds.add(chatId);

                // Отправка сообщения о запуске бота
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Бот успешно запущен!");

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            if("/stop".equalsIgnoreCase(text)) {
                chatIds.remove(chatId);

                // Отправка сообщения о остановке бота
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Бот успешно остановлен!");

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "MyIttyBittyBot";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void sendImageToAllChats(String imageUrl) {
        for (String chatId : chatIds) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            sendPhoto.setPhoto(new InputFile(imageUrl));

            try {
                execute(sendPhoto);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private static long getTimeUntilNext7AM() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime today7AM = now.withHour(7).withMinute(0).withSecond(0).withNano(0);
        if (now.isBefore(today7AM)) {
            // Если текущее время до 7 утра сегодня
            return Duration.between(now, today7AM).toMillis();
        } else {
            // Если текущее время после 7 утра сегодня, нужно найти 7 утра следующего дня
            LocalDateTime nextDay7AM = today7AM.plusDays(1);
            return Duration.between(now, nextDay7AM).toMillis();
        }

}

    public static void main(String[] args) throws GeneralSecurityException, IOException, TelegramApiException {
        MainClass bot = new MainClass();
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
            String fileName = "reference-unity-383120-97164bb76379.json";
            String workingDirectory = System.getProperty("user.dir");
            String filePath = workingDirectory + File.separator + fileName;
            GoogleDriveService googleDriveService = new GoogleDriveService(filePath);
            String folderName = "cringe";
            String folderId = googleDriveService.getFolderIdByName(folderName);

            if (folderId != null) {
                List<String> imageUrls = googleDriveService.getImageUrlsFromFolder(folderId);
                AtomicInteger currentIndex = new AtomicInteger(0); // Создайте AtomicInteger для индекса текущего изображения
                Runnable sendImageTask = () -> {
                    int index = currentIndex.getAndIncrement();
                    if (index >= imageUrls.size()) {
                        currentIndex.set(0);
                        index = 0;
                    }
                    bot.sendImageToAllChats(imageUrls.get(index));
                };
                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                long delay = getTimeUntilNext7AM();

                scheduler.scheduleAtFixedRate(sendImageTask, delay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

