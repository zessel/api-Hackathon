import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
import java.net.URL;
import org.json.*;



public class main{
    public static void main(String[] arc){
        String hello = "http://dataservice.accuweather.com/locations/v1/cities/geoposition/search.json?q=40.79,-77.86&apikey=fQv5FRVu962QbeNcBo5O3O1bFU4fQmzf";
        String userAgent = "Mozilla/5.0";
        URL location;
        HttpURLConnection connection;
        String inputLine;
        StringBuffer response;
        try {
            location = new URL(hello);
            connection = (HttpURLConnection) location.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", userAgent);
            int responseCode = connection.getResponseCode();
            System.out.println("GET Response Code ::" + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println(response);
                JSONObject js = new JSONObject(response.toString());
                System.out.println(js);
                String key = js.getString("Key");
                String weatherSite = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/" + key + "?apikey=fQv5FRVu962QbeNcBo5O3O1bFU4fQmzf";
                URL weather = new URL(weatherSite);
                HttpURLConnection weatherCon = (HttpURLConnection) weather.openConnection();
                weatherCon.setRequestMethod("GET");
                weatherCon.setRequestProperty("User-Agent", userAgent);
                responseCode = weatherCon.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader in2 = new BufferedReader(new InputStreamReader(weatherCon.getInputStream()));
                    response = new StringBuffer();
                    while ((inputLine = in2.readLine()) != null){
                        response.append(inputLine);
                    }
                    in.close();
                    System.out.println(response);
                    JSONObject weatherJS = new JSONObject(response.toString());
                    System.out.println(weatherJS.getJSONObject("Headline").getString("Text"));
                    System.out.println(weatherJS.getJSONObject("Headline").getString("Category"));
                    System.out.println(weatherJS.getJSONArray("DailyForecasts").get(0));
                    JSONObject temperature = ((JSONObject)weatherJS.getJSONArray("DailyForecasts").get(0)).getJSONObject("Temperature");
                    System.out.println(temperature.getJSONObject("Minimum").getInt("Value") + " " + temperature.getJSONObject("Minimum").getString("Unit")+ " and the High will be " +temperature.getJSONObject("Maximum").getInt("Value"));
                } else{
                    System.out.println("Bad GET");
                }
            }
            else{
                System.out.println("GET request didn't work");
            }
        }catch (IOException e){
            System.out.println(e);
            System.exit(9);
        }

    }

}