package org.telegram.bot.engine;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.Arrays;
import java.util.List;

public class ThreatDetectionBot {

    static List<String> threatKeywords = Arrays.asList("keyword1", "keyword2", "keyword3");

    static boolean isThreat(String message) {
        for (String keyword : threatKeywords) {
            if (message.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }


    private static final String TELEGRAM_API_TOKEN = "6020628537:AAHmE6xKAoysdbB9QlOHinZAH2q95GahjYg";

    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot(TELEGRAM_API_TOKEN);
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update.message() != null && update.message().text() != null) {
                    String messageText = update.message().text();
                    if (isThreat(messageText)) {
                        // Notify user or take appropriate action
                        SendMessage sendMessage = new SendMessage(update.message().chat().id(), "Warning: Dangerous message detected!");
                        bot.execute(sendMessage);
                    }
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    // The isThreat function from the previous example
}
