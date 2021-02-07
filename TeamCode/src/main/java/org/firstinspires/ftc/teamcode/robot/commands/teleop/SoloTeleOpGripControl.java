package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.robot.subsystems.Grip;

public class SoloTeleOpGripControl implements Command {
    private TeleOpGripControl gripControl;

    public SoloTeleOpGripControl(Grip grip, Gamepad gamepad) {
        gripControl = new TeleOpGripControl(grip, gamepad);
    }

    @Override
    public void start() {
        gripControl.start();
    }

    @Override
    public void periodic() {
        gripControl.periodic();
    }

    @Override
    public void stop() {
        gripControl.stop();
    }

    @Override
    public boolean isCompleted() {
        return gripControl.isCompleted();
    }
}
