package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.commands.auto.ArmByEncoder;
import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByEncoder;
import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByTimer;
import org.firstinspires.ftc.teamcode.robot.commands.auto.GripSetState;
import org.firstinspires.ftc.teamcode.robot.commands.auto.KickerSetState;
import org.firstinspires.ftc.teamcode.robot.commands.auto.RunShooterForTime;
import org.firstinspires.ftc.teamcode.robot.commands.auto.StrafeByEncoder;
import org.firstinspires.ftc.teamcode.robot.commands.auto.TurnByGyro;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Grip;
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.robot.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystems.Transfer;

@Autonomous(name = "Command Auto", group = "Finalized")
public class CommandAuto extends LinearOpMode implements DogeOpMode {
    private boolean PS = false;
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
            if(gamepad1.a){
                PS = true;
            }
            if(gamepad1.b){
                PS = false;
            }
            //TODO: Camera implementation
            telemetry.addLine("Ready!");
            telemetry.addData("Power Shots?",PS);
            telemetry.addLine("Camera in progress...");
            telemetry.update();
        }

        commander.runCommandsParallel(
                new RunShooterForTime(shooter,-1,Drive.POWER_LEVELS.MEDIUM.getPower()),                              // Turn on shooter
                new DriveByEncoder(drive,InchToCount(55),0,.3,10),                 // Drive to line
                new ArmByEncoder(arm,Arm.TARGETS.UP.getTarget(),0,.3,2),    // Bring wobble arm up
                new GripSetState(grip,Grip.TARGETS.CLOSE.getTarget()));

        sleep(1000);

        if(PS){
            commander.runCommand(new StrafeByEncoder(drive,InchToCount(-5),0,.3,3));
        }

        //Shot 1
        commander.runCommand(new KickerSetState(kicker,true,1));
        sleep(500);
        commander.runCommand(new KickerSetState(kicker,true,1));

        if(PS){
            commander.runCommand(new StrafeByEncoder(drive,InchToCount(-5),0,.3,3));
        }

        //Shot 2
        sleep(500);
        commander.runCommand(new KickerSetState(kicker,true,1));
        sleep(500);
        commander.runCommand(new KickerSetState(kicker,true,1));

        if(PS){
            commander.runCommand(new StrafeByEncoder(drive,InchToCount(-5),0,.3,3));
        }

        //Shot 3
        sleep(500);
        commander.runCommand(new KickerSetState(kicker,true,1));

        if(PS){
            commander.runCommand(new StrafeByEncoder(drive,InchToCount(15),0,.3,3));
        }

        commander.runCommandsParallel(
                new RunShooterForTime(shooter,0,.3), // Turn off shooter
                new TurnByGyro(drive,30,.3,2) // Turn to face drop zone
                );
        sleep(3000);

        // Drive in front of drop zone
        commander.runCommand(new DriveByEncoder(drive,InchToCount(10),0,0.3,5));
        sleep(1000);

        // Put arm out
        commander.runCommand(new ArmByEncoder(arm,Arm.TARGETS.OUT.getTarget(),0,.3,2));
        sleep(200);

        // Open grip to drop Wobble Goal
        commander.runCommand(new GripSetState(grip,true));
        sleep(300);

        // Drive back to line
        commander.runCommand(new DriveByTimer(drive,0.5,-.7));

        commander.stop();
    }

    private int InchToCount(double inch){return (int)(inch*Drive.COUNTS_PER_INCH_EMPIRICAL);}
}
