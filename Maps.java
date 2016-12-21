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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.AllDirectedPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;


public class Maps {
    public static String source="40.8396125,14.1852795"; //coordinate di MSA.   
    public ArrayList<LatLng> steps;
  
    
    public void Maps(){}
    
    // HTTP GET request
    public StringBuilder GetRequest(String src, String dest) throws Exception {
        String USER_AGENT ="Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36";
	
        String url="https://maps.googleapis.com/maps/api/directions/json?";
               url+="origin="+src;
               url+="&destination="+ dest;
               url+="&key=AIzaSyBYoKm3CXK5_s1SlB4gkIROS93lIti9ksE";

        URL obj = new URL(url);                                             /** Creates a URL object from the String representation.    */
	HttpURLConnection con = (HttpURLConnection) obj.openConnection();   /** openConnection():
                                                                              * Returns a URLConnection instance that represents a connection 
                                                                              * to the remote object referred to by the URL.
                                                                              */ 
	con.setRequestMethod("GET");    // optional default is GET
	con.setRequestProperty("User-Agent", USER_AGENT);   //add request header
        
        int responseCode = con.getResponseCode();   /** getResponseCode() :An int representing the three digit HTTP Status-Code.    */
        StringBuilder response;
        /** getInputStream() :Returns an input stream that reads from this open connection, 
          * A SocketTimeoutException can be thrown when reading from the returned input stream 
          * if the read timeout expires before data is available for read.
          */
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            response = new StringBuilder();
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        
        return(response);
    }

    // HTTP GET request
    public StringBuilder GetRequest(String src, String dest, ArrayList WP) throws Exception {
        String USER_AGENT ="Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36";
	String waypoint="";
        for(int i=0; i<WP.size(); i++){
            waypoint+=WP.get(i)+"|";
        }
        
        String url="https://maps.googleapis.com/maps/api/directions/json?";
               url+="origin="+src;
               url+="&waypoints="+ waypoint; 
               url+="&destination="+ dest;
               url+="&optimizeWaypoints:true";
               url+="&key=AIzaSyBYoKm3CXK5_s1SlB4gkIROS93lIti9ksE";

        URL obj = new URL(url);
	HttpURLConnection con = (HttpURLConnection) obj.openConnection();

	// optional default is GET
	con.setRequestMethod("GET");
	
	con.setRequestProperty("User-Agent", USER_AGENT);
        
        int responseCode = con.getResponseCode();
        
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
     * This method returns the number of km required to reach the destination.
     * @param response
     * @return 
     **/
    public long getKm(StringBuilder response){    
        JsonElement element=new Gson().fromJson(response.toString(), JsonElement.class);
        
        JsonArray legs=element.getAsJsonObject()
                        .get("routes").getAsJsonArray().get(0).getAsJsonObject()
                        .get("legs").getAsJsonArray();
        
        long KM=0;
        /**
         * Sums the distances of all the paths included in a delivery.
         */
        for(int k=0; k<legs.size(); k++){            
            KM+=legs.get(k).getAsJsonObject()
                            .get("distance").getAsJsonObject()
                            .get("value").getAsLong();
        }
        
        return (KM/1000);
    }
    
    /**
     * This method returns the Departure and arrival time of a delivery.
     * @param response
     * @return 
     **/
    public long getArrivalTime(StringBuilder response){    
        JsonElement element=new Gson().fromJson(response.toString(), JsonElement.class);
        
        JsonArray legs=element.getAsJsonObject()
                        .get("routes").getAsJsonArray().get(0).getAsJsonObject()
                        .get("legs").getAsJsonArray();
        
        long ArrivalTime=0;
        
        for(int i=0; i<legs.size(); i++){            /** Sums all the Arrival times of the waypoints of a delivery.  */
            ArrivalTime+=legs.get(i).getAsJsonObject()
                            .get("duration").getAsJsonObject()
                            .get("value").getAsLong();
        }
        
        return (ArrivalTime/60);
    }
    
    
    /**
     * This method returns a value indicating how long a delivery will take.
     * @param response
     * @return 
     **/
    public String getDuration(StringBuilder response){   
        
        JsonElement element=new Gson().fromJson(response.toString(), JsonElement.class);
                
        String duration=element.getAsJsonObject()
                        .get("routes").getAsJsonArray().get(0).getAsJsonObject()
                        .get("legs").getAsJsonArray().get(0).getAsJsonObject()
                        .get("duration").getAsJsonObject()
                        .get("text").getAsString();
        
        return(duration);
    }
    
    /**
     * Builds the url cotaining the static map of a waypint.
     * @param pati
     * @return 
     */
    public String buildUrl(ArrayList<LatLng> pati){      
        String path = "http://maps.googleapis.com/maps/api/staticmap?";
        path += "&size=402x309";
        path += "&maptype=roadmap";
        path += "&key=AIzaSyBYoKm3CXK5_s1SlB4gkIROS93lIti9ksE";
        path += "&format=jpg";
        path += "&path=color:0x0000ff|weight:5";
        path += "|enc:";
        path += encode(pati);
        return path;
    }
    
    /**
     * Builds the url containing the static map of the last path also showing its marker.
     * @param pati
     * @param marker
     * @return 
     */
    public String buildUrl(ArrayList<LatLng> pati, String marker){      
        String path = "http://maps.googleapis.com/maps/api/staticmap?";
        path += "&size=402x309";
        path += "&maptype=roadmap";
        path += "&key=AIzaSyBYoKm3CXK5_s1SlB4gkIROS93lIti9ksE";
        path += "&format=jpg";
        path += "&markers=color:red%7Clabel:%7C"+marker;
        path += "&path=color:0x0000ff|weight:5";
        path += "|enc:";
        path += encode(pati);
        return path;
    }
    
    /**
     * Builds the url containing the static map also showing the markers of the beginning and the end of the journey.
     * @param pati
     * @param src
     * @param dst     
     * @return 
     */
    public String buildUrl(ArrayList<LatLng> pati, String src, String dst){      
        String path = "http://maps.googleapis.com/maps/api/staticmap?";
        path += "&size=402x309";
        path += "&maptype=roadmap";
        path += "&key=AIzaSyBYoKm3CXK5_s1SlB4gkIROS93lIti9ksE";
        path += "&format=jpg";
        path += "&markers=color:red%7Clabel:%7C"+src;
        path += "&markers=color:red%7Clabel:%7C"+dst;
        path += "&path=color:0x0000ff|weight:5";
        path += "|enc:";
        path += encode(pati);
        
        return path;
    }
    
    
    /**
     * Builds the delivery's path on the map.
     * @param response
     * @return 
     **/
    public ArrayList<LatLng> getPath(StringBuilder response){
        JsonElement element=new Gson().fromJson(response.toString(), JsonElement.class);
        JsonArray legs=element.getAsJsonObject()
                        .get("routes").getAsJsonArray().get(0).getAsJsonObject()
                        .get("legs").getAsJsonArray();
        
        steps=new ArrayList<>();
        for(int i=0; i<legs.size(); i++ ){
            int N=legs.get(i).getAsJsonObject().get("steps").getAsJsonArray().size();
            for(int j=0; j<N; j++){
                steps.addAll(decode(legs.get(i).getAsJsonObject()
                        .get("steps").getAsJsonArray().get(j).getAsJsonObject()
                        .get("polyline").getAsJsonObject()
                        .get("points").getAsString()));
            }
        }
       
        return(steps);
    }
    
    /**
     *  Divides the coordinats of a too long path so that it can be memorized in an URL.
     * @param steps
     * @return 
     */
    public ArrayList<ArrayList<LatLng>> getPartsPart(ArrayList<LatLng> steps){
        ArrayList<ArrayList<LatLng>> res = new ArrayList<>();
        ArrayList<LatLng> tmp;
        int regione;
        
        if(steps.size()>1500){
            regione = steps.size()/1500;    
        }else{
            regione=1;
        }
        int pins = steps.size()/regione;    
        
        for(int i = 0;i<regione;i++){
            tmp = new ArrayList<>();
            for(int j = i*pins;j<(i+1)*pins;j++ ){
                tmp.add(steps.get(j));
            }
            res.add(i, tmp);
        }
        return res;
    }
    
    
    
    
    
    /**
     * Add to the graph the new vertexes and the archs (src->dest and dest->src) if they are not already there,
     * for each new inserted arch a new GoogleMaps request is made in order to define the distance in km of the two points,
     * the arch's weight will be rapresented from the distance returned.
     * @param graph
     * @param src
     * @param dest
     */
    public void fillGraph(SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph, String src, String dest){
        //Maps maps=new Maps();
        
        if(!graph.containsVertex(src))
            graph.addVertex(src);
        if(!graph.containsVertex(dest))  
            graph.addVertex(dest);
        
        if(!src.equals(dest)){
            if(!graph.containsEdge(src, dest)){
                DefaultWeightedEdge e1 = graph.addEdge(src, dest); 
                try {
                    //graph.setEdgeWeight(e1, maps.getKm(maps.GetRequest(src, dest)));
                    graph.setEdgeWeight(e1, getKm(GetRequest(src, dest)));
                } catch (Exception ex) {
                    Logger.getLogger(Assignments.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
            
            if(!graph.containsEdge(dest,src)){
                DefaultWeightedEdge e2 = graph.addEdge(dest,src); 
                try {
                    //graph.setEdgeWeight(e2, maps.getKm(maps.GetRequest(dest,src)));
                    graph.setEdgeWeight(e2, getKm(GetRequest(dest,src)));
                } catch (Exception ex) {
                    Logger.getLogger(Assignments.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
    }
    
    /**
     * This method is used to trasform the graph into a connected one.
     * @param graph
     */
    public void completeGraph(SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph){
        Set<String> Vertex=graph.vertexSet();
        
        for(String i: Vertex)
            for(String j :Vertex)
                fillGraph(graph, i, j);
    }
    
    /**
     * This method is used to build an oriented and connected graph which has the 
     * adresses of the daily deliveries (currAdd) as nodes and the distances in km between them as archs.
     * The shortest path between the nodes is found using the Dijkstra algorithm, then the
     * found waypoints are inserted in an arraylist and given as paramethers to the method that 
     * displays the path on the map.
     * @param currAdd
     * @param client
     * @return 
     */
    public ArrayList<ArrayList<String>> findWaypoint(String currAdd, ArrayList<String> client){
        SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph;
        graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        
        fillGraph(graph, source, currAdd);                //Method used to add the vertexes to the graph.
        for(int i=0; i<client.size(); i++){            
            fillGraph(graph, currAdd, client.get(i));
        }
    
        completeGraph(graph);   /** Method to make a graph into a connected one. */
        
        /**
         * AlldirectedPaths: 
         * A Dijkstra-like algorithm to find all paths between two sets of nodes in a directed graph, 
         * with options to search only simple paths and to limit the path length. 
         */
        AllDirectedPaths<String, DefaultWeightedEdge> p=new AllDirectedPaths<>(graph); 
        /**
         * getAllPaths:
         * Calculate all paths from the source vertices to the target vertices. 
         */
        //List<GraphPath<String, DefaultWeightedEdge>> allP =p.getAllPaths(source, currAdd, rootPaneCheckingEnabled, null);  
        List<GraphPath<String, DefaultWeightedEdge>> allP =p.getAllPaths(source, currAdd, true, null);  
        
        
        ArrayList<ArrayList<String>> allPathList=new ArrayList<>();
        /** 
         * Builds the Array which contains the waypoint of the shortest paths from source to currAdd, source and currAdd excluded.
         */
        String path;
        for(int i=0; i<allP.size(); i++){
            ArrayList<String> APP=new ArrayList<>();
            String prec="";
            path=allP.get(i).toString();
            String [] tmp=path.split("[( : )]");
            
            for(String j :tmp){
                if(!j.equals(source) && !j.equals(currAdd) && !j.equals("[") && !j.equals("]") && !j.equals("") && !j.equals(","))
                    if(!prec.equals(j)){
                        prec=j;
                        APP.add(j);
                    }
            }
            allPathList.add(APP);         
        }
    
        return(allPathList);        
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
