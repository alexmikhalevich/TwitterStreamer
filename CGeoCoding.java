package ru.fizteh.fivt.students.maked0n.moduletests.library;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CGeoCoding {
    static final int EARTH_RADIUS = 6371;

    public static double[] getPlace(CJCommanderParameters commander)
            throws Exception {
        GeoApiContext context = new GeoApiContext()
                .setApiKey("AIzaSyBSDOenl2KKAaFFWOqk_OhGxTvHeH8SV1o");
        GeocodingResult[] result = GeocodingApi.geocode(context,
                commander.getPlace()).await();

        double lat1 = result[0].geometry.bounds.northeast.lat;
        double lng1 = result[0].geometry.bounds.northeast.lng;
        double lat2 = result[0].geometry.bounds.southwest.lat;
        double lng2 = result[0].geometry.bounds.southwest.lng;
        double radius = (EARTH_RADIUS * Math.acos(Math.sin(lat1) * Math.sin(lat2)
                + Math.cos(lng1) * Math.cos(lng2) * Math.cos(Math.abs(lng1 - lng2)))) / 2;
        return new double[] {result[0].geometry.location.lat,
                result[0].geometry.location.lng, radius};
    }

    public static double[] getCurrentLocation() throws IOException {
        URL url = new URL("http://ipinfo.io/json");
        URLConnection urlConnection = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                urlConnection.getInputStream()));
        String inputLine;
        StringBuilder urlPage = new StringBuilder("");
        while ((inputLine = in.readLine()) != null) {
            urlPage.append(inputLine);
        }
        in.close();
        Pattern pattern = Pattern.compile("\\d+\\.\\d+,\\d+\\.\\d+");
        Matcher matcher = pattern.matcher(urlPage.toString());
        if (matcher.find()) {
            String location = matcher.group();
            String[] locationArray = location.split(",");
            return new double[] {Double.parseDouble(locationArray[0]),
                    Double.parseDouble(locationArray[1])};
        } else {
            throw new IOException("Unable to get current location");
        }
    }
}
