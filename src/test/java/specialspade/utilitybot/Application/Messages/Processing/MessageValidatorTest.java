package specialspade.utilitybot.Application.Messages.Processing;

import org.junit.jupiter.api.Test;
import specialspade.utilitybot.Application.Message.Processing.MessageValidator;
import specialspade.utilitybot.Application.Message.MessageTypes.DeleteTaskMessage;
import specialspade.utilitybot.Application.Message.MessageTypes.UserMessage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

public class MessageValidatorTest {


    @Test
    public void canCreateMessage(){
        DeleteTaskMessage msg = new DeleteTaskMessage("deletetask");
        ArrayList<UserMessage> list = new ArrayList<>();
        list.add(msg);
        MessageValidator mv = new MessageValidator(list);
        assertTrue(mv.canCreateMessage("deletetask"));
    }

    @Test
    public void canCreateMessageFail(){
        DeleteTaskMessage msg = new DeleteTaskMessage("deletetask");
        ArrayList<UserMessage> list = new ArrayList<>();
        list.add(msg);
        MessageValidator mv = new MessageValidator(list);
        assertFalse(mv.canCreateMessage("notDeleteTask"));
    }
}
