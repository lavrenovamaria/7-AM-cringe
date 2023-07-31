package presentation.controllers;


import core.domain.models.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class StartCommandHandler implements CommandHandler {
    private final TelegramBot bot;

    public StartCommandHandler(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void handleCommand(Update update) throws GeneralSecurityException, IOException {
        String chatId = update.getMessage().getChat().getId().toString();
        String response = "Welcome to the bot!";
        bot.sendMessage(chatId, response);
    }
}
