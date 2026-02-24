package specialspade.utilitybot.Application.Weather;

import org.telegram.telegrambots.meta.api.objects.Update;
import specialspade.utilitybot.Application.TemperatureData.TownTemperatureData;

public interface WeatherServiceInterface {
    TownTemperatureData getWeatherData(Update update);
}
