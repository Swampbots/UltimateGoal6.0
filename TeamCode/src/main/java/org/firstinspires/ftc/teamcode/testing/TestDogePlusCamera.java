package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.commands.auto.CameraControlAuto;
import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByTimer;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpArmControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpDriveControl;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Camera;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;

@Autonomous(name = "Test Doge+Cam",group = "testing")
public class TestDogePlusCamera extends LinearOpMode implements DogeOpMode {
    private CameraControlAuto cam;
    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        Drive drive             = new Drive(hardwareMap);


        commander.registerSubsystem(drive);

        cam = new CameraControlAuto(new Camera(hardwareMap), gamepad1, gamepad2, telemetry);

        while (!opModeIsActive()) {
            cam.periodic();
        }

        commander.init();
        waitForStart();

        commander.runCommandsParallel(
                new DriveByTimer(drive, 2, 0.3)
        );


        commander.stop();
    }
}
