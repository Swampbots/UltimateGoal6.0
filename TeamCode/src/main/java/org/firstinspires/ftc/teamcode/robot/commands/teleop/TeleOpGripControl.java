package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.robot.subsystems.Grip;

public class TeleOpGripControl implements Command {
    private Grip grip;
    private Gamepad gamepad;

    private boolean gripToggleCheck;

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
        if(gamepad.a && gripToggleCheck) {
            grip.togglePos();
            gripToggleCheck = false;
        } else if(!gamepad.a && !gripToggleCheck){
            gripToggleCheck = true;
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
