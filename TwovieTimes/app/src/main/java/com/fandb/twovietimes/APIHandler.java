package com.fandb.twovietimes;

import android.graphics.Movie;
import android.util.Log;

import com.google.gson.Gson;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Steve on 4/22/2016.
 */
//Docs: http://developer.tmsapi.com/docs/v2/
public class APIHandler {
    private static final String API_KEY = "9mdax3qa5xncse6gmc6bccyq";
    private static final String TAG = "APIHANDLER";
    private static double mLong;
    private static double mLat;

    //NOTE: Default value for boolean is false
    //NOTE: This method will work for any get request (Not just the tmsapi)
    //uri = the uri to access, addAPIKey is whether to append the api key to the string
    //Returns the result as a single string
    public static String getRequest(String uri, boolean addAPIKey){
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
        }
        finally{
            return response.toString();
        }
    }
}
