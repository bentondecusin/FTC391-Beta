/**
 * Author: Benton Li '19
 * Version: 2.0
 *
 * */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="Beta Driving")

public class BDriving extends OpMode{

    //motors
    private Servo ser1 = null;
    private DcMotor left = null;
    private DcMotor right = null;
    private DcMotor lift = null;

    //set up encoders
    static final double COUNTS_PER_REV    = 1140 ;
    static final double WHEEL_DIAMETER = 4 ; //in inches
    static final double COUNTS_PER_INCH = COUNTS_PER_REV/(WHEEL_DIAMETER*Math.PI);
    static final double COUNTS_Per_DEGREE = COUNTS_PER_REV/((130)/WHEEL_DIAMETER);

    private String speedStatus ;

    private double speed = .5 ;
    private Boolean nutsTooMuch = false;
    @Override
    public void init() {
        left = hardwareMap.get(DcMotor.class, "mot0");
        right = hardwareMap.get(DcMotor.class, "mot1");
        lift = hardwareMap.get(DcMotor.class, "mot2");

        left.setDirection(DcMotor.Direction.FORWARD);
        right.setDirection(DcMotor.Direction.REVERSE);
        lift.setDirection(DcMotor.Direction.REVERSE);

        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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
        if (gamepad1.b == true){
            speed = 1 ;
            speedStatus = "Maximum Overdrive~";
        }
        if (gamepad1.right_stick_button){
            speed = .2;
            speedStatus = "Slow Speed";
        }
        if(gamepad1.a == true){
            speed = 0.5;
            speedStatus = "Medium Speed";
        }
        if (gamepad1.left_bumper){
            //   liftUp();
        }
    }


    @Override
    public void loop() {
        checkKeys();
        left.setPower(speed*(gamepad1.left_stick_y-gamepad1.right_stick_x));
        right.setPower(speed*(gamepad1.left_stick_y+gamepad1.right_stick_x));
        ser1.setPosition(gamepad1.left_trigger);
        telemetry.addData("Speed:",speed);
        telemetry.addData("Speed Status",speedStatus);
      //  telemetry.addData("debug lift", lift.getCurrentPosition());

        telemetry.update();
    }
}
