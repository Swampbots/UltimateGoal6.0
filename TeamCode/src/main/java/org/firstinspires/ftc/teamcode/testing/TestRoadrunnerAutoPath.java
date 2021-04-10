package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint;
import com.disnodeteam.dogecommander.DogeCommander;
import com.disnodeteam.dogecommander.DogeOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.robot.commands.auto.ArmByEncoder;
import org.firstinspires.ftc.teamcode.robot.commands.auto.GripSetState;
import org.firstinspires.ftc.teamcode.robot.commands.auto.KickerSetState;
import org.firstinspires.ftc.teamcode.robot.commands.auto.RunShooterForTime;
import org.firstinspires.ftc.teamcode.robot.subsystems.Arm;
import org.firstinspires.ftc.teamcode.robot.subsystems.Grip;
import org.firstinspires.ftc.teamcode.robot.subsystems.Intake;
import org.firstinspires.ftc.teamcode.robot.subsystems.Kicker;
import org.firstinspires.ftc.teamcode.robot.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.robot.subsystems.Transfer;

import java.util.Arrays;

@Autonomous(name = "AAATest Roadrunner Path", group = "testing")
public class TestRoadrunnerAutoPath extends LinearOpMode implements DogeOpMode {
    private DogeCommander commander = new DogeCommander(this);

    private Kicker kicker;
    private Shooter shooter;
    private Arm arm;
    private Grip grip;
    private Intake intake;
    private Transfer transfer;

    private double shootingTurn = 3.0;  // Degrees

    @Override
    public void runOpMode() throws InterruptedException {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);

        kicker           = new Kicker(hardwareMap);
        shooter         = new Shooter(hardwareMap);
        arm                 = new Arm(hardwareMap);
        grip               = new Grip(hardwareMap);
        intake           = new Intake(hardwareMap);
        transfer       = new Transfer(hardwareMap);

        commander.registerSubsystem(kicker);
        commander.registerSubsystem(shooter);
        commander.registerSubsystem(arm);
        commander.registerSubsystem(grip);
        commander.registerSubsystem(intake);
        commander.registerSubsystem(transfer);

        Pose2d startPose = new Pose2d(-64,-50, 0.0);

        drive.setPoseEstimate(startPose);

        Trajectory commonPath = drive.trajectoryBuilder(startPose)
                .addTemporalMarker(0.3, ()->{
                    // Lock Arm in place
                    arm.setPower(1);
                    arm.setTargetPos(Arm.TARGETS.UP.getTarget());
                    arm.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);

                    grip.close();
                    kicker.out();
                    commander.runCommand(new RunShooterForTime(shooter, false, 0.7));
                })
                .splineToLinearHeading(new Pose2d(-2,-53, Math.toRadians(40.0)), 0)

                .build();

        Trajectory fourRingsDrop1 = drive.trajectoryBuilder(commonPath.end().plus(new Pose2d(0,0, Math.toRadians(2 * shootingTurn))))
                .splineToSplineHeading(new Pose2d(48, -58, 0), Math.toRadians(-50))
                .build();

        Trajectory fourRingsStrafeOffWall = drive.trajectoryBuilder(fourRingsDrop1.end())
                .strafeTo(new Vector2d(46,-53))
                .build();

        Trajectory fourRingsGet2 = drive.trajectoryBuilder(fourRingsStrafeOffWall.end().plus(new Pose2d(0,0, Math.toRadians(160))))
                .splineToSplineHeading(new Pose2d(0, -12, Math.toRadians(11.5-180)), Math.toRadians(-160))
                .addDisplacementMarker((dist)->0.2*dist, ()->{
                    grip.open();
                    commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.DOWN.getTarget(), 0.4, 4));
                })
//                .addDisplacementMarker(20, ()->{
//                    commander.runCommandsParallel(
//                            new ArmByEncoder(arm, Arm.TARGETS.DOWN.getTarget(), 0.4, 4, telemetry),
//                            new GripSetState(grip, Grip.TARGETS.OPEN.getTarget())
//                    );
//                })
                .lineTo(new Vector2d(-35.4641,-20.0924))

//                .addDisplacementMarker(()->{
//                    commander.runCommand(new GripSetState(grip, Grip.TARGETS.CLOSE.getTarget()));
//                    sleep(500);
//
//                    commander.runCommandsParallel(new ArmByEncoder(arm, Arm.TARGETS.UP.getTarget(), 1.0, 2));
//                })
            .build();

        telemetry.addLine("Ready!");
        telemetry.update();

//        TelemetryPacket telemetryPacket = new TelemetryPacket();
//        telemetryPacket.addLine("Ready");
//        FtcDashboard.getInstance().sendTelemetryPacket(telemetryPacket);

        commander.init();
        waitForStart();

        if (isStopRequested()) return;



        drive.followTrajectory(commonPath);

        shoot();

        drive.turn(Math.toRadians(shootingTurn));

        shoot();

        drive.turn(Math.toRadians(shootingTurn));

       shoot();

       commander.runCommand(new RunShooterForTime(shooter, 0,0));

        drive.followTrajectory(fourRingsDrop1);

        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.DOWN.getTarget(), 1.0, 0.5));  // Bring wobble arm up
        sleep(1000);

        // Open grip to drop Wobble Goal
        commander.runCommand(new GripSetState(grip,Grip.TARGETS.OPEN.getTarget()));
        sleep(500);

        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.UP.getTarget(), 1.0, 0.5));  // Bring wobble arm up
        sleep(500);


        sleep(200);
        drive.followTrajectory(fourRingsStrafeOffWall);    // Strafe off wall
        drive.turn(Math.toRadians(160));

        drive.followTrajectory(fourRingsGet2);

        // Open grip to drop Wobble Goal
        commander.runCommand(new GripSetState(grip,Grip.TARGETS.CLOSE.getTarget()));
        sleep(500);

        commander.runCommand(new ArmByEncoder(arm, Arm.TARGETS.UP.getTarget(), 1.0, 0.5));  // Bring wobble arm up
        sleep(500);

        commander.stop();


    }

    private void shoot(){
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.IN.getTarget()));
        sleep(1250);
        commander.runCommand(new KickerSetState(kicker, Kicker.TARGETS.OUT.getTarget()));
        sleep(250);
    }
}
