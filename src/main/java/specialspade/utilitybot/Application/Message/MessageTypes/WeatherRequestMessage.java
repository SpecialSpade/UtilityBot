package specialspade.utilitybot.Application.Message.MessageTypes;


public class WeatherRequestMessage extends UserMessage {

    public WeatherRequestMessage(String messageType){
        super(messageType);
    }


    public static UserMessage userMessage(){
        return new WeatherRequestMessage("weather");
    }

}
