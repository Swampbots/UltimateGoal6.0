package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;

public class TeleOpArmControl implements Command {
    private Gamepad gamepad;

    private Arm wobble;

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
//        if(gamepad.dpad_up){
//            wobble.setTargetPos(Arm.TARGETS.UP.getTarget());
//        }
//        if(gamepad.dpad_down){
//            wobble.setTargetPos(Arm.TARGETS.DOWN.getTarget());
//        }
//        if(gamepad.dpad_left){
//            wobble.setTargetPos(Arm.TARGETS.OUT.getTarget());
//        }
//        if(gamepad.dpad_right){
//            wobble.resetTargetPos();
//        }
//
//
//        if(gamepad.a){
//            wobble.setPower(.2);
//        } else {
//            wobble.setPower(0);
//        }
        double powerScalar = 0.8;
        double wobblePower = (gamepad.left_trigger - gamepad.right_trigger);
        wobble.setPower(powerScalar * wobblePower);


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
