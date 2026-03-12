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

    /**
     * Setting current user requested mode to display in GUI.
     * @param string Message to display.
     */
    void setProperty(String string);

    /**
     *  Send the message to the specified recipient inside the {@code message}.
     * @param message Message with receiver.
     */
    void sendMessage(SendMessage message);

    /**
     * Sends a not found message to the recipient inside {@code message}.
     * @param message Message received from the user.
     */
    void sendNotFound(Message message);

    /**
     * Sends the requested data to the user.
     * @param weatherData   Data to be sent.
     * @param message       Message received from the user.
     */
    void sendWeatherUpdate(TownTemperatureData weatherData, Message message);

}
