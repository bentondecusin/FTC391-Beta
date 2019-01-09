/*
 * Author: Benton Li '19
 * Version: 1.0
 *
 * */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name = "Angular Testing")

public class AngularTesting extends LinearOpMode {
    //preparation for these cool vuforia stuffs

    //configure motors

    private DcMotor left = null;
    private DcMotor right = null;
    private DcMotor lift = null;
    private Servo launch = null;


    ElapsedTime runTime = new ElapsedTime();
    double checkpoint1 = 10;




    private void configureMotors() {

        left = hardwareMap.get(DcMotor.class, "mot0");
         right = hardwareMap.get(DcMotor.class, "mot1");
         lift = hardwareMap.get(DcMotor.class,"mot2");
         launch = hardwareMap.get(Servo.class,"ser");
        //set rotational direction

         left.setDirection(DcMotor.Direction.REVERSE);
         right.setDirection(DcMotor.Direction.FORWARD);
         lift.setDirection(DcMotor.Direction.FORWARD);


        waitForStart();
    }

    public void linearForward(double dX){//dx means displacement
        runTime.reset();
        left.setPower(.5);//power depends on the the robot and case studies are needed
        right.setPower(.5);
        while (runTime.seconds()<dX){
            telemetry.addData("time",runTime.seconds());
            telemetry.update();
        }
        left.setPower(0);
        right.setPower(0);
    }
    public void angularClockwise(double dA){//dA means change in angular degree
        runTime.reset();;
        left.setPower(.5);
        right.setPower(-.5);//power depends on the the robot and case studies are needed
        while (runTime.seconds()<dA){
            telemetry.addData("time",runTime.seconds());
            telemetry.update();
        }
        left.setPower(0);
        right.setPower(0);
    }







    @Override
    public void runOpMode() {
        configureMotors();
        angularClockwise(2);
        stop();
        }

    }





