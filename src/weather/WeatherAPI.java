package weather;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAPI {

    private String userAgent = "Mozilla/5.0";
    final private String locKeyURLBase = "tp://dataservice.accuweather.com/locations/v1/cities/geoposition/search.json?q=";
    final private String apikey = "ZSLD4hAmz8yFoRzW1cTiTDXc3UeFlyOi";
    private String locationKey;

    public WeatherAPI(float[] coords){
        getlocationKey(coords);
    }

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
                System.out.println(response);
                JSONObject js = new JSONObject(response.toString());//Turn response into a JSON object.
                //the KEY object within the JSON is the location key we need to get the weather forecast.
                this.locationKey = js.getString("Key");
            }
        } catch (IOException e){
            System.out.println(e.getStackTrace());
        }

    }

}



