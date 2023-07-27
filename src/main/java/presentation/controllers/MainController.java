package presentation.controllers;

import core.domain.models.DayTime;
import core.domain.models.DriveApiClientImpl;
import core.domain.models.TelegramBot;
import core.domain.services.ImageService;
import core.domain.services.ScheduleService;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    public static void main(String[] args) throws TelegramApiException, GeneralSecurityException, IOException {
        TelegramBot bot = new TelegramBot();
        DriveApiClientImpl driveApiClient = new DriveApiClientImpl();
        ImageService imageService = new ImageService(bot, driveApiClient);
        CommandController commandController = new CommandController();

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        DayTime scheduledTime = new DayTime(9, 33);
        ScheduleService.scheduleTaskAt(new Runnable() {
            @Override
            public void run() {
                List<String> chatIds = new ArrayList<>(commandController.getChatIds());
                imageService.sendImagesToTelegram(chatIds);
            }
        }, scheduledTime);
    }
}

//public class MainController {
//    public static void main(String[] args) throws TelegramApiException, GeneralSecurityException, IOException {
//        // Define the file path to the credentials JSON file
//        String fileName = "client_secret.json";
//        String workingDirectory = System.getProperty("user.dir");
//        String filePath = workingDirectory + File.separator + fileName;
//
//        // Create the DriveApiClientImpl instance with the file path
//        DriveApiClientImpl driveApiClient = new DriveApiClientImpl(filePath);
//
//        // Rest of your code remains unchanged
//        TelegramBot bot = new TelegramBot(driveApiClient);
//        ImageService imageService = new ImageService(bot, driveApiClient);
//        CommandController commandController = new CommandController();
//
//        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
//        try {
//            botsApi.registerBot(bot);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//
//        DayTime scheduledTime = new DayTime(9, 33);
//        ScheduleService.scheduleTaskAt(new Runnable() {
//            @Override
//            public void run() {
//                List<String> chatIds = new ArrayList<>(commandController.getChatIds());
//                imageService.sendImagesToTelegram(chatIds);
//            }
//        }, scheduledTime);
//    }
//}