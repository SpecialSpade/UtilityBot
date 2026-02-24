package specialspade.utilitybot.Application.Message.Processing.ProcessingTypes;

import org.telegram.telegrambots.meta.api.objects.Update;
import specialspade.utilitybot.Application.Application.AppInterface;

import java.util.Map;

public class WeatherProcessor implements ProcessorInterface {

    private final String processable = "weather";

    @Override
    public Object process(Update update, AppInterface app, Map<Long, String> userOptions) {
        return app.getWeatherData(update);


    }


    @Override
    public boolean canProcess(String task) {
        return processable.equals(task);
    }
}
