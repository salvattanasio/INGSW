package ingsftw;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.maps.model.LatLng;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Maps {
    
    public ArrayList<LatLng> steps;
  
    public void Maps(){}
    
    // HTTP GET request
    public StringBuilder GetRequest(String dest) throws Exception {
        String USER_AGENT ="Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36";
	String source="40.8396125,14.1852795";
        
        String url="https://maps.googleapis.com/maps/api/directions/json?origin="+source;
               url+="&destination="+ dest;
               //url+="&waypoints="+ dest;  
               //url+="&key=AIzaSyBYoKm3CXK5_s1SlB4gkIROS93lIti9ksE";

        URL obj = new URL(url);
	HttpURLConnection con = (HttpURLConnection) obj.openConnection();

	// optional default is GET
	con.setRequestMethod("GET");

	//add request header
	con.setRequestProperty("User-Agent", USER_AGENT);
        
        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'GET' request to URL : " + url);
        //System.out.println("Response Code : " + responseCode);
        
        StringBuilder response;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            response = new StringBuilder();
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        
        return(response);
    }
	
    /**
     * RESTITUISCE il tempo che occorre per arrivare a destinazione.
     * @param response
     * @return 
     **/
    public long getArrivalTime(StringBuilder response){
    
        JsonElement element=new Gson().fromJson(response.toString(), JsonElement.class);
        
        long ArrivalTime=element.getAsJsonObject()
                        .get("routes").getAsJsonArray().get(0).getAsJsonObject()
                        .get("legs").getAsJsonArray().get(0).getAsJsonObject()
                        .get("duration").getAsJsonObject()
                        .get("value").getAsLong();
        
        return (ArrivalTime);
    }
    
    /**
     * RESTITUISCE il tempo che occorre per arrivare a destinazione.
     * @param response
     * @return 
     **/
    public String getParse(StringBuilder response){
        
        JsonElement element=new Gson().fromJson(response.toString(), JsonElement.class);
        
        
        String duration=element.getAsJsonObject()
                        .get("routes").getAsJsonArray().get(0).getAsJsonObject()
                        .get("legs").getAsJsonArray().get(0).getAsJsonObject()
                        .get("duration").getAsJsonObject()
                        .get("text").getAsString();
        
        return(duration);
    }
    
    /**
     *  COSTRUISCE IL PERCORSO DELLA SPEDIZIONE SULLA MAPPA.
     * @param response
     * @return 
     **/
    public ArrayList<LatLng> getPath(StringBuilder response){
        JsonElement element=new Gson().fromJson(response.toString(), JsonElement.class);
         //RESTITUISCE una stringa di coordinate dei steps intermedi
        JsonArray stepA=element.getAsJsonObject()
                        .get("routes").getAsJsonArray().get(0).getAsJsonObject()
                        .get("legs").getAsJsonArray().get(0).getAsJsonObject()
                        .get("steps").getAsJsonArray();
      
        steps=new ArrayList<>();
        for(int i = 0;i<stepA.size();i++ ){
            steps.addAll(decode(stepA.get(i).getAsJsonObject()
                    .get("polyline").getAsJsonObject()
                    .get("points").getAsString()));
        }
        return(steps);
    }
    
    /**
     *  Divide le coordinate di un percorso troppo lungo da poter essere memorizzato in un URL in sottoRegioni.
     * @param steps
     * @return 
     */
    public ArrayList<ArrayList<LatLng>> getPartsPart(ArrayList<LatLng> steps){
        ArrayList<ArrayList<LatLng>> res = new ArrayList<>();
        ArrayList<LatLng> tmp = new ArrayList();
        int regione = 0;
        int pins = 0;

        if(steps.size()>1500){
            regione = steps.size()/1500;    //regione=9
        }else{
            regione=1;
        }
        pins = steps.size()/regione;    //pins=1564
        
        for(int i = 0;i<regione;i++){
            tmp = new ArrayList<>();
            for(int j = i*pins;j<(i+1)*pins;j++ ){
                tmp.add(steps.get(j));
            }
            res.add(i, tmp);
        }
        return res;
    }
    
    public static List<LatLng> decode(final String encodedPath) {
        int len = encodedPath.length();

        final List<LatLng> path = new ArrayList<>(len / 2);
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new LatLng(lat * 1e-5, lng * 1e-5));
        }
    
        return path;
    }

    public static String encode(final List<LatLng> path) {
        long lastLat = 0;
        long lastLng = 0;

        final StringBuffer result = new StringBuffer();

        for (final LatLng point : path) {
            long lat = Math.round(point.lat * 1e5);
            long lng = Math.round(point.lng * 1e5);

            long dLat = lat - lastLat;
            long dLng = lng - lastLng;

            encode(dLat, result);
            encode(dLng, result);

            lastLat = lat;
            lastLng = lng;
        }
        
        return result.toString();
  }

    private static void encode(long v, StringBuffer result) {
        v = v < 0 ? ~(v << 1) : v << 1;
        while (v >= 0x20) {
            result.append(Character.toChars((int) ((0x20 | (v & 0x1f)) + 63)));
            v >>= 5;
        }
    
        result.append(Character.toChars((int) (v + 63)));
    }

    public static String encode(LatLng[] path) {
    return encode(Arrays.asList(path));
  }
}
