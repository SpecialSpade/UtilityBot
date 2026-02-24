package specialspade.utilitybot.Application.Application;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import specialspade.utilitybot.Application.Database.DatabaseAddTaskInterface;
import specialspade.utilitybot.Application.Database.DatabaseDeleteTaskInterface;
import specialspade.utilitybot.Application.Database.DatabaseListTasksInterface;
import specialspade.utilitybot.Application.FileLogging.FileLoggerInterface;
import specialspade.utilitybot.Application.TemperatureData.TownTemperatureData;
import specialspade.utilitybot.Application.Weather.WeatherServiceInterface;

public interface AppInterface extends FileLoggerInterface, WeatherServiceInterface,
        DatabaseAddTaskInterface, DatabaseListTasksInterface, DatabaseDeleteTaskInterface {


    SendMessage createMessageToSend(Message message, String text);

    void setProperty(String string);
    void sendMessage(SendMessage message);
    void sendNotFound(Message message);
    void sendWeatherUpdate(TownTemperatureData weatherData, Message message);

}
