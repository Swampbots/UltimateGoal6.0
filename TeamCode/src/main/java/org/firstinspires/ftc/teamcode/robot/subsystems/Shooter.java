package org.firstinspires.ftc.teamcode.robot.subsystems;


import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Shooter implements Subsystem {
    // Hardware map
    private HardwareMap hardwareMap;

    private DcMotor shooter;

    private final double SLOW = 0.3;
    private final double MEDIUM = 0.35;
    private final double FAST = 0.4;
    private final double OFF = 0;
    private final double MAX_SPEED = 1;

    private double      power = OFF;
    private boolean     reverse = false;
    private boolean     shoot = false;

    public enum POWER_LEVELS{
        MAX,FAR,MEDIUM,SHORT,MIN,
        ADJ;

        public double getPower() {
            switch (this){
                case MAX: return 1.0;
                case MIN: return 0.0;
                case FAR: return 0.85;
                case MEDIUM: return 0.75;
                case SHORT: return 0.60;
                case ADJ: return 0.70;
                default: return 0.75;
            }
        }
    }

    public Shooter(HardwareMap hardwareMap){
        this.hardwareMap = hardwareMap;

    }

    @Override
    public void initHardware(){
        shooter = hardwareMap.get(DcMotor.class, "shooter");
    }

    @Override
    public void periodic() {
        shooter.setPower(power * (reverse?-1:1) * (shoot?1:0));
    }

    public void setPower(double power){
        this.power = power;
    }

    public void reversePower(boolean reverse){
        this.reverse = reverse;
    }

    public void setShoot(boolean shoot) {
        this.shoot = shoot;
    }

    //TODO: Finish this
    public void shoot(){
        this.shoot = !this.shoot;
    }

    //TODO: Finish this
    public void reverse(){
        this.reverse = !this.reverse;
    }

    public double getPower() {
        return power;
    }

    public boolean isReverse() {
        return reverse;
    }

    public boolean getShoot() {
        return shoot;
    }


}
