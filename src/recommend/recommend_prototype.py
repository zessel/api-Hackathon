import urllib.request
import urllib.parse
import json
import time

# Uses google distance to find the time
# Obviously the hardcoded url won't work but I didn't want to deal with the text parsing
# you can change travel type like we talked about

my_distance_key =  '' 
google_url = 'https://maps.googleapis.com/maps/api/distancematrix/json?origins=1400+John+F+Kennedy+Blvd,+Philadelphia,+PA+191907&destinations=1925+N+12th+St,+Philadelphia,+pa+19122&key='
distance_json_result = json.load(urllib.request.urlopen(google_url+my_distance_key))

# 12 Hour forecast (longest I can get with trial) gives results by the hour
# this is just a quick way to convert the epoch time + the estimated travel time
# to the closest hour when you arrive

time_taken = distance_json_result['rows'][0]['elements'][0]['duration']['value']
arrival_time_epoch = round((time.time() + time_taken) / 3600) * 3600

# accuweather forces you to get their location id before getting a forecast
# I figured the zip code was the best way for us to search.  If you run this
# you should put your own key in because I think there are only 50 a day

accuweather_key = ''
accuweather_locationID_url = 'http://dataservice.accuweather.com/locations/v1/postalcodes/search?apikey=' + accuweather_key + '&q=19112'
locationID_json_result = json.load(urllib.request.urlopen(accuweather_locationID_url))
locationID = locationID_json_result[0]['Key']

# This is the actual 12 hour forecast request same as the others

forecast_url = 'http://dataservice.accuweather.com/forecasts/v1/hourly/12hour/' + locationID + '?apikey=' + accuweather_key
forecast_json_result = json.load(urllib.request.urlopen(forecast_url))

# not a python expert so I don't know how scope / initialization works so I
# tried to play it safe here.  Scans the forecast json for the matching time
# then takes the sky, temperature, and chance of rain

icon_phrase = ''
temp = 0
precipitation = ''

for x in range(12):
    if arrival_time_epoch == forecast_json_result[x]['EpochDateTime']:
        icon_phrase = forecast_json_result[x]['IconPhrase']
        temp = forecast_json_result[x]['Temperature']['Value']
        precipitation = forecast_json_result[x]['PrecipitationProbability']


# prints everything just to check.
    
print('Time:  ' + str(time_taken))
print (arrival_time_epoch)

print(icon_phrase)
print(temp)
print(precipitation)

print(json.dumps(distance_json_result, indent=4, sort_keys=True))
print(json.dumps(locationID_json_result, indent=4, sort_keys=True))
print(json.dumps(forecast_json_result, indent=4, sort_keys=True))

# quick elif ladder that I'll make a switch when I got to java

if temp > 80:
    print('Shorts and a tshirt would be appropriate')
elif temp > 70 and temp < 80:
    print('Shorts or pants with a tshirt would be appropriate')
elif temp > 60 and temp < 70:
    print('You could wear jeans and a tshirt with a light jacket or hoodie')
elif temp > 50 and temp < 60:
    print('You should wear pants, a long sleeve shirt, and a jacket')
elif temp > 40 and temp < 50:
    print('Make sure you take a heavy jacket and a warm hat with you')
else:
    print('It\'s freezing out there.  Make sure you wear a heavy jacket and warm hat.\nBring a scarf and gloves if you have them.') 
