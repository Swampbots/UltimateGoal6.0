package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;

public class TurnByGyro implements Command {
    private Drive drive;

    private ElapsedTime timer;
    private double timeout;
    private double power;

    private double target;

    public TurnByGyro(Drive drive, double degrees, double power, double timeout){
        this.drive = drive;
        this.target = degrees;
        this.power = power;
        this.timeout = timeout;

        timer = new ElapsedTime();
    }

    @Override
    public void start() {
        timer.reset();
    }

    @Override
    public void periodic() {
        drive.setPower(
                 power * (target - drive.heading() < 0 ? 1 : -1),
                power * (target - drive.heading() > 0 ? 1 : -1));
    }

    @Override
    public void stop() {
        drive.setPower(0,0);
    }

    @Override
    public boolean isCompleted() {

        return Range.clip(drive.heading(),target+1,target-1) == target || timer.seconds() > timeout;
    }
}
