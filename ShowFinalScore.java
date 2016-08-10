package com.gmail.willsims.quick_grade;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;



import java.text.DecimalFormat;
import java.util.ArrayList;

public class ShowFinalScore extends AppCompatActivity {

    public static String FINAL_GRADE_KEY = "finalKEY";
    public static String FACTOR_ARRAY_KEY = "factor_key27";


    final DecimalFormat formatter = new DecimalFormat("#.##");

    public ArrayList<myFactor> factorsArray;


    // Arrays

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_final_score);
;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.FinalScoreHomeButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),HomeScreen.class);
                startActivity(i);

            }
        });

        TextView finalScoreBox = (TextView)findViewById(R.id.finalScoreTextView) ;

        // Receives intent and stores bundle
        Bundle b = getIntent().getExtras();

        // Gets final score from previous activity for display
        double finalScore = b.getDouble(FINAL_GRADE_KEY);
        factorsArray = b.getParcelableArrayList(FACTOR_ARRAY_KEY);

        String finalScoreDisplay = formatter.format(finalScore) + "%";


        finalScoreBox.setText(finalScoreDisplay);
    }


}
