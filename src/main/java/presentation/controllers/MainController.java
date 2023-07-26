package presentation.controllers;

import core.domain.models.DayTime;
import core.domain.models.DriveApiClientImpl;
import core.domain.models.TelegramBot;
import core.domain.services.ImageService;
import core.domain.services.ScheduleService;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    public static void main(String[] args) throws TelegramApiException, GeneralSecurityException, IOException {
        TelegramBot bot = new TelegramBot();

        //String credentialsFilePath = "client_secret_622097366523-9m4v33a80bglu0ah7v3bdlgkdtlkgmnp.apps.googleusercontent.com.json";
        DriveApiClientImpl driveApiClient = new DriveApiClientImpl();
        ImageService imageService = new ImageService(bot, driveApiClient);
        CommandController commandController = new CommandController();

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        DayTime scheduledTime = new DayTime(7, 0);
        ScheduleService.scheduleTaskAt(() -> {
            // Get the list of chatIds where you want to send the images.
            List<String> chatIds = new ArrayList<>(commandController.getChatIds());
            imageService.sendImagesToTelegram(chatIds);
        }, scheduledTime);

        // Rest of your code for initializing other services and starting your application.
    }
}