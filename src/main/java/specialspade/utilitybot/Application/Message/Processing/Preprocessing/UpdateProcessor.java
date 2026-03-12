package specialspade.utilitybot.Application.Message.Processing.Preprocessing;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import specialspade.utilitybot.Application.Application.AppInterface;
import specialspade.utilitybot.Application.Message.Processing.MessageProcessor;
import specialspade.utilitybot.Application.Message.Processing.MessageValidator;
import specialspade.utilitybot.Application.Message.Sending.MessageSender;
import specialspade.utilitybot.Application.TemperatureData.TownTemperatureData;

import java.util.HashMap;
import java.util.Map;

public class UpdateProcessor implements UpdateProcessorInterface {

    private final HashMap<Long, String> userOptions = new HashMap<>();

    private final Map<String, String> responses;
    private final MessageValidator messageValidator;
    private final MessageProcessor processor;
    private final AppInterface app;

    public UpdateProcessor(AppInterface app, MessageValidator messageValidator, MessageProcessor processor, Map<String, String> responses) {
        this.messageValidator = messageValidator;
        this.processor = processor;
        this.responses = responses;
        this.app = app;
    }


    public void process(Update update) {
        Message message = update.getMessage();
        long userId = message.getFrom().getId();
        app.writeToLogFileFromUser(message);

        if (userOptions.containsKey(userId)) {
            Object result = processor.processUpdate(update, app, userOptions);
            if (result instanceof String) {
                app.writeToLogFileToUser(result.toString(), message);
                app.setProperty(userOptions.get(userId) + " started.");
                sendMessageToUser(update, (String) result);
            } else if (result instanceof TownTemperatureData) {
                app.sendWeatherUpdate((TownTemperatureData) result, message);
                app.setProperty(userOptions.get(userId) +  " started.");
                app.writeToLogFileTemperature((TownTemperatureData) result, message);
            }
            userOptions.remove(userId);

        } else if (messageValidator.canCreateMessage(message.getText()) && messageValidator.needImmediateResponse(message)) {
            Object result = processor.processUpdate(update, app, userOptions);
            sendImmediateResponse(update, result);
            app.writeToLogFileToUser(result.toString(), message);

        } else if (messageValidator.canCreateMessage(message.getText())) {
            messageValidator.writeToUserOptions(userId, message, userOptions);
            sendMessageToUser(update, responses.get(userOptions.get(userId)));
            app.writeToLogFileToUser(responses.get(userOptions.get(userId)), update.getMessage());
        }

    }

    private void sendMessageToUser(Update update, String message) {
        MessageSender messageSender = new MessageSender();
        messageSender.sendMessage(update.getMessage(), app, message);
    }

    private void sendImmediateResponse(Update update, Object result) {
        app.setProperty("List tasks");
        if (result == null) {
            app.sendMessage(app.createMessageToSend(update.getMessage(), "No tasks"));
            return;
        }
        if (result instanceof Map) {
            if (((Map<?, ?>) result).isEmpty()) {
                app.sendMessage(app.createMessageToSend(update.getMessage(), "No tasks"));
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<Long, String> entry : ((HashMap<Long, String>) result).entrySet()) {
                sb.append(entry.getKey().toString()).append(": ").append(entry.getValue()).append("\n");
            }
            app.sendMessage(app.createMessageToSend(update.getMessage(), sb.toString()));
        }
    }

}
