package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Arm implements Subsystem {
    // Hardware map
    private HardwareMap hardwareMap;

    private DcMotor arm;

    public enum TARGETS {
        UP,
        DOWN,
        OUT;

        public int getTarget(){
            switch (this){
                case UP:    return 1000;
                case OUT:   return 0;
                case DOWN:  return -1000;
                default:    return 0;
            }
        }
    }

    private double power = 0;

    private int targetPos;
    private int targetOffset = 0;

    public Arm(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        arm = hardwareMap.get(DcMotor.class, "arm");

        targetPos = arm.getCurrentPosition();

        arm.setTargetPosition(targetPos);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void periodic() {
        if(arm.getMode() == DcMotor.RunMode.RUN_TO_POSITION){
            arm.setTargetPosition(targetPos);
        } else if(arm.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER){
            arm.setPower(power);
        }

    }

    public void resetTargetPos(){
        targetOffset = arm.getCurrentPosition() - targetOffset;
    }

    public void setTargetPos(int targetPos){
        this.targetPos = targetPos - targetOffset;
    }

    public int getCurrentPos(){
        return arm.getCurrentPosition() - targetOffset;
    }

    public int getTargetPos(){
        return arm.getTargetPosition() - targetOffset;
    }


    public void setRunMode(DcMotor.RunMode runMode){
        arm.setMode(runMode);
    }

    public DcMotor.RunMode getRunMode(){
        return arm.getMode();
    }

    public void setPower(double power){
        this.power = power;
    }

    public double getPower(){
        return power;
    }




}
