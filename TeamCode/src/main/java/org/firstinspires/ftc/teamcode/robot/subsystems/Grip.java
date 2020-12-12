package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Grip implements Subsystem {
    // Hardware map
    private HardwareMap hardwareMap;

    private Servo grip;

    private double targetPos;

    public enum TARGETS {
        OPEN,
        CLOSE;

        public double getTarget(){
            switch (this){
                case OPEN:  return 0;
                case CLOSE: return 1;
                default:    return 0;
            }
        }
    }
    public Grip(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        //arm = hardwareMap.get(DcMotor.class, "arm");
        grip = hardwareMap.get(Servo.class, "grip");

        //targetPos = arm.getCurrentPosition();

        //arm.setTargetPosition(targetPos);
        //arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        grip.setPosition(TARGETS.OPEN.getTarget());
    }

    @Override
    public void periodic() {
        grip.setPosition(targetPos);
    }

    public void setTargetPos(double targetPos){
        this.targetPos = targetPos;
    }

    public double getCurrentPos(){
        return grip.getPosition();
    }

    public double getTargetPos() {
        return targetPos;
    }

    public void togglePos(){
        targetPos = Math.abs(targetPos - TARGETS.OPEN.getTarget()) < Math.abs(targetPos - TARGETS.CLOSE.getTarget()) ? TARGETS.CLOSE.getTarget() : TARGETS.OPEN.getTarget();
    }
}
