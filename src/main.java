import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
import java.net.URL;
import org.json.*;



public class main{
    public static void main(String[] arc){
        //Setting up host
        final String api1 = "fQv5FRVu962QbeNcBo5O3O1bFU4fQmzf";
        final String api2 = "e23R1oiKk6PqXmV3Q2QAVAABawaREYTF";
        final String api3 = "ZSLD4hAmz8yFoRzW1cTiTDXc3UeFlyOi";
        String locationSite = "http://dataservice.accuweather.com/locations/v1/cities/geoposition/search.json?q=40.79,-77.86&apikey=" + api3;
        String userAgent = "Mozilla/5.0";
        URL location;
        HttpURLConnection connection;
        String inputLine;
        StringBuffer response;
        try {
            System.out.println("Running");
            //Try to establish a connection with URL
            location = new URL(locationSite);
            connection = (HttpURLConnection) location.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", userAgent);
            int responseCode = connection.getResponseCode();
            System.out.println("GET Response Code ::" + responseCode);
            //If our GET is bad, then nothing works.
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);//Response will be all the JSON gathered from the site.
                }
                in.close();
                System.out.println(response);
                JSONObject js = new JSONObject(response.toString());//Turn response into a JSON object.
                //the KEY object within the JSON is the location key we need to get the weather forecast.
                String key = js.getString("Key");
                String weatherSite = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/" + key + "?apikey=" + api3;
                //Try to establish a connection with getting the weather.
                URL weather = new URL(weatherSite);
                HttpURLConnection weatherCon = (HttpURLConnection) weather.openConnection();
                weatherCon.setRequestMethod("GET");
                weatherCon.setRequestProperty("User-Agent", userAgent);
                responseCode = weatherCon.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    in = new BufferedReader(new InputStreamReader(weatherCon.getInputStream()));
                    response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    //Here is the weather forecast for the location.
                    System.out.println(response);
                    JSONObject weatherJS = new JSONObject(response.toString());
                    System.out.println(weatherJS.getJSONObject("Headline").getString("Text"));
                    System.out.println(weatherJS.getJSONObject("Headline").getString("Category"));
                    System.out.println(weatherJS.getJSONArray("DailyForecasts").get(0));
                    JSONObject temperature = ((JSONObject) weatherJS.getJSONArray("DailyForecasts").get(0)).getJSONObject("Temperature");
                    System.out.println(temperature.getJSONObject("Minimum").getInt("Value") + " " + temperature.getJSONObject("Minimum").getString("Unit") + " and the High will be " + temperature.getJSONObject("Maximum").getInt("Value"));


                } else {
                    System.out.println("Bad GET for forecast");
                }


                String currentTempSite = "http://dataservice.accuweather.com/currentconditions/v1/" + key + "?apikey=" + api3;
                System.out.println(currentTempSite);
                URL currentTempURL = new URL(currentTempSite);
                System.out.println("Made URL");
                HttpURLConnection tempCon = (HttpURLConnection) currentTempURL.openConnection();
                System.out.println("Opened Connection");
                tempCon.setRequestMethod("GET");
                tempCon.setRequestProperty("User-Agent", userAgent);
                responseCode = weatherCon.getResponseCode();
                System.out.println("Got Response Code");
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    in = new BufferedReader(new InputStreamReader(tempCon.getInputStream()));
                    response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    //Here is the weather forecast for the location.
                    System.out.println(response);
                    JSONArray weatherJS = new JSONArray(response.toString());
                    //JSONObject weatherJS = new JSONObject(response.toString());
                    /*System.out.println(weatherJS.getJSONObject("Headline").getString("Text"));
                    System.out.println(weatherJS.getJSONObject("Headline").getString("Category"));
                    System.out.println(weatherJS.getJSONArray("DailyForecasts").get(0));
                    JSONObject temperature = ((JSONObject) weatherJS.getJSONArray("DailyForecasts").get(0)).getJSONObject("Temperature");
                    System.out.println(temperature.getJSONObject("Minimum").getInt("Value") + " " + temperature.getJSONObject("Minimum").getString("Unit") + " and the High will be " + temperature.getJSONObject("Maximum").getInt("Value"));
                */
                    JSONObject temp = ((JSONObject)weatherJS.get(0)).getJSONObject("Temperature");
                    System.out.println(weatherJS.get(0));
                    System.out.println(temp.getJSONObject("Metric").getInt("Value"));
                } else{
                    System.out.println("Bad GET for current Temps.");
                }
            }
            else{
                System.out.println("Bad GET for location");
            }
        }catch (IOException e){
            System.out.println(e);
            System.exit(9);
        }

    }

}