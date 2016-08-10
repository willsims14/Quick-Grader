package com.gmail.willsims.quick_grade;

import android.app.Activity;

import android.content.res.Resources;
import android.os.Parcel;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Will Sims on 7/16/2016.
 */
public class MyListAdapter extends BaseAdapter {

    ArrayList<myFactor> mFactors;
    Parcel p = Parcel.obtain();
    myFactor tempFactor = new myFactor(p);
    Activity mActivity;
    public Resources mResources;
    List list = null;

    public static String KEY = "JACK23: ";


    final DecimalFormat formatter = new DecimalFormat("#.##");

    private static LayoutInflater inflater = null;


    public MyListAdapter(Activity activity, ArrayList<myFactor> factors, Resources res) {
        mActivity = activity;
        mFactors = factors;
        mResources = res;


        inflater = ((Activity) mActivity).getLayoutInflater();
    }


    @Override
    public int getCount() {
        if (mFactors.size() <= 0) {
            return 1;
        }
        return mFactors.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView nameField;
        public TextView numField;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder = new ViewHolder();


        if (convertView == null) {
            v = inflater.inflate(R.layout.list_view_layout2, parent, false);


            holder.nameField = (TextView)v.findViewById(R.id.customListItem1);
            holder.numField = (TextView)v.findViewById(R.id.customListItem2);

            holder.nameField.setText("");
            holder.numField.setText("");

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }
            if (mFactors.size() <= 0) {
                v.setTag(holder);
                Log.i(KEY, "Size < 0");
            } else {
                list = null;

                tempFactor = mFactors.get(position);

                String firstItemForList = tempFactor.getFactorName();
                String secondItemForList;

                if (parent.getId() == R.id.list1) {     // MainActivity
                    secondItemForList = "<b>" + formatter.format(tempFactor.getFactorWeight()) + "</b>" + " <small>pts</small>";
                }else {                                 // EnterScore Activity
                    if (tempFactor.scoreHasBeenEntered()) {         // If current factor has a score stored (!= -1), store "score/weight"
                        secondItemForList = "<b>" + formatter.format(tempFactor.getFactorScore()) + "</b>/" + formatter.format(tempFactor.getFactorWeight());
                    } else {                                        // If current factor doesn't have a score, (== -1 or uninitialized) store "-/weight"
                        secondItemForList = "-/" + formatter.format(tempFactor.getFactorWeight());
                    }
                }


                // Set List Values in Holder
                holder.nameField.setText(Html.fromHtml(firstItemForList));
                holder.numField.setText(Html.fromHtml(secondItemForList));
            }

        return v;
    }
}
