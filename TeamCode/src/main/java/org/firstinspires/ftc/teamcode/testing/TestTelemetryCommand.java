package org.firstinspires.ftc.teamcode.testing;

import android.os.SystemClock;

import com.disnodeteam.dogecommander.Command;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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
    private List<TelemetryEntry> data;
    private String sign;


    protected enum TYPE {
        LINE, DATA, ACTION, DATA_LIST, IGNORE;

        public String getType() {
            return this.toString();
        }
    }

    /**
     * Constructor for Command-Based Telemetry module
     *
     * @param telemetry The Telemetry Subsystem
     */
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
     * Adds a line of Telemetry
     *
     * @param lineCaption The text to display
     * @return Itself
     * @see org.firstinspires.ftc.robotcore.external.Telemetry#addLine(String lineCaption)
     */
    public TestTelemetryCommand addLine(String lineCaption) {
        data.add(new TelemetryEntry(TYPE.LINE, lineCaption, null, null));

        return this;
    }

    /**
     * Adds a blank line of Telemetry
     *
     * @return Itself
     * @see org.firstinspires.ftc.robotcore.external.Telemetry#addLine()
     */
    public TestTelemetryCommand addLine() {
        return addLine("");
    }

    /**
     * Adds a line of Telemetry with a singular Data entry
     *
     * @param caption The text to display
     * @param value The data to be displayed
     * @return Itself
     * @see org.firstinspires.ftc.robotcore.external.Telemetry#addData(String caption, Object value)
     */
    public TestTelemetryCommand addData(String caption, Object value) {
        data.add(new TelemetryEntry(TYPE.DATA, caption, null, value));

        return this;
    }

    /**
     * Adds a line of Telemetry with a singular Data entry
     *
     * @param value The data to be displayed
     * @return Itself
     * @see org.firstinspires.ftc.robotcore.external.Telemetry#addData(String caption, Object value)
     */
    public TestTelemetryCommand addData(Object value) {
        return addData(null, value);
    }

    /**
     * Adds a line of Telemetry with multiple Data entries
     *
     * @param caption The text to display
     * @param args The data to be displayed
     * @return Itself
     * @see org.firstinspires.ftc.robotcore.external.Telemetry#addData(String caption, String format, Object... args)
     */
    public TestTelemetryCommand addData(String caption, String format, Object... args) {
        data.add(new TelemetryEntry(TYPE.DATA_LIST, caption, format, args));

        return this;
    }

    /**
     * Adds a line of Telemetry with multiple Data entries
     *
     * @param args The data to be displayed
     * @return Itself
     * @see org.firstinspires.ftc.robotcore.external.Telemetry#addData(String caption, String format, Object... args)
     */
    public TestTelemetryCommand addData(String format, Object... args) {
        return addData(null, format, args);
    }


    /**
     * Adds a line of Telemetry through an Action
     *
     * @param action The Runnable Action to be displayed
     * @return Itself
     * @see org.firstinspires.ftc.robotcore.external.Telemetry#addAction(Runnable action)
     */
    public TestTelemetryCommand addAction(Runnable action) {
        data.add(new TelemetryEntry(TYPE.ACTION, "", null, action));

        return this;
    }

    /**
     * Remove an item from the Telemetry List
     *
     * @param index the element to remove
     * @return Itself
     * @see Telemetry#clear()
     * @see TestTelemetryCommand#clear()
     * @see TestTelemetryCommand#clearAll()
     */
    public TestTelemetryCommand clear(int index) {
        try {
            data.remove(index);
        } catch (IndexOutOfBoundsException e) {
        }

        return this;
    }

    /**
     * Removes the first item from the Telemetry List
     *
     * @return Itself
     * @see TestTelemetryCommand#clear(int index)
     * @see TestTelemetryCommand#clearAll()
     */
    public TestTelemetryCommand clear() {
        // Cycle thru until the Type is not Ignore
        int i = 0;
        while(data.get(i).getType() == TYPE.IGNORE) i++;

        return clear(i);
    }

    /**
     * Removes all items from the Telemetry List
     *
     * @return Itself
     * @see Telemetry#clearAll()
     * @see TestTelemetryCommand#clear()
     * @see TestTelemetryCommand#clear(int index)
     */
    public TestTelemetryCommand clearAll() {
        data.clear();

        return this;
    }

    /**
     * Signs this Telemetry Section. Signing a Section allows access of the Telemetry from
     * elsewhere. Starting a Sign with '_' will preserve it between updates
     *
     * @param sign The Signature of this Telemetry
     * @return Itself
     * @see TestTelemetryCommand#get(String sign)
     */
    public TestTelemetryCommand sign(String sign) {
        data.add(new TelemetryEntry(TYPE.IGNORE, sign, null, null));

        return this;
    }

    /**
     * Creates a new Section of Telemetry
     *
     * @param level The priority of this Section. Higher values have more priority
     * @return Itself
     */
    public TestTelemetryCommand addSection(int level) {
        this.level = level;

        return this;
    }

    /**
     * Push the section to Subsystem to be handed and displayed to the phone
     * @see TestTelemetrySubsystem
     */
    public void push() {
        // Ensure that all parameters are initialized when being passed in
        if((Object) level == null)  level = 0;
        if(sign == null)            sign = String.valueOf(-System.currentTimeMillis()); // This will put all unsigned Sections at the bottom

        sub.addSection(data, level, sign);
    }

    /**
     * Gets a Section from the Subsystem based on it's sign
     *
     * @param sign The signature to pull for
     * @return Itself
     * @see TestTelemetryCommand#sign(String sign)
     * @see TestTelemetrySubsystem
     */
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
