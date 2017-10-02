/*
 * Copyright (c) 2017. Sadman, CMPUT 301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the
 * Code of Students Behaviour at University of Alberta
 */

package com.example.sadman.countbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity to see details of a counter and edit some values;
 * And perform reset + delete
 */
public class EditCounter extends AppCompatActivity {

    private int position;
    private TextView init;
    private TextView val;
    private TextView name;
    private TextView comment;
    private TextView update;
    private Counter counter;

    /**
     * Create a new EditCounter activity
     * Sets the values from a counter
     * Sets some buttons with actions
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_counter);

        // Getting position -> counter to add in all values
        Intent intent = getIntent();
        position = Integer.parseInt(intent.getStringExtra(MainActivity.COUNTER_POSITION));
        counter = MainActivity.counters.get(position);

        // Getting xml things
        TextView date = (TextView) findViewById(R.id.counter_date);
        init = (TextView) findViewById(R.id.counter_init_value);
        val = (TextView) findViewById(R.id.counter_current_value);
        name = (TextView) findViewById(R.id.counter_name);
        comment = (TextView) findViewById(R.id.counter_comment);
        update = (TextView) findViewById(R.id.counter_date_last);

        // Setting xml things
        date.setText("Created on "+MainActivity.counters.get(position).getCreationString());
        init.setText(MainActivity.counters.get(position).getInitVal()+"");
        val.setText(MainActivity.counters.get(position).getCurrentVal()+"");
        name.setText(MainActivity.counters.get(position).getName());
        comment.setText(MainActivity.counters.get(position).getComment());
        updateUpdate();

        // all these are edited individually
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText("Set Name", name);
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText("Set Comment", comment);
            }
        });
        init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText("Set Initial Value", init);
            }
        });
        val.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText("Set Current Value", val);
            }
        });

        // Getting buttons
        Button plus = (Button) findViewById(R.id.counter_button_plus);
        Button minus = (Button) findViewById(R.id.counter_button_minus);
        Button delete = (Button) findViewById(R.id.counter_button_delete);
        Button reset = (Button) findViewById(R.id.counter_button_reset);


        // Set buttons
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustCurrent(1);
            }});
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustCurrent(-1);
            }});

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.parseInt(init.getText().toString());
                val.setText(String.valueOf(value));
                counter.setCurrentVal(value);
                updateUpdate();
                MainActivity.saveInFile(EditCounter.this);
            }});
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.counters.remove(position);
                finish();
            }});

    }

    /**
     * updates the text on screen with Counter.updateDate, by the second
     */
    private void updateUpdate(){
        update.setText("Last Update on "+MainActivity.counters.get(position).getUpdateString());
    }

    /**
     * This is slightly different from CustomAdapter.updateCounter
     * @see CustomAdapter
     * @param amt {+-1}
     */
    private void adjustCurrent(int amt) {
        int value = Integer.parseInt(val.getText().toString());
        value += amt;
        if(value > 999999999){
            MainActivity.toastMe("MAX: 999,999,999", EditCounter.this);
        }else if(value < 0){
            MainActivity.toastMe("MIN: 0", EditCounter.this);
        }else {
            val.setText(String.valueOf(value));
            counter.setCurrentVal(value);
            updateUpdate();
            MainActivity.saveInFile(EditCounter.this);
        }
    }

    /**
     * opens a dialog to edit values
     * @param title title for alert dialog
     * @param type what exactly I'm editing
     */
    private void editText(String title, final TextView type){
        // Inflating from xml
        final LayoutInflater li = LayoutInflater.from(EditCounter.this);
        final View prompt = li.inflate(R.layout.alert_custom_edit, null);

        final EditText input = (EditText) prompt.findViewById(R.id.edit_thing);

        // decide the type and adjust the input method
        input.setText(type.getText());
        if(type == name){
            input.setSingleLine(true);
        }else if(type == val || type == init){
            input.setHint("Number Here!");
            input.setRawInputType(Configuration.KEYBOARD_12KEY);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
        }else{
            input.setHint("Comment Here!");
        }

        /*
        Make alert dialog
        very basic
         */
        final AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(EditCounter.this, R.style.AlertDialogCustom))
                .setTitle(title)
                .setView(prompt)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setCancelable(false)
                .create();

        dialog.show();
        // Override after showing
        // This is to prevent [OK] from dismissing dialog
        (dialog.getButton(DialogInterface.BUTTON_POSITIVE)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isdone = false;
                String input_text = input.getText().toString().trim();
                // Comment can equal empty string, so we do checks if anything else is empty
                if(!input_text.equals("") && type != comment){
                    isdone = true;
                    // Case for name, val, and init
                    if(type == name){
                        name.setText(input_text);
                        counter.setName(input_text);
                    }else if(input_text.length() > 9 && (type == val || type == init)){
                        // We don't check for negatives because it is impossible to input
                        // We check noValue below
                        MainActivity.toastMe("MAX: 999,999,999", EditCounter.this);
                        isdone = false;
                    }else if(type == init){
                        init.setText(input_text);
                        counter.setInitVal(Integer.parseInt(input_text));
                    }else if(type == val){
                        val.setText(input_text);
                        counter.setCurrentVal(Integer.parseInt(input_text));
                    }
                }else{
                    if(type == comment){
                        // Case for comment
                        comment.setText(input_text);
                        counter.setComment(input_text);
                        isdone = true;
                    }else{
                        MainActivity.toastMe("Please enter something!", EditCounter.this);
                    }
                }
                // saves to file on any edit
                if(isdone){
                    dialog.dismiss();
                    updateUpdate();
                    MainActivity.saveInFile(EditCounter.this);
                }
            }
        });
    }

}

