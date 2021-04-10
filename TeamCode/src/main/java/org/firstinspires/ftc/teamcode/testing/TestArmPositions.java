package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
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

import static org.firstinspires.ftc.teamcode.CommandDrive.ONE_PERSON_CONTROLS;

@Disabled
@TeleOp(name = "Test Arm Pos", group = "testing")
public class TestArmPositions extends LinearOpMode implements DogeOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DogeCommander commander = new DogeCommander(this);


        Arm arm                 = new Arm(hardwareMap);
        Grip grip               = new Grip(hardwareMap);

        commander.registerSubsystem(arm);
        commander.registerSubsystem(grip);

        commander.init();

        waitForStart();


        commander.runCommandsParallel(
                new TestArmPositionsCommand(arm,gamepad2, telemetry),
                new SoloTeleOpGripControl(grip,gamepad1)
        );



        commander.stop();
    }
}
