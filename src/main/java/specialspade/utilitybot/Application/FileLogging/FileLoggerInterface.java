package specialspade.utilitybot.Application.FileLogging;

import org.telegram.telegrambots.meta.api.objects.Message;
import specialspade.utilitybot.Application.TemperatureData.TownTemperatureData;

public interface FileLoggerInterface {
    void writeToLogFileFromUser(Message message);
    void writeToLogFileToUser(String content, Message message);
    void writeToLogFileTemperature(TownTemperatureData temperatureData, Message message);

}
