package com.example.sadman.countbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


class CustomAdapter extends ArrayAdapter<Counter> {
    CustomAdapter(Context context,  ArrayList<Counter> counters) {
        super(context, R.layout.custom_list, counters);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View custom = inflater.inflate(R.layout.custom_list, parent, false);

        String name = getItem(position).getName();
        String date = getItem(position).getDateString();
        String number = getItem(position).getCurrentVal() + "";

        TextView t_name = (TextView) custom.findViewById(R.id.list_name);
        TextView t_date = (TextView) custom.findViewById(R.id.list_date);
        TextView t_number = (TextView) custom.findViewById(R.id.list_number);

        Button b_minus = (Button) custom.findViewById(R.id.list_button_minus);
        Button b_plus = (Button) custom.findViewById(R.id.list_button_plus);

        LinearLayout main = (LinearLayout) custom.findViewById(R.id.layout_main);

        t_name.setText(name);
        t_date.setText(date);
        t_number.setText(number);

        b_plus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                updateCounter(1, position, custom);
            }
        });
        b_minus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                updateCounter(-1, position, custom);
            }
        });
        main.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                editCounter(position);
            }
        });

        return custom;


    }
    public void updateCounter(int val, int position, View custom){
        Counter c = getItem(position);
        int value = c.getCurrentVal() + val;
        if(value > 999999999){
            MainActivity.toastMe("MAX: 999,999,999", getContext());
        }else if(value < 0){
            MainActivity.toastMe("MIN: 0", getContext());
        }else{
            ((TextView) custom.findViewById(R.id.list_number)).setText(value+"");
            c.setCurrentVal(value);
            MainActivity.saveInFile(getContext());
        }
    }
    public void editCounter(int position){
        Intent intent = new Intent(getContext(), EditCounter.class);
        intent.putExtra(MainActivity.COUNTER_POSITION, String.valueOf(position));
        ((Activity)getContext()).startActivityForResult(intent, 0);

    }


}
