package presentation.controllers;


import core.domain.models.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StartCommandHandler implements CommandHandler {

    @Override
    public void handleCommand(Update update) {
        // Send a message to the user.
        String chatId = update.getMessage().getChat().getId().toString();
        String response = "Welcome to the bot!";

        TelegramBot bot = new TelegramBot();
        bot.sendMessage(chatId, response);
    }
}
