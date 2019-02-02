/**
 * Author: Benton Li '19
 * Version: 2.0
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

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


@Autonomous(name = "Vermis Near", group = "Beta")

public class VermisNearBeta extends LinearOpMode {
    //vuforia thingy
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private static final String VUFORIA_KEY = "AUrb6t//////AAABmZ7sUnVME0wvu2pmOKRP5ilgE5gzg4vWVqHNhc0ef2FEwf9NlosWkTS81UmRvZ0UTHFjPeQYLKL6iY60ZJQcJFcMftURUv/1nA/9YELScRwzltxrUAFpfMA/VE9VTaNPTQQYUfm1Z1wUwY6fAJBwDvZJP+UBqPD0AJxz0Gf8jgcdCVgu4A7VtVdk1PRMTSUkHdOEm+VmXzpjxL9X4d/v81mx3aqJbVc6+qhUD53umiep/wCgl9WxHYY6ZEM2tuS7Eih3TexL24HLFvdEu79t24yTzCFz6du/hB12nfyySO78UWbdlusHuHIv0ZI5/IWh4RigF057FaLWc4F+EluGBkO0c6ygIaciN5fHPS9l7dtj";
    private String goldLocation;
    private int goldMineralX = -1;
    private int silverMineral1X = -1;
    private int silverMineral2X = -1;
    private String confidence = "";

    //IMU thingy
    BNO055IMU imu;

    private double bufferAngle=20;
    private double speed = .5;

    //set up encoders
    static final double COUNTS_Per_REV    = 1160 ;
    static final double WHEEL_DIAMETER = 4 ; //in inches
    static final double COUNTS_Per_INCH = COUNTS_Per_REV/(WHEEL_DIAMETER*Math.PI);


    //configure motors
    private DcMotor left = null;
    private DcMotor right = null;
    private DcMotor lift = null;
    private Servo launch = null;
    private Servo gate = null;
    //st up time
    ElapsedTime runTime = new ElapsedTime();
    double checkpoint1 = 4;

    public void land(){
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setTargetPosition(25000);
        lift.setPower(1);
        while(opModeIsActive()&&lift.isBusy()){
            telemetry.addData("Status","Landing" );
        }
        lift.setPower(0);

    }
    public void sample() {

        goldLocation = "N";

        //while landing, initialize vuforia and imu

        tfod.activate();
        runTime.reset();
        while (opModeIsActive()&&runTime.time()<checkpoint1) {
            if (tfod != null) {
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    telemetry.addData("Gold Mineral Position", goldLocation);
                    telemetry.addData("Confidence", confidence);
                    telemetry.addData("Status","Sampling" );
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

    //--------------------------Vuforia&Tfod---------------------------------------Start
    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.FRONT;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
    //--------------------------Vuforia&Tfod---------------------------------------End

    //--------------------------Motor, servo, encoder---------------------------------------Start
    private void configureMotors() {

        left = hardwareMap.get(DcMotor.class, "mot0");
        right = hardwareMap.get(DcMotor.class, "mot1");
        lift = hardwareMap.get(DcMotor.class,"mot2");
        launch = hardwareMap.get(Servo.class,"ser0");
        gate = hardwareMap.get(Servo.class,"ser1");
        //set rotational direction

        left.setDirection(DcMotor.Direction.REVERSE);
        right.setDirection(DcMotor.Direction.FORWARD);
        lift.setDirection(DcMotor.Direction.FORWARD);
        launch.setDirection(Servo.Direction.REVERSE);
        gate.setPosition(1);

    }
    //--------------------------Motor, servo, encoder---------------------------------------End

    //--------------------------Movement---------------------------------------Start
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
    public void turnClockwise(double dA){
        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        double targetAngle, currentAngle ;
        currentAngle = getCurrentAngle();
        targetAngle = getTargetAngle(dA);
        left.setPower(speed);//power depends on the the robot and case studies are needed
        right.setPower(-speed);
        while (opModeIsActive()&&(currentAngle>targetAngle) )
        {   currentAngle = getCurrentAngle();
            telemetry.addData("Current Angle1",currentAngle );
            telemetry.addData("Target Angle", targetAngle);
            telemetry.update();
        }
        while (opModeIsActive()&&(currentAngle<(targetAngle-bufferAngle)) )
        {   currentAngle = getCurrentAngle();
            telemetry.addData("Current Angle2",currentAngle );
            telemetry.addData("Target Angle", targetAngle);
            telemetry.update();
        }
        left.setPower(0);
        right.setPower(0);
    }
    public void turnCounterClockwise(double dA){
        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        double targetAngle, currentAngle ;
        currentAngle = getCurrentAngle();
        targetAngle = getTargetAngle(-dA+bufferAngle-2);
        left.setPower(-speed);//power depends on the the robot and case studies are needed
        right.setPower(speed);
        while (opModeIsActive()&&(currentAngle<targetAngle) )
        {   currentAngle = getCurrentAngle();
            telemetry.addData("Current Angle2",currentAngle );
            telemetry.addData("Target Angle", targetAngle);
            telemetry.update();
        }
        while (opModeIsActive()&&(currentAngle>targetAngle) )
        {   currentAngle = getCurrentAngle();
            telemetry.addData("Current Angle1",currentAngle );
            telemetry.addData("Target Angle", targetAngle);

            telemetry.update();
        }

        left.setPower(0);
        right.setPower(0);

    }

    //--------------------------Movement---------------------------------------End


//--------------------------IMU---------------------------------------Start

    public void initIMU(){
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    public double getTargetAngle(double delta){
        double direction;
        direction = delta +1 - imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        if (direction < 0){
            direction += 360;
        }
        else if(direction>360){
            direction -= 360;
        }
        return direction;
    }

    private double getCurrentAngle()
    {   double direction;
        direction = 1-imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        if (direction < 0){
            direction += 360;
        }
        if (direction > 360){
            direction -= 360;
        }
        return direction;
    }

    //--------------------------IMU---------------------------------------End


    private void pitch() {//pitch the ball means game starts. lower down, leave the latch, come up right in front of the mineral

    }

    private void bat(String location) {

        runTime.reset();
        if (location == "L"|| location == "N") {

            turnClockwise(135);
            sleep(500);
            moveBackward(33);
            turnCounterClockwise(90);
            sleep(500);
            moveForward(35);
            sleep(500);
            turnCounterClockwise(55);
            sleep(500);
            launch.setPosition(.6);
            sleep(500);

            turnClockwise(60);
            sleep(500);


        }
        if (location == "R") {
            turnClockwise(45);
            sleep(500);
            moveForward(41);
            turnCounterClockwise(90);
            sleep(500);
            moveForward(35);
            launch.setPosition(-.6);
            sleep(500);

        }
        if (location == "C" ) {
            turnClockwise(180);
            sleep(500);
            moveBackward(46);
            turnClockwise(135);
            sleep(500);
            launch.setPosition(-.6);
            sleep(1000);
            turnCounterClockwise(20);// we are using the wall as a tool
            sleep(500);




        }
    }



    public void initialization(){
        configureMotors();
        initIMU();
        initVuforia();
        initTfod();


    }

    public void sprint(){
        runTime.reset();
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        left.setTargetPosition((int)(-120 * COUNTS_Per_INCH));
        right.setTargetPosition((int)(-120 * COUNTS_Per_INCH));

        left.setPower(-1);
        right.setPower(-1);
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
    @Override
    public void runOpMode() {
        initialization();
        waitForStart();

        land();
        sample();
        bat(goldLocation);
        sprint();



    }

}



