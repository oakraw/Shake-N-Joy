package com.oakraw.shakenjoy;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.oakraw.shakenjoy.gif.GifAnimationDrawable;
import com.oakraw.shakenjoy.location.LocationProvider;

import java.io.IOException;


public class MyActivity extends Activity {

    private ActionBar mActionBar;
    private View mCustomView;
    private ImageView shakeIC;
    private ToggleButton foodBtn;
    private ToggleButton nightLifeBtn;
    private ToggleButton coffeeBtn;
    private ToggleButton shoppingBtn;
    private ToggleButton dessertBtn;
    private ToggleButton sightsBtn;
    private ShakeDetector sd;
    //private SensorManager sensorManager;
    private String categorySelected;
    private ImageView header;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private View shakeArea;
    private TextView areaSize;
    private boolean isShaked = false;
    private View filter;
    private int areaRadius;
    private LocationProvider location;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        //launch Prelude
        SharedPreferences prefs = this.getSharedPreferences(
                "shake_n_joy", Context.MODE_PRIVATE);

        /*first time run app checking*/
        boolean isFirstTime = prefs.getBoolean("isFirstTime",true);
        if(isFirstTime) {
            /*if first time launch tutorial*/
            startActivity(new Intent(this, PreludeActivity.class));
            finish();
        }

        filter = (View) findViewById(R.id.filter);


        /*Set Shake Detector*/
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        shakeArea = (View) findViewById(R.id.shakeArea);
        areaSize = (TextView) findViewById(R.id.areaSize);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                shakeArea.setVisibility(View.VISIBLE);
                areaSize.setText(getArea(count));

                /*expanding the area of red circle when shake detected*/
                count = (count*10)+150;
                float wid = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, count, getResources().getDisplayMetrics());
                Log.d("shake", String.valueOf(wid));
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) shakeArea.getLayoutParams();
                params.width = Math.round(wid);
                params.height = Math.round(wid);
                shakeArea.setLayoutParams(params);
                isShaked = true;

            }

            @Override
            public void onStopShake() {
                /*when user stop shaking for 2 sec then this method will called*/
                Log.d("shake", "STOP");
                shakeArea.setVisibility(View.INVISIBLE);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) shakeArea.getLayoutParams();
                float wid = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                params.width = Math.round(wid);
                params.height = Math.round(wid);
                shakeArea.setLayoutParams(params);
                if(isShaked) {
                    newActivity();
                    isShaked = false;
                }
            }
        });

        mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        header = (ImageView)mCustomView.findViewById(R.id.header);

        /*create gif image*/
        GifAnimationDrawable gif = null;
        try {
            gif = new GifAnimationDrawable(getResources().openRawResource(R.raw.icon_shake),true);
        } catch (IOException e) {
            e.printStackTrace();
        }



        shakeIC = (ImageView)findViewById(R.id.shakeIC);
        shakeIC.setImageDrawable(gif);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        foodBtn = (ToggleButton)findViewById(R.id.foodBtn);
        nightLifeBtn = (ToggleButton)findViewById(R.id.nightLifeBtn);
        coffeeBtn = (ToggleButton)findViewById(R.id.coffeeBtn);
        dessertBtn = (ToggleButton)findViewById(R.id.dessertBtn);
        shoppingBtn = (ToggleButton)findViewById(R.id.shoppingBtn);
        sightsBtn = (ToggleButton)findViewById(R.id.sightsBtn);


        /*assign listener to all button*/
        foodBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                resetAllBtn();
                if(isChecked){
                    buttonView.setChecked(isChecked);
                    categorySelected = "Food";
                    showGif();
                }
                else{
                    hideGif();
                }
            }
        });

        nightLifeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                resetAllBtn();
                if(isChecked){
                    buttonView.setChecked(isChecked);
                    categorySelected = "NightLife";
                    showGif();
                }
                else{
                    hideGif();
                }
            }
        });
        coffeeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                resetAllBtn();
                if(isChecked){
                    buttonView.setChecked(isChecked);
                    categorySelected = "Coffee";
                    showGif();
                }
                else{
                    hideGif();

                }
            }
        });

        dessertBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                resetAllBtn();
                if(isChecked){
                    buttonView.setChecked(isChecked);
                    categorySelected = "Dessert";
                    showGif();
                }
                else{
                    hideGif();
                }
            }
        });

        shoppingBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                resetAllBtn();
                if(isChecked){
                    buttonView.setChecked(isChecked);
                    categorySelected = "Shopping";
                    showGif();
                }
                else{
                    hideGif();

                }
            }
        });

        sightsBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                resetAllBtn();
                if(isChecked){
                    buttonView.setChecked(isChecked);
                    categorySelected = "Sights";
                    showGif();
                }
                else{
                    hideGif();
                }
            }
        });




    }

    private String getArea(int count) {
        String text;

        if(count<10){
            text = count*100 + " m";
        }
        else{
            text = (float)count/10 + " km";
        }

        areaRadius = count*100;
        return text;
    }

    @Override
    protected void onResume() {
        super.onResume();
        final LinearLayout noInternet = (LinearLayout)findViewById(R.id.no_internet);
        //check internet connetion and then check location
        if(hasConnection()){
            noInternet.setVisibility(View.GONE);
            setLocation();
        }
        else{
            noInternet.setVisibility(View.VISIBLE);
            noInternet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hasConnection()) {
                        noInternet.setVisibility(View.GONE);
                        setLocation();
                    }
                }
            });
        }
    }

    /*check internet connection*/
    public boolean hasConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;
    }

    /*check GPS location enable state then show dialog*/
    private void setLocation(){
        if(location == null) {
            location = new LocationProvider(this);
            location.setOnGPSReady(new LocationProvider.OnGPSReadyListener() {
                @Override
                public void onGPSReady() {
                    progressBar.setVisibility(View.GONE);
                }
            });

            // check for location service
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //Toast.makeText(this, "gps enabled", Toast.LENGTH_SHORT).show();
                location.startLocation();
                progressBar.setVisibility(View.VISIBLE);
                Toast.makeText(this, getResources().getString(R.string.update_location), Toast.LENGTH_LONG).show();



            } else {
                Toast.makeText(this, getResources().getString(R.string.gps_disabled), Toast.LENGTH_SHORT).show();
                LocationAlertDialog();
            }
        }
        else{
            location.startLocation();
        }


    }


    /*show dialog to enable GPS*/
    public void LocationAlertDialog() {

        AlertDialog.Builder d;
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            d = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);

        }else {
            d = new AlertDialog.Builder(this);
        }
        d.setTitle(getResources().getString(R.string.gps_dialog_header));
        d.setMessage(getResources().getString(R.string.gps_dialog_content));

        d.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        d.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(i, 0);
            }
        });

        d.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            //location.startLocation();
        }else {
            Toast.makeText(this,"GPS service still disabled",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
        if(location != null)
            location.stopLocation();
    }

    /*uncheck all toggle buttons*/
    private void resetAllBtn(){
        foodBtn.setChecked(false);
        nightLifeBtn.setChecked(false);
        coffeeBtn.setChecked(false);
        shoppingBtn.setChecked(false);
        dessertBtn.setChecked(false);
        sightsBtn.setChecked(false);
    }

    /*show shake scene*/
    private void showGif(){
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideGif();
                resetAllBtn();
            }
        });
        filter.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn).duration(500).playOn(filter);
        areaSize.setVisibility(View.VISIBLE);
        header.setImageResource(R.drawable.logo2);
        shakeIC.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.BounceIn).duration(500).playOn(shakeIC);
        //sd.start(sensorManager);
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,    SensorManager.SENSOR_DELAY_UI);
    }

    /*hide shake scene*/
    private void hideGif(){
        YoYo.with(Techniques.FadeOut).duration(500).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                filter.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).playOn(filter);

        shakeArea.setVisibility(View.INVISIBLE);
        isShaked = false;
        areaSize.setVisibility(View.INVISIBLE);
        areaSize.setText("0 m");
        header.setImageResource(R.drawable.logo);
        YoYo.with(Techniques.ZoomOut).duration(500).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                shakeIC.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).playOn(shakeIC);
        //sd.stop();
        mSensorManager.unregisterListener(mShakeDetector);
    }

    /*launch result page*/
    public void newActivity() {
        //Toast.makeText(this,catagorySelected,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,DetailActivity.class);
        hideGif();
        resetAllBtn();
        /*send category location and radius to search*/
        intent.putExtra("category", categorySelected);
        intent.putExtra("lat", location.getLatitude());
        intent.putExtra("lng", location.getLongitude());
        intent.putExtra("arearadius",String.valueOf(areaRadius));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(filter.getVisibility() == View.VISIBLE){
            hideGif();
            resetAllBtn();
        }else {
            super.onBackPressed();
        }
    }
}
