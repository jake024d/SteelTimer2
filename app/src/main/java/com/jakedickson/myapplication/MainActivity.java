package com.jakedickson.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import com.jakedickson.myapplication.framework.util.Timer;

public class MainActivity extends AppCompatActivity {

    private Button startButton, addFuelButton, resetButton;
    private TextView mainTextView;
    private Timer mainTimer;
    private static Timer fuelTimer;
    private Spinner fuelSpinner;
    private EditText fuelEditText;

    static TextView fuelTextView;

    private Calendar calender = Calendar.getInstance();

    private String TIMER_RUNNING = "booleanValue";

    public boolean mainTimerBoolean = false;

    private double fuelTimeBeingAdded;

    private Context c;

    long MAIN_TIME_IN_MILLIS = 201600000;
    long FUEL_TIME_IN_MILLIS = 43200000;

    private String MAIN_FINISH_TIME = "whenMainTimerRunsOutInMillis";
    private String FUEL_FINISH_TIME = "whenFuelTimerRunsOutInMillis";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // recovering the instance state
        if (savedInstanceState != null) {
            super.onCreate(savedInstanceState);
            mainTimerBoolean = savedInstanceState.getBoolean(TIMER_RUNNING);
            if (mainTimerBoolean) {

                long mainFinishTime = savedInstanceState.getLong(MAIN_FINISH_TIME);
                long fuelFinishTime = savedInstanceState.getLong(FUEL_FINISH_TIME);
                mainFinishTime -= calender.getTimeInMillis();
                fuelFinishTime -= calender.getTimeInMillis();

                mainTimer = new Timer(mainFinishTime, 1000, mainTextView, this);
                fuelTimer = new Timer(fuelFinishTime, 1000, fuelTextView, this);

                mainTimer.start();
                fuelTimer.start();

                startButton.setVisibility(View.INVISIBLE);
                addFuelButton.setVisibility(View.VISIBLE);
                resetButton.setVisibility(View.VISIBLE);

            }
        } else {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            init();
        }

    }


    // get the selected dropdown list value
    public void init() {
        c = this;

        mainTextView = findViewById(R.id.main_Timer_Text_View);
        fuelTextView = findViewById(R.id.fuel_Timer_Text_View);


        fuelEditText = findViewById(R.id.fuel_amount_edit_text);
        fuelEditText.setVisibility(View.INVISIBLE);

        fuelSpinner = findViewById(R.id.fuel_spinner);
        fuelSpinner.setVisibility(View.INVISIBLE);

        startButton = findViewById(R.id.start_button);

        addFuelButton = findViewById(R.id.add_fuel_button);
        addFuelButton.setVisibility(View.INVISIBLE);


        resetButton = findViewById(R.id.reset_button);
        resetButton.setVisibility(View.INVISIBLE);

        startButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mainTimerBoolean) {
                    fuelTimer.cancel();
                    fuelTimer = new Timer(FUEL_TIME_IN_MILLIS, 1000, fuelTextView, c);
                    fuelTimer.start();
                }
                if (!mainTimerBoolean) {
                    mainTimerBoolean = true;
                    mainTimer = new Timer(MAIN_TIME_IN_MILLIS, 1000, mainTextView, c);
                    fuelTimer = new Timer(FUEL_TIME_IN_MILLIS, 1000, fuelTextView, c);
                    mainTimer.start();
                    fuelTimer.start();
                    startButton.setBackground(getDrawable(R.drawable.fillupbutton));
                    addFuelButton.setVisibility(View.VISIBLE);
                    resetButton.setVisibility(View.VISIBLE);
                    fuelSpinner.setVisibility(View.VISIBLE);
                    fuelEditText.setVisibility(View.VISIBLE);
                }

            }

        });

        resetButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mainTimerBoolean) {
                    mainTimerBoolean = false;
                    String mainTime = "02:08:00:00";
                    String fuelTime = "12:00:00";
                    mainTimer.onReset(mainTime);
                    fuelTimer.onReset(fuelTime);
                    addFuelButton.setVisibility(View.INVISIBLE);
                    resetButton.setVisibility(View.INVISIBLE);
                    fuelEditText.setVisibility(View.INVISIBLE);
                    fuelSpinner.setVisibility(View.INVISIBLE);
                    startButton.setBackground(getDrawable(R.drawable.startbutton));
                    mainTimer.cancel();
                    fuelTimer.cancel();
                }

            }

        });

        addFuelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String expression = fuelEditText.getText().toString();

                if (!expression.equals("")) {

                    String fuelType = fuelSpinner.getSelectedItem().toString();

                    if (fuelType.equals("Block of wood")) {
                        fuelTimeBeingAdded = Double.parseDouble(expression) * 19200000;
                        fuelTimer.fuelAmountInMillis = (long) (fuelTimeBeingAdded);
                    } else if (fuelType.equals("Coal")) {
                        fuelTimeBeingAdded = Double.parseDouble(expression) * 4800000;
                        fuelTimer.fuelAmountInMillis = (long) (fuelTimeBeingAdded);
                    } else if (fuelType.equals("Branch")) {
                        fuelTimeBeingAdded = Double.parseDouble(expression) * 2400000;
                        fuelTimer.fuelAmountInMillis = (long) (fuelTimeBeingAdded);
                    }
                }
            }
        });


    }

    public static void addFuel(long newFuelTimeInMillis, Context context) {
        fuelTimer.cancel();
        fuelTimer = new Timer(newFuelTimeInMillis, 1000, fuelTextView, context);
        fuelTimer.start();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        long mainFinishInMillis, fuelFinishInMillis;

        if (mainTimerBoolean) {
            // call superclass to save any view hierarchy
            super.onSaveInstanceState(outState);

            mainFinishInMillis = mainTimer.onSaveTimer();
            fuelFinishInMillis = fuelTimer.onSaveTimer();

            //Saving important values
            outState.putLong(MAIN_FINISH_TIME, mainFinishInMillis);
            outState.putLong(FUEL_FINISH_TIME, fuelFinishInMillis);

            outState.putBoolean(TIMER_RUNNING, mainTimerBoolean);
        }

    }


}
