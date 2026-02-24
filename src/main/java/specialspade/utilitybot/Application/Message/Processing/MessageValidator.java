package specialspade.utilitybot.Application.Message.Processing;

import org.telegram.telegrambots.meta.api.objects.Message;
import specialspade.utilitybot.Application.Message.MessageTypes.MessageTypes;
import specialspade.utilitybot.Application.Message.MessageTypes.UserMessage;

import java.util.List;
import java.util.Map;

public class MessageValidator {

    private final List<UserMessage> messageTypes;

    public MessageValidator(List<UserMessage> messageTypes) {
        this.messageTypes = messageTypes;
    }


    public void writeToUserOptions(Long userId, Message message, Map<Long, String> userOptions) {
        String validatedMsg = validateTypeAndNormalizeType(message.getText());
        if(validatedMsg == null)
            return;
        userOptions.put(userId, validatedMsg);
    }

    public boolean canCreateMessage(String message) {
        String validated = validateTypeAndNormalizeType(message);
        if(validated == null)
            return false;
        validated = validated.toLowerCase();
        for (UserMessage messageType : messageTypes) {
            if (messageType.getMessageType().equals(validated))
                return true;
        }
        return false;

    }

    public static String validateTypeAndNormalizeType(String input) {
        String toCheck = input;
        toCheck = toCheck.replaceAll("[^A-Za-z]", "");
        toCheck = toCheck.toUpperCase();
        if (!toCheck.isEmpty()) {
            String[] split_input = toCheck.split((" "));
            for (MessageTypes type : MessageTypes.values()) {
                if (split_input[0].equals(type.toString())) {
                    return split_input[0].toLowerCase();
                }
            }
        }
        return null;
    }

    public boolean needImmediateResponse(Message message) {
        for (UserMessage messageType : messageTypes) {
            String validatedMsg = validateTypeAndNormalizeType(message.getText());
            if(validatedMsg == null)
                return false;
            if (messageType.getMessageType().equals(validatedMsg.toLowerCase()) && messageType.requireImmediateResponse())
                return true;
        }
        return false;
    }
}
