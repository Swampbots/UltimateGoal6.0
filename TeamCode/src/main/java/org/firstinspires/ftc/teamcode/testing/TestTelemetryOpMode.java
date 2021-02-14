package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.commands.teleop.SoloTeleOpArmControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.SoloTeleOpDriveControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.SoloTeleOpGripControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.SoloTeleOpIntakeControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.SoloTeleOpKickerControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.SoloTeleOpShooterControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.SoloTeleOpTransferControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpArmControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpDriveControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpGripControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpIntakeControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpKickerControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpShooterControl;
import org.firstinspires.ftc.teamcode.robot.commands.teleop.TeleOpTransferControl;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;
import org.firstinspires.ftc.teamcode.robot.subsystems.Grip;
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.robot.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystems.Transfer;

@TeleOp(name = "Test Telemetry", group = "testing")
public class TestTelemetryOpMode extends LinearOpMode implements DogeOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);

        Drive drive             = new Drive(hardwareMap,true);
//        Kicker kicker           = new Kicker(hardwareMap);
//        Shooter shooter         = new Shooter(hardwareMap);
//        Arm arm                 = new Arm(hardwareMap);
//        Grip grip               = new Grip(hardwareMap);
//        Intake intake           = new Intake(hardwareMap);
//        Transfer transfer       = new Transfer(hardwareMap);
        TestTelemetrySubsystem teleSub = new TestTelemetrySubsystem(telemetry);

        commander.registerSubsystem(drive);
//        commander.registerSubsystem(kicker);
//        commander.registerSubsystem(shooter);
//        commander.registerSubsystem(arm);
//        commander.registerSubsystem(grip);
//        commander.registerSubsystem(intake);
//        commander.registerSubsystem(transfer);
        commander.registerSubsystem(teleSub);

        commander.init();

//        kicker.kicker.setPosition(Kicker.TARGETS.OUT.getTarget());
        waitForStart();

        commander.runCommandsParallel(
                new TeleOpDriveControl(drive,gamepad1, telemetry),
//                new TeleOpKickerControl(kicker,gamepad1),
//                new TeleOpShooterControl(shooter,gamepad2),
//                new TeleOpArmControl(arm,gamepad1),
//                new TeleOpGripControl(grip,gamepad1),
//                new TeleOpIntakeControl(intake,gamepad2),
//                new TeleOpTransferControl(transfer,gamepad2),
                new TestTelemetryCommand(teleSub)
        );


        commander.stop();
    }
}
