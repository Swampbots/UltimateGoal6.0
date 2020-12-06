package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;

public class ArmByEncoder implements Command {
    private Arm arm;

    private ElapsedTime timer;
    private double power;
    private int counts;
    private double timeout;

    private final double DEFAULT_TIMEOUT = 5.0;
    private DcMotor.RunMode prevRunMode;

    public ArmByEncoder(Arm arm, int counts, double target, double power, double timeout){
        timer = new ElapsedTime();

        this.arm = arm;
        this.counts = counts;
        this.power = power;
        this.timeout = timeout;
    }

    public ArmByEncoder(Arm arm, int counts, double target, double power){
        timer = new ElapsedTime();

        this.arm = arm;
        this.counts = counts;
        this.power = power;
        this.timeout = DEFAULT_TIMEOUT;
    }

    @Override
    public void start() {
        timer.reset();

        int currentPos = arm.getTargetPos();

    }

    @Override
    public void periodic() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
