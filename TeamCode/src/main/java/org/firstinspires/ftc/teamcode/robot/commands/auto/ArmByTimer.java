package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;

public class ArmByTimer implements Command {
    private Arm arm;

    private ElapsedTime timer;
    private double seconds;
    private double power;

    public ArmByTimer(Arm arm, double seconds, double power){
        timer = new ElapsedTime();

        this.arm = arm;
        this.seconds = seconds;
        this.power = power;
    }

    @Override
    public void start(){
        timer.reset();
        arm.setPower(power);
    }

    @Override
    public void periodic() {

    }

    @Override
    public void stop(){
        arm.setPower(0);
    }

    @Override
    public boolean isCompleted() {
        return timer.seconds() > seconds;
    }
}
