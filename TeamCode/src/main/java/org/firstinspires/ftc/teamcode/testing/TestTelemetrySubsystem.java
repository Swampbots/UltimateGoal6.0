package org.firstinspires.ftc.teamcode.testing;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.disnodeteam.dogecommander.Subsystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class TestTelemetrySubsystem implements Subsystem {
    protected Telemetry telemetry;

    private List<TelemetrySection> sections;

    public TestTelemetrySubsystem(Telemetry telemetry) {
        this.telemetry = telemetry;
        sections = new ArrayList<>();
    }

    @Override
    public void initHardware() {
        addLine("Telemetry Ready!");
    }

    @Override
    public void periodic() {
        Collections.sort(sections, new SortByLevel());  // Sort the Sections by Level
        telemetry.addLine();                            // Add new line for readability

        for(TelemetrySection section : sections) {                  // Cycle through all the sections
            for(TelemetryEntry entry : section.getEntries()) {      // Cycle through all the entries in each section
                switch (entry.getType()) {                          // Select the appropriate way to add the Telemetry
                    case LINE:
                        addLine(entry.getCaption());
                        break;
                    case DATA:
                        addDataEntry(entry.getCaption(), entry.getData());
                        break;
                    case ACTION:
                        addAction(entry.getData());
                        break;
                    case DATA_LIST:
                        addDataList(entry.getCaption(), entry.getFormat(), entry.getData());
                        break;
                    default:

                        break;
                }
            }
            addLine(null);
        }

        telemetry.update();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addSection(TelemetrySection section) {
        // Removes old Sections if their Signatures match. This prevents the Section List from
        // containing duplicates from one Location.
        // Exception: If the Signature starts with '!', then it will not be removed.
        sections.removeIf(x -> (x.getSign() == section.getSign() && !x.getSign().startsWith("!")));
        sections.add(section);
    }

    private void addLine(String caption) {
        if(caption == null) telemetry.addLine();
        else telemetry.addLine(caption);
    }

    private void addDataEntry(String caption, @NotNull Object data) {
        if(caption == null) telemetry.addData("", data);
        else telemetry.addData(caption, data);
    }

    private void addDataList(String caption, @NotNull String format, @NotNull Object... data) {
        if(caption == null) telemetry.addData("", String.valueOf(format), data);
        else telemetry.addData(caption, String.valueOf(format), data);

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
    public TelemetrySection getEntry(String sign) {
        for(TelemetrySection section : sections)
            if(section.getSign() == sign) return section;

        return new TelemetrySection();
    }

    class SortByLevel implements Comparator<TelemetrySection> {
        @Override
        public int compare(TelemetrySection a, TelemetrySection b) {
            return b.getLevel() - a.getLevel();
        }
    }

}