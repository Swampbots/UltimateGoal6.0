package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.Subsystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.jetbrains.annotations.NotNull;

import java.text.Format;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class TestTelemetrySubsystem implements Subsystem {
    protected Telemetry telemetry;

    private HashMap<Integer, List<TelemetryEntry>> data;

    public TestTelemetrySubsystem(Telemetry telemetry) {
        this.telemetry = telemetry;
        data = new HashMap<Integer, List<TelemetryEntry>>();
    }

    @Override
    public void initHardware() {
        addLine("Telemetry Ready!");
    }

    @Override
    public void periodic() {
        sortDataByKey();

        for(List<TelemetryEntry> section : data.values()) {
            for(TelemetryEntry entry : section) {
                switch (entry.getType()) {
                    case LINE:
                        addLine(entry.getString());
                        break;
                    case DATA:
                        addDataEntry(entry.getString(), entry.getData());
                        break;
                    case ACTION:
                        addAction(entry.getData());
                        break;
                    case DATA_LIST:
                        addDataList(entry.getString(), entry.getFormat(), entry.getData());
                        break;
                    default:

                        break;
                }
            }
            addLine(null);
        }

        telemetry.update();
    }

    public void addSection(List<TelemetryEntry> entries, int level) {
        data.put(level, entries);
    }

    private void addLine(String str) {
        if(str == null) telemetry.addLine();
        else telemetry.addLine(str);
    }

    private void addDataEntry(String str, @NotNull Object object) {
        if(str == null) telemetry.addData("", object);
        else telemetry.addData(str, object);
    }

    private void addDataList(String str, String format, @NotNull Object... object) {
        if(str == null) telemetry.addData("", String.valueOf(format), object);
        else telemetry.addData(str, String.valueOf(format), object);

    }

    private void addAction(@NotNull Object action) {
        try {
            telemetry.addAction((Runnable) action);
        } catch (Exception e) {
            telemetry.addData("Error when trying to add action", (String) action, e);
        }
    }

    /**
     * Sort Data by Key value (level)
     */
    private void sortDataByKey(){
        HashMap<Integer, List<TelemetryEntry>> sorted = new HashMap<>();

        ArrayList<Integer> sortedKeys = new ArrayList<Integer>(data.keySet());

        Collections.sort(sortedKeys);

        for(Integer x : sortedKeys) {
            sorted.put(x, data.get(x));
        }

        data = sorted;
    }

    public HashMap<Integer, List<TelemetryEntry>> getEntry(String sign) {
        HashMap<Integer, List<TelemetryEntry>> out = new HashMap<>();
        int i = 0;
        for(List<TelemetryEntry> entry : data.values()) {
            if(entry.get(0).getType() == TestTelemetryCommand.TYPE.IGNORE && entry.get(0).getString() == sign) {
                Object key = data.keySet().toArray()[i];
                out.put((Integer) key, data.get(key));
                break;
            }
            i++;
        }

        return out;
    }

}