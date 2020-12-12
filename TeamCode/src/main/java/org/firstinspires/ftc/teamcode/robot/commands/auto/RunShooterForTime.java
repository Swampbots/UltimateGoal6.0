package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.subsystems.Shooter;

public class RunShooterForTime implements Command {
    private Shooter shooter;

    private ElapsedTime timer;
    private double seconds;
    private double power;
    private boolean stop;

    public RunShooterForTime(Shooter shooter, double seconds, double power){
        this.shooter = shooter;
        this.seconds = seconds;
        this.power = power;

        stop = true;

        timer = new ElapsedTime();
    }

    public RunShooterForTime(Shooter shooter, boolean stopAtEnd, double power){
        this.shooter = shooter;
        this.power = power;
        seconds = 0;
        stop = false;

        timer = new ElapsedTime();
    }

    @Override
    public void start() {
        timer.reset();
        shooter.setPower(-power);
        shooter.setShoot(true);
    }

    @Override
    public void periodic() {

    }

    @Override
    public void stop() {
        if(stop) {shooter.setPower(0); shooter.setShoot(false);}
    }

    @Override
    public boolean isCompleted() {
        return timer.seconds() > seconds;
    }
}
