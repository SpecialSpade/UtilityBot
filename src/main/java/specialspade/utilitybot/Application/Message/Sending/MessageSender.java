package specialspade.utilitybot.Application.Message.Sending;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import specialspade.utilitybot.Application.Application.AppInterface;

public class MessageSender {

    public void sendMessage(Message message, AppInterface app, String text){
        SendMessage newMsg = app.createMessageToSend(message, text);
        app.sendMessage(newMsg);
    }

}
