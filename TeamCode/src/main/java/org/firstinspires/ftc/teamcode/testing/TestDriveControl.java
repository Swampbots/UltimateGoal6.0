package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.subsystems.Drive;

public class TestDriveControl implements Command {
    // Subsystem
    private Drive drive;
    // Input
    private Gamepad gamepad;

    private TestTelemetryCommand telemetry;

    private final double TRIGGER_THRESHOLD = 0.7;

    // Constructor
    public TestDriveControl(Drive drive, Gamepad gamepad, TestTelemetryCommand telemetry) {
        this.drive = drive;
        this.gamepad = gamepad;
        this.telemetry = telemetry;
    }
    public TestDriveControl(Drive drive, Gamepad gamepad) {
        this(drive ,gamepad, null);
    }

    @Override
    public void start() {
        drive.setPower(0,0);
    }

    @Override
    public void periodic() {
        double dri = -gamepad.left_stick_y;
        double str = gamepad.left_stick_x;
        double twi = gamepad.right_stick_x;
        boolean goSlow = gamepad.left_bumper;

        drive.setMecanumPower(dri, str, twi, goSlow);

        if(telemetry != null) {
            telemetry.addSection(0).addLine("Test").addLine();
//            telemetry.addData("dri", dri);
//            telemetry.addData("str", str);
//            telemetry.addData("twi", twi);
//            telemetry.addLine();
//            telemetry.addData("FL Target", drive.getCurrentPositions()[0]);
//            telemetry.addData("FR Target", drive.getCurrentPositions()[1]);
//            telemetry.addData("RL Target", drive.getCurrentPositions()[2]);
//            telemetry.addData("RR Target", drive.getCurrentPositions()[3]);
//            telemetry.update();

        }

    }

    @Override
    public void stop() {
        drive.setPower(0,0);
    }

    @Override
    public boolean isCompleted(){
        return false;
    }


















}
