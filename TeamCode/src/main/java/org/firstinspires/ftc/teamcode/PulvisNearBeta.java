/**
 * Author: Benton Li '19
 * Version: 1.0
 *
 * */

/**
 * Sample() Introduction:
 * When Vuforia and Tensor Flow are initialized, the camera will look for the cube first.
 * The camera can only see two objects (center and right)
 * If we find the cube, we fetch the cube's x-coordinate in the image.
 *      If it's less than 650, then the cube is in the "center" (99% sure)
 *      If it's more than 650, then the cube is on the "right"  (99% sure)
 * If we can't find the cube, we look for the ball.
 *      If there are two balls, then the cube is on the "right" (99% sure)
 *      If there is only one ball, we fetch the ball's x-coordinate
 *          If it's less than 650, then the cube is on the "right" (49% sure)
 *              we assume we failed to detect the cube
 *          If it's more than 650, then the cube is in the "center" (49% sure)
 *              we assume we failed to detect the cube
 *      If there is none, we are out of our luck and we'll guess.
 *          the cube is in the "center" (33% sure)
 */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


@Autonomous(name = "Pulvis Near 2", group = "Beta")

public class PulvisNearBeta extends LinearOpMode {
    //preparation for these cool vuforia stuffs
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private static final String VUFORIA_KEY = "AUrb6t//////AAABmZ7sUnVME0wvu2pmOKRP5ilgE5gzg4vWVqHNhc0ef2FEwf9NlosWkTS81UmRvZ0UTHFjPeQYLKL6iY60ZJQcJFcMftURUv/1nA/9YELScRwzltxrUAFpfMA/VE9VTaNPTQQYUfm1Z1wUwY6fAJBwDvZJP+UBqPD0AJxz0Gf8jgcdCVgu4A7VtVdk1PRMTSUkHdOEm+VmXzpjxL9X4d/v81mx3aqJbVc6+qhUD53umiep/wCgl9WxHYY6ZEM2tuS7Eih3TexL24HLFvdEu79t24yTzCFz6du/hB12nfyySO78UWbdlusHuHIv0ZI5/IWh4RigF057FaLWc4F+EluGBkO0c6ygIaciN5fHPS9l7dtj";
    String goldLocation;
    int goldMineralX = -1;
    int silverMineral1X = -1;
    int silverMineral2X = -1;
    String confidence = "";


    //set up encoders
    static final double COUNTS_Per_REV    = 1160 ;
    static final double WHEEL_DIAMETER = 4 ; //in inches
    static final double COUNTS_Per_INCH = COUNTS_Per_REV/(WHEEL_DIAMETER*Math.PI);
    static final double COUNTS_Per_DEGREE = COUNTS_Per_REV/((240)/WHEEL_DIAMETER);
    //configure motors
    private DcMotor left = null;
    private DcMotor right = null;
    private DcMotor lift = null;
    private Servo launch = null;

    //st up time
    ElapsedTime runTime = new ElapsedTime();
    double checkpoint1 = 15;

    //set speed
    static final double speed = .3 ;

    public void sample() {
        runTime.reset();
        goldLocation = "N";
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setTargetPosition(-26000);
        lift.setPower(-1);
        initVuforia();
        initTfod();
        tfod.activate();
        while (opModeIsActive() && lift.isBusy()) {
            if (tfod != null) {
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    telemetry.addData("Gold Mineral Position", goldLocation);
                    telemetry.addData("Confidence", confidence);
                    if (updatedRecognitions.size() == 2 || updatedRecognitions.size() == 1 ) {
                        int goldMineralX = -1;
                        int silverMineral1X = -1;
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                goldMineralX = (int) recognition.getLeft();
                                if (goldMineralX < 650) {
                                    goldLocation = "C";
                                    confidence = "99%";
                                }
                                else if (goldMineralX > 650) {
                                    goldLocation = "R";
                                    confidence = "99%";
                                }
                            } else if (silverMineral1X == -1) {
                                silverMineral1X = (int) recognition.getLeft();
                            } else {
                                silverMineral2X = (int) recognition.getLeft();
                            }
                        }

                        if (goldMineralX ==-1) {
                            if (updatedRecognitions.size() == 2) {
                                goldLocation = "L";
                                confidence = "99%";
                            }
                            else if (silverMineral1X > 650) {
                                goldLocation = "C";
                                confidence = "49%";
                            }
                            else if(silverMineral1X != -1){
                                goldLocation = "R";
                                confidence = "49%";
                            }
                            else{
                                goldLocation = "C";
                                confidence = "33%";
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


    private void configureMotors() {

        left = hardwareMap.get(DcMotor.class, "mot0");
        right = hardwareMap.get(DcMotor.class, "mot1");
        lift = hardwareMap.get(DcMotor.class,"mot2");
        launch = hardwareMap.get(Servo.class,"ser");
        //set rotational direction

        left.setDirection(DcMotor.Direction.REVERSE);
        right.setDirection(DcMotor.Direction.FORWARD);
        lift.setDirection(DcMotor.Direction.FORWARD);
        launch.setDirection(Servo.Direction.REVERSE);


        waitForStart();
    }


    public void moveForward(double dX){//dx means displacement in inches
        runTime.reset();
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        left.setTargetPosition((int)(dX * COUNTS_Per_INCH));
        right.setTargetPosition((int)(dX * COUNTS_Per_INCH));

        left.setPower(speed);
        right.setPower(speed);
        while (opModeIsActive()&&left.isBusy()){
            telemetry.addData("Left",left.getCurrentPosition());
            telemetry.addData("Right",right.getCurrentPosition());
            telemetry.addData("goldlocation",goldLocation );
            telemetry.addData("gold value",goldMineralX );
            telemetry.addData("silver value",silverMineral1X );
            telemetry.update();
        }
        left.setPower(0);
        right.setPower(0);
    }

    public void moveBackward(double dX){//dx means displacement in inches
        runTime.reset();
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        left.setTargetPosition((int)(-dX * COUNTS_Per_INCH));
        right.setTargetPosition((int)(-dX * COUNTS_Per_INCH));

        left.setPower(-speed);
        right.setPower(-speed);
        while (opModeIsActive()&&left.isBusy()){
            telemetry.addData("Left",left.getCurrentPosition());
            telemetry.addData("Right",right.getCurrentPosition());
            telemetry.update();
        }
        left.setPower(0);
        right.setPower(0);
    }
    public void turnClockwise(double dA){//dx means displacement
        runTime.reset();
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        left.setTargetPosition((int)(dA * COUNTS_Per_DEGREE));
        right.setTargetPosition((int)(-dA * COUNTS_Per_DEGREE));

        left.setPower(speed);//power depends on the the robot and case studies are needed
        right.setPower(-speed);
        while (opModeIsActive()&&left.isBusy()){
            telemetry.addData("Left",left.getCurrentPosition());
            telemetry.addData("Right",right.getCurrentPosition());
            telemetry.update();
        }
        left.setPower(0);
        right.setPower(0);
    }
    public void turnCounterClockwise(double dA){//dx means displacement
        runTime.reset();
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RESET_ENCODERS);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        left.setTargetPosition((int)(-dA * COUNTS_Per_DEGREE));
        right.setTargetPosition((int)(dA * COUNTS_Per_DEGREE));

        left.setPower(-.5);//power depends on the the robot and case studies are needed
        right.setPower(.5);
        while (opModeIsActive()&&left.isBusy()){
            telemetry.addData("Left",left.getCurrentPosition());
            telemetry.addData("Right",right.getCurrentPosition());
            telemetry.update();
        }
        left.setPower(0);
        right.setPower(0);
    }
    private void pitch() {//pitch the ball means game starts. lower down, leave the latch, come up right in front of the mineral

    }

    private void bat(String location) {
        while (opModeIsActive()&&lift.isBusy()) {
            telemetry.addData("goldlocation",goldLocation );
        }
        lift.setPower(0);

        if (location == "L"|| location == "N") {

            turnClockwise(135-30);
            moveBackward(30);
            turnCounterClockwise(90);
            moveForward(25);
        }
        if (location == "R") {
            turnClockwise(180-20);
            moveBackward(30);
            turnClockwise(90);
            moveForward(25);
        }
        if (location == "C" ) {
            turnClockwise(180-50);
            moveBackward(33);
            turnClockwise(180-50);
            moveForward(15);



        }
    }

    private void homeRun() {
        launch.setPosition(-.6);
    }


    @Override
    public void runOpMode() {
        configureMotors();
        sample();
        bat(goldLocation);
        homeRun();



    }

}



