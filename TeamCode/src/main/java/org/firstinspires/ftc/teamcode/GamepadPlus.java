package org.firstinspires.ftc.teamcode;


import android.os.Build;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import androidx.annotation.RequiresApi;

import static com.qualcomm.robotcore.hardware.Gamepad.Type.SONY_PS4;


public class GamepadPlus {
    private Gamepad gp;
    private ElapsedTime timer;

    //private ArrayList<String> indexHandler = new ArrayList<>();
    //private ArrayList<Long> timeActivated = new ArrayList<>();

    private HashMap<String, Double> currentInputs;
    private HashMap<String, Double> previousInputs;

    private static final float TRIGGER_THRESHOLD = 0.7f;

    private boolean isPS4Controller;
    private boolean ltButton;
    private boolean rtButton;



    public GamepadPlus(@NotNull Gamepad gp) {
        this.gp = gp;

        isPS4Controller = gp.type().equals(SONY_PS4);

        currentInputs = new HashMap<String, Double>();
        previousInputs = new HashMap<String, Double>();

        ltButton = false;
        rtButton = false;

        timer = new ElapsedTime();
    }

    public GamepadPlus(@NotNull Gamepad gp, boolean makeTriggersButtons){
        this.gp = gp;

        isPS4Controller = gp.type().equals(SONY_PS4);

        currentInputs = new HashMap<String, Double>();
        previousInputs = new HashMap<String, Double>();

        ltButton = makeTriggersButtons;
        rtButton = makeTriggersButtons;

        timer = new ElapsedTime();
    }

    public GamepadPlus(@NotNull Gamepad gp, boolean makeLeftTriggerButton, boolean makeRightTriggerButton){
        this.gp = gp;

        isPS4Controller = gp.type().equals(SONY_PS4);

        currentInputs = new HashMap<String, Double>();
        previousInputs = new HashMap<String, Double>();

        ltButton = makeLeftTriggerButton;
        rtButton = makeRightTriggerButton;

        timer = new ElapsedTime();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update(){
        /*if(gp.type().equals(UNKNOWN)){ //Prevents update from running if not controller is plugged in
            return;
        }*/

        //timer = new ElapsedTime();             // Update the timer

        // Run button updates
        if(isPS4Controller){ // Have to separate PS4 buttons
            updateButtonTime(gp.circle,"b");
            updateButtonTime(gp.cross,"a");
            updateButtonTime(gp.triangle,"y");
            updateButtonTime(gp.square,"x");
        } else {
            updateButtonTime(gp.a,"a");
            updateButtonTime(gp.b,"b");
            updateButtonTime(gp.x,"x");
            updateButtonTime(gp.y,"y");
        }

        updateButtonTime(gp.dpad_up,"dpad_up");
        updateButtonTime(gp.dpad_down,"dpad_down");
        updateButtonTime(gp.dpad_left,"dpad_left");
        updateButtonTime(gp.dpad_right,"dpad_right");

        updateButtonTime(gp.left_bumper,"left_bumper");
        updateButtonTime(gp.right_bumper,"right_bumper");

        if(ltButton){
            updateButtonTime((gp.left_trigger > TRIGGER_THRESHOLD), "left_trigger");
        }
        if(rtButton){
            updateButtonTime((gp.right_trigger > TRIGGER_THRESHOLD),"right_trigger");
        }
    }

    public boolean isPressed(String button){
        return currentInputs.containsKey(normalizeButtonInputs(button)) ? currentInputs.get(normalizeButtonInputs(button)) != 0 : false;
        //return indexHandler.indexOf(convertButtonInputs(button)) != -1;
    }

    public double howLongPressed(String button){
        return currentInputs.containsKey(normalizeButtonInputs(button)) ? timer.milliseconds() - currentInputs.get(normalizeButtonInputs(button)) : -1;
        //if(!isPressed(button)) return 0;

        //return timer.getTime() - timeActivated.get(indexHandler.indexOf(convertButtonInputs(button)));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean wasJustPressed(String button){
        return currentInputs.getOrDefault(normalizeButtonInputs(button),-1.0) != -1.0 &&
               previousInputs.getOrDefault(normalizeButtonInputs(button),-1.0) == -1.0;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean hasBeenPressed(String button){
        return previousInputs.getOrDefault(normalizeButtonInputs(button),-1.0) != -1.0;
    }

    public double getTimer(){
        return timer.milliseconds();
    }

    /*public ArrayList<String> getIndexHandler(){
        return indexHandler;
    }

    public ArrayList<Long> getTimeActivated(){
        return timeActivated;
    }*/
    public Set<String> getCurrentIndexes(){
        return currentInputs.keySet();
    }

    public Collection<Double> getCurrentTimes(){
        return currentInputs.values();
    }

    public Set<String> getPreviousIndexes(){
        return previousInputs.keySet();
    }

    public Collection<Double> getPreviousTimes(){
        return previousInputs.values();
    }

    public void resetTimer(){timer.reset();}


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateButtonTime(boolean button, String name) {
        previousInputs.put(name,currentInputs.getOrDefault(name,-1L));
        currentInputs.put(name,button?timer.getTime():-1L);
        previousInputs.put(name,currentInputs.getOrDefault(name,-1.0));
        currentInputs.put(name,button ? timer.milliseconds() : -1.0);
        /*if (button) { // button clicked
            inputs.put(name,timer.getTime());
            if (indexHandler.indexOf(name) == -1) { // button not in (cycle 0)
                indexHandler.add(name);
                timeActivated.add(timer.getTime());
            } else { // button is in; update time (cycle 1,2,3..,n-1)
                //timeActivated.set(indexHandler.indexOf(name), timer.getTime());
            }
        } else {
            if (indexHandler.indexOf(name) != -1) { // button just released (cycle n); remove from list
                timeActivated.remove(indexHandler.indexOf(name));
                indexHandler.remove(name);

                indexHandler.trimToSize();
                timeActivated.trimToSize();
            }
        }*/

    }


    // ensures compatibility between ps4 and non-ps4 controllers
    private String normalizeButtonInputs(String button){
        switch (button){
            case  "cross":
                return "a";
            case "circle":
                 return "b";
            case "square":
                return  "x";
            case "triangle":
                return  "y";
            default:
                return button;
        }
    }
}
