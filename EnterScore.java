package com.gmail.willsims.quick_grade;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class EnterScore extends AppCompatActivity {


    // Constants
    public static String KEY = "JACK23: ";
    public static String FACTOR_ARRAY_KEY = "factor_key27";
    public static String FINAL_GRADE_KEY = "finalKEY";

    // Public Objects
    public TextView factorNameBox;
    public EditText scoreBox;
    public ListView list;
    public View v;

    // Data Structure
    ArrayList<myFactor> factorsArray = new ArrayList<myFactor>(0);

    // MyArrayListAdapter Declaration
    MyListAdapter adapter;


    // Decimal Formatter Declaration
    final DecimalFormat formatter = new DecimalFormat("#.##");
    Resources res;
    Parcel p;

    public int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_score);

        res = getResources();

        scoreBox = (EditText)findViewById(R.id.scoreBox);
        factorNameBox = (TextView)findViewById(R.id.scoreNameBox);

        // Hides keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Receives intent and stores bundle
        Bundle b = getIntent().getExtras();

        factorsArray = b.getParcelableArrayList(FACTOR_ARRAY_KEY);

        if(factorsArray != null) {
            Log.i(KEY, "FactorsArray != null");
            for (int i = 0; i < factorsArray.size(); i++) {
                Log.i(KEY, "factorsArray[i]: " + factorsArray.get(i).getFactorName());
            }
        }


        // OnActionDone Listener to remove focus and hide keyboard when user hits done
        //  after entering a score
        scoreBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(scoreBox.getApplicationWindowToken(), 0);
                    scoreBox.clearFocus();
                }
                return false;
            }
        });


        // Objects
        list = (ListView)findViewById(R.id.list2);

        if(list == null){
            list = (ListView)findViewById(R.id.list2);
        }


        if(factorsArray.get(0) != null){
            Log.i(KEY, "FactorsArray[0] != NULL");
            String scoreBoxString = factorsArray.get(0).getFactorName() + " Score: ";
            CharSequence tempHint = "-/" + formatter.format(factorsArray.get(0).getFactorWeight());

            scoreBox.setHint(tempHint);
            factorNameBox.setText(scoreBoxString);
        }

        // Initialize counter to 0
        counter = 0;

        // Sets the public adapter to the newly-generated array (display) to be displayed using the list (R.id.list2)
        adapter = new MyListAdapter(this, factorsArray, res);



        adapter.notifyDataSetChanged();

        list.setAdapter(adapter);
        if(getSupportActionBar() != null){getSupportActionBar().setDisplayHomeAsUpEnabled(true);}
    }

    // Adds a factor's score to a display ArrayList and a score array (double)
    public void addScore(View view){
        p = Parcel.obtain();
        myFactor factor = new myFactor(p);

        v = view;

        // Object Assignments
        list = (ListView)findViewById(R.id.list2);
        scoreBox = (EditText)findViewById(R.id.scoreBox);
        factorNameBox = (TextView)findViewById(R.id.scoreNameBox);

        // Prohibits user from entering more scores than factors entered
        int amountOfFactors = factorsArray.size();
        if(counter >= amountOfFactors){
            error("Error", "You cannot enter more scores that you have factors");
            return;
        }if(scoreBox.getText().toString().isEmpty() || scoreBox.getText().toString().equalsIgnoreCase("")){
            error("Error!", "Must enter a valid number as a score.");
            return;
        }


        // Parses the double to check for user input error
        double tempScore = Double.parseDouble(scoreBox.getText().toString());

        // Input error check
        if(!checkNumber(tempScore)){
            scoreBox.setText("");
            return;
        }
        factor.setFactorScore(tempScore);

        // Adds the newly-entered score into the factorScores vector(double)
        factorsArray.get(counter).setFactorScore(tempScore);

        // Set adapter to list
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);

        // Hides the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(scoreBox.getApplicationWindowToken(), 0);

        // Reset EditText where user enters their scores
        scoreBox.setText("");

        counter++;
        if(counter != factorsArray.size() && factorsArray.get(counter) != null){
            String newName = factorsArray.get(counter).getFactorName() + " Score: ";
            String newWeight = "-/" + formatter.format(factorsArray.get(counter).getFactorWeight());
            factorNameBox.setText(newName);
            scoreBox.setHint(newWeight);
        }
        p.recycle();
    }

    // Checks score that user inputs for errors
    public boolean checkNumber(Double num){
        if(num > factorsArray.get(counter).getFactorWeight()){
            error("Input Error", "Your score cannot exceed the highest possible score for that respective factor.");
            return false;
        }else if(num < 0){
            error("Input Error", "Your score cannot be less than zero.");
            return false;
        }else{
            return true;
        }

    }

    // Clears the relevant arrays and EditText fields
    public void clearScores(View view){
        counter = 0;

        v = view;

        for(int i = 0; i < factorsArray.size(); i++){
            factorsArray.get(i).clearScore();
        }

        // Resets the scoreBox and nameBox
        CharSequence tempHint = "-/" + formatter.format(factorsArray.get(0).getFactorWeight());
        String firstFactorName = factorsArray.get(0).getFactorName() + " Score: ";

        scoreBox.setHint(tempHint);
        factorNameBox.setText(firstFactorName);

        // Resets global list and adapter
        adapter = new MyListAdapter(this, factorsArray, res);
        list = (ListView)findViewById(R.id.list2);
        list.setAdapter(adapter);
    }

    public void error(String title, String msg){
        AlertDialog alertDialog = new AlertDialog.Builder(EnterScore.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void calculate(View view){
        Bundle b1 = new Bundle();

        final Intent intent = new Intent(getApplicationContext(), ShowFinalScore.class);

        double totalScore = 0;
        double totalWeight = 0;
        double finalScore = 0;

        counter = 0;

        for(int i = 0; i < factorsArray.size(); i++){
            if(!factorsArray.get(i).scoreHasBeenEntered()){
                error("Error!", "You have to enter a score for every factor!");
                return;
            }
            totalScore  += factorsArray.get(i).getFactorScore();
            totalWeight += factorsArray.get(i).getFactorWeight();
        }


        finalScore = (totalScore/totalWeight) * 100;

        b1.putDouble(FINAL_GRADE_KEY, finalScore);
        b1.putParcelableArrayList(FACTOR_ARRAY_KEY, factorsArray);

        intent.putExtras(b1);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        Log.i(KEY, "onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        list = (ListView)findViewById(R.id.list2);
        scoreBox = (EditText)findViewById(R.id.scoreBox);
        factorNameBox = (TextView)findViewById(R.id.scoreNameBox);


        if(factorsArray.get(0) != null) {

            CharSequence tempHint = "-/" + formatter.format(factorsArray.get(0).getFactorWeight());
            String nameBoxString = factorsArray.get(0).getFactorName() + " Score: ";
            ;


            Log.i(KEY, "SCORE: onResume()");
            for (int i = 0; i < factorsArray.size(); i++) {
                factorsArray.get(i).setFactorScore(-1);
            }
            adapter.notifyDataSetChanged();
            list.setAdapter(adapter);

            factorNameBox.setText(nameBoxString);
            scoreBox.setHint(tempHint);
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(KEY, "SCORE:onPause()");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        Log.i(KEY, "SCORE:onRestart()");
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        Log.i(KEY, "SCORE:BACK PRESSED ENTER SCORE");
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        Log.i(KEY, "SCORE:DESTROYED");
        super.onDestroy();
    }

    @Override
    public void onDetachedFromWindow() {
        Log.i(KEY, "SCORE:Detached from Window");
        super.onDetachedFromWindow();
    }
}
