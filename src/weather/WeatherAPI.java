package weather;

import org.json.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAPI {

    private String userAgent = "Mozilla/5.0";
    final private String locKeyURLBase = "tp://dataservice.accuweather.com/locations/v1/cities/geoposition/search.json?q=";
    final private String dailyForcastURLBase = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/";
    final private String currentCondURLBase = "http://dataservice.accuweather.com/currentconditions/v1/";
    final private String apikey = "ZSLD4hAmz8yFoRzW1cTiTDXc3UeFlyOi";
    private String locationKey = null;

    public WeatherAPI(float[] coords){
        getlocationKey(coords);
    }
    //Finds location key for accuweather API based on provided coordinates (lat, lon)
    public void getlocationKey(float[] coords){
        String url = locKeyURLBase + coords[0] + "," + coords[1] + "&apikey=" + apikey;
        try {
            URL location = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) location.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", userAgent);
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer response = new StringBuffer();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);//Response will be all the JSON gathered from the site.
                }
                in.close();
                JSONObject js = new JSONObject(response.toString());//Turn response into a JSON object.
                //the KEY object within the JSON is the location key we need to get the weather forecast.
                locationKey = js.getString("Key");
            } else{
                System.out.println("Bad GET request for location Key");
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public JSONObject getCurrentConditions(){
        if (locationKey == null){
            return null;
        }
        String url = currentCondURLBase + locationKey + "?apikey=" + apikey;
        try {
            URL currenConds = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)currenConds.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", userAgent);
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer response = new StringBuffer();
                String inputLine;
                while((inputLine = in.readLine()) != null){
                    response.append(inputLine);
                }
                in.close();
                return new JSONObject(response.toString());//Turn response into a JSON object.
            } else{
                System.out.println("Bad GET request for current conditions");
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }


    public JSONObject 
}



