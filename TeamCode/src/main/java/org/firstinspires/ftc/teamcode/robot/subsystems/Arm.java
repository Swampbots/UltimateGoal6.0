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
        OUT,
        DOWN;

        //TODO: Confirm targets
        public int getTarget() {
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

    public Arm(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;

        initHardware();
    }

    public void initHardware() {
        arm = hardwareMap.get(DcMotor.class, "arm");

        targetPos = arm.getCurrentPosition();

        arm.setTargetPosition(targetPos);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void periodic() {
        if(arm.getMode() == DcMotor.RunMode.RUN_TO_POSITION) {
            arm.setTargetPosition(targetPos);
        }

        arm.setPower(power);
    }

    public DcMotor.RunMode getRunMode(){
        return arm.getMode();
    }

    public double getPower(){
        return power;
    }

    public boolean isBusy() {return arm.isBusy();}

    public void setRunMode(DcMotor.RunMode runMode){
        arm.setMode(runMode);
    }

    public void setPower(double power){
        this.power = power;
    }


    //TODO: Test out encoder functions
    public void setTargetPos(int targetPos){
        this.targetPos = targetPos;
    }

    public int getCurrentPos(){
        return arm.getCurrentPosition();
    }

    public int getTargetPos(){
        return arm.getTargetPosition();
    }

}

