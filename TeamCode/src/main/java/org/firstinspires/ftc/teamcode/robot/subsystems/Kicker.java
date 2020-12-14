package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Kicker implements Subsystem {
    // Hardware Map
    private HardwareMap hardwareMap;

    private Servo kicker;

    public enum TARGETS{
        IN,
        OUT;

        public double getTarget(){
            switch (this){
                case IN:    return 0.6;
                case OUT:   return 0.4;
                default:    return 0.1;
            }
        }
    }


    private double targetPos = TARGETS.IN.getTarget();

    public Kicker(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        kicker = hardwareMap.get(Servo.class, "kicker");
//        kicker.scaleRange(0,0.8); quote blake 12/12/2020: wtf
    }

    @Override
    public void periodic() {
        kicker.setPosition(targetPos);
    }

    public void setTargetPos(double targetPos){
        this.targetPos = targetPos;
    }

    public double getTargetPos() {
        return targetPos;
    }

    public double getCurrentPos() {
        return kicker.getPosition();
    }

    public void togglePos(){ //   0 |----1----| .5 |----0----| 1
        targetPos = Math.abs(targetPos- TARGETS.IN.getTarget()) < Math.abs(targetPos- TARGETS.OUT.getTarget()) ? TARGETS.OUT.getTarget() : TARGETS.OUT.getTarget() /* <---- ???????! */;
    }
}
