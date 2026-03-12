package specialspade.utilitybot.Application.Weather;

import org.telegram.telegrambots.meta.api.objects.Update;
import specialspade.utilitybot.Application.TemperatureData.TownTemperatureData;

public interface WeatherServiceInterface {
    /**
     * Fetches the requested weather data.
     * @param update    Update received from the user.
     * @return          Weather data.
     */
    TownTemperatureData getWeatherData(Update update);
}
