package core.domain.models;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class TelegramBotHandler {
    public static void handleUpdate(TelegramBot bot, Update update) {
        try {
            bot.getCommandController().handleCommand(update);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
