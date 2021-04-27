package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.GamepadCooldowns;
import org.firstinspires.ftc.teamcode.RingPatternPipeline;
import org.firstinspires.ftc.teamcode.RingPlacement;
import org.firstinspires.ftc.teamcode.robot.subsystems.Camera;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import static org.firstinspires.ftc.teamcode.CommandDrive.TRIGGER_THRESHOLD;
import static org.firstinspires.ftc.teamcode.RingPlacement.FOUR_RINGS;
import static org.firstinspires.ftc.teamcode.RingPlacement.ONE_RING;
import static org.firstinspires.ftc.teamcode.RingPlacement.ZERO_RINGS;


public class AutoCameraControl {
    private Camera camera;
    private Gamepad gamepad1;
    private Gamepad gamepad2;
    private MultipleTelemetry multiTelemetry;
    private Telemetry telemetry;

    private GamepadCooldowns gp1;
    private GamepadCooldowns gp2;
    private double runtime = 0.0;

    // Toggle for output overlays   [type, enabled?]
    // Toggle for output overlays [Blur, Rect, Point, HSV]
    private final boolean[] overlays = {false, true, true, false};
    private int togglePoint = 0;
    // Cooldown in the order: a, b, x, y
    private final boolean[] buttonCooldown = {false, false, false, false};

    private final String BOLD_MODIFIER = "\033[0;1m"; // \u001B[1m doesn't work     | still need to test \u001BE\u0001


    public static final double THRESHOLD_STEP = 0.04;

    public static final double HUE_MAX = 180.0;
    public static final double SAT_MAX = 255.0;
    public static final double VAL_MAX = 255.0;
    public static final double HSV_MIN = 0.0;

    public static final double RECT_STEP = 0.04;
    public static final double RECT_MIN = 0.0;

    public static final int IMG_WIDTH = 480;
    public static final int IMG_HEIGHT = 640;

    private List<MatOfPoint> contours;
    private final double CONTOUR_THRESHOLD = 100;
    private RingPlacement placement;

    public AutoCameraControl(Camera camera, Gamepad gamepad1, Gamepad gamepad2, MultipleTelemetry multiTelemetry){
        this.camera = camera;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.multiTelemetry = multiTelemetry;

        gp1 = new GamepadCooldowns();
        gp2 = new GamepadCooldowns();

    }

    public AutoCameraControl(Camera camera, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry){
        this.camera = camera;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.telemetry = telemetry;

        gp1 = new GamepadCooldowns();
        gp2 = new GamepadCooldowns();

    }

    public AutoCameraControl(Camera camera, Gamepad gamepad1, Gamepad gamepad2) {
        this(camera, gamepad1, gamepad2, null);
    }

    public void periodic() {
        // Update local HSV threshold references
        double[] localHsvHue = camera.getHsvHue();
        double[] localHsvSat = camera.getHsvSat();
        double[] localHsvVal = camera.getHsvVal();

        // Update runtime read
        runtime = new Date().getTime(); //TODO: Make less jank?

        //--------------------------------------------------------------------------------------
        // START HSV THRESHOLD CONTROLS
        //--------------------------------------------------------------------------------------

            /*
                CONTROLS: (increase, decrease)
                Hue min: gp1.up,    gp1.down
                Hue max: gp1.y,     gp1.a
                Sat min: gp1.right, gp1.left
                Sat max: gp1.b,     gp1.x
                Val min: gp1.lb,    gp1.lt
                Val max: gp1.rb,    gp1.rt
             */

        // Modify threshold variables if the buttons are pressed and thresholds are within outer limits 0 & 255

        // HUE MINIMUM
        if (gamepad1.dpad_down && gp1.dpDown.ready(runtime)) {
            if (localHsvHue[0] > HSV_MIN)
                camera.setHsvHueMin(localHsvHue[0] - THRESHOLD_STEP)   /*hsvHue[0] -= THRESHOLD_STEP*/;
            else
                camera.setHsvHueMin(HSV_MIN)                           /*hsvHue[0] = HSV_MIN*/;
            gp1.dpDown.updateSnapshot(runtime);
        }

        if (gamepad1.dpad_up && gp1.dpUp.ready(runtime)) {
            if (localHsvHue[0] < localHsvHue[1])
                camera.setHsvHueMin(localHsvHue[0] + THRESHOLD_STEP)   /*hsvHue[0] += THRESHOLD_STEP*/;
            else
                camera.setHsvHueMin(localHsvHue[1])                    /*hsvHue[0] = hsvHue[1]*/;
            gp1.dpUp.updateSnapshot(runtime);
        }

        // HUE MAXIMUM
        if (gamepad1.y && gp1.y.ready(runtime)) {
            if (localHsvHue[1] < HUE_MAX)
                camera.setHsvHueMax(localHsvHue[1] + THRESHOLD_STEP)   /*hsvHue[1] += THRESHOLD_STEP*/;
            else
                camera.setHsvHueMax(HUE_MAX)                           /*hsvHue[1] = HUE_MAX*/;
            gp1.y.updateSnapshot(runtime);
        }

        if (gamepad1.a && gp1.a.ready(runtime)) {
            if (localHsvHue[1] > localHsvHue[0])
                camera.setHsvHueMax(localHsvHue[1] - THRESHOLD_STEP)   /*hsvHue[1] -= THRESHOLD_STEP*/;
            else
                camera.setHsvHueMax(localHsvHue[0])                    /*hsvHue[1] = hsvHue[0]*/;
            gp1.a.updateSnapshot(runtime);
        }

        // SAT MINIMUM
        if (gamepad1.dpad_left && gp1.dpLeft.ready(runtime)) {
            if (localHsvSat[0] > HSV_MIN)
                camera.setHsvSatMin(localHsvSat[0] - THRESHOLD_STEP)   /*hsvSat[0] -= THRESHOLD_STEP*/;
            else
                camera.setHsvSatMin(HSV_MIN)                           /*hsvSat[0] = HSV_MIN*/;
            gp1.dpLeft.updateSnapshot(runtime);
        }

        if (gamepad1.dpad_right && gp1.dpRight.ready(runtime)) {
            if (localHsvSat[0] < localHsvSat[1])
                camera.setHsvSatMin(localHsvSat[0] + THRESHOLD_STEP)   /*hsvSat[0] += THRESHOLD_STEP*/;
            else
                camera.setHsvSatMin(localHsvSat[1])                    /*hsvSat[0] = hsvSat[1]*/;
            gp1.dpRight.updateSnapshot(runtime);
        }

        // SAT MAXIMUM
        if (gamepad1.b && gp1.b.ready(runtime)) {
            if (localHsvSat[1] < SAT_MAX)
                camera.setHsvSatMax(localHsvSat[1] + THRESHOLD_STEP)   /*hsvSat[1] += THRESHOLD_STEP*/;
            else
                camera.setHsvSatMax(SAT_MAX)                           /*hsvSat[1] = SAT_MAX*/;
            gp1.b.updateSnapshot(runtime);
        }

        if (gamepad1.x && gp1.x.ready(runtime)) {
            if (localHsvSat[1] > localHsvSat[0])
                camera.setHsvSatMax(localHsvSat[1] - THRESHOLD_STEP)   /*hsvSat[1] -= THRESHOLD_STEP*/;
            else
                camera.setHsvSatMax(localHsvSat[0])                    /*hsvSat[1] = hsvSat[0]*/;
            gp1.x.updateSnapshot(runtime);
        }

        // VAL MINIMUM
        if (gamepad1.left_trigger > TRIGGER_THRESHOLD && gp1.lt.ready(runtime)) {
            if (localHsvVal[0] > HSV_MIN)
                camera.setHsvValMin(localHsvVal[0] - THRESHOLD_STEP)   /*hsvVal[0] -= THRESHOLD_STEP*/;
            else
                camera.setHsvValMin(HSV_MIN)                           /*hsvVal[0] = HSV_MIN*/;
            gp1.lt.updateSnapshot(runtime);
        }

        if (gamepad1.left_bumper && gp1.lb.ready(runtime)) {
            if (localHsvVal[0] < localHsvVal[1])
                camera.setHsvValMin(localHsvVal[0] + THRESHOLD_STEP)   /*hsvVal[0] += THRESHOLD_STEP*/;
            else
                camera.setHsvValMin(localHsvVal[1])                    /*hsvVal[0] = hsvVal[1]*/;
            gp1.lb.updateSnapshot(runtime);
        }

        // VAL MAXIMUM
        if (gamepad1.right_trigger > TRIGGER_THRESHOLD && gp1.rt.ready(runtime)) {
            if (localHsvVal[1] > localHsvVal[0])
                camera.setHsvValMax(localHsvVal[1] - THRESHOLD_STEP)  /*hsvVal[1] -= THRESHOLD_STEP*/;
            else
                camera.setHsvValMax(localHsvVal[0])                   /*hsvVal[1] = hsvVal[0]*/;
            gp1.rt.updateSnapshot(runtime);
        }

        if (gamepad1.right_bumper && gp1.rb.ready(runtime)) {
            if (localHsvVal[1] < VAL_MAX)
                camera.setHsvValMax(localHsvVal[1] + THRESHOLD_STEP)   /*hsvVal[1] += THRESHOLD_STEP*/;
            else
                camera.setHsvValMax(VAL_MAX)                           /*hsvVal[1] = VAL_MAX*/;
            gp1.rb.updateSnapshot(runtime);
        }

        //--------------------------------------------------------------------------------------
        // END HSV THRESHOLD CONTROLS
        //--------------------------------------------------------------------------------------


            /*
                NEW Controls: (left stick and right stick configuration)
                    - Left stick: change top-left corner values relative to
                        ~ left_stick_x (changes left bound)
                        ~ left_stick_y (changes top bound)
                    - Right stick: change bottom-right corner values relative to
                        ~ right_stick_x (changes right bound)
                        ~ right_stick_y (changes bottom bound)
             */

        // Get rectangle boundaries
        double localRectTop = camera.getRectTop();
        double localRectLeft = camera.getRectLeft();
        double localRectBot = camera.getRectBot();
        double localRectRight = camera.getRectRight();

        double localBound = camera.getBound();


        camera.setRectTop(Range.clip(localRectTop + (gamepad2.left_stick_y * RECT_STEP), RECT_MIN, IMG_HEIGHT))   /*rectTop     = trim(rectTop      + (gamepad2.left_stick_y * RECT_STEP), RECT_MIN, IMG_HEIGHT)*/;
        camera.setRectLeft(Range.clip(localRectLeft + (gamepad2.left_stick_x * RECT_STEP), RECT_MIN, IMG_WIDTH))    /*rectLeft    = trim(rectLeft     + (gamepad2.left_stick_x * RECT_STEP), RECT_MIN, IMG_WIDTH)*/;
        camera.setRectBot(Range.clip(localRectBot + (gamepad2.right_stick_y * RECT_STEP), RECT_MIN, IMG_HEIGHT))  /*rectBot     = trim(rectBot      + (gamepad2.right_stick_y * RECT_STEP), RECT_MIN, IMG_HEIGHT)*/;
        camera.setRectRight(Range.clip(localRectRight + (gamepad2.right_stick_x * RECT_STEP), RECT_MIN, IMG_WIDTH))   /*rectRight   = trim(rectRight    + (gamepad2.right_stick_x * RECT_STEP), RECT_MIN, IMG_WIDTH)*/;

        // Bound is set to between left and right and controlled by dpad up and down
        if(gamepad2.dpad_up && gp2.dpUp.ready(runtime)) {
            localBound += RECT_STEP;
            gp2.dpUp.updateSnapshot(runtime);
        }
        if(gamepad2.dpad_down && gp2.dpDown.ready(runtime)) {
            localBound -= RECT_STEP;
            gp2.dpDown.updateSnapshot(runtime);
        }
        camera.setBound(Range.clip(localBound, localRectLeft, localRectRight)); // Updates at end for when bounding rect changes

        //camera.setBound(Range.clip(localBound + RECT_STEP * ((gamepad2.dpad_up ? 1 : 0) - (gamepad2.dpad_down ? 1 : 0)), localRectLeft, localRectRight));

        //--------------------------------------------------------------------------------------
        // START OVERLAY CONTROLS
        //--------------------------------------------------------------------------------------

            /*
                Controls:
                gp2.a   Toggle Selected Overlay
                gp2.b   Disable All Overlays
                gp2.y   Move Forward in Overlay Selection
                gp2.x   Move Backward in Overlay Selection

             */

        // Convert String to int, then toggle between 1 and 0, then turn back into string
        if(gamepad2.a && buttonCooldown[0])         overlays[togglePoint] = !overlays[togglePoint];
        else if(!gamepad2.a && !buttonCooldown[0])  buttonCooldown[0] = true;

        // Disable all overlays
        if(gamepad2.b) Arrays.fill(overlays, false);

        // Move backward in queue
        if(gamepad2.x && buttonCooldown[2])         togglePoint = (--togglePoint+overlays.length) % overlays.length;
        else if(!gamepad2.x && !buttonCooldown[2])  buttonCooldown[2] = true;

        // Move forward in queue
        if(gamepad2.y && buttonCooldown[3])         togglePoint = (++togglePoint) % overlays.length;
        else if(!gamepad2.y && !buttonCooldown[3])  buttonCooldown[3] = true;

        // Blur
        camera.setShowBlur(overlays[0]);

        // Rect
        camera.setDrawRect(overlays[1]);

        // Point
        camera.setShowPoint(overlays[2]);

        // Black & White
        camera.setReturnHSV(overlays[3]);

        //--------------------------------------------------------------------------------------
        // END OVERLAY CONTROLS
        //--------------------------------------------------------------------------------------

        contours = camera.getContoursOutput();
        double contoursTop = 0;
        double contoursBot = 0;

        /*
        loop thru contours
            if contour.getY > bound
            contour.addArea to upper
            else
            contour.addArea to lower

        end
        if upper > threshold
        run: 4 ring
        else if lower > threshold
        run: 1 ring
        else
        run: 0 ring
        store past 10-15 entries and determine path based on those
         */

        // Create Point variable holding top-center coordinates of boundingRect
        Point rectPoint = new Point();
        double rectBoundCorrection = 0.95;  // correction factor to increase box size by a small amount to correct for contours spilling out

        RingPatternPipeline.rectPoints.clear();

        if(telemetry != null) {
            telemetry.addData("contour count", contours.size());
        }
        if(multiTelemetry != null) {
            multiTelemetry.addData("Contour Count", contours.size());
        }
        try {
            for (MatOfPoint c : contours) {
                Rect contourRect = Imgproc.boundingRect(c);

                // Telemetry data on contour location
//                telemetry.addData("contour x", contourRect.x);
//                telemetry.addData("contour y", contourRect.y);
//                telemetry.addData("contour w", contourRect.width);
//                telemetry.addData("contour h", contourRect.height);

                // Ensure contour is inside bounding box
                if(contourRect.x >= localRectLeft / rectBoundCorrection &&
                        contourRect.y >= localRectTop / rectBoundCorrection &&
                        contourRect.x + contourRect.width <= localRectRight * (2 - rectBoundCorrection) &&
                        contourRect.y + contourRect.height <= localRectBot * (2 - rectBoundCorrection)) {



                    // Get the center of the contour
                    rectPoint.x = contourRect.width;
                    rectPoint.y = (2 * contourRect.y + contourRect.height) / 2.0;


                    RingPatternPipeline.rectPoints.add(rectPoint);
                    if(telemetry != null) {
                        telemetry.addLine("In box");
                        telemetry.addData("rect point", rectPoint);
                    }
                    if(multiTelemetry != null) {
                        multiTelemetry.addLine("In box");
                        multiTelemetry.addData("Rect point", rectPoint);
                    }

                    // Check if contour is above the one ring line
                    if(rectPoint.x > localBound) {
                        contoursTop += contourRect.area();
                    } else {
                        contoursBot += contourRect.area();
                    }
                }
                if(telemetry != null) {
                    telemetry.addLine();
                }
                if(multiTelemetry != null) {
                    multiTelemetry.addLine();
                }
            }
        } catch (Exception e) {
            if(telemetry != null) {
                telemetry.addLine("Error while iterating through contours!");
            }
            if(multiTelemetry != null) {
                multiTelemetry.addLine("Error while iterating through contours!");
            }

            //contourIterateError = true;
        }

        RingPlacement currentPlacement;
        if (contoursTop > CONTOUR_THRESHOLD) {
            currentPlacement = FOUR_RINGS;
        } else if (contoursBot > CONTOUR_THRESHOLD) {
            currentPlacement = ONE_RING;
        } else {
            currentPlacement = ZERO_RINGS;
        }
        placement = currentPlacement;
//
//
//
//        // Get the largest tally
//        double largestTally = largest(contoursProportionLeft, contoursProportionCenter, contoursProportionRight);
//
//        // Divide all three tallies by the largest to get proportions
//        try {
//            contoursProportionLeft /= largestTally;
//            contoursProportionCenter /= largestTally;
//            contoursProportionRight /= largestTally;
//        } catch (Exception e) {
//            telemetry.addLine("Error while dividing contour tallies by largest tally.");
//        }
//
//        // Get the smallest proportioned tally
//        double smallestProportionedTally = smallest(contoursProportionLeft, contoursProportionCenter, contoursProportionRight);
//
//        // Subtract from one to get confidence
//        double confidence = 1.0 - smallestProportionedTally;
//
//
//        // Compare area tallies before determining badData to take advantage of SkystonePlacement.UNKNOWN
//        RingPlacement currentPlacement =
//                compareAreaTallies(contoursProportionLeft, contoursProportionCenter, contoursProportionRight);


        // Compare contour area tallies to see which third of the bounding rectangle
        // has the least (which will be the third with the Skystone in it).
        // If data is below our confidence threshold, keep the last reading instead
        // of getting a new one from bad data.
//        boolean badData =
//                confidence < SKYSTONE_CONFIDENCE_THRESHOLD ||
//                        Double.isNaN(confidence) ||
//                        contourIterateError ||
//                        currentPlacement == UNKNOWN;    // true if confidence is too low or if we get NaN as confidence or if contour iteration fails
//        if (badData) {
//            // Do nothing; last reading will be kept
//        } else {
//            // Good data! Update our decision.
//            placement = currentPlacement;
//        }

        camera.periodic();

        if(telemetry != null) {
            telemetry.addLine("Running!");
            telemetry.addLine();
            telemetry.addLine(String.format(Locale.ENGLISH, "Hue: [%.2f, %.2f]", localHsvHue[0], localHsvHue[1]));
            telemetry.addLine(String.format(Locale.ENGLISH, "Sat: [%.2f, %.2f]", localHsvSat[0], localHsvSat[1]));
            telemetry.addLine(String.format(Locale.ENGLISH, "Val: [%.2f, %.2f]", localHsvVal[0], localHsvVal[1]));
            telemetry.addLine();
            telemetry.addData("contoursTop", String.format(Locale.ENGLISH, "%.2f", contoursTop));
            telemetry.addData("contoursBot", String.format(Locale.ENGLISH, "%.2f", contoursBot));
            telemetry.addLine();
            telemetry.addData("placement", placement);
            telemetry.addLine();
            telemetry.addData("Rect left",String.format(Locale.ENGLISH, "%.2f", localRectLeft));
            telemetry.addData("Rect top",String.format(Locale.ENGLISH, "%.2f", localRectTop));
            telemetry.addData("Rect right",String.format(Locale.ENGLISH, "%.2f", localRectRight));
            telemetry.addData("Rect bottom",String.format(Locale.ENGLISH, "%.2f", localRectBot));
            telemetry.addData("Rect bound error", rectBoundCorrection);
            telemetry.addLine();
            telemetry.addData("bound",String.format(Locale.ENGLISH, "%.2f", camera.getBound()));
            telemetry.addLine();
            telemetry.addLine("Visual Modifiers");
            telemetry.addData((togglePoint == 0 ? "u\001B[1m" : "") + "Show Blur",  (togglePoint == 0 ? "u\001B[1m" : "") + overlays[0]);
            telemetry.addData((togglePoint == 1 ? "u\001B[1m" : "") + "Show Rect",  (togglePoint == 1 ? "u\001B[1m" : "") + overlays[1]);
            telemetry.addData((togglePoint == 2 ? "u\001B[1m" : "") + "Show Point", (togglePoint == 2 ? "u\001B[1m" : "") + overlays[2]);
            telemetry.addData((togglePoint == 3 ? "u\001B[1m" : "") + "Show HSV",   (togglePoint == 3 ? "u\001B[1m" : "") + overlays[3]);
//            telemetry.addLine();
//            telemetry.addData("Confidence", String.format(Locale.ENGLISH, "%.2f", confidence));
//            if (badData)
//                telemetry.addLine("Confidence is below threshold or not a number. Keeping last placement decision.");
            telemetry.update();
        }
        if(multiTelemetry != null) {
            multiTelemetry.addLine("Running!");
            multiTelemetry.addLine();
            multiTelemetry.addLine(String.format(Locale.ENGLISH, "Hue: [%.2f, %.2f]", localHsvHue[0], localHsvHue[1]));
            multiTelemetry.addLine(String.format(Locale.ENGLISH, "Sat: [%.2f, %.2f]", localHsvSat[0], localHsvSat[1]));
            multiTelemetry.addLine(String.format(Locale.ENGLISH, "Val: [%.2f, %.2f]", localHsvVal[0], localHsvVal[1]));
            multiTelemetry.addLine();
            multiTelemetry.addData("contoursTop", String.format(Locale.ENGLISH, "%.2f", contoursTop));
            multiTelemetry.addData("contoursBot", String.format(Locale.ENGLISH, "%.2f", contoursBot));
            multiTelemetry.addLine();
            multiTelemetry.addData("placement", placement);
            multiTelemetry.addLine();
            multiTelemetry.addData("Rect left",String.format(Locale.ENGLISH, "%.2f", localRectLeft));
            multiTelemetry.addData("Rect top",String.format(Locale.ENGLISH, "%.2f", localRectTop));
            multiTelemetry.addData("Rect right",String.format(Locale.ENGLISH, "%.2f", localRectRight));
            multiTelemetry.addData("Rect bottom",String.format(Locale.ENGLISH, "%.2f", localRectBot));
            multiTelemetry.addData("Rect bound error", rectBoundCorrection);
            multiTelemetry.addLine();
            multiTelemetry.addData("bound",String.format(Locale.ENGLISH, "%.2f", camera.getBound()));
            multiTelemetry.addLine(); telemetry.addLine("Visual Modifiers");
            multiTelemetry.addData((togglePoint == 0 ? BOLD_MODIFIER : "") + "Show Blur",  (togglePoint == 0 ? BOLD_MODIFIER : "") + overlays[0]);
            multiTelemetry.addData((togglePoint == 1 ? BOLD_MODIFIER : "") + "Show Rect",  (togglePoint == 1 ? BOLD_MODIFIER : "") + overlays[1]);
            multiTelemetry.addData((togglePoint == 2 ? BOLD_MODIFIER : "") + "Show Point", (togglePoint == 2 ? BOLD_MODIFIER : "") + overlays[2]);
            multiTelemetry.addData((togglePoint == 3 ? BOLD_MODIFIER : "") + "Show HSV",   (togglePoint == 3 ? BOLD_MODIFIER : "") + overlays[3]);
//            telemetry.addLine();
//            telemetry.addData("Confidence", String.format(Locale.ENGLISH, "%.2f", confidence));
//            if (badData)
//                telemetry.addLine("Confidence is below threshold or not a number. Keeping last placement decision.");
            multiTelemetry.update();
        }

    }

    public double largest(double ...arr){
        return Math.max(arr[0],Math.max(arr[1],arr[2]));
    }

    public double smallest(double ...arr) {
        return Math.min(arr[0],Math.min(arr[1],arr[2]));
    }

    public RingPlacement getPlacement() {
        return placement;
    }
}
