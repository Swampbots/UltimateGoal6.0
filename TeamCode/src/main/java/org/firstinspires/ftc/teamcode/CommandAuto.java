package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByTimer;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Grip;
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.robot.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystems.Transfer;

@Autonomous(name = "Command Auto", group = "Finalized")
public class CommandAuto extends LinearOpMode implements DogeOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        Drive drive             = new Drive(hardwareMap);
        Kicker kicker           = new Kicker(hardwareMap);
        Shooter shooter         = new Shooter(hardwareMap);
        Arm arm                 = new Arm(hardwareMap);
        Grip grip               = new Grip(hardwareMap);
        Intake intake           = new Intake(hardwareMap);
        Transfer transfer       = new Transfer(hardwareMap);

        commander.registerSubsystem(drive);
        commander.registerSubsystem(kicker);
        commander.registerSubsystem(shooter);
        commander.registerSubsystem(arm);
        commander.registerSubsystem(grip);
        commander.registerSubsystem(intake);
        commander.registerSubsystem(transfer);

        commander.init();

        // Run after init but before start
        while (!isStarted()){
            //TODO: Camera implementation
            telemetry.addLine("Camera in progress");
            telemetry.update();
        }
        

        commander.runCommand(new DriveByTimer(drive,3,0.3));

        commander.stop();
    }
}
