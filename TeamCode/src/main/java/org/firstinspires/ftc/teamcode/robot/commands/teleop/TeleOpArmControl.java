package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;

import static org.firstinspires.ftc.teamcode.CommandDrive.TRIGGER_THRESHOLD;

public class TeleOpArmControl implements Command {
    private Gamepad gamepad;

    private Arm wobble;

    private final double POWER_SCALAR = 0.8;

    public TeleOpArmControl(Arm wobble, Gamepad gamepad){
        this.gamepad = gamepad;

        this.wobble = wobble;
    }

    @Override
    public void start() {
        wobble.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        wobble.setPower(0);
    }

    @Override
    public void periodic() {
        // In (encoder): RT     Out (encoder): LT   Up (power): dUp     Down (power): dDown
        if(gamepad.left_trigger > TRIGGER_THRESHOLD || gamepad.right_trigger > TRIGGER_THRESHOLD) {
            wobble.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
            wobble.setPower(POWER_SCALAR);

            if(gamepad.left_trigger > TRIGGER_THRESHOLD)        wobble.setTargetPos(Arm.TARGETS.OUT.getTarget());
            else if(gamepad.right_trigger > TRIGGER_THRESHOLD)  wobble.setTargetPos(Arm.TARGETS.UP.getTarget());
        }

        if(gamepad.dpad_up || gamepad.dpad_down) {
            wobble.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            if(gamepad.dpad_up)         wobble.setPower(POWER_SCALAR);
            else if(gamepad.dpad_down)  wobble.setPower(-POWER_SCALAR);
            }
    }

    @Override
    public void stop() {
        wobble.setPower(0);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
