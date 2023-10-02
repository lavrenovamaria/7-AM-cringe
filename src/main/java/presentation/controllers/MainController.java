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

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    public static void main(String[] args) throws TelegramApiException, GeneralSecurityException, IOException, InterruptedException {

        String fileName = "client_secret.json";
        InputStream inputStream = MainController.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new FileNotFoundException("File not found: " + fileName);
        }

        // Read the contents of the InputStream into a String
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }

        String fileContent = stringBuilder.toString();

        // Save the JSON content to a file
        File tempFile = File.createTempFile("client_secret", ".json");
        try (FileWriter fileWriter = new FileWriter(tempFile)) {
            fileWriter.write(fileContent);
        }

        // TempFile path to create the DriveApiClientImpl
        DriveApiClient driveApiClient = new DriveApiClientImpl(tempFile.getAbsolutePath());

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

        DayTime scheduledTime = new DayTime(5, 0);
        ScheduleService.scheduleTaskAt(() -> {
            List<String> chatIds = new ArrayList<>(commandController.getChatIds());
            imageService.sendImagesToTelegram(chatIds);
        }, scheduledTime);
    }
}
