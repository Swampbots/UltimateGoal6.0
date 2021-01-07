package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Kicker implements Subsystem {
    // Hardware Map
    private HardwareMap hardwareMap;

    private Servo kicker;

    private boolean in = true;

    public enum TARGETS {
        IN,
        OUT;

        public double getTarget() {
            switch (this) {
                case IN:    return 0.6;
                case OUT:   return 0.4;
                default:    return 0.1;
            }
        }
    }

    public Kicker(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        kicker = hardwareMap.get(Servo.class, "kicker");
    }

    @Override
    public void periodic() {
        kicker.setPosition(in ? TARGETS.IN.getTarget() : TARGETS.OUT.getTarget());
    }

    public double getCurrentPos() {
        return kicker.getPosition();
    }

    public void in() {
        in = true;
    }

    public void out() {
        in = false;
    }

    public void togglePos() {
        in = !in;
    }
}
