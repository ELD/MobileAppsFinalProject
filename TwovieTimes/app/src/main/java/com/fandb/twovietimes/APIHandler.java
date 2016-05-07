package com.fandb.twovietimes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.NetworkOnMainThreadException;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Steve on 4/22/2016.
 */

enum APIType {
    GetTheaters,
};

public class APIHandler {
    public static final String API_KEY = "9mdax3qa5xncse6gmc6bccyq";
    public static final String TAG = "APIHANDLER";

    public static boolean mInit = false;
    public static Date mDate = new Date();
    public static Context mContext = null;
    public static Activity mActivity = null;
    public static String mTheatreId;
    public static String mZip;
    public static int mRadius = 5;
    public static int mDays = 1;
    public static int mTimeTolerance = 30;

    //Title -> Movie
    public static HashMap<String, Movie> mMovies = new HashMap<String, Movie>();
    //TheatreId -> MovieTime[]
    public static HashMap<String, ArrayList<MovieTime>> mMovieTimes = new HashMap<String, ArrayList<MovieTime>>();
    //TheatreId -> Theater
    public static HashMap<String, Theater> mTheaters = new HashMap<String, Theater>();

    public static String[] getGenresByMovieTime(MovieTime mt){

        String[] ret = null;

        Iterator it = mMovies.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Movie m = (Movie) pair.getValue();

            Log.d(TAG, m.getTitle() + ", " + mt.getTitle());
            if(m.getTitle().equals(mt.getTitle())){
                ret = m.getGenres();
                break;
            }

        }
        return ret;
    }

    public static ArrayList<MoviePair> getMoviePairsGenres(String gen1, String gen2){
        ArrayList<MoviePair> mp = new ArrayList<MoviePair>();

        ArrayList<MovieTime> mt = new ArrayList<MovieTime>();

        Iterator it = mMovieTimes.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();

            if(!pair.getKey().equals(mTheatreId)) continue;

            ArrayList<MovieTime> m = (ArrayList<MovieTime>) pair.getValue();
            for(MovieTime mot : m){
                ArrayList<String> st = new ArrayList(Arrays.asList(getGenresByMovieTime(mot)));
                if(!st.contains(gen2) && !st.contains(gen1)) continue;
                if(!mt.contains(mot)) mt.add(mot);
            }
        }

        ArrayList<MovieTime> checked = new ArrayList<MovieTime>();
        for(MovieTime m : mt){
            for(MovieTime a : mt){
                if(m.getTitle().equals(a.getTitle()) || checked.contains(a)) continue;
                float beg = Math.abs(a.getStartTime().getTime() - m.getStartTime().getTime());
                float end = Math.abs(a.getEndTime().getTime() - m.getEndTime().getTime());
                //if(beg > mTimeTolerance || end > mTimeTolerance) continue;
                MoviePair temp = new MoviePair(getMovieByTime(m), getMovieByTime(a), m.getmDuration(), a.getmDuration(), m.getStartTime(), a.getStartTime());
                if(mp.contains(temp)){
                    continue;
                }
                mp.add(temp);
            }
        }

        return mp;
    }

    public static ArrayList<MoviePair> getMoviePairsGenre(String mov1, String gen1){
        ArrayList<MoviePair> mp = new ArrayList<MoviePair>();

        ArrayList<MovieTime> mt = new ArrayList<MovieTime>();

        Iterator it = mMovieTimes.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry) it.next();

            if(!pair.getKey().equals(mTheatreId)) continue;

            ArrayList<MovieTime> m = (ArrayList<MovieTime>) pair.getValue();
            for(MovieTime mot : m){
                ArrayList<String> st = new ArrayList(Arrays.asList(getGenresByMovieTime(mot)));
                if(mot.getTitle() != mov1 && !st.contains(gen1)) continue;
                if(!mt.contains(mot)) mt.add(mot);
            }
        }

        ArrayList<MovieTime> checked = new ArrayList<MovieTime>();
        for(MovieTime m : mt){
            for(MovieTime a : mt){
                if(m.getTitle().equals(a.getTitle()) || checked.contains(a)) continue;
                float beg = Math.abs(a.getStartTime().getTime() - m.getStartTime().getTime());
                float end = Math.abs(a.getEndTime().getTime() - m.getEndTime().getTime());
                //if(beg > mTimeTolerance || end > mTimeTolerance) continue;
                MoviePair temp = new MoviePair(getMovieByTime(m), getMovieByTime(a), m.getmDuration(), a.getmDuration(), m.getStartTime(), a.getStartTime());
                if(mp.contains(temp)){
                    continue;
                }
                mp.add(temp);
            }
        }

        return mp;
    }

    public static ArrayList<MoviePair> getMoviePairs(String mov1, String mov2){
        ArrayList<MoviePair> mp = new ArrayList<MoviePair>();

        ArrayList<MovieTime> mt = new ArrayList<MovieTime>();

        Log.d(TAG, "Member: " + String.valueOf(mMovieTimes.size()));

        Iterator it = mMovieTimes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            if(!pair.getKey().equals(mTheatreId)) continue;

            ArrayList<MovieTime> m = (ArrayList<MovieTime>) pair.getValue();

            for(MovieTime mot : m){
                if(mot.getTitle() != mov1 && mot.getTitle() != mov2) continue;
                if(!mt.contains(mot)) mt.add(mot);

            }
        }

        Log.d(TAG, String.valueOf(mt.size()));


        ArrayList<MovieTime> checked = new ArrayList<MovieTime>();
        for(MovieTime m : mt){
            for(MovieTime a : mt){
                if(m.getTitle().equals(a.getTitle()) || checked.contains(a)) continue;
                float beg = Math.abs(a.getStartTime().getTime() - m.getStartTime().getTime());
                float end = Math.abs(a.getEndTime().getTime() - m.getEndTime().getTime());
                //if(beg > mTimeTolerance || end > mTimeTolerance) continue;
                MoviePair temp = new MoviePair(getMovieByTime(m), getMovieByTime(a), m.getmDuration(), a.getmDuration(), m.getStartTime(), a.getStartTime());
                if(mp.contains(temp)){
                    continue;
                }
                mp.add(temp);
            }
        }

        //Log.d(TAG, String.valueOf(checked));

        //Log.d(TAG, String.valueOf(mp));

        return mp;
    }

    public static Movie getMovieByTime(MovieTime mt){
        Iterator it = mMovies.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();


            Movie mo = (Movie) pair.getValue();

            if(mo.getTitle() == mt.getTitle()){
                //Log.d(TAG, String.valueOf(mo));
                return mo;
            }

        }
        return null;
    }

    public static String getAddress(String link, String theatre) throws IOException {
        if (mTheaters.containsKey(theatre)) {
            if (mTheaters.get(theatre).getAddress() != "") {
                return mTheaters.get(theatre).getAddress();
            }
        } else return "";

        Document doc = Jsoup.connect(link)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:45.0) Gecko/20100101 Firefox/45.0")
                .maxBodySize(0)
                .timeout(1000000000)
                .get();

        Element ele = doc.getElementById("maplink");
        if (ele == null) {
            ele = doc.select("h2").first().select("a").first();
            if (ele != null) return getAddress(ele.attr("href"), theatre);
        } else {
            Theater t;
            t = mTheaters.get(theatre);
            t.setAddress(ele.text());
            mTheaters.put(theatre, t);
            return ele.text();
        }

        return "";
    }

    public static Integer parseRunTime(String runtime) {
        if(runtime == null) return 0;
        runtime = runtime.replace("PT", "");
        runtime = runtime.replace("M", "");
        String[] rt = runtime.split("H");

        return Integer.parseInt(rt[0]) * 60 + Integer.parseInt(rt[1]);
    }

    public static String parseRunTimeStr(String runtime){
        return String.valueOf(parseRunTime(runtime));
    }

    public static Date getStartDate(String date) {
        String[] pieces = date.split("T");

        DateFormat form = new SimpleDateFormat("yyyy-MM-ddHH:mm", Locale.ENGLISH);
        try {
            return form.parse(pieces[0] + pieces[1]);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return null;
    }

    public static Date getEndDate(String date, int duration) {
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

    private static MovieTime.Rating getRating(String rating) {
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

    private static void populateMovieTimes(APIMovieTemplate[] mts) {
        for (APIMovieTemplate mt : mts) {
            String title = mt.title;
            MovieTime.Rating ret;
            if (mt.ratings == null) ret = MovieTime.Rating.G;
            else ret = getRating(mt.ratings[0].code);

            if (mt.runTime == null) mt.runTime = "PT00H01M";
            int duration = parseRunTime(mt.runTime);

            for (APIMovieTemplate.showtimes s : mt.showtimes) {
                {
                    ArrayList<MovieTime> time;
                    if (mMovieTimes.containsKey(s.theatre.id))
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

    private static void populateMovies(APIMovieTemplate[] mts) {
        for (APIMovieTemplate mt : mts) {
            MovieTime.Rating rat;
            if (mt.ratings == null) rat = MovieTime.Rating.G;
            else rat = getRating(mt.ratings[0].code);

            mMovies.put(mt.title, new Movie(mt.title, mt.genres, mt.releaseDate, mt.longDescription, mt.shortDescription, mt.directors, mt.officalUrl, mt.runTime, rat));
        }
    }

    public static ArrayList<String> getGenres() {
        ArrayList<String> ret = new ArrayList<String>();

        Iterator it = mMovies.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            Movie m = (Movie) pair.getValue();

            if (m.getGenres() == null) continue;
            for (String genre : m.getGenres()) {
                if (ret.contains(genre)) continue;
                else ret.add(genre);
            }

        }
        Collections.sort(ret);
        return ret;
    }

    public static ArrayList<String> getMoviesByGenre(String genre) {
        ArrayList<String> ret = new ArrayList<String>();

        Iterator it = mMovies.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            Movie m = (Movie) pair.getValue();

            if (Arrays.asList(m.getGenres()).contains(genre)) {
                if (ret.contains((String) pair.getKey())) continue;
                else ret.add((String) pair.getKey());
            }
        }

        Collections.sort(ret);
        return ret;
    }

    public static ArrayList<String> getMovieNames() {
        ArrayList<String> ret = new ArrayList<String>();

        Iterator it = mMovies.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            if (ret.contains((String) pair.getKey())) continue;
            else ret.add((String) pair.getKey());

        }
        Collections.sort(ret);
        return ret;
    }

    private static String getDate(Date d, boolean output) {
        DateFormat df;
        if (output) df = new SimpleDateFormat("yyyyMMdd");
        else df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(d);
    }

    private static String readFile() {
        String json = "";
        try {
            BufferedReader inputFile = new BufferedReader(new InputStreamReader(mContext.openFileInput(getDate(mDate, true) + mZip + ".txt")));

            String line;

            while ((line = inputFile.readLine()) != null) {
                json += line;
            }
            inputFile.close();
            json = json.replace("\n", "");
        } catch (Exception e) {
            return null;
        }

        return json;
    }

    private static void writeFile(String output) {
        try {
            mContext.deleteFile(getDate(mDate, true) + ".txt");
            FileOutputStream fos = mContext.openFileOutput(getDate(mDate, true) + mZip + ".txt", Context.MODE_PRIVATE);
            fos.write(output.getBytes());
            fos.close();
        } catch (Exception e) {
        }
    }

    public static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private static String getZipCodeFromLocation(Location location) {
        Address addr = getAddressFromLocation(location);
        if (addr.getPostalCode() == null) {
            Log.d(TAG, "Postal code is null, using 80401");
        }
        return addr.getPostalCode() == null ? "80401" : addr.getPostalCode();
    }

    private static Address getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        Address address = new Address(Locale.getDefault());
        try {
            List<Address> addr = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Log.d(TAG, String.valueOf(addr));
            if (addr.size() > 0) {
                address = addr.get(0);
            } else address = addr.get(0);
        } catch (Exception e) {
            Log.d(TAG, String.valueOf(e));
            Log.d(TAG, "Could not get location from long and lat");
        }
        return address;
    }
    private static void getZipCode() {

        try {
            LocationManager mLocationManager;
            mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            List<String> providers = mLocationManager.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                    Log.d(TAG, "Waiting for permission");
                    mZip = "";
                    return;
                }
                Location l = mLocationManager.getLastKnownLocation(provider);
                if (l == null) {
                    Log.d(TAG, "Null");
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
            if(bestLocation == null){
                Log.d(TAG, "location is null");
                mZip = "80401";
                return;
            }
            mZip = getZipCodeFromLocation(bestLocation);
        }catch(Exception e){
            Log.d(TAG, "Could not get location manager");
            mZip = "80401";
        }
    }

    public static void init(){
        if(mInit == true) return;
        if(mZip == null || mZip == "") getZipCode();
        //Waiting for permission
        if(mZip == "") return;
        String request = "http://data.tmsapi.com/v1.1/movies/showings?startDate="+getDate(mDate, false)+"&zip="+mZip+"&api_key="+API_KEY + "&numDays="+mDays+"&radius="+mRadius;
        Log.d(TAG, request);

        if(mDate == null) mDate = new Date();

        String json = "";
        //if(!mContext.getFileStreamPath(getDate(mDate, true) + mZip + ".txt").isFile()){
        if(true){
            Log.d(TAG, "No cache file found");
            json = getRequest(request, false).toString();
        }

        else{
            Log.d(TAG, "Reading from cache...");
            json = readFile();
        }


        if(json.equals("java.io.FileNotFoundException: " + request) || !isOnline()) {
            Log.d(TAG, "Loading from cache");
            json = readFile();
        }
        else if(json == "" || json == null) return;
        else writeFile(json);

        Gson gson = new Gson();


        APIMovieTemplate[] mts = gson.fromJson(json, APIMovieTemplate[].class);

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
                    add = getAddress("http://www.fandango.com/tms.asp?t=AADVD&m=158522&d=2016-04-26", s.theatre.id);
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
