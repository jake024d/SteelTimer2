# SteelTimer2
A new version of SteelTimer

The time at the top of the screen is the time until the Steel is finished. The second time is how much fuel is left.

Upon starting the app, there will be two TextViews and a Button that says START. Pushing the start button starts two instances of the 
customclass Timer, naming them mainTimer and fuelTimer. Then two more Buttons become visible, ADD FUEL and RESET, while switching the image
of the START Button to show FILL UP instead, and will show that until the RESET Button is pushed. An EditText and a Spinner also become 
visible.

The ADD FUEL Button will not work if the EditText is empty. Also, if you try to add more fuel than the Steel Crucible can hold it will 
instead give you a Toast message saying so. It does this by reading the Spinner state to find the fuel type selected, and depending on 
which one is selected, multiply the EditText value by the appropriate amount to get the time in milliseconds.

The FILL UP Button will raise the fuel amount back up to 12 hours, althought it should have a check against the selected fuel type to not 
allow it to be filled more than it would in-game.

------------------------------------
TO-BE-DONE: If no name next to it, no one has decided to fix this yet.
------------------------------------

1. First priority is to make sure the timers are saved no matter what.  --Jake but could use help
Currently they are saved in the onSavedInstanceState method in the       
MainActivity, by saving the amount of milliseconds left added to the current time in milliseconds.

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

This works as long as the current memory isn't lost by another app needing too much memory. I believe changing the app permisions to 
include read/write of files, we can save these values when onDestroy() is called. Would also need a catch to look for said file first, 
opening and reading file to load the times.

