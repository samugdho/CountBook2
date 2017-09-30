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

    private TextView amount;
    public static ArrayList<Counter> counters;
    private ListView listView;
    public static ArrayAdapter<Counter> adapter;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater li;
    private View prompt;
    public static final String COUNTER_POSITION = "THIS.IS.COUNTER.POSITION";
    public static final String COUNTER_FILENAME = "counter_file.save";


//    private void dialogInitValue(final String counterName){
//        builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setTitle("New Counter");
//        final EditText input = new EditText(MainActivity.this);
//        input.setInputType(InputType.TYPE_CLASS_NUMBER);
//        input.setRawInputType(Configuration.KEYBOARD_12KEY);
//        builder.setView(input);
//        builder.setPositiveButton("Next", new DialogInterface.OnClickListener(){
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String answer = counterName + input.getText().toString();
//                arrayList.add(answer);
//                adapter.notifyDataSetChanged();
//
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });
//        builder.show();
//    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("CHAOS", "Starting!");
        loadCounters();
        adapter = new CustomAdapter(this, counters);
        amount = (TextView) findViewById(R.id.counter_amount);
        updateAmount();
        listView.setAdapter(adapter);


        // Testing
//        counters = new ArrayList<>();
//        adapter.notifyDataSetChanged();
//        for(int i=0;i<10;i++){
//            Counter c = new Counter(
//                    "Counter-"+String.valueOf(i),
//                    "Comment-"+String.valueOf(i),
//                    i);
//            counters.add(c);
//        }
//        adapter.notifyDataSetChanged();
        //Testing End


    }
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
    public void updateAmount(){
        int s = counters.size();
        if(s == 0){
            amount.setText("No Counters");
        }else if(s == 1){
            amount.setText("1 Counter");
        }else{
            amount.setText(s+" Counters");
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.new_button);
        listView = (ListView) findViewById(R.id.main_list);


        // TODO
        // Make so that name and value cannot be empty



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO
                // Case for name is empty
                // Case for value is empty
                // Fix newline in comment
                li = LayoutInflater.from(MainActivity.this);
                prompt = li.inflate(R.layout.alert_custom_new_init, null);

                builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.AlertDialogCustom));
                builder.setView(prompt);

                final EditText counterName = (EditText) prompt.findViewById(R.id.editName);
                final EditText counterVal = (EditText) prompt.findViewById(R.id.editVal);
                counterVal.setRawInputType(Configuration.KEYBOARD_12KEY);
                final EditText counterComment = (EditText) prompt.findViewById(R.id.editComment);

                builder.setTitle("New Counter");

                builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int id){
                        }
                    }
                );
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                dialog.cancel();
                            }
                        });
                builder.setCancelable(false);

                dialog = builder.create();
                dialog.show();
                (dialog.getButton(DialogInterface.BUTTON_POSITIVE)).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        boolean done = false;
                        String name = counterName.getText().toString().trim();
                        String num = counterVal.getText().toString().trim();

                        if(!name.equals("")){
                            if(!num.equals("") && num.length() < 10){
                                Counter counter = new Counter(name,
                                        counterComment.getText().toString(),
                                        Integer.parseInt(num)
                                );
                                counters.add(counter);
                                adapter.notifyDataSetChanged();
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
                            dialog.dismiss();
                        }
                    }
                });

            }




        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.notifyDataSetChanged();
        updateAmount();
        saveInFile(MainActivity.this);
    }
    public static void toastMe(String s, Context context){
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show();
    }
}
