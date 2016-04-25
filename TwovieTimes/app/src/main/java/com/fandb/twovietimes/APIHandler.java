package com.fandb.twovietimes;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.provider.MediaStore;
import android.util.Log;

import com.google.gson.Gson;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * Created by Steve on 4/22/2016.
 */

enum APIType{
    GetTheaters,
};

public class APIHandler {
    public static final String API_KEY = "9mdax3qa5xncse6gmc6bccyq";
    public static final String TAG = "APIHANDLER";

    public static boolean mInit = false;
    public static String mTheatreId;
    static AssetManager mAM;
    //Title -> Movie
    public static HashMap<String, Movie> mMovies = new HashMap<String, Movie>();
    //TheatreId -> MovieTime[]
    public static HashMap<String, ArrayList<MovieTime> > mMovieTimes = new HashMap<String, ArrayList<MovieTime> >();
    //TheatreId -> Theater
    public static HashMap<String, Theater> mTheaters = new HashMap<String, Theater>();


    public static String getAddress(String link, String theatre) throws IOException {
        if(mTheaters.containsKey(theatre)) {
            if (mTheaters.get(theatre).getAddress() != "") {
                return mTheaters.get(theatre).getAddress();
            }
        }
        else return "";

        Document doc = Jsoup.connect(link)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:45.0) Gecko/20100101 Firefox/45.0")
                .maxBodySize(0)
                .timeout(1000000000)
                .get();

        Element ele = doc.getElementById("maplink");
        if(ele == null){
             ele = doc.select("h2").first().select("a").first();
            if(ele != null) return getAddress(ele.attr("href"), theatre);
        }
        else{
            Theater t;
            t = mTheaters.get(theatre);
            t.setAddress(ele.text());
            mTheaters.put(theatre, t);
            return ele.text();
        }

        return "";
    }

    public static Integer parseRunTime(String runtime){
        runtime = runtime.replace("PT", "");
        runtime = runtime.replace("M", "");
        String[] rt = runtime.split("H");

        return Integer.parseInt(rt[0]) * 60 + Integer.parseInt(rt[1]);
    }

    public static Date getStartDate(String date){
        String[] pieces = date.split("T");

        DateFormat form = new SimpleDateFormat("yyyy-MM-ddHH:mm", Locale.ENGLISH);
        try {
            return form.parse(pieces[0] + pieces[1]);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return null;
    }

    public static Date getEndDate(String date, int duration){
        String[] pieces = date.split("T");

        DateFormat form = new SimpleDateFormat("yyyy-MM-ddHH:mm", Locale.ENGLISH);
        try {
            Date dat = form.parse(pieces[0] + pieces[1]);
            form = new SimpleDateFormat("kk:mm", Locale.ENGLISH);

            pieces[0] = String.valueOf(duration % 60);
            pieces[1] = String.valueOf(duration / 60);

            return new Date(dat.getTime() + form.parse(pieces[0] + ":" + pieces[1]).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return null;
    }

    public static ArrayList<MovieTime> getMovieTimes() {
        return mMovieTimes.get(mTheatreId);
    }

    private static MovieTime.Rating getRating(String rating){
        MovieTime.Rating rat;
        switch (rating) {
            case "R":
                rat = MovieTime.Rating.R;
                break;
            case "PG13":
                rat = MovieTime.Rating.PG13;
                break;
            case "PG":
                rat = MovieTime.Rating.PG;
                break;
            case "G":
                rat = MovieTime.Rating.G;
                break;
            default:
                rat = MovieTime.Rating.NC17;
                break;
        }

        return rat;
    }

    private static void populateMovieTimes(APIMovieTemplate[] mts){
        for (APIMovieTemplate mt : mts) {
            String title = mt.title;
            MovieTime.Rating ret;
            if(mt.ratings == null) ret = MovieTime.Rating.G;
            else ret = getRating(mt.ratings[0].code);

            if(mt.runTime == null) mt.runTime = "PT00H01M";
            int duration = parseRunTime(mt.runTime);

            for (APIMovieTemplate.showtimes s : mt.showtimes) {
                {
                    ArrayList<MovieTime> time;
                    if(mMovieTimes.containsKey(s.theatre.id))
                        time = mMovieTimes.get(s.theatre.id);
                    else
                        time = new ArrayList<MovieTime>();
                    time.add(new MovieTime(title, ret, getStartDate(s.dateTime), getEndDate(s.dateTime, duration), duration));
                    mMovieTimes.put(s.theatre.id, time);

                }
            }
        }
    }

    public static ArrayList<Movie> getMovies() {
        return new ArrayList<Movie>(mMovies.values());
    }

    private static void populateMovies(APIMovieTemplate[] mts){
        for (APIMovieTemplate mt : mts) {
            MovieTime.Rating rat;
            if(mt.ratings == null)  rat = MovieTime.Rating.G;
            else rat = getRating(mt.ratings[0].code);

            mMovies.put(mt.title, new Movie(mt.title, mt.genres, mt.releaseDate, mt.longDescription, mt.shortDescription, mt.directors, mt.officalUrl, mt.runTime, rat));
        }
    }

    public static void init(){
        if(mInit == true) return;
        String json = getRequest("http://data.tmsapi.com/v1.1/movies/showings?startDate=2016-04-25&zip=80401&api_key=9mdax3qa5xncse6gmc6bccyq", false).toString();

        if(json.equals("java.io.FileNotFoundException: http://data.tmsapi.com/v1.1/movies/showings?startDate=2016-04-25&zip=80401&api_key=9mdax3qa5xncse6gmc6bccyq")){
            json = "";
            try{
                InputStream inputFile = (mAM.open("backup.txt"));

                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputFile));

                String line;

                while ((line = bufferReader.readLine()) != null)   {
                    json+= line;
                }
                bufferReader.close();
                json = json.replace("\n", "");
            }catch(Exception e){
                return;
            }
        }

        Gson gson = new Gson();

        APIMovieTemplate[] mts = gson.fromJson(json, APIMovieTemplate[].class);

        Log.d(TAG, String.valueOf(mts[0]));

        populateTheatres(mts);
        populateMovies(mts);
        populateMovieTimes(mts);

        mInit = true;
    }

    public static ArrayList<Theater> getTheaters(){
        return new ArrayList<Theater>(mTheaters.values());
    }

    private static void populateTheatres(APIMovieTemplate[] mts){
        for(APIMovieTemplate mt : mts){
            for(APIMovieTemplate.showtimes s : mt.showtimes){
                String add = "";
                try {
                    add = getAddress("http://www.fandango.com/tms.asp?t=AADVD&m=158522&d=2016-04-25", s.theatre.id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mTheaters.put(s.theatre.name, new Theater(s.theatre.name, "", add, s.theatre.id));
            }
        }
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
