package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.commands.auto.ArmByTimer;
import org.firstinspires.ftc.teamcode.robot.commands.auto.DriveByTimer;
import org.firstinspires.ftc.teamcode.robot.commands.auto.GripSetState;
import org.firstinspires.ftc.teamcode.robot.commands.auto.KickerSetState;
import org.firstinspires.ftc.teamcode.robot.commands.auto.RunShooterForTime;
import org.firstinspires.ftc.teamcode.robot.commands.auto.StrafeByTimer;
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
    private boolean POWER_SHOT = false;
    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        Drive drive             = new Drive(hardwareMap,true);
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
                POWER_SHOT = true;
            }
            if(gamepad1.b){
                POWER_SHOT = false;
            }
            //TODO: Camera implementation
            telemetry.addLine("Ready!");
            telemetry.addData("Power Shots?", POWER_SHOT);
            telemetry.addLine("Camera in progress...");
            telemetry.update();
        }

        commander.runCommand(new ArmByTimer(arm,1,.5));
        commander.runCommand(new GripSetState(grip,Grip.TARGETS.CLOSE.getTarget()));
        commander.runCommand(new ArmByTimer(arm,.8,.5));

        commander.runCommandsParallel(
                new RunShooterForTime(shooter,false, 0.65),                              // Turn on shooter
                new DriveByTimer(drive,3.75,-0.3)//,
                //new DriveByEncoder(drive,InchToCount(55),0,.3,10),                 // Drive to line
//                new ArmByTimer(arm,1,.3),    // Bring wobble arm up
//                new GripSetState(grip,Grip.TARGETS.CLOSE.getTarget())
                );

        sleep(2000);

        if(POWER_SHOT){
            commander.runCommand(new StrafeByTimer(drive,0.7,0.4));
        }

        telemetry.addLine("Shot 1");
        telemetry.update();

        //Shot 1

        commander.runCommand(new KickerSetState(kicker,0,2));
        sleep(500);
        commander.runCommand(new KickerSetState(kicker,1,2));
        sleep(500);
        if(POWER_SHOT) {
            commander.runCommand(new StrafeByTimer(drive,0.7,0.4));
        }

            telemetry.addLine("Shot 2");
        telemetry.update();

        //Shot 2
        sleep(500);
        commander.runCommand(new KickerSetState(kicker,0,2));
        sleep(500);
        commander.runCommand(new KickerSetState(kicker,1,2));

        if(POWER_SHOT){
            commander.runCommand(new StrafeByTimer(drive,0.7,0.4));
        }

        telemetry.addLine("Shot 3");
        telemetry.update();

        //Shot 3
        sleep(500);
        commander.runCommand(new KickerSetState(kicker,0,2));


        if(POWER_SHOT){
            commander.runCommandsParallel(
                    new StrafeByTimer(drive,2,-.4),//,
                    new RunShooterForTime(shooter,0,0));
        }



        telemetry.addLine("Go to Wobble Goal");
        telemetry.update();

        /*commander.runCommandsParallel(
                new RunShooterForTime(shooter,0,.3), // Turn off shooter
                new TurnByGyro(drive,30,.3,2) // Turn to face drop zone
                );*/

        sleep(3000);
//
//        // Drive in front of drop zone
//        //commander.runCommand(new DriveByEncoder(drive,InchToCount(10),0,0.3,5));
//        commander.runCommand(new DriveByTimer(drive,1,-0.4));
//        sleep(1000);

        commander.runCommand(new TurnByGyro(drive,180,0.4,5));
        sleep(500);
        
        // Put arm out
        commander.runCommand(new ArmByTimer(arm,1,-.3));
        sleep(2000);

        // Open grip to drop Wobble Goal
        commander.runCommand(new GripSetState(grip,true));
        sleep(300);
//
//        // Drive back to line
        //commander.runCommand(new DriveByTimer(drive,0.5,0.7));
//
        commander.stop();
    }

    private int InchToCount(double inch){return (int)(inch*Drive.COUNTS_PER_INCH_EMPIRICAL);}
}
