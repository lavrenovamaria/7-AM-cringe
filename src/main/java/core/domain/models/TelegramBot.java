package core.domain.models;

import core.domain.services.ImageService;
import core.domain.services.ScheduleService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import com.google.api.services.drive.model.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import presentation.controllers.CommandController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

public class TelegramBot extends TelegramLongPollingBot {

    private final CommandController commandController;
    private final ImageService imageService;

    public TelegramBot() throws GeneralSecurityException, IOException {
        commandController = new CommandController();
        imageService = new ImageService(this, new DriveApiClientImpl());
    }

    @Override
    public String getBotUsername() {
        return "MyIttyBittyBot";
    }

    @Override
    public String getBotToken() {
        return "6020628537:AAHmE6xKAoysdbB9QlOHinZAH2q95GahjYg";
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            commandController.handleCommand(update);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
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

    public void sendPhoto(String chatId, String caption, File photoFile) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(caption);

        try (InputStream inputStream = new FileInputStream(photoFile.getName())) {
            InputFile inputFile = new InputFile(inputStream, photoFile.getName());
            sendPhoto.setPhoto(inputFile);
            execute(sendPhoto);
        } catch (IOException | TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
