package specialspade.utilitybot.Application.Message.MessageTypes;


public abstract class UserMessage {
    protected final String messageType;
    protected final boolean requireImmediateResponse = false;

    public UserMessage(String messageType){
        this.messageType = messageType;
    }

    public String getMessageType(){
        return messageType;
    }


    public boolean requireImmediateResponse(){
        return requireImmediateResponse;
    }


}
