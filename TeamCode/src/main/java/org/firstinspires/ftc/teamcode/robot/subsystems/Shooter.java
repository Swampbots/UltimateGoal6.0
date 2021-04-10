package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.drive.StandardTrackingWheelLocalizer;

@Config
public class Shooter implements Subsystem {
    // Hardware map
    private HardwareMap hardwareMap;

    private DcMotorEx shooter;

    private double      power = POWER_LEVELS.FAR.getPower();
    private boolean     shoot = false;
    private MODE        mode  = MODE.POWER;

    public static double TEST_ALTER_VELOS = 60;
    public static double TEST_PIDF_P = 10.0;
    public static double TEST_PIDF_I = 0;
    public static double TEST_PIDF_D = 0;
    public static double TEST_PIDF_F = 0;

    public enum MODE {
        POWER,
        VELOCITY;

        public boolean getType() {
            return this == POWER;
        }
    }

    public enum POWER_LEVELS {
        FAR,
        SHORT,
        POWER_SHOT;

        public double getPower() {
            switch (this) {
                case FAR: return 1.0;
                case SHORT: return 0.90;
                case POWER_SHOT: return 0.85;
                default: return 0.85;
            }
        }
    }

    public enum VELO_LEVELS {
        FAR,
        SHORT,
        POWER_SHOT;

        public double getVelo() {
            switch (this) {
                case FAR: return TEST_ALTER_VELOS;
                case SHORT: return 55;
                case POWER_SHOT: return 50;
                default: return 0.85;
            }
        }
    }

    public Shooter(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;
    }

    @Override
    public void initHardware() {
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        shooter.setDirection(DcMotorSimple.Direction.FORWARD);
        shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(TEST_PIDF_P, TEST_PIDF_I, TEST_PIDF_D, TEST_PIDF_F));
    }

    @Override
    public void periodic() {
        if(mode.getType())
            shooter.setPower(power * (shoot ? 1 : 0));
        else
            shooter.setVelocity(power * (shoot ? 1 : 0), AngleUnit.RADIANS);
    }

    public void setPower(double power) {
        this.mode = MODE.POWER;
        this.power = power;
    }

    public void setDirection(DcMotorSimple.Direction direction) {
        shooter.setDirection(direction);
    }

    public void setShoot(boolean shoot) {
        this.shoot = shoot;
    }

    public void setVelocity(double velocity) {
        power = velocity;
        mode = MODE.VELOCITY;
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

    public double getVelocity() {
        return shooter.getVelocity(AngleUnit.RADIANS);
    }

    public boolean getShoot() {
        return shoot;
    }

    public void shoot() {
        this.shoot = !this.shoot;
    }
}
