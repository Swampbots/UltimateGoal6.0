package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.robot.subsystems.Transfer;

public class TeleOpTransferControl implements Command {
    private Gamepad gamepad;
    private Transfer transfer;

    public TeleOpTransferControl(Transfer transfer, Gamepad gamepad){
        this.transfer = transfer;
        this.gamepad = gamepad;
    }

    @Override
    public void start() {
        transfer.setPower(0);
        transfer.setReverse(false);
    }

    @Override
    public void periodic() {
        transfer.setPower(gamepad.right_trigger > 0.3f ? 1 : gamepad.left_trigger > 0.3f ? -1 : 0);

        /*if(gamepad.left_bumper){
            transfer.setReverse(false);
        }
        if(gamepad.right_bumper){
            transfer.setReverse(true);
        }*/
    }

    @Override
    public void stop() {
        transfer.setPower(0);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
