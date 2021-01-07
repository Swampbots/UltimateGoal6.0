package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.robot.subsystems.Shooter;


public class TeleOpShooterControl implements Command {
    private Shooter shooter;
    private Gamepad gamepad;

    private boolean revCheck;



    public TeleOpShooterControl(Shooter shooter, Gamepad gamepad){
        this.shooter = shooter;
        this.gamepad = gamepad;
    }

    @Override
    public void start() {
        shooter.setPower(0);
        shooter.setShoot(false);
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void periodic() {
        if(gamepad.dpad_down){
            shooter.setPower(Shooter.POWER_LEVELS.SHORT.getPower());
        }
        if(gamepad.dpad_up){
            shooter.setPower(Shooter.POWER_LEVELS.FAR.getPower());
        }
//        if(gamepad.dpad_left){
//            shooter.setPower(Shooter.POWER_LEVELS.MEDIUM.getPower());
//        }
//        if(gamepad.dpad_right){
//            shooter.setPower(Shooter.POWER_LEVELS.ADJ.getPower());
//        }

//        if(gamepad.right_bumper && gamepad.left_bumper){
//            if(revCheck){
//                shooter.reverse();
//                revCheck = false;
//            }
//        } else {
//            revCheck = true;
            if(gamepad.right_bumper) shooter.setShoot(false);
            else if(gamepad.left_bumper) shooter.setShoot(true);
//
//
//        }

    }

    @Override
    public void stop() {
        shooter.setPower(0);
        shooter.setShoot(false);
        shooter.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
