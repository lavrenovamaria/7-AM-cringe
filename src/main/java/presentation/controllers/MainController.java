package presentation.controllers;

import core.domain.models.TelegramBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class MainController {
    public static void main(String[] args) throws TelegramApiException {
        TelegramBot bot = new TelegramBot();

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        // Rest of your code for initializing other services and starting your application.
    }
}