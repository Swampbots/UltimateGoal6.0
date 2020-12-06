package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
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
        shooter.reversePower(false);
    }

    @Override
    public void periodic() {
        if(gamepad.dpad_down){
            shooter.setPower(0);
        }
        if(gamepad.dpad_up){
            shooter.setPower(.4);
        }
        if(gamepad.dpad_left){
            shooter.setPower(0.3);
        }
        if(gamepad.dpad_right){
            shooter.setPower(0.35);
        }

        if(gamepad.right_bumper && gamepad.left_bumper){
            if(revCheck){
                shooter.reverse();
                revCheck = false;
            }
        } else {
            revCheck = true;
            if(gamepad.right_bumper) shooter.setShoot(false);
            if(gamepad.left_bumper) shooter.setShoot(true);


        }

    }

    @Override
    public void stop() {
        shooter.setPower(0);
        shooter.setShoot(false);
        shooter.reversePower(false);
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
