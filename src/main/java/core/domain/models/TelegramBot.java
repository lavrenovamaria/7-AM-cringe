package core.domain.models;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import com.google.api.services.drive.model.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import presentation.controllers.CommandController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TelegramBot extends TelegramLongPollingBot {

    private final CommandController commandController;

    public TelegramBot(CommandController commandController) {
        this.commandController = commandController;
    }

    @Override
    public String getBotUsername() {
        return "Your bot name";
    }

    @Override
    public String getBotToken() {
        return "Your bot token";
    }

    @Override
    public void onUpdateReceived(Update update) {
        TelegramBotHandler.handleUpdate(this, update);
    }

    public void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public CommandController getCommandController() {
        return commandController;
    }

    public void sendPhoto(String chatId, String message, String fileName, InputStream inputStream) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(message);

        InputFile inputFile = new InputFile(inputStream, fileName);
        sendPhoto.setPhoto(inputFile);

        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
