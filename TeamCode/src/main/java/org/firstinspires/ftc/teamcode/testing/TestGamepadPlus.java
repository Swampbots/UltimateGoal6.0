package org.firstinspires.ftc.teamcode.testing;

import android.os.Build;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.GamepadPlus;

import androidx.annotation.RequiresApi;

@TeleOp
public class TestGamepadPlus extends OpMode {

    GamepadPlus gp1;

    @Override
    public void init() {
        gp1 = new GamepadPlus(gamepad1);

        telemetry.addData("gp type",gamepad1.type());
        telemetry.update();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void loop() {
        gp1.update();

        String button = "a";


        //Should return 'true' while the button is pressed
        telemetry.addData("Is "+ button +" pressed?",gp1.isPressed(button));
        telemetry.addLine();

        //Should return the amount of milliseconds the button was pressed"
        telemetry.addData("Time "+ button +" pressed",gp1.howLongPressed(button));
        telemetry.addLine();

        //Should return 'true' for the first few milliseconds after the button was pressed
        telemetry.addData("Was "+button+" just pressed?",gp1.wasJustPressed(button));
        telemetry.addLine();

        telemetry.addData("Timer",gp1.getTimer());
        telemetry.addLine();

        telemetry.addData("Index List",gp1.getCurrentIndexes());
        telemetry.addLine();

        telemetry.addData("Timer list",gp1.getCurrentTimes());
        telemetry.addLine();

        telemetry.addData("Index-1 List",gp1.getPreviousIndexes());
        telemetry.addLine();

        telemetry.addData("Timer-1 list",gp1.getPreviousTimes());
        telemetry.addLine();

        telemetry.addData("Sanity chack",gamepad1.a);
        telemetry.update();
    }
}
