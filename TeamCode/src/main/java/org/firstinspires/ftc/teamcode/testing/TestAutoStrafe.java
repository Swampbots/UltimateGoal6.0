package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robot.commands.auto.StrafeByTimer;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Grip;
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.robot.subsystems.Transfer;

@Autonomous
public class TestAutoStrafe extends LinearOpMode implements DogeOpMode {
    //private DcMotor shooterMotor;
    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        Drive drive = new Drive(hardwareMap, true);
        //Kicker kicker = new Kicker(hardwareMap);
        //Shooter shooter         = new Shooter(hardwareMap);
        //Arm arm = new Arm(hardwareMap);
        //Grip grip = new Grip(hardwareMap);
        //Intake intake = new Intake(hardwareMap);
        //Transfer transfer = new Transfer(hardwareMap);

        commander.registerSubsystem(drive);
        //commander.registerSubsystem(kicker);
        //commander.registerSubsystem(shooter);
        //commander.registerSubsystem(arm);
        //commander.registerSubsystem(grip);
        //commander.registerSubsystem(intake);
        //commander.registerSubsystem(transfer);

        //shooterMotor = hardwareMap.get(DcMotor.class, "shooter");

        commander.init();

        waitForStart();

        commander.runCommand(new StrafeByTimer(drive,1,0.5));
    }
}
