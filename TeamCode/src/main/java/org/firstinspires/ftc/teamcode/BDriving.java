package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="B Driving ")

public class BDriving extends OpMode{
    private DcMotor left = null;
    private DcMotor right = null;
    private DcMotor dick = null;

    private int counter = 0;
    private double speed = 1 ;

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
    }

    @Override
    public void loop() {
        checkKeys();
        left.setPower(speed*(-gamepad1.left_stick_x+gamepad1.left_stick_y));
        right.setPower(speed*(-gamepad1.left_stick_x-gamepad1.left_stick_y));
        dick.setPower(gamepad1.right_stick_y);
        telemetry.addData("Speed:",speed);
        telemetry.update();
    }
}
