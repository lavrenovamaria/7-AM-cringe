package presentation.controllers;


import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface CommandHandler {
    void handleCommand(Update update) throws GeneralSecurityException, IOException;
}
