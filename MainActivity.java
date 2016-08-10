package com.gmail.willsims.quick_grade;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Parcel;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Constants
    public static String KEY = "JACK23: ";
    public static String FACTOR_ARRAY_KEY = "factor_key27";

    final DecimalFormat formatter = new DecimalFormat("#.##");
    InputMethodManager imm;
    Resources res;

    // Data Structure
    ArrayList<myFactor> factorArrayList = new ArrayList<myFactor>(0);

    // Adapter
    MyListAdapter customAdapter;
    ArrayAdapter<myFactor> resetAdapter;
    ListView list;




    // Public
    public String mFactorName = "";
    public double mFactorWeight = 0;
    public String possibleErrors = "";
    public int numFactors;
    public EditText myWeightBox;
    public EditText myNameBox;

    public Activity myActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar bar = getSupportActionBar();
        if(bar != null){bar.hide();}

        res = getResources();
        myActivity = this;
        list = (ListView)findViewById(R.id.list1);
        customAdapter = new MyListAdapter(myActivity, factorArrayList, res);



        myWeightBox = (EditText)findViewById(R.id.percentBox);
        myWeightBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(myWeightBox.getApplicationWindowToken(), 0);
                    myWeightBox.clearFocus();
                }
                return false;
            }
        });




    }

    // Adds a factor and its percentage to respective arrays, then sets adapter for list view
    public void add(View view){
        numFactors = factorArrayList.size();  // Get size(i) to store new factor in array[i]

        // Creates keyboard object so we can hide it when user adds factor
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);



        // Object Declarations
        myWeightBox = (EditText) findViewById(R.id.percentBox);
        myNameBox = (EditText) findViewById(R.id.nameBox);

        // Creates Parcel for passing ArrayList<myFactors> to next activity
        Parcel p = Parcel.obtain();

        // Creates new myFactors
        myFactor factor = new myFactor(p);

        // Recycles the parcel after it is passed to myFactors constructor
        p.recycle();

        // Sets the current context
        myActivity = this;
        list = (ListView)findViewById(R.id.list1);

        customAdapter = new MyListAdapter(this, factorArrayList, res);

        // Ensures that user has text in the name and weight fields before proceeding to data storage
        if(myNameBox.getText().length() == 0 && myWeightBox.getText().length() == 0){
            error("Need a Name & Weight!", "You must enter a name AND weight before adding the factor to your grade.");
            return;
        }else if(myWeightBox.getText().length() == 0 || myWeightBox.getText().toString().equals(("0"))){
            error("Need a Weight!","You must enter a name AND weight before adding the factor to your grade" );
            return;
        }else if(myNameBox.getText().length() == 0){
            error("Need a Name!", "You must enter a name AND weight before adding the factor to your grade.");
            return;
        }

        // Converts user input (factor name and weight) to String and Double, respectively
        mFactorName = myNameBox.getText().toString();
        mFactorWeight = Double.parseDouble((myWeightBox.getText().toString()));

        factor.setFactorValues(mFactorName, mFactorWeight);

        possibleErrors = checkForUserErrors(mFactorWeight, mFactorName);

        switch(possibleErrors){
            case "numError":
                Toast.makeText(this, "Internal Error: Rebooting", Toast.LENGTH_SHORT).show();
                return;
            case "stringError":
                Toast.makeText(this, "Internal Error: Rebooting", Toast.LENGTH_SHORT).show();
                return;
        }

        factorArrayList.add(numFactors, factor);

        if(getCurrentFocus()!=null){imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);}
        myNameBox.setText("");
        myWeightBox.setText("");

        myNameBox.setHint("Factor's Name");
        myWeightBox.setHint("Weight");

        list.setAdapter(customAdapter);

        myNameBox.clearFocus();
        myWeightBox.clearFocus();
    }

    // Performed after "SUBMIT" is clicked
    public void send(View view) {
        EditText mNameBox = (EditText)findViewById(R.id.nameBox);
        Bundle b = new Bundle();

        for(int i = 0; i < factorArrayList.size(); i++){
            Log.i(KEY, "Factor[i]: " + (factorArrayList.get(i)).getFactorName());
        }

        if(factorArrayList.size() == 0){
            error("Error!", "You must enter a name and weight before proceeding.");
            return;
        }


        b.putParcelableArrayList(FACTOR_ARRAY_KEY, factorArrayList);



        // Hides keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mNameBox.getWindowToken(), 0);


        Intent intent = new Intent(getApplicationContext(), EnterScore.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    // Returns the factor's name(string) and percent(double) as a formatted list item (string)
    public String checkForUserErrors(Double number, String name){
        if(name.equals("") || name.equals("") || name.isEmpty()){
            error("Error","\nYou must enter a name for your factor");
            return "stringError";
        }
        if(number < 0 && number > 300 && name.length() > 75){
            error("Error", "The name of your factors cannot be longer than 75 characters.");
            return "numError";
        }else {

            return name + ":       " + formatter.format(number) + " points";
        }
    }

    // Performed after "CLEAR" is clicked
    public void clear(View view){
        // Clear array of myFactors
        factorArrayList.clear();

        // Create adapter (using SIMPLE_LIST_ITEM1)
        resetAdapter = new ArrayAdapter<myFactor>(this, android.R.layout.simple_list_item_1, factorArrayList);

        // Set context (not sure if necesary)
        myActivity = this;

        // EditText boxes reset
        myNameBox = (EditText)findViewById(R.id.nameBox);
        myWeightBox = (EditText)findViewById(R.id.percentBox);

        // ResetAdapter set to List
        ((ListView) findViewById(R.id.list1)).setAdapter(resetAdapter);

        // Resets EditText boxes text to empty
        myNameBox.setText("");
        myWeightBox.setText("");

        myNameBox.requestFocus();
    }

    public void error(String title, String msg){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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



}

