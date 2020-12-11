package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.subsystems.Kicker;

public class KickerSetState implements Command {
    private Kicker kicker;

    private double state;
    private double timeout;
    private ElapsedTime timer;

    public KickerSetState(Kicker kicker, double state, double timeout){
        this.kicker = kicker;
        this.state = state;
        this.timeout = timeout;
    }

    public KickerSetState(Kicker kicker, boolean toggle, double timeout){
        this.kicker = kicker;
        this.state = -1;
        this.timeout = timeout;

        kicker.togglePos();
    }

    @Override
    public void start() {
        timer.reset();
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
        return kicker.getTargetPos() == kicker.getCurrentPos() || timer.seconds() > timeout;
    }
}
