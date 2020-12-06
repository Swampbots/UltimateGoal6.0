package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByTimer;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;


@Autonomous
public class TestDogeAuto extends LinearOpMode implements DogeOpMode {
    public void runOpMode(){
        DogeCommander commander = new DogeCommander(this);

        Drive drive             = new Drive(hardwareMap, true);

        commander.registerSubsystem(drive);

        commander.init();

        waitForStart();

        commander.runCommandsParallel(
                new DriveByTimer(drive, 1, 0.3)

        );




        commander.stop();
    }



}
