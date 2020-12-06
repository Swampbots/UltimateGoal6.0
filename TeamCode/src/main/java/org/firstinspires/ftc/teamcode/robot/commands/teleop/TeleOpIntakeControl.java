package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;

public class TeleOpIntakeControl implements Command {
    private Gamepad gamepad;

    private Intake intake;


    public TeleOpIntakeControl(Intake intake, Gamepad gamepad) {
        this.intake = intake;
        this.gamepad = gamepad;
    }

    @Override
    public void start() {
        intake.setPower(0);
        intake.setReverse(false);
    }

    @Override
    public void periodic() {
        intake.setPower(gamepad.left_trigger > 0.3f ? 1 : 0);

        if(gamepad.left_bumper){
            intake.setReverse(false);
        }
        if(gamepad.right_bumper){
            intake.setReverse(true);
        }
    }

    @Override
    public void stop() {
        intake.setPower(0);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
