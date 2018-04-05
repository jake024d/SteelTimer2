package com.jakedickson.myapplication.framework.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import com.jakedickson.myapplication.MainActivity;

import java.util.Calendar;
import java.util.Locale;

public class Timer extends CountDownTimer {

    private Context c;
    private TextView timerText;
    public long fuelAmountInMillis = 0;
    private long finish_time;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public Timer(long millisInFuture, long countDownInterval, TextView textView, Context context) {
        super(millisInFuture, countDownInterval);
        timerText = textView;

        Calendar calender = Calendar.getInstance();
        finish_time = calender.getTimeInMillis() + millisInFuture;

        c = context.getApplicationContext();

    }

    @Override
    public void onTick(long millisUntilFinished) {

        if (fuelAmountInMillis != 0) {
            long mTimeLeftInMillis;

            if ( millisUntilFinished + fuelAmountInMillis <= 43200000) {
                mTimeLeftInMillis = millisUntilFinished + fuelAmountInMillis;
                onRefuel(mTimeLeftInMillis);
            } else {
                fuelAmountInMillis = 0;
                onCantRefuel(c);
                updateCountDownText(millisUntilFinished, timerText);
            }
        }

        updateCountDownText(millisUntilFinished, timerText);

    }

    @Override
    public void onFinish() {
    }

    public void onReset(String finish) {
        timerText.setText(finish);
    }

    private void onRefuel(long newTime) {
        MainActivity.addFuel(newTime, c);
    }

    private void updateCountDownText(long mTimeLeftInMillis, TextView textView) {
        int days = (int) (mTimeLeftInMillis / 1000) / 86400;
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600 % 24;
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60 % 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;

        //Setting current burn time
        if (days > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
            textView.setText(timeLeftFormatted);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
            textView.setText(timeLeftFormatted);
        }

    }

    public void onCantRefuel(Context context) {
        CharSequence text = "Too much fuel!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    public Long onSaveTimer () {
        return finish_time;
    }

}
