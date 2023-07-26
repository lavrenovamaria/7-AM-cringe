package core.domain.models;

import core.domain.services.ImageService;
import core.domain.services.ScheduleService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.google.api.services.drive.model.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import presentation.controllers.CommandController;

import java.io.IOException;
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
        return "YourBotUsername";
    }

    @Override
    public String getBotToken() {
        return "YourBotToken";
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Delegate the command handling to the CommandController.
        commandController.handleCommand(update);
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
        sendPhoto.setPhoto(photoFile);

        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void scheduleSendImagesTask() {
        DayTime scheduledTime = new DayTime(7, 0);
        ScheduleService.scheduleTaskAt(() -> {
            imageService.sendImagesToTelegram(chatIds);
        }, scheduledTime);
    }
}
