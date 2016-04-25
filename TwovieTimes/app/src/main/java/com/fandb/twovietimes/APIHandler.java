package com.fandb.twovietimes;

import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.google.gson.Gson;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Steve on 4/22/2016.
 */

enum APIType{
    GetTheaters,
};

public class APIHandler {
    public static final String API_KEY = "9mdax3qa5xncse6gmc6bccyq";
    public static final String TAG = "APIHANDLER";

    public static String[] getAddress(URL link) throws IOException {
        String[] address = null;

        Document doc = Jsoup.connect(link.toString())
                .maxBodySize(0)
                .timeout(1000000000)
                .get();

        Element ele = doc.getElementById("maplink");
        if(ele == null){
             ele = doc.select("h2").first().select("a").first();
            if(ele != null) getAddress(new URL(ele.attr("href")));
            else Log.d(TAG, "null :(");
        }
        else{
            Log.d(TAG, "AMIHERE????");
        }

        return address;
    }

    public static ArrayList<Theater> getTheaters(){
        String json = getRequest("http://data.tmsapi.com/v1.1/movies/showings?startDate=2016-04-24&zip=80401&api_key=9mdax3qa5xncse6gmc6bccyq", false).toString();

        Gson gson = new Gson();
        APIMovieTemplate[] mts = gson.fromJson(json, APIMovieTemplate[].class);

        ArrayList<Theater> ths = new ArrayList<Theater>();
        HashSet<String> con = new HashSet<String>();
        for(APIMovieTemplate mt : mts){
            for(APIMovieTemplate.showtimes s : mt.showtimes){
                if(con.contains(s.theatre.id)) continue;
                con.add(s.theatre.id);
                ths.add(new Theater(s.theatre.name, "", "", s.theatre.id));
            }
        }

        return ths;
    }


    //NOTE: Default value for boolean is false
    //NOTE: This method will work for any get request (Not just the tmsapi)
    //uri = the uri to access, addAPIKey is whether to append the api key to the string
    //Returns the result as a single string
    public static StringBuffer getRequest(String uri, boolean addAPIKey){
        if(addAPIKey) uri += "&api_key=" + API_KEY;

        StringBuffer response = new StringBuffer();

        try{
            URL obj = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        }
        //I might want to change how these errors are handled.
        catch(MalformedURLException e){
            Log.d(TAG, String.valueOf(e));
            response.append(e.toString());
        } catch (ProtocolException e) {
            Log.d(TAG, String.valueOf(e));
            response.append(e.toString());
        //Catches 403 Error
        } catch (IOException e) {
            Log.d(TAG, String.valueOf(e));
            response.append(e.toString());
        //This should never happen since it's Async
        }catch (NetworkOnMainThreadException e) {
            Log.d(TAG, String.valueOf(e));
            response.append(e.toString());
        }
        return response;

    }
}
