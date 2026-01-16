package specialspade.utilitybot;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface WeatherServiceInterface {
    TownTemperatureData getWeatherData(Update update);
}
