package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Beta-Driving")

public class BDriving extends OpMode{
    private DcMotor left = null;
    private DcMotor right = null;
    private DcMotor dick = null;
    //set up encoders
    static final double COUNTS_Per_REV    = 1140 ;
    static final double WHEEL_DIAMETER = 4 ; //in inches
    static final double COUNTS_Per_INCH = COUNTS_Per_REV/(WHEEL_DIAMETER*Math.PI);
    static final double COUNTS_Per_DEGREE = COUNTS_Per_REV/((130)/WHEEL_DIAMETER);

    private int counter = 0;
    private double speed = .5 ;

    @Override
    public void init() {
        left = hardwareMap.get(DcMotor.class, "mot0");
        right = hardwareMap.get(DcMotor.class, "mot1");
        dick = hardwareMap.get(DcMotor.class, "mot2");
        left.setDirection(DcMotor.Direction.FORWARD);
        right.setDirection(DcMotor.Direction.REVERSE);
        dick.setDirection(DcMotor.Direction.REVERSE);
        telemetry.addData("Status", "Initialized");

    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {

    }
    public ElapsedTime runTime = new ElapsedTime();

    public void checkKeys(){
        if (gamepad1.x == true){
            speed = speed + 0.1;
        }
        if (gamepad1.y == true){
            speed = speed + 0.1;
        }
        if (gamepad1.left_bumper){
            liftUp();
        }
    }

    public void liftUp(){
        dick.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        dick.setTargetPosition((int)(10 * COUNTS_Per_INCH));
        dick.setPower(speed);
        while (dick.isBusy()){
            telemetry.addData("Climbing",100*dick.getCurrentPosition()/(10 * COUNTS_Per_INCH)+"%");
            telemetry.update();
        }
        dick.setPower(0);
    }


    @Override
    public void loop() {
        checkKeys();
        left.setPower(speed*(-gamepad1.left_stick_x+gamepad1.right_stick_y));
        right.setPower(speed*(-gamepad1.left_stick_x-gamepad1.right_stick_y));
        dick.setPower(gamepad1.left_trigger - gamepad1.right_trigger);
        telemetry.addData("Speed:",speed);

        telemetry.update();
    }
}
