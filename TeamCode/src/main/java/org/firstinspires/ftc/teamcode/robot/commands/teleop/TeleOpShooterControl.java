package org.firstinspires.ftc.teamcode.robot.commands.teleop;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.robot.subsystems.Shooter;

import static org.firstinspires.ftc.teamcode.CommandDrive.ONE_PERSON_CONTROLS;


public class TeleOpShooterControl implements Command {
    private Shooter shooter;
    private Gamepad gamepad;

    private boolean shootToggleCheck = true;

    public TeleOpShooterControl(Shooter shooter, Gamepad gamepad){
        this.shooter = shooter;
        this.gamepad = gamepad;
    }

    @Override
    public void start() {
        shooter.setShoot(false);
        shooter.setPower(0.85);     // Default Shooter Power
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void periodic() {
        if(ONE_PERSON_CONTROLS) {
            if(gamepad.y && shootToggleCheck) {
                shooter.shoot();
                shootToggleCheck = false;
            } else if(!gamepad.a && !shootToggleCheck) {
                shootToggleCheck = true;
            }
            return;
        }
        // Far: dUp, Short: dLeft, Power Shot: dDown, Shoot: a (toggle)
        if(gamepad.dpad_up)     shooter.setPower(Shooter.POWER_LEVELS.FAR.getPower());
        if(gamepad.dpad_left)   shooter.setPower(Shooter.POWER_LEVELS.SHORT.getPower());
        if(gamepad.dpad_down)   shooter.setPower(Shooter.POWER_LEVELS.POWER_SHOT.getPower());

        if(gamepad.a && shootToggleCheck) {
            shooter.shoot();
            shootToggleCheck = false;
        } else if(!gamepad.a && !shootToggleCheck) {
            shootToggleCheck = true;
        }
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
