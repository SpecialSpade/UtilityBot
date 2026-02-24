package specialspade.utilitybot.Application.Message.MessageTypes;


public class TaskListMessage extends  UserMessage{

    public TaskListMessage(String messageType){
        super(messageType);
    }

    public static UserMessage userMessage(){
        return new TaskListMessage("tasklist");
    }

    @Override
    public boolean requireImmediateResponse(){
        return true;
    }
}
