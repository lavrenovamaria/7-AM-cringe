package presentation.controllers;



import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommandController {
    private Map<String, CommandHandler> commandHandlers;
    private Set<String> chatIds;

    public CommandController() {
        commandHandlers = new HashMap<>();
        commandHandlers.put("/start", new StartCommandHandler());
        commandHandlers.put("/stop", new StartCommandHandler());
        // Add other command handlers if needed.

        chatIds = new HashSet<>();
    }

//    public void handleCommand(Update update) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            String command = update.getMessage().getText();
//            CommandHandler handler = commandHandlers.get(command);
//            if (handler != null) {
//                handler.handleCommand(update);
//            }
//        }
//    }

    public void handleCommand(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String command = update.getMessage().getText();
            CommandHandler handler = commandHandlers.get(command);
            if (handler != null) {
                handler.handleCommand(update);
            }

            // If the command is to start the bot, add the chatId to the set
            if ("/start".equals(command)) {
                String chatId = update.getMessage().getChatId().toString();
                chatIds.add(chatId);
            }
        }
    }

    public Set<String> getChatIds() {
        return chatIds;
    }
}
