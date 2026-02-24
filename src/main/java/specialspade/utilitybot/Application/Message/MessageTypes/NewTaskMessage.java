package specialspade.utilitybot.Application.Message.MessageTypes;


public class NewTaskMessage extends UserMessage {

    public NewTaskMessage(String messageType){
        super(messageType);
    }


    public static UserMessage userMessage(){
        return new NewTaskMessage("newtask");
    }
}
