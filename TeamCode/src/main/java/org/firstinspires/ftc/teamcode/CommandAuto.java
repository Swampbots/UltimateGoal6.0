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

        waitForStart();


        // Drive to shooting position
        commander.runCommandsParallel(
                new RunShooterForTime(shooter,false, 0.75),
                new DriveByTimer(drive,3.75,-0.3)
                );

        sleep(2000);

        telemetry.addLine("Shot 1");
        telemetry.update();
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.OUT.getTarget(),2.0));
        sleep(500);
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.IN.getTarget(),2.0));
        sleep(500);


        telemetry.addLine("Shot 2");
        telemetry.update();
        sleep(1500);
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.OUT.getTarget(),2.0));
        sleep(500);
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.IN.getTarget(),2.0));


        telemetry.addLine("Shot 3");
        telemetry.update();
        sleep(1500);
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.OUT.getTarget()));
        sleep(500);
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.IN.getTarget(),2.0));
        commander.runCommand(new RunShooterForTime(shooter, true, 0.0)); // Power down shooter



        telemetry.addLine("Wobble Goal");
        telemetry.update();
        sleep(3000);
        commander.runCommand(new TurnByGyro(drive,-135,0.5,4, telemetry));
        sleep(500);
        // Put arm out
        commander.runCommand(new ArmByTimer(arm,1,-.3));  // Bring wobble arm up)
        sleep(2000);

        // Open grip to drop Wobble Goal
        commander.runCommand(new GripSetState(grip,Grip.TARGETS.OPEN.getTarget()));
        sleep(300);

        // Drive back to line
        commander.runCommand(new DriveByTimer(drive, 0.5,0.3));

        commander.stop();
    }

    private int InchToCount(double inch){return (int)(inch*Drive.COUNTS_PER_INCH_EMPIRICAL);}
}
