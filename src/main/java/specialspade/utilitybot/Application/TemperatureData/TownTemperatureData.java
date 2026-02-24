package specialspade.utilitybot.Application.TemperatureData;

public class TownTemperatureData {
    private final String town;
    private final Temperature temp;

    public TownTemperatureData(String town, Temperature temp) {
        this.town = town;
        this.temp = temp;

    }

    public String getTown() {
        return this.town;
    }


    public Double getTemperatureData(){
        return temp.getTemperature();
    }

}