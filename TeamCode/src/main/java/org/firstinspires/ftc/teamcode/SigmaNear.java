/*
 * Author: Benton Li '19
 * Version: 1.0
 *
 * */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


@Autonomous(name = "Near-2")

public class SigmaNear extends LinearOpMode {
    //preparation for these cool vuforia stuffs
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private DcMotor core = null;
    private static final String VUFORIA_KEY = "AUrb6t//////AAABmZ7sUnVME0wvu2pmOKRP5ilgE5gzg4vWVqHNhc0ef2FEwf9NlosWkTS81UmRvZ0UTHFjPeQYLKL6iY60ZJQcJFcMftURUv/1nA/9YELScRwzltxrUAFpfMA/VE9VTaNPTQQYUfm1Z1wUwY6fAJBwDvZJP+UBqPD0AJxz0Gf8jgcdCVgu4A7VtVdk1PRMTSUkHdOEm+VmXzpjxL9X4d/v81mx3aqJbVc6+qhUD53umiep/wCgl9WxHYY6ZEM2tuS7Eih3TexL24HLFvdEu79t24yTzCFz6du/hB12nfyySO78UWbdlusHuHIv0ZI5/IWh4RigF057FaLWc4F+EluGBkO0c6ygIaciN5fHPS9l7dtj";
    String goldLocation;

    //configure motors
    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightBack = null;
    private DcMotor lift = null;

    int p = 1; //default power
    float x = 0;//default x-coord
    float y = 0;//default y-coord
    String direction = "static";//default direction
    ElapsedTime runTime = new ElapsedTime();
    double checkpoint1 = 10;
    double checkpoint2 = 20;





    public void sample() {
        runTime.reset();
        initVuforia();
        initTfod();
        tfod.activate();

        goldLocation = "N";
        while (goldLocation == "N" && runTime.time() < checkpoint1) {
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    if (updatedRecognitions.size() == 3) {
                        int goldMineralX = -1;
                        int silverMineral1X = -1;
                        int silverMineral2X = -1;
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                goldMineralX = (int) recognition.getLeft();
                            } else if (silverMineral1X == -1) {
                                silverMineral1X = (int) recognition.getLeft();
                            } else {
                                silverMineral2X = (int) recognition.getLeft();
                            }
                        }
                        if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                            if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                                telemetry.addData("Gold Mineral Position", "Left");
                                goldLocation = "L";
                                break;

                            } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                telemetry.addData("Gold Mineral Position", "Right");
                                goldLocation = "R";
                                break;

                            } else {
                                telemetry.addData("Gold Mineral Position", "Center");
                                goldLocation = "C";
                                break;
                            }
                        }
                    }
                    telemetry.update();
                }
            }
        }
        //Four endings for this story: left, right, center, nothing


    }

    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }


    private void configureMotors(){
        leftFront = hardwareMap.get(DcMotor.class, "mot0");
        leftBack = hardwareMap.get(DcMotor.class, "mot2");
        rightFront = hardwareMap.get(DcMotor.class, "mot1");
        rightBack = hardwareMap.get(DcMotor.class, "mot3");
       // lift = hardwareMap.get(DcMotor.class,"lift");

        //set rotational direction
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.FORWARD);
    }

    public void move(String dir, double magnitude){
        direction = dir;
        if (direction == "static"){
            runTime.reset();
            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);
            while (runTime.seconds()<magnitude){
                telemetry.addData("time",runTime.seconds());
                telemetry.update();
            }
        }
        if (direction == "LF"){
            runTime.reset();
            leftFront.setPower(0);
            leftBack.setPower(p);
            rightFront.setPower(0);
            rightBack.setPower(p);
            while (runTime.seconds()<magnitude){
                telemetry.addData("time",runTime.seconds());
                telemetry.update();
            }
        }
        if (direction == "RF"){
            runTime.reset();
            leftFront.setPower(p);
            leftBack.setPower(0);
            rightFront.setPower(p);
            rightBack.setPower(0);
            while (runTime.seconds()<magnitude){
                telemetry.addData("time",runTime.seconds());
                telemetry.update();
            }
        }
        if (direction == "LB"){
            runTime.reset();
            leftFront.setPower(-p);
            leftBack.setPower(0);
            rightFront.setPower(-p);
            rightBack.setPower(0);
            while (runTime.seconds()<magnitude){
                telemetry.addData("time",runTime.seconds());
                telemetry.update();
            }
        }
        if (direction == "RB"){
            runTime.reset();
            leftFront.setPower(0);
            leftBack.setPower(-p);
            rightFront.setPower(0);
            rightBack.setPower(-p);
            while (runTime.seconds()<magnitude){
                telemetry.addData("time",runTime.seconds());
                telemetry.update();
            }
        }
        if (direction == "RR"){
            runTime.reset();
            leftFront.setPower(p);
            leftBack.setPower(-p);
            rightFront.setPower(p);
            rightBack.setPower(-p);
            while (runTime.seconds()<magnitude){
                telemetry.addData("time",runTime.seconds());
                telemetry.update();
            }
        }
        if (direction == "LL"){
            runTime.reset();
            leftFront.setPower(-p);
            leftBack.setPower(p);
            rightFront.setPower(-p);
            rightBack.setPower(p);
            while (runTime.seconds()<magnitude){
                telemetry.addData("time",runTime.seconds());
                telemetry.update();
            }
        }
        if (direction == "FF"){
            runTime.reset();
            leftFront.setPower(.7*p);
            leftBack.setPower(.7*p);
            rightFront.setPower(.7*p);
            rightBack.setPower(.7*p);
            while (runTime.seconds()<magnitude){
                telemetry.addData("time",runTime.seconds());
                telemetry.update();
            }
        }
        if (direction == "BB"){
            runTime.reset();
            leftFront.setPower(-.7*p);
            leftBack.setPower(-.7*p);
            rightFront.setPower(-.7*p);
            rightBack.setPower(-.7*p);
            while (runTime.seconds()<magnitude){
                telemetry.addData("time",runTime.seconds());
                telemetry.update();
            }
        }

        if (direction == "CO"){
            runTime.reset();
            leftFront.setPower(-.7*p);
            leftBack.setPower(-.7*p);
            rightFront.setPower(+.7*p);
            rightBack.setPower(+.7*p);
            while (runTime.seconds()<magnitude){
                telemetry.addData("time",runTime.seconds());
                telemetry.update();
            }
        }

        if (direction == "CC"){
            runTime.reset();
            leftFront.setPower(+.7*p);
            leftBack.setPower(+.7*p);
            rightFront.setPower(-.7*p);
            rightBack.setPower(-.7*p);
            while (runTime.seconds()<magnitude){
                telemetry.addData("time",runTime.seconds());
                telemetry.update();
            }
        }

    }
    private void pitch(){//pitch the ball means game starts. lower down, leave the latch, come up right in front of the mineral

    }

    private void bat(){

    }

    private void homeRun(){

    }




    @Override
    public void runOpMode() {
        configureMotors();

        }

    }


