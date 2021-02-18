package org.firstinspires.ftc.teamcode.testing;

import java.text.Format;

public class TelemetryEntry {
    private TestTelemetryCommand.TYPE type;
    private String string;
    private String format;
    private Object data;

    public TelemetryEntry(TestTelemetryCommand.TYPE type, String string, String format, Object data) {
        this.type = type;
        this.string = string;
        this.format = format;
        this.data = data;
    }

    public TestTelemetryCommand.TYPE getType() {
        return type;
    }

    public String getString() {
        return string;
    }

    public String getFormat() {
        return format;
    }

    public Object getData() {
        return data;
    }
}
