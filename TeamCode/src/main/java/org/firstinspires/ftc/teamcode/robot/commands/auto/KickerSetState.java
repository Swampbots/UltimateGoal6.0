package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;

import org.firstinspires.ftc.teamcode.robot.subsystems.Kicker;

public class KickerSetState implements Command {
    private Kicker kicker;

    private double state;

    public KickerSetState(Kicker kicker, double state){
        this.kicker = kicker;
        this.state = state;
    }

    public KickerSetState(Kicker kicker, boolean toggle){
        this.kicker = kicker;
        this.state = -1;

        kicker.togglePos();
    }

    @Override
    public void start() {
        if(state != -1) kicker.setTargetPos(state);
    }

    @Override
    public void periodic() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isCompleted() {
        return kicker.getTargetPos() == kicker.getCurrentPos();
    }
}
