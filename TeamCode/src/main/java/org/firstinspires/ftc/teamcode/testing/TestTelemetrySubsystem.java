package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.Subsystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;
import java.util.List;

public class TestTelemetrySubsystem implements Subsystem {
    protected Telemetry telemetry;

    //TODO: Get this to work
//    private List<LinkedList<<String, Object>>> display;

    public TestTelemetrySubsystem(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public void initHardware() {

    }

    @Override
    public void periodic() {

//        for(LinkedList<HashMap<String, Object>> l : display) {
//            for(HashMap<String, Object> e: l) {
//                if(e.containsValue(null) && e.containsKey("")) {
//                    telemetry.addLine();
//                } else if(e.containsValue(null)) {
//                }
//            }
//
//        }

        telemetry.update();
    }

    public void addSection(List<HashMap<String, Object>> section, int level) {
        for (HashMap<String, Object> e : section) {
//            display.get(level).add(e);
        }
    }
}