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

    //set up motors
    private DcMotor left = null;
    private DcMotor right = null;
    private DcMotor lift = null;
    private Servo gate = null;

    //set up speed
    private String speedStatus ;
    private double speed = .5 ;


    @Override
    public void init() {
        left = hardwareMap.get(DcMotor.class, "mot0");
        right = hardwareMap.get(DcMotor.class, "mot1");
        lift = hardwareMap.get(DcMotor.class, "mot2");
        gate = hardwareMap.get(Servo.class, "ser1");

        left.setDirection(DcMotor.Direction.FORWARD);
        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        right.setDirection(DcMotor.Direction.REVERSE);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lift.setDirection(DcMotor.Direction.REVERSE);
        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Initialized");

    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {

    }


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

        if(gamepad1.left_bumper == true){

            gate.setPosition(1.2); //open gate
        }
        if(gamepad1.right_bumper == true){

            gate.setPosition(.2); //close gate. secure mineral

        }




    }



    @Override
    public void loop() {
        checkKeys();

        left.setPower(speed*(gamepad1.left_stick_y-gamepad1.right_stick_x));
        right.setPower(speed*(gamepad1.left_stick_y+gamepad1.right_stick_x));
        lift.setPower(gamepad1.left_trigger - gamepad1.right_trigger);

        telemetry.addData("servo", gate.getPosition());
        telemetry.addData("Speed:",speed);
        telemetry.addData("Speed Status",speedStatus);

        telemetry.update();
    }
}
