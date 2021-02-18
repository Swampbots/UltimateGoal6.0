package org.firstinspires.ftc.teamcode.testing;

import com.disnodeteam.dogecommander.Command;

import org.opencv.ml.DTrees;

import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class TestTelemetryCommand implements Command {
    private TestTelemetrySubsystem sub;

    private int level;
    private boolean clearOnUpdate = false;
    private List<TelemetryEntry> data;
    private String sign;


    protected enum TYPE {
        LINE, DATA, ACTION, DATA_LIST, IGNORE;

        public String getType() {
            return this.toString();
        }
    }

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

    /**
     *
     * @deprecated
     * @param str
     * @param format
     * @param objs
     */
    private void parseLine(String str, Format format, Object... objs) {
        List<Object> line = new ArrayList<Object>();
        line.add(str);
        line.add(format);
        line.add(objs);

        //data.add(line);
    }

    // Add Line
    public TestTelemetryCommand addLine(String str) {
        data.add(new TelemetryEntry(TYPE.LINE, str, null, null));

        return this;
    }

    public TestTelemetryCommand addLine() {
        return addLine("");
    }

    // Add Data (singular)
    public TestTelemetryCommand addData(String str, Object obj) {
        data.add(new TelemetryEntry(TYPE.DATA, str, null, obj));

        return this;
    }

    public TestTelemetryCommand addData(Object obj) {
        return addData(null, obj);
    }

    // Add Data (multiple)
    public TestTelemetryCommand addData(String str, String format, Object... obj) {
        data.add(new TelemetryEntry(TYPE.DATA_LIST, str, format, obj));

        return this;
    }

    public TestTelemetryCommand addData(String format, Object... obj) {
        return addData(null, format, obj);
    }


    // Add Action
    public TestTelemetryCommand addAction(Object obj) {
        data.add(new TelemetryEntry(TYPE.ACTION, "", null, obj));

        return this;
    }

    /**
     * Remove an item from the Telemetry List
     *
     * @param index the element to remove
     * @return this
     */
    public TestTelemetryCommand clear(int index) {
        try {
            data.remove(index);
        } catch (IndexOutOfBoundsException e) {
        }

        return this;
    }

    public TestTelemetryCommand clear() {
        return clear(0);
    }

    public TestTelemetryCommand clearAll() {
        data.clear();

        return this;
    }

    public TestTelemetryCommand sign(String sign) {
        data.add(new TelemetryEntry(TYPE.IGNORE, sign, null, null));

        return this;
    }

    public TestTelemetryCommand addSection(int level) {
        this.level = level;

        return this;
    }

    public TestTelemetryCommand clearOnUpdate(boolean clear) {
        this.clearOnUpdate = clear;

        return this;
    }

    public void push() {
        sub.addSection(data, level);
    }

    public TestTelemetryCommand get(String sign) {
        HashMap<Integer, List<TelemetryEntry>> full = sub.getEntry(sign);

        this.level = (int) full.entrySet().toArray()[0];
        this.data = full.get(level);

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
