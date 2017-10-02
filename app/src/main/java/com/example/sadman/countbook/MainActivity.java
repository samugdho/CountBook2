/*
 * Copyright (c) 2017. Sadman, CMPUT 301, University of Alberta - All Rights Reserved.
 * You may use, distribute, or modify this code under terms and conditions of the
 * Code of Students Behaviour at University of Alberta
 */

package com.example.sadman.countbook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView amount_of_comments;
    private ListView mainListView;
    private AlertDialog.Builder newCounterBuilder;
    private AlertDialog newCounterDialog;
    private LayoutInflater newCounterLayoutInflater;
    private View prompt_for_custom_view;

    public static ArrayAdapter<Counter> counterAdapter;
    public static ArrayList<Counter> counters;

    public static final String COUNTER_POSITION = "THIS.IS.COUNTER.POSITION";
    public static final String COUNTER_FILENAME = "counter_file.save";


    /**
     * To builder: Please use API 25
     */

    /**
     * Creates the main activity;
     * Sets what happens when [NEW] is clicked
     * @see Counter
     * @param savedInstanceState
     */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newButton = (Button) findViewById(R.id.new_button);
        mainListView = (ListView) findViewById(R.id.main_list);
        // Everything that happens when [NEW] is clicked
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCounterLayoutInflater = LayoutInflater.from(MainActivity.this);
                prompt_for_custom_view = newCounterLayoutInflater.inflate(R.layout.alert_custom_new_init, null);

                newCounterBuilder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.AlertDialogCustom));
                newCounterBuilder.setView(prompt_for_custom_view);

                final EditText counterName = (EditText) prompt_for_custom_view.findViewById(R.id.editName);
                // Set keyboard to numerical
                final EditText counterVal = (EditText) prompt_for_custom_view.findViewById(R.id.editVal);
                counterVal.setRawInputType(Configuration.KEYBOARD_12KEY);
                final EditText counterComment = (EditText) prompt_for_custom_view.findViewById(R.id.editComment);

                newCounterBuilder.setTitle("New Counter");
                // I override this below
                newCounterBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id){
                        }
                    }
                );
                newCounterBuilder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                dialog.cancel();
                            }
                        });
                newCounterBuilder.setCancelable(false);

                newCounterDialog = newCounterBuilder.create();
                newCounterDialog.show();
                (newCounterDialog.getButton(DialogInterface.BUTTON_POSITIVE)).setOnClickListener(new View.OnClickListener(){
                    // Gives Toast warnings on wrong input and nothing happens when clicking ok if error.
                    @Override
                    public void onClick(View v) {
                        boolean done = false;
                        // Trim strings so names are not made of spaces
                        String name = counterName.getText().toString().trim();
                        String num = counterVal.getText().toString().trim();

                        if(!name.equals("")){
                            // Integer max size is 2^32, that's 2 billion. I limit the app to 1 billion - 1 by checking length
                            if(!num.equals("") && num.length() < 10){
                                Counter counter = new Counter(name,
                                        counterComment.getText().toString(),
                                        Integer.parseInt(num)
                                );
                                counters.add(counter);
                                counterAdapter.notifyDataSetChanged();
                                updateAmount();
                                saveInFile(MainActivity.this);
                                done = true;
                            }else {
                                toastMe("Please enter a value! \n0 to 999,999,999", MainActivity.this);
                            }
                        }else{
                            toastMe("Please enter a name!", MainActivity.this);
                        }
                        if(done){
                            newCounterDialog.dismiss();
                        }
                    } // onClick
                }); // onClickListener for newCounterDialog
            } // onClick
        }); // onClickListener for newButton
    } // onCreate

    /**
     * Set up adapter and load counters from file.
     * Loading code is adapted from LonelyTwitter
     * @see CustomAdapter
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("CHAOS", "Starting!");
        loadCounters();
        counterAdapter = new CustomAdapter(this, counters);
        amount_of_comments = (TextView) findViewById(R.id.counter_amount);
        updateAmount();
        mainListView.setAdapter(counterAdapter);
    }

    /**
     * Loads counters from json using Gson
     * @see Gson
     */
    private void loadCounters(){
        try {
            FileInputStream fis = openFileInput(COUNTER_FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Counter>>() {}.getType();
            counters = gson.fromJson(in, listType);
        }catch (FileNotFoundException e){
            counters = new ArrayList<>();
        }
    }

    /**
     * static save function adapted from LonelyTwitter;
     * takes a context so it can be used in multiple activities
     * Save file to json with Gson
     * @see Gson
     * @param ctx
     */
    public static void saveInFile(Context ctx){
        try {
            FileOutputStream fos = ctx.openFileOutput(COUNTER_FILENAME,
                    Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(counters, writer);
            writer.flush();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Updates the summary that counts comments
     */
    public void updateAmount(){
        int s = counters.size();
        if(s == 0){
            amount_of_comments.setText("No Counters");
        }else if(s == 1){
            amount_of_comments.setText("1 Counter");
        }else{
            amount_of_comments.setText(s+" Counters");
        }
    }

    /**
     * Update things after returning from other activities
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        counterAdapter.notifyDataSetChanged();
        updateAmount();
        saveInFile(MainActivity.this);
    }

    /**
     * static Toast method to make easy Toasts
     * @param s
     * @param context
     */
    public static void toastMe(String s, Context context){
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show();
    }
}
