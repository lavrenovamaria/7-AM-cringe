package presentation.controllers;


import core.domain.models.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class StopCommandHandler implements CommandHandler {
    private final TelegramBot bot;

    public StopCommandHandler(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void handleCommand(Update update) {
        String chatId = update.getMessage().getChat().getId().toString();
        String response = "Bye!";
        bot.sendMessage(chatId, response);
    }
}