package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.robot.subsystems.Grip;

public class TeleOpGripControl implements Command {
    private Grip grip;

    private Gamepad gamepad;

    public TeleOpGripControl(Grip grip, Gamepad gamepad){
        this.gamepad = gamepad;
        this.grip = grip;
    }

    @Override
    public void start() {
        grip.close();
    }

    @Override
    public void periodic() {
        if(gamepad.x){
            grip.open();
        } else {
            grip.close();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
