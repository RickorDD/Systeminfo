package de.reikodd.systeminfo;

import android.app.usage.UsageStatsManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

import de.reikodd.systeminfo.models.Currently;
import de.reikodd.systeminfo.models.Weather;
import de.reikodd.systeminfo.services.WeatherService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private static int i=0;
    private static final String TAG = "Reiko";
    private static Double tempCelsius;
    private static float tempRund;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sysinfo();

        Button clickButton = (Button) findViewById(R.id.button1);
        clickButton.setOnClickListener( new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    sysinfo();
                }
            });


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.darksky.net/forecast/47a492dd59c94d29bad67f6479bc5f41/51.05,13.73/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService weatherService = retrofit.create(WeatherService.class);
        Call<Weather> weatherData = weatherService.getWeather();

        weatherData.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {

                Currently currently = response.body().getCurrently();
                tempCelsius = (currently.getTemperature()-32)/(1.8);
                Log.i(TAG, "Temp: "+ tempCelsius +"C" );

            }

            @Override
            public void onFailure(Call<Weather> call, Throwable t) {

                Log.i(TAG, "Retrofit Error!"+t);

            }
        });


        }


    public void sysinfo()
    {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        final TextView tv1 = (TextView) findViewById(R.id.tv1);
        final TextView tv2 = (TextView) findViewById(R.id.tv2);
        final TextView tv3 = (TextView) findViewById(R.id.tv3);
        final TextView tv4 = (TextView) findViewById(R.id.tv4);
        final TextView tv5 = (TextView) findViewById(R.id.tv5);
        final TextView tv6 = (TextView) findViewById(R.id.tv6);
        final TextView tv7 = (TextView) findViewById(R.id.tv7);
        final TextView tv8 = (TextView) findViewById(R.id.tv8);
        final TextView tv9 = (TextView) findViewById(R.id.tv9);

        String TAG = "Reiko";

        tv1.setText("Device : " + Build.MODEL);

        tv2.setText("SDK : " + Build.VERSION.SDK_INT);

        tv3.setText("32_BIT_ABIS : " + Arrays.toString(Build.SUPPORTED_32_BIT_ABIS));

        tv4.setText("64_BIT_ABIS : " + Arrays.toString(Build.SUPPORTED_64_BIT_ABIS));

        float height = displayMetrics.heightPixels;
        float width = displayMetrics.widthPixels;
        float densityDpi = displayMetrics.densityDpi;
        float wdp = (width/densityDpi)*160;
        float hdp = (height/densityDpi)*160;

        tv5.setText(""+ (int) height+"px"+ " x " +(int) width+"px, "+ (int) densityDpi + "dpi, " + (int) hdp + "dp x " + (int) wdp + "dp" );


        tv6.setText("fontScale "+getResources().getConfiguration().fontScale);

        /*
        0.75 -ldpi
        1.0 - mdpi
        1.5 - hdpi
        2.0 - xhdpi
        3.0 - xxhdpi
        4.0 - xxxhdpi


        tv7.setText("Density "+displayMetrics.density);

        tv8.setText("scaleDensity "+displayMetrics.scaledDensity);

        */

        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            tv7.setText("StandbyBucket "+usageStatsManager.getAppStandbyBucket());
        }
        else
        {
            tv7.setVisibility(View.INVISIBLE);
        }

        tv8.setVisibility(View.INVISIBLE);


        i++;
        if(tempCelsius != null)
        {
            tempRund = Math.round(tempCelsius *10) / 10;
        }
        else
        {
            tempRund = 0;
        }

        tv9.setText(""  + tempRund + "C");

        double fontsizeSP = 23.5;
        double fontsize = fontsizeSP * displayMetrics.scaledDensity;
        tv9.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) fontsize);
    }
}
