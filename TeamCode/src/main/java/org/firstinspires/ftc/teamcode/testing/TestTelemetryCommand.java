package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.Command;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class TestTelemetryCommand implements Command {
    private TestTelemetrySubsystem sub;

    private List<HashMap<String, Object>> list;
    private List<List<HashMap<String,Object>>> allLines;
    private List<HashMap<String, Object>> nextLines;



    public TestTelemetryCommand(TestTelemetrySubsystem telemetry) {
        this.sub = telemetry;
    }

    @Override
    public void start() {
        sub.telemetry.addLine("Ready!");
    }

    @Override
    public void periodic() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isCompleted() {
        return false;
    }

    public TestTelemetryCommand addLine() {
        return addLine("");
    }

    public TestTelemetryCommand addLine(String str) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(str, null);
         nextLines.add(map);
        return this;
    }

    public void put() {
        allLines.add(nextLines);
    }


    public TestTelemetryCommand addSection(int level) {
        Hashtable<TestTelemetryCommand, Integer> table = new Hashtable<>();
        table.put(this, level);

        return this;
    }
}


/*

(Telemetry Module).
AddSection(priority level).     This defines where the telemetry will appear on the phone
AddLine("abc").
AddData("w",3).
push()




 */
