package testgroupz.utilitybot;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class WeatherService implements WeatherServiceInterface {

    private String weather_api_key = System.getenv("WEATHER_API_KEY");
    private StringBuilder url = new StringBuilder("https://api.weatherapi.com/v1/current.json?q=");

    @Override
    public TownTemperatureData getWeatherData(Update update) {
        return getWeather(update);
    }

    private TownTemperatureData extractData(BufferedReader reader) {
        String[] lines;
        try {
            lines = reader.readLine().split("},");
            String tempLine = "";
            String townLine = "";
            for (String string : lines) {
                if (string.contains("temp_c")) {
                    tempLine = string;
                }
                if (string.contains("name")) {
                    townLine = string;
                }
            }
            String town = extractTown(townLine);
            Temperature temp = extractTemp(tempLine);
            return new TownTemperatureData(town, temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Temperature extractTemp(String line) {
        if (line != null) {
            String[] splitLines = line.split(",");
            for (String string : splitLines) {
                if (string.contains("temp_c")) {
                    return new Temperature(Double.parseDouble(string.split(":")[1]));
                }
            }
        }
        return null;
    }

    private String extractTown(String line) {
        if (line != null) {
            String[] splitLines = line.split(",");
            for (String string : splitLines) {
                if (string.contains("name")) {
                    String town = string.split(":\"")[1].replaceAll("\"", "").replace(",", "");
                    return town;
                }
            }
        }
        return null;
    }

    public TownTemperatureData getWeather(Update update) {
        String inputTown = update.getMessage().getText().replaceAll("ä", "ae").replaceAll("ö", "oe")
                .replaceAll("ü", "ue").replaceAll(" ", "_");
        String tempUrl = url + inputTown.trim();
        tempUrl += "&key=" + weather_api_key;
        TownTemperatureData tempTownData = connection(tempUrl);
        return tempTownData;

    }

    public TownTemperatureData connection(String url) {
        try {
            if (url != null) {
                URL urlWeather = new URI(url).toURL();
                HttpURLConnection con = (HttpURLConnection) urlWeather.openConnection();
                con.setRequestMethod("GET");
                if (con.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    return extractData(reader);
                } else {
                    return null;
                }
            }
            return null;
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
