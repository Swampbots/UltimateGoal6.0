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
import java.util.List;

public class Camera {
    private HardwareMap hardwareMap;

    private OpenCvCamera webcam;
    private RingPatternPipeline vision;


    // HSV Threshold input variables
    private static final double THRESHOLD_STEP = 1.0;

    private static final double HUE_MAX = 180.0;
    private static final double SAT_MAX = 255.0;
    private static final double VAL_MAX = 255.0;
    private static final double HSV_MIN = 0.0;

    // Initializes HSV values to the range used during testing
    private static double[] hsvHue = new double[]{10.0, 25.0};
    private static double[] hsvSat = new double[]{153.0, 255.0};
    private static double[] hsvVal = new double[]{75.0, 207.0};


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

    private double bound;

    private static final double RING_CONFIDENCE_THRESHOLD = 0.65;



    //Outputs
    private Mat blurOutput = new Mat();
    private Mat hsvThresholdOutput = new Mat();
    private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
    private ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();

    public Camera(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;

        initHardware();
    }

    public void initHardware() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam1"), cameraMonitorViewId);
        webcam.openCameraDevice();
        vision = new RingPatternPipeline();
        webcam.setPipeline(vision);
        webcam.startStreaming(640, 480, OpenCvCameraRotation.SIDEWAYS_RIGHT);
    }

    public void periodic() {
        vision.setRectLeft(rectLeft);
        vision.setRectTop(rectTop);
        vision.setRectRight(rectRight);
        vision.setRectBot(rectBot);
        vision.setBound(bound);

        vision.setReturnHSV(returnHSV);
        vision.setDrawRect(drawRect);

        vision.setHsvHueMin(hsvHue[0]);
        vision.setHsvHueMax(hsvHue[1]);
        vision.setHsvSatMin(hsvSat[0]);
        vision.setHsvSatMax(hsvSat[1]);
        vision.setHsvValMin(hsvVal[0]);
        vision.setHsvValMax(hsvVal[1]);
    }

    public double[] getHsvHue() {
        return vision.gethsvHue();
    }

    public double[] getHsvSat() {
        return vision.getHsvSat();
    }

    public double[] getHsvVal() {
        return vision.getHsvVal();
    }



    public boolean isDrawRect() {
        return drawRect;
    }


    public double getBound() {
        return bound;
    }

    public ArrayList<MatOfPoint> getFindContoursOutput() {
        return findContoursOutput;
    }

    public static double getRectBot() {
        return rectBot;
    }

    public static double getRectLeft() {
        return rectLeft;
    }

    public static double getRectMin() {
        return RECT_MIN;
    }


    public static double getRectRight() {
        return rectRight;
    }


    public static double getRectTop() {
        return rectTop;
    }

    public static double getThresholdStep() {
        return THRESHOLD_STEP;
    }

    public void setHsvHueMin(double hueMin) {
        hsvHue[0] = Math.max(hueMin, HSV_MIN);
    }

    public void setHsvHueMax(double hueMax) {
        hsvHue[1] = Math.min(hueMax, HUE_MAX);
    }

    public void setHsvSatMin(double satMin) {
        hsvSat[0] = Math.max(satMin, HSV_MIN);
    }

    public void setHsvSatMax(double satMax) {
        hsvSat[1] = Math.min(satMax, SAT_MAX);
    }
    public void setHsvValMin(double valMin) {
        hsvVal[0] = Math.max(valMin, HSV_MIN);
    }

    public void setHsvValMax(double valMax) {
        hsvVal[1] = Math.min(valMax, VAL_MAX);
    }

    public static void setDrawRect(boolean drawRect) {
        Camera.drawRect = drawRect;
    }

    public static void setRectBot(double rectBot) {
        Camera.rectBot = rectBot;
    }

    public static void setRectLeft(double rectLeft) {
        Camera.rectLeft = rectLeft;
    }

    public static void setRectRight(double rectRight) {
        Camera.rectRight = rectRight;
    }

    public static void setRectTop(double rectTop) {
        Camera.rectTop = rectTop;
    }

    public List<MatOfPoint> getContoursOutput(){
        return filterContoursOutput;
    }

    public void setReturnHSV(boolean returnHSV){
        Camera.returnHSV = returnHSV;
    }

    public void setBound(double bound) {
        this.bound = bound;
    }

}
