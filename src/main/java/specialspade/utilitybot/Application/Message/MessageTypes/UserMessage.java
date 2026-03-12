package specialspade.utilitybot.Application.Message.MessageTypes;


public abstract class UserMessage {
    protected final String messageType;

    public UserMessage(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return messageType;
    }


    public boolean requireImmediateResponse() {
        return false;
    }


}
