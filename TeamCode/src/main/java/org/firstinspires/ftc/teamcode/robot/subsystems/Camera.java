package org.firstinspires.ftc.teamcode.robot.subsystems;

import com.disnodeteam.dogecommander.Subsystem;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.RingPatternPipeline;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;

public class Camera extends OpenCvPipeline implements Subsystem {
    private HardwareMap hardwareMap;

    private OpenCvCamera webcam;
    private RingPatternPipeline vision;


    // HSV Threshold input variables
    private static final double THRESHOLD_STEP = 1.0;

    private static final double HUE_MAX = 180.0;
    private static final double SAT_MAX = 255.0;
    private static final double VAL_MAX = 255.0;
    private static final double HSV_MIN = 0.0;

    private static double[] hsvHue = new double[]{80.0, 119.0};     // Starts with red outside of threshold (red is hue of 120)
    private static double[] hsvSat = new double[]{175.0, 255.0};
    private static double[] hsvVal = new double[]{50.0, 255.0};      // Testing showed 50 (min) to be enough to cut out most of the Skystone


    private static double rectTop   = 433.0;
    private static double rectLeft  = 34.0;
    private static double rectBot   = 506.0;
    private static double rectRight = 464.0;

    private static final double RECT_STEP = 0.04;
    private static final double RECT_MIN = 0.0;

    private static final int IMG_WIDTH = 480;
    private static final int IMG_HEIGHT = 640;

    private static boolean returnHSV = true;
    private static boolean drawRect = true;

    private static double leftBound = 0;
    private static double centerBound = 0;

    private static final double RING_CONFIDENCE_THRESHOLD = 0.65;



    //Outputs
    private Mat blurOutput = new Mat();
    private Mat hsvThresholdOutput = new Mat();
    private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
    private ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();

    @Override
    public void initHardware() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam1"), cameraMonitorViewId);
        webcam.openCameraDevice();
        vision = new RingPatternPipeline();
        webcam.setPipeline(vision);
        webcam.startStreaming(640, 480, OpenCvCameraRotation.SIDEWAYS_RIGHT);
    }

    @Override
    public void periodic() {

    }

    @Override
    public Mat processFrame(Mat input) {
        return null;
    }
}
