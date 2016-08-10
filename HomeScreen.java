package com.gmail.willsims.quick_grade;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;


public class HomeScreen extends AppCompatActivity {


    public static String KEY = "JACK23: ";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);



        /******************************************************************************/

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Log.i(KEY, "Resolution (W x H): " + metrics.widthPixels + " x " + metrics.heightPixels);

        int densityDpi = (int)(metrics.density * 160f);
        String dpi;

        if(densityDpi < 160){
            dpi = "Ldpi";
        }else if(densityDpi < 240){
            dpi = "Mdpi";
        }else if(densityDpi < 320){
            dpi = "Hdpi";
        }else if(densityDpi < 480) {
            dpi = "(X)Hdpi";
        }else if(densityDpi < 640){
            dpi = "(XX)Hdpi";
        }else{
            dpi = "(XXX)Hdpi";
        }


        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        String toastMsg;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                toastMsg = "Extra Large Screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                toastMsg = "Large screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                toastMsg = "Normal screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                toastMsg = "Small screen";
                break;
            default:
                toastMsg = "Screen size is neither large, normal or small";
        }
        toastMsg += "\nDensityDPI: " + dpi + "(" + densityDpi + ")";
        Log.i(KEY, toastMsg);

        /******************************************************************************/




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);

            }
        });
    }



}
