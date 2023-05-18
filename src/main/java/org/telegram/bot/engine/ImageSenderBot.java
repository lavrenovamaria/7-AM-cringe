package org.telegram.bot.engine;

import java.util.HashSet;
import java.util.Set;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

// Attention, bad design!
// SRP violation: class mixes abstractions of command handling and high level bot features (subscribe/unsubscribe/send images)
// OCP violation: both Command and onUpdateReceived should be changed when new Command is introduced.
// Possibly, implementing command pattern is a good idea here (couldn't figure out how to decouple command from bot - callbacks?)
// Unnecessary duplication: sendText/sendImage. Maybe it is result of bad telegrambots lib design.
public class ImageSenderBot extends TelegramLongPollingBot implements ImageSender {

    private final Set<Long> subscribers = new HashSet<>();

    public void subscribeChat(Long chatId) {
        subscribers.add(chatId);
        this.sendText(chatId, "Бот успешно остановлен!");
    }

    public void unsubscribeChat(Long chatId) {
        subscribers.remove(chatId);
        this.sendText(chatId, "Бот успешно запущен!");
    }

    public void register() {
        try {
            new TelegramBotsApi(DefaultBotSession.class).registerBot(this);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Telegram Api error", e);
        }
    }

    @Override
    public void send(String uri) {
        for (Long chatId : subscribers) {
            this.sendImage(chatId, uri);
        }
    }

    {
        this.register();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        var command = Command.parse(update.getMessage().getText());
        var chatId = update.getMessage().getChatId();

        switch (command) {
            case START -> subscribeChat(chatId);
            case STOP -> unsubscribeChat(chatId);
        }
    }

    @Override
    public String getBotUsername() {
        return "MyIttyBittyBot";
    }

    public void sendText(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            this.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Telegram Api error", e);
        }
    }

    public void sendImage(Long chatId, String imageUrl) {
        SendPhoto message = new SendPhoto();
        message.setChatId(chatId);
        message.setPhoto(new InputFile(imageUrl));

        try {
            this.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Telegram Api error", e);
        }
    }

}

enum Command {
    START, STOP;

    static Command parse(String commandString) {
        var lowercasedCommand = commandString.toLowerCase();
        return switch (lowercasedCommand) {
            case "/start" -> START;
            case "/stop" -> STOP;
            default -> throw new RuntimeException("Command %s is not supported".formatted(lowercasedCommand));
        };
    }
}
