package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.subsystems.Shooter;

public class RunShooterForTime implements Command {
    private Shooter shooter;

    private ElapsedTime timer;
    private double seconds;
    private double power;

    public RunShooterForTime(Shooter shooter, double seconds, double power){
        this.shooter = shooter;
        this.seconds = seconds;
        this.power = power;

        timer = new ElapsedTime();
    }

    @Override
    public void start() {
        timer.reset();
        shooter.setPower(power);
    }

    @Override
    public void periodic() {

    }

    @Override
    public void stop() {
        shooter.setPower(0);
    }

    @Override
    public boolean isCompleted() {
        return timer.seconds() > seconds;
    }
}
