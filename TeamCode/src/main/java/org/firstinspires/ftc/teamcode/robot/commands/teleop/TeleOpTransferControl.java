package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.robot.subsystems.Transfer;

import static org.firstinspires.ftc.teamcode.CommandDrive.TRIGGER_THRESHOLD;

public class TeleOpTransferControl implements Command {
    private Gamepad gamepad;
    private Transfer transfer;

    private final double POWER_SCALAR = 1.0;

    public TeleOpTransferControl(Transfer transfer, Gamepad gamepad){
        this.transfer = transfer;
        this.gamepad = gamepad;
    }

    @Override
    public void start() {
        transfer.setPower(0);
        transfer.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void periodic() {
        // In: LB, RT   Out: LT
        transfer.setPower(
                (gamepad.left_bumper || gamepad.right_trigger > TRIGGER_THRESHOLD ?
                        1 : gamepad.left_trigger > TRIGGER_THRESHOLD ?
                        -1 : 0
        ) * POWER_SCALAR);

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
