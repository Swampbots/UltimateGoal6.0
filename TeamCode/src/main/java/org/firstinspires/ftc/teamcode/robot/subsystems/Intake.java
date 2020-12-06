package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake implements Subsystem {
    private HardwareMap hardwareMap;
    private DcMotor intake;

    private double power = 0;
    private boolean reverse = false;

    public Intake(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        intake = hardwareMap.get(DcMotor.class,"intake");
    }

    @Override
    public void periodic() {
        intake.setPower(power * (reverse?-1:1));
    }

    public void setPower(double power){
        this.power = power;
    }

    public double getPower(){
        return power;
    }

    public void setReverse(boolean reverse){
        this.reverse = reverse;
    }

    public void reverse(){
        this.reverse = !this.reverse;
    }

    public boolean getReverse(){
        return reverse;
    }
}
