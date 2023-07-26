package presentation.controllers;


import core.domain.models.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class StopCommandHandler implements CommandHandler {

    @Override
    public void handleCommand(Update update) throws GeneralSecurityException, IOException {
        // Send a message to the user.
        String chatId = update.getMessage().getChat().getId().toString();
        String response = "Bye!";

        TelegramBot bot = new TelegramBot();
        bot.sendMessage(chatId, response);
    }
}
