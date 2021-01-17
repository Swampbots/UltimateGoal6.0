package org.firstinspires.ftc.teamcode.robot.commands.auto;

import com.disnodeteam.dogecommander.Command;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.GamepadCooldowns;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.RingPlacement;
import org.firstinspires.ftc.teamcode.robot.subsystems.Camera;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.Date;
import java.util.List;
import java.util.Locale;


import static java.lang.Runtime.getRuntime;
import static org.firstinspires.ftc.teamcode.CommandDrive.TRIGGER_THRESHOLD;
import static org.firstinspires.ftc.teamcode.RingPlacement.FOUR_RINGS;
import static org.firstinspires.ftc.teamcode.RingPlacement.ONE_RING;
import static org.firstinspires.ftc.teamcode.RingPlacement.UNKNOWN;
import static org.firstinspires.ftc.teamcode.RingPlacement.ZERO_RINGS;


public class CameraControlAuto {
    private Camera camera;
    private Gamepad gamepad1;
    private Gamepad gamepad2;
    private Telemetry telemetry;

    private GamepadCooldowns gp1;
    private GamepadCooldowns gp2;
    private double runtime = 0.0;
    
    public static final double THRESHOLD_STEP = 1.0;

    public static final double HUE_MAX = 180.0;
    public static final double SAT_MAX = 255.0;
    public static final double VAL_MAX = 255.0;
    public static final double HSV_MIN = 0.0;

    // Initializes HSV values to the range used during testing
    private static double[] hsvHue = new double[]{10.0, 25.0};
    private static double[] hsvSat = new double[]{153.0, 255.0};
    private static double[] hsvVal = new double[]{75.0, 207.0};

    private static double rectTop   = 433.0;
    private static double rectLeft  = 34.0;
    private static double rectBot   = 506.0;
    private static double rectRight = 464.0;

    public static final double RECT_STEP = 0.04;
    public static final double RECT_MIN = 0.0;

    public static final int IMG_WIDTH = 480;
    public static final int IMG_HEIGHT = 640;

    private static boolean returnHSV = false;
    private static boolean drawRect = true;

    private static double bound = (rectTop - rectBot) / 4 + rectBot;

    private List<MatOfPoint> contours;
    private final double CONTOUR_THRESHOLD = 100;
    private RingPlacement placement;

    public CameraControlAuto(Camera camera, Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry){
        this.camera = camera;
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.telemetry = telemetry;

        gp1 = new GamepadCooldowns();
        gp2 = new GamepadCooldowns();

    }

    public CameraControlAuto(Camera camera, Gamepad gamepad1, Gamepad gamepad2) {
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
            camera.setBound(Range.clip(localBound + RECT_STEP, localRectLeft, localRectRight));
            gp2.dpUp.updateSnapshot(runtime);
        }
        if(gamepad2.dpad_down && gp2.dpDown.ready(runtime)) {
            camera.setBound(Range.clip(localBound - RECT_STEP, localRectLeft, localRectBot));
        }

        //camera.setBound(Range.clip(localBound + RECT_STEP * ((gamepad2.dpad_up ? 1 : 0) - (gamepad2.dpad_down ? 1 : 0)), localRectLeft, localRectRight));

        camera.setReturnHSV(gamepad2.a)                /*returnHSV = gamepad2.a*/;
        if (gamepad2.x) camera.setDrawRect(false)       /*drawRect = false*/;
        else if (gamepad2.y) camera.setDrawRect(true)   /*drawRect = true*/;


        contours = camera.getContoursOutput();
        double contoursTop = 0;
        double contoursBot = 0;

        //double bound = camera.getBound();

        // Calculate horizontal boundary line for cropping rectangle
        //camera.changeBound(localRectBot + Math.abs(localRectTop - localRectBot) / 4.0);            /*leftBound = rectLeft + Math.abs((rectRight - rectLeft) / 3.0)*/;         // x position plus 1/3 of the width

        // Create Point variable holding center coordinates of boundingRect
        Point rectCenter = new Point();


        // Keep track of error while iterating through contours
        //boolean contourIterateError = false;

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

        try {
            for (MatOfPoint c : contours) {
                Rect contourRect = Imgproc.boundingRect(c);

                // Ensure contour is inside bounding box
                if(contourRect.x >= localRectLeft &&
                        contourRect.y >= localRectTop &&
                        contourRect.x + contourRect.width <= localRectRight &&
                        contourRect.y + contourRect.height <= localRectBot) {

                    // Get the center of the contour
                    rectCenter.x = (2 * contourRect.x + contourRect.width) / 2.0;
                    rectCenter.y = (2 * contourRect.y + contourRect.height) / 2.0;

                    // Check if contour is above the one ring line
                    if(rectCenter.y > localBound) {
                        contoursTop += contourRect.area();
                    } else {
                        contoursBot += contourRect.area();
                    }
                }
            }
        } catch (Exception e) {
            if(telemetry != null) {
                telemetry.addLine("Error while iterating through contours!");
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
            telemetry.addLine("Running");
            telemetry.addLine();
            telemetry.addLine(String.format("Hue: [%s, %s]", localHsvHue[0], localHsvHue[1]));
            telemetry.addLine(String.format("Sat: [%s, %s]", localHsvSat[0], localHsvSat[1]));
            telemetry.addLine(String.format("Val: [%s, %s]", localHsvVal[0], localHsvVal[1]));
            telemetry.addLine();
            telemetry.addData("contoursTop", String.format(Locale.ENGLISH, "%.2f", contoursTop));
            telemetry.addData("contoursBot", String.format(Locale.ENGLISH, "%.2f", contoursBot));
            telemetry.addLine();
            telemetry.addData("placement", placement);
            telemetry.addLine();
            telemetry.addData("Rect left",localRectLeft);
            telemetry.addData("Rect top",localRectTop);
            telemetry.addData("Rect right",localRectRight);
            telemetry.addData("Rect bottom",localRectBot);
            telemetry.addLine();
            telemetry.addData("localBound",localBound);
            telemetry.addData("bound",camera.getBound());
            telemetry.addLine();
            telemetry.addData("showHSV",gamepad2.a);
            telemetry.addData("showRect",camera.isDrawRect());
//            telemetry.addLine();
//            telemetry.addData("Confidence", String.format(Locale.ENGLISH, "%.2f", confidence));
//            if (badData)
//                telemetry.addLine("Confidence is below threshold or not a number. Keeping last placement decision.");
            telemetry.update();
        }

    }


    public double largest(double ...arr){
        return Math.max(arr[0],Math.max(arr[1],arr[2]));
    }

    public double smallest(double ...arr) {
        return Math.min(arr[0],Math.min(arr[1],arr[2]));
    }
}
