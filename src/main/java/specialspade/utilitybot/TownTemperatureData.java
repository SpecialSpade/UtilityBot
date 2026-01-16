package specialspade.utilitybot;

public class TownTemperatureData {
    private String town;
    private Temperature temp;

    public TownTemperatureData(String town, Temperature temp) {
        this.town = town;
        this.temp = temp;

    }

    public String getTown() {
        return this.town;
    }

    public Temperature getTemperature() {
        return temp;
    }

}