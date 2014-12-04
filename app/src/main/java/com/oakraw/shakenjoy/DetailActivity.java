package com.oakraw.shakenjoy;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable;
import com.melnykov.fab.FloatingActionButton;
import com.oakraw.shakenjoy.adapter.BannerAdapter;
import com.oakraw.shakenjoy.json.ShopExplore;
import com.oakraw.shakenjoy.json.ShopInfo;
import com.oakraw.shakenjoy.json.ShopList;
import com.squareup.seismic.ShakeDetector;
import com.viewpagerindicator.CirclePageIndicator;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

public class DetailActivity extends Activity implements ShakeDetector.Listener {

    private String category;
    private String areaRadius;
    private TextView shopName;
    private TextView shopLocation;
    private TextView rating;
    private TextView title;
    private TextView readMore;
    private FloatingActionButton fab;
    private LinearLayout bannerIndicator;
    private RelativeLayout ui;
    private ProgressBar progressBar;
    private ShakeDetector sd;
    private SensorManager sensorManager;
    private ShopList resultDessert;
    private ShopExplore resultAll;
    private ViewPager viewPager = null;
    private Context context;
    private int sCode;
    private FrameLayout rateCircle;
    private TextView totalScore;
    private double lat = 0.0;
    private double lng = 0.0;
    private View circle;
    private View circleAnim;
    private int tempY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        final Activity mActivity = this;
        //Action Bar
        final ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar2, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        title = (TextView)mCustomView.findViewById(R.id.title);
        FrameLayout backBtn = (FrameLayout)mCustomView.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(mActivity);
                //finish();
            }
        });

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sd = new ShakeDetector(this);

        setContentView(R.layout.activity_detail);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);//animate transition screen

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            category = null;
        } else {
            category = extras.getString("category");
            areaRadius = extras.getString("arearadius");
            lat = extras.getDouble("lat");
            lng = extras.getDouble("lng");
        }

        String URL;
        /*create url for get all places*/
        if(category.equals("Dessert")) {
            URL = "https://api.foursquare.com/v2/venues/search?ll="+lat+","+lng +
                    "&categoryId=" + Data.myMap.get(category) +
                    "&radius=" +areaRadius+
                    "&limit=50" +
                    "&client_id=" + getResources().getString(R.string.CLIENT_ID) +
                    "&client_secret=" + getResources().getString(R.string.CLIENT_SECRET) +
                    "&v=20140806";
        }
        else{
            URL = "https://api.foursquare.com/v2/venues/explore?ll="+lat+","+lng +
                    "&section="+Data.myMap.get(category)+
                    "&limit=50"+
                    "&radius=" +areaRadius+
                    "&client_id=" + getResources().getString(R.string.CLIENT_ID) +
                    "&client_secret=" + getResources().getString(R.string.CLIENT_SECRET) +
                    "&v=20140806";
        }

        Log.d("myTAG",URL);

        new SimpleTask().execute(URL);

        shopName = (TextView)findViewById(R.id.shopName);
        shopLocation = (TextView)findViewById(R.id.shopLocation);
        rating = (TextView)findViewById(R.id.rating);
        readMore = (TextView) findViewById(R.id.readMore);
        totalScore = (TextView) findViewById(R.id.totalScore);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        ui = (RelativeLayout) findViewById(R.id.ui);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rateCircle = (FrameLayout) findViewById(R.id.rateCircle);
        circle = (View) findViewById(R.id.circle);

        /*initiate progressbar*/
        progressBar.setIndeterminateDrawable(new FoldingCirclesDrawable.Builder(this).colors(getResources().getIntArray(R.array.colors)).build());

        ui.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    /*listener for shake again*/
    @Override
    public void hearShake() {
        sd.stop();
        if(viewPager != null) {
            viewPager.removeAllViews();
        }
        ui.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        title.setText("Loading..");
        if(category.equals("Dessert")){
            getShopInfoDessert();
        }
        else{
            getShopInfoAll();
        }
    }

    /*Thread for request url*/
    private class SimpleTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // Create Show ProgressBar
        }

        protected String doInBackground(String... urls)   {
            String result = "";
            try {

                HttpGet httpGet = new HttpGet(urls[0]);
                HttpClient client = new DefaultHttpClient();

                HttpResponse response = client.execute(httpGet);

                int statusCode = response.getStatusLine().getStatusCode();
                sCode = statusCode;
                if (statusCode == 200) {
                    InputStream inputStream = response.getEntity().getContent();
                    BufferedReader reader = new BufferedReader
                            (new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result += line;
                    }
                }
                else {
                    Log.d("statusCode", statusCode + "");
                }

            } catch (ClientProtocolException e) {

            } catch (IOException e) {

            }
            return result;
        }

        protected void onPostExecute(String jsonString)  {
            // Dismiss ProgressBar
            Log.d("myTaG", jsonString.toString());
            showData(jsonString);
            //
        }
    }


    private void showData(String jsonString) {

        if(sCode != 200){
            Toast.makeText(context,getResources().getString(R.string.problem_network),Toast.LENGTH_LONG).show();
            finish();
        }

        try {

            if(category.equals("Dessert")) {
                JSONObject json = (JSONObject) new JSONTokener(jsonString).nextValue();
                JSONObject json2 = json.getJSONObject("response");

                try {
                    json2.getString("venues");
                    Gson gson = new Gson();
                    resultDessert = gson.fromJson(json2.toString(), ShopList.class);

                    try {
                        int ranNum = randomShop(resultDessert.venues.size());
                        String idVenue = resultDessert.venues.get(ranNum).id;

                        /*create url for the chosen place to get detail of this place*/
                        String URLVenue = "https://api.foursquare.com/v2/venues/" + idVenue +
                                "?client_id=" + getResources().getString(R.string.CLIENT_ID) +
                                "&client_secret=" + getResources().getString(R.string.CLIENT_SECRET) +
                                "&v=20140806";

                        Log.d("myTag", URLVenue);
                        resultDessert.venues.remove(randomShop(ranNum));
                        new SimpleTask().execute(URLVenue);
                    } catch (Exception e) {
                        dontHaveInfo();
                    }

                } catch (JSONException e1) {
                    Gson gson = new Gson();
                    ShopInfo result = gson.fromJson(json2.toString(), ShopInfo.class);
                    attachData(result);
                    Log.d("myTaG", result.toString());

                }
            }
            else{
                JSONObject json = (JSONObject) new JSONTokener(jsonString).nextValue();
                JSONObject json2 = json.getJSONObject("response");

                try {
                    json2.getString("groups");
                    Gson gson = new Gson();
                    resultAll = gson.fromJson(json2.toString(), ShopExplore.class);

                    for (ShopExplore.Groups.Items s : resultAll.groups.get(0).items) {
                        //Log.d("myTaG", s.toString()+" "+s.location.toString());
                    }

                    try {
                        int ranNum = randomShop(resultAll.groups.get(0).items.size());
                        String idVenue = resultAll.groups.get(0).items.get(ranNum).venue.id;
                        String URLVenue = "https://api.foursquare.com/v2/venues/" + idVenue +
                                "?client_id=" + getResources().getString(R.string.CLIENT_ID) +
                                "&client_secret=" + getResources().getString(R.string.CLIENT_SECRET) +
                                "&v=20140806";
                        resultAll.groups.get(0).items.remove(ranNum);
                        Log.d("myTag", URLVenue);
                        new SimpleTask().execute(URLVenue);
                    } catch (Exception e) {
                        dontHaveInfo();
                    }

                } catch (JSONException e1) {
                    Gson gson = new Gson();
                    ShopInfo result = gson.fromJson(json2.toString(), ShopInfo.class);
                    attachData(result);
                    Log.d("myTaG", result.toString());

                }
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*get detail of a new dessert shop when user shake again*/
    private void getShopInfoDessert(){
        try {
            int ranNum = randomShop(resultDessert.venues.size());
            String idVenue = resultDessert.venues.get(ranNum).id;
            String URLVenue = "https://api.foursquare.com/v2/venues/" + idVenue +
                    "?client_id=" + getResources().getString(R.string.CLIENT_ID) +
                    "&client_secret=" + getResources().getString(R.string.CLIENT_SECRET) +
                    "&v=20140806";

            Log.d("myTag", URLVenue);
            resultDessert.venues.remove(randomShop(ranNum));
            new SimpleTask().execute(URLVenue);
        } catch (Exception e) {
            dontHaveInfo();
        }
    }

    /*get detail of a new place when user shake again*/
    private void getShopInfoAll(){
        try {
            int ranNum = randomShop(resultAll.groups.get(0).items.size());
            String idVenue = resultAll.groups.get(0).items.get(ranNum).venue.id;
            String URLVenue = "https://api.foursquare.com/v2/venues/" + idVenue +
                    "?client_id=" + getResources().getString(R.string.CLIENT_ID) +
                    "&client_secret=" + getResources().getString(R.string.CLIENT_SECRET) +
                    "&v=20140806";
            resultAll.groups.get(0).items.remove(ranNum);
            Log.d("myTag", URLVenue);
            new SimpleTask().execute(URLVenue);
        } catch (Exception e) {
            dontHaveInfo();
        }
    }

    private void dontHaveInfo() {
        Toast.makeText(this,getResources().getString(R.string.not_found),Toast.LENGTH_LONG).show();
        finish();
    }

    /*randomly picking a shop*/
    private int randomShop(int size) {
        Random rand = new Random();
        return rand.nextInt(size-1);
    }

    /*attach data into view*/
    private void attachData(final ShopInfo result) {
        shopName.setText(result.venue.name);
        shopLocation.setText(result.venue.location.getFormattedAddress());

        if(result.venue.rating == 0.0)
            rating.setText(String.valueOf(0));
        else
            rating.setText(String.valueOf(result.venue.rating));

        title.setText(result.venue.getCatagories());
        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(result.venue.canonicalUrl));
                startActivity(viewIntent);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://maps.google.com/?q="+result.venue.location.toString();
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(url));
                startActivity(viewIntent);
            }
        });
        setupViewPager(result);
        ui.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        /*register sensor manager*/
        sd.start(sensorManager);

    }

    /*attach image to viewpager*/
    private void setupViewPager(ShopInfo result){

        if(result.venue.photos.groups.size() != 0) {
            List<ShopInfo.Venue.Photos.Groups.Items> bannerImg = result.venue.photos.groups.get(0).items;
            viewPager = (ViewPager) findViewById(R.id.viewPager);
            final BannerAdapter adapter = new BannerAdapter(this, bannerImg);
            viewPager.setAdapter(adapter);
            CirclePageIndicator titleIndicator = (CirclePageIndicator)findViewById(R.id.titles);
            titleIndicator.setViewPager(viewPager);
        }
    }
}
