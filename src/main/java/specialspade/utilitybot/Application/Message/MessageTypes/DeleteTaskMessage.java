package specialspade.utilitybot.Application.Message.MessageTypes;


public class DeleteTaskMessage extends UserMessage{

    public DeleteTaskMessage(String messageType){
        super(messageType);
    }


    public static UserMessage userMessage(){
        return new DeleteTaskMessage("deletetask");
    }
}
