package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Shooter implements Subsystem {
    // Hardware map
    private HardwareMap hardwareMap;

    private DcMotor shooter;

    private double      power = POWER_LEVELS.FAR.getPower();
    private boolean     shoot = false;

    public enum POWER_LEVELS {
        FAR,
        SHORT,
        POWER_SHOT;

        public double getPower() {
            switch (this) {
                case FAR: return 0.95;
                case SHORT: return 0.90;
                case POWER_SHOT: return 0.85;
                default: return 0.85;
            }
        }
    }

    public Shooter(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        shooter = hardwareMap.get(DcMotor.class, "shooter");
        shooter.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public void periodic() {
        shooter.setPower(power * (shoot ? 1 : 0));
    }

    public void setPower(double power) {
        this.power = power;
    }

    public void setDirection(DcMotorSimple.Direction direction) {
        shooter.setDirection(direction);
    }

    public void setShoot(boolean shoot) {
        this.shoot = shoot;
    }

    public void reverse() {
        if(shooter.getDirection() == DcMotorSimple.Direction.FORWARD) shooter.setDirection(DcMotorSimple.Direction.REVERSE);
        else shooter.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public double getPower() {
        return power;
    }

    public DcMotorSimple.Direction getReverse() {
        return shooter.getDirection();
    }

    public boolean getShoot() {
        return shoot;
    }

    //TODO: Finish this
    public void shoot() {
        this.shoot = !this.shoot;
    }
}
