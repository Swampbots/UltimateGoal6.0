package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;

import org.firstinspires.ftc.teamcode.robot.subsystems.Grip;

public class GripSetState implements Command {
    private Grip grip;

    private double state;

    public GripSetState(Grip grip, double state){
        this.grip = grip;
        this.state = state;
    }

    public GripSetState(Grip grip, boolean toggle){
        this.grip = grip;
        this.state = -1;

        grip.togglePos();
    }

    @Override
    public void start() {
        if(state != -1) grip.setTargetPos(state);
    }

    @Override
    public void periodic() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isCompleted() {
        return grip.getCurrentPos() == grip.getTargetPos();
    }
}
