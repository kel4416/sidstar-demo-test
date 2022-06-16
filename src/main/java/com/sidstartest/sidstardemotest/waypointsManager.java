package com.sidstartest.sidstardemotest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@RestController
public class waypointsManager {
    @GetMapping("/getTopTwoAssoWaypoints")
    public JSONObject getTopTwoAssoWaypoints(@RequestParam String icao,@RequestParam String stdIntrutmentType){
        System.out.println("Start getting top two associated Waypoints for Airport");

        if(icao == null){
            JSONObject toRet = new JSONObject();
            toRet.put("Error","Airport icao code not found");
            return toRet;
        }
        try {
            URL url = new URL("https://open-atms.airlab.aero/api/v1/airac/" + stdIntrutmentType + "/airport/" + icao);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("api-key", "G9Tw58HE6HDzyq94HFmnd2yOymAuU32k2mEgL3oTVbhLl6E1opu5Hqxb5BASwCWv");
            conn.connect();
            int responsecode = conn.getResponseCode();
            System.out.println(responsecode);
            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                String inline = "";
                Scanner scanner = new Scanner(conn.getInputStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                //Close the scanner
                scanner.close();

                //Using the JSON simple library parse the string into a json object
                JSONParser parse = new JSONParser();
                Object obj = parse.parse(inline);
                if (obj instanceof JSONObject) {
                    JSONObject jo = (JSONObject) obj;
                    return jo;
                } else {
                    JSONArray ja = (JSONArray) obj;

                    HashMap<String,Integer> waypointsAssociationCount = new HashMap<String,Integer>();
                    int topCount = 0;
                    int secondCount = 0;
                    for(int i=0; i < ja.size();i++){
                        JSONObject standardInstrument = (JSONObject)ja.get(i);
                        JSONArray waypoints = (JSONArray)standardInstrument.get("waypoints");
                        for(int j=0; j < waypoints.size();j++){
                            JSONObject waypointData = (JSONObject)waypoints.get(j);
                            String waypointUID = (String)waypointData.get("name");
                            if(waypointsAssociationCount.containsKey(waypointData.get("name").toString())){
                                waypointsAssociationCount.put((String)waypointData.get("name"),waypointsAssociationCount.get(waypointUID)+1);
                            }else{
                                waypointsAssociationCount.put((String)waypointData.get("name"),1);
                            }

                        }
                    }

                    //Find top 2 counts
                    for (Map.Entry<String, Integer> set : waypointsAssociationCount.entrySet()) {
                        if(set.getValue() > topCount ){
                          topCount = set.getValue();
                        } else if(set.getValue() < topCount && set.getValue() > secondCount){
                            secondCount = set.getValue();
                        }
                    }

                    JSONObject toReturn = new JSONObject();
                    toReturn.put("aiport","WSSS");

                    JSONArray waypointsToReturn = new JSONArray();
                    for (Map.Entry<String, Integer> set : waypointsAssociationCount.entrySet()) {
                        if(set.getValue() == topCount || set.getValue() == secondCount){
                            JSONObject waypointD = new JSONObject();
                            waypointD.put("name",set.getKey());
                            waypointD.put("count",set.getValue());
                            waypointsToReturn.add(waypointD);
                        }
                    }

                    toReturn.put("topWaypoints",waypointsToReturn);
                    return toReturn;
                }
            }
        }catch(Exception e){
            System.out.print("Connection Error");
            System.out.println(e.toString());
        }
        return new JSONObject();
    }

    static <K,V extends Comparable<? super V>>
    SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res : 1;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

}
