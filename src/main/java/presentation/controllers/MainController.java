package presentation.controllers;

import core.domain.models.DayTime;
import core.domain.models.DriveApiClient;
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

        String fileName = "src/main/resources/client_secret.json";
        String workingDirectory = System.getProperty("user.dir");
        String filePath = workingDirectory + File.separator + fileName;

        DriveApiClient driveApiClient = new DriveApiClientImpl(filePath);

        CommandController commandController = new CommandController();

        TelegramBot bot = new TelegramBot(commandController);

        ImageService imageService = new ImageService(bot, driveApiClient);

        commandController.registerCommand("/start", new StartCommandHandler(bot));
        commandController.registerCommand("/stop", new StopCommandHandler(bot));

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        DayTime scheduledTime = new DayTime(5, 00);
        ScheduleService.scheduleTaskAt(() -> {
            List<String> chatIds = new ArrayList<>(commandController.getChatIds());
            imageService.sendImagesToTelegram(chatIds);
        }, scheduledTime);
    }
}
