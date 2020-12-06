package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.BasicDriveHardware;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.util.List;
import java.util.Locale;

@Autonomous(name = "Test Cam",group = "Testing")
public class TestCamera extends LinearOpMode {

    BasicDriveHardware hardware = new BasicDriveHardware();

    private List<MatOfPoint> contours; // Contours from pipeline after filtering


    //private SkystonePlacement placement = CENTER;



    //GamepadCooldowns gp1 = new GamepadCooldowns();
    double runtime = 0.0;
    private final double TRIGGER_THRESHOLD = 0.7;

    @Override
    public void runOpMode() throws InterruptedException {

        hardware.init(hardwareMap, true, true);


        // This loop will run after pressing "Init" and before pressing "Play"
        while (!isStarted()) {
            // Update local HSV threshold references
            double[] localHsvHue = hardware.vision.gethsvHue();
            double[] localHsvSat = hardware.vision.getHsvSat();
            double[] localHsvVal = hardware.vision.getHsvVal();

            // Update runtime read
            runtime = getRuntime();

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
//            if (gamepad1.dpad_down && gp1.dpDown.ready(runtime)) {
//                if (localHsvHue[0] > HSV_MIN)
//                    hardware.vision.setHsvHueMin(localHsvHue[0] - THRESHOLD_STEP)   /*hsvHue[0] -= THRESHOLD_STEP*/;
//                else
//                    hardware.vision.setHsvHueMin(HSV_MIN)                           /*hsvHue[0] = HSV_MIN*/;
//                gp1.dpDown.updateSnapshot(runtime);
//            }
//
//            if (gamepad1.dpad_up && gp1.dpUp.ready(runtime)) {
//                if (localHsvHue[0] < localHsvHue[1])
//                    hardware.vision.setHsvHueMin(localHsvHue[0] + THRESHOLD_STEP)   /*hsvHue[0] += THRESHOLD_STEP*/;
//                else
//                    hardware.vision.setHsvHueMin(localHsvHue[1])                    /*hsvHue[0] = hsvHue[1]*/;
//                gp1.dpUp.updateSnapshot(runtime);
//            }
//
//
//            // HUE MAXIMUM
//            if (gamepad1.y && gp1.y.ready(runtime)) {
//                if (localHsvHue[1] < HUE_MAX)
//                    hardware.vision.setHsvHueMax(localHsvHue[1] + THRESHOLD_STEP)   /*hsvHue[1] += THRESHOLD_STEP*/;
//                else
//                    hardware.vision.setHsvHueMax(HUE_MAX)                           /*hsvHue[1] = HUE_MAX*/;
//                gp1.y.updateSnapshot(runtime);
//            }
//
//            if (gamepad1.a && gp1.a.ready(runtime)) {
//                if (localHsvHue[1] > localHsvHue[0])
//                    hardware.vision.setHsvHueMax(localHsvHue[1] - THRESHOLD_STEP)   /*hsvHue[1] -= THRESHOLD_STEP*/;
//                else
//                    hardware.vision.setHsvHueMax(localHsvHue[0])                    /*hsvHue[1] = hsvHue[0]*/;
//                gp1.a.updateSnapshot(runtime);
//            }
//
//
//            // SAT MINIMUM
//            if (gamepad1.dpad_left && gp1.dpLeft.ready(runtime)) {
//                if (localHsvSat[0] > HSV_MIN)
//                    hardware.vision.setHsvSatMin(localHsvSat[0] - THRESHOLD_STEP)   /*hsvSat[0] -= THRESHOLD_STEP*/;
//                else
//                    hardware.vision.setHsvSatMin(HSV_MIN)                           /*hsvSat[0] = HSV_MIN*/;
//                gp1.dpLeft.updateSnapshot(runtime);
//            }
//
//            if (gamepad1.dpad_right && gp1.dpRight.ready(runtime)) {
//                if (localHsvSat[0] < localHsvSat[1])
//                    hardware.vision.setHsvSatMin(localHsvSat[0] + THRESHOLD_STEP)   /*hsvSat[0] += THRESHOLD_STEP*/;
//                else
//                    hardware.vision.setHsvSatMin(localHsvSat[1])                    /*hsvSat[0] = hsvSat[1]*/;
//                gp1.dpRight.updateSnapshot(runtime);
//            }
//
//
//            // SAT MAXIMUM
//            if (gamepad1.b && gp1.b.ready(runtime)) {
//                if (localHsvSat[1] < SAT_MAX)
//                    hardware.vision.setHsvSatMax(localHsvSat[1] + THRESHOLD_STEP)   /*hsvSat[1] += THRESHOLD_STEP*/;
//                else
//                    hardware.vision.setHsvSatMax(SAT_MAX)                           /*hsvSat[1] = SAT_MAX*/;
//                gp1.b.updateSnapshot(runtime);
//            }
//
//            if (gamepad1.x && gp1.x.ready(runtime)) {
//                if (localHsvSat[1] > localHsvSat[0])
//                    hardware.vision.setHsvSatMax(localHsvSat[1] - THRESHOLD_STEP)   /*hsvSat[1] -= THRESHOLD_STEP*/;
//                else
//                    hardware.vision.setHsvSatMax(localHsvSat[0])                    /*hsvSat[1] = hsvSat[0]*/;
//                gp1.x.updateSnapshot(runtime);
//            }
//
//
//            // VAL MINIMUM
//            if (gamepad1.left_trigger > TRIGGER_THRESHOLD && gp1.lt.ready(runtime)) {
//                if (localHsvVal[0] > HSV_MIN)
//                    hardware.vision.setHsvValMin(localHsvVal[0] - THRESHOLD_STEP)   /*hsvVal[0] -= THRESHOLD_STEP*/;
//                else
//                    hardware.vision.setHsvValMin(HSV_MIN)                           /*hsvVal[0] = HSV_MIN*/;
//                gp1.lt.updateSnapshot(runtime);
//            }
//
//            if (gamepad1.left_bumper && gp1.lb.ready(runtime)) {
//                if (localHsvVal[0] < localHsvVal[1])
//                    hardware.vision.setHsvValMin(localHsvVal[0] + THRESHOLD_STEP)   /*hsvVal[0] += THRESHOLD_STEP*/;
//                else
//                    hardware.vision.setHsvValMin(localHsvVal[1])                    /*hsvVal[0] = hsvVal[1]*/;
//                gp1.lb.updateSnapshot(runtime);
//            }
//
//
//            // VAL MAXIMUM
//            if (gamepad1.right_trigger > TRIGGER_THRESHOLD && gp1.rt.ready(runtime)) {
//                if (localHsvVal[1] > localHsvVal[0])
//                    hardware.vision.setHsvValMax(localHsvVal[1] - THRESHOLD_STEP)  /*hsvVal[1] -= THRESHOLD_STEP*/;
//                else
//                    hardware.vision.setHsvValMax(localHsvVal[0])                   /*hsvVal[1] = hsvVal[0]*/;
//                gp1.rt.updateSnapshot(runtime);
//            }
//
//            if (gamepad1.right_bumper && gp1.rb.ready(runtime)) {
//                if (localHsvVal[1] < VAL_MAX)
//                    hardware.vision.setHsvValMax(localHsvVal[1] + THRESHOLD_STEP)   /*hsvVal[1] += THRESHOLD_STEP*/;
//                else
//                    hardware.vision.setHsvValMax(VAL_MAX)                           /*hsvVal[1] = VAL_MAX*/;
//                gp1.rb.updateSnapshot(runtime);
//            }


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
            double localRectTop = hardware.vision.getRectTop();
            double localRectLeft = hardware.vision.getRectLeft();
            double localRectBot = hardware.vision.getRectBot();
            double localRectRight = hardware.vision.getRectRight();


//            hardware.vision.setRectTop(Range.clip(localRectTop + (gamepad2.left_stick_y * RECT_STEP), RECT_MIN, IMG_HEIGHT))   /*rectTop     = trim(rectTop      + (gamepad2.left_stick_y * RECT_STEP), RECT_MIN, IMG_HEIGHT)*/;
//            hardware.vision.setRectLeft(Range.clip(localRectLeft + (gamepad2.left_stick_x * RECT_STEP), RECT_MIN, IMG_WIDTH))    /*rectLeft    = trim(rectLeft     + (gamepad2.left_stick_x * RECT_STEP), RECT_MIN, IMG_WIDTH)*/;
//            hardware.vision.setRectBot(Range.clip(localRectBot + (gamepad2.right_stick_y * RECT_STEP), RECT_MIN, IMG_HEIGHT))  /*rectBot     = trim(rectBot      + (gamepad2.right_stick_y * RECT_STEP), RECT_MIN, IMG_HEIGHT)*/;
//            hardware.vision.setRectRight(Range.clip(localRectRight + (gamepad2.right_stick_x * RECT_STEP), RECT_MIN, IMG_WIDTH))   /*rectRight   = trim(rectRight    + (gamepad2.right_stick_x * RECT_STEP), RECT_MIN, IMG_WIDTH)*/;


            hardware.vision.setReturnHSV(gamepad2.a)                /*returnHSV = gamepad2.a*/;
            if (gamepad2.x) hardware.vision.setDrawRect(false)       /*drawRect = false*/;
            else if (gamepad2.y) hardware.vision.setDrawRect(true)   /*drawRect = true*/;


            contours = hardware.vision.filterContoursOutput();
            double contoursProportionLeft = 0;
            double contoursProportionCenter = 0;
            double contoursProportionRight = 0;


            double leftBound = hardware.vision.getLeftBound();
            double centerBound = hardware.vision.getCenterBound();

            // Calculate left and center boundary lines for cropping rectangle
            hardware.vision.setLeftBound(localRectLeft + Math.abs(localRectRight - localRectLeft) / 3.0)            /*leftBound = rectLeft + Math.abs((rectRight - rectLeft) / 3.0)*/;         // x position plus 1/3 of the width
            hardware.vision.setCenterBound(localRectLeft + Math.abs(localRectRight - localRectLeft) * 2.0 / 3.0)    /*centerBound = rectLeft + Math.abs((rectRight - rectLeft) * 2.0 / 3.0)*/; // x position plus 2/3 of the width

            // Create Point variable holding center coordinates of boundingRect
            Point rectCenter = new Point();


            // Keep track of error while iterating through contours
            boolean contourIterateError = false;

            try {
                for (MatOfPoint c : contours) {
                    Rect boundingRect = Imgproc.boundingRect(c);

                    // See if boundingRect is inside of the cropping rectangle
                    if (boundingRect.x >= localRectLeft &&
                            boundingRect.y >= localRectTop &&
                            boundingRect.x + boundingRect.width <= localRectRight &&
                            boundingRect.y + boundingRect.height <= localRectBot) {
                        // We've got a valid contour!
                        // Now classify as left, center, or right
                        rectCenter.x = (2 * boundingRect.x + boundingRect.width) / 2.0;     // Get the center of the rectangle
                        rectCenter.y = (2 * boundingRect.y + boundingRect.height) / 2.0;
                        if (rectCenter.x < leftBound)
                            contoursProportionLeft += boundingRect.area(); // rectangle in left 1/3 of the screen
                        else if (rectCenter.x < centerBound)
                            contoursProportionCenter += boundingRect.area(); // rectangle in center 1/3 of the screen
                        else
                            contoursProportionRight += boundingRect.area(); // rectangle in right 1/3 of the screen
                    }
                }
            } catch (Exception e) {
                telemetry.addLine("Error while iterating through contours!");
                contourIterateError = true;
            }

            // Get the largest tally
            double largestTally = Math.max(Math.max(contoursProportionLeft, contoursProportionCenter), contoursProportionRight);

            // Divide all three tallies by the largest to get proportions
            try {
                contoursProportionLeft /= largestTally;
                contoursProportionCenter /= largestTally;
                contoursProportionRight /= largestTally;
            } catch (Exception e) {
                telemetry.addLine("Error while dividing contour tallies by largest tally.");
            }

            // Get the smallest proportioned tally
            double smallestProportionedTally = Math.min(Math.min(contoursProportionLeft, contoursProportionCenter), contoursProportionRight);

            // Subtract from one to get confidence
            double confidence = 1.0 - smallestProportionedTally;


//            // Compare area tallies before determining badData to take advantage of SkystonePlacement.UNKNOWN
//            SkystonePlacement currentPlacement =
//                    compareAreaTallies(contoursProportionLeft, contoursProportionCenter, contoursProportionRight);
//
//
//            // Compare contour area tallies to see which third of the bounding rectangle
//            // has the least (which will be the third with the Skystone in it).
//            // If data is below our confidence threshold, keep the last reading instead
//            // of getting a new one from bad data.
//            boolean badData =
//                    confidence < RING_CONFIDENCE_THRESHOLD ||
//                            Double.isNaN(confidence) ||
//                            contourIterateError ||
//                            currentPlacement == UNKNOWN;    // true if confidence is too low or if we get NaN as confidence or if contour iteration fails
//            if (badData) {
//                // Do nothing; last reading will be kept
//            } else {
//                // Good data! Update our decision.
//                placement = currentPlacement;
//            }

            telemetry.addLine("Running");
            telemetry.addLine();

            telemetry.addLine();
            telemetry.addLine(String.format("Hue: [%s, %s]", localHsvHue[0], localHsvHue[1]));
            telemetry.addLine(String.format("Sat: [%s, %s]", localHsvSat[0], localHsvSat[1]));
            telemetry.addLine(String.format("Val: [%s, %s]", localHsvVal[0], localHsvVal[1]));
            telemetry.addLine();
            telemetry.addData("contoursProportionLeft", String.format(Locale.ENGLISH, "%.2f", contoursProportionLeft));
            telemetry.addData("contoursProportionCenter", String.format(Locale.ENGLISH, "%.2f", contoursProportionCenter));
            telemetry.addData("contoursProportionRight", String.format(Locale.ENGLISH, "%.2f", contoursProportionRight));
            telemetry.addLine();
//            telemetry.addData("placement", placement);
//            telemetry.addLine();
//            telemetry.addData("Confidence", String.format(Locale.ENGLISH, "%.2f", confidence));
//            if (badData)
//                telemetry.addLine("Confidence is below threshold or not a number. Keeping last placement decision.");
            telemetry.update();
        }
    }
}