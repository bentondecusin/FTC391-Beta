package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="B Driving ")

public class BDriving extends OpMode{
    private DcMotor[] drive = new DcMotor[2];
    private DcMotor dick = null;
    private int counter = 0;

    @Override
    public void init() {
        drive[0] = hardwareMap.get(DcMotor.class, "mot0");
        drive[1] = hardwareMap.get(DcMotor.class, "mot1");
        dick = hardwareMap.get(DcMotor.class, "mot2");
        drive[0].setDirection(DcMotor.Direction.FORWARD);
        drive[1].setDirection(DcMotor.Direction.FORWARD);
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

    }

    @Override
    public void loop() {
        checkKeys();
        drive[0].setPower(-gamepad1.left_stick_x+gamepad1.left_stick_y);
        drive[1].setPower(-gamepad1.left_stick_x-gamepad1.left_stick_y);
        dick.setPower(gamepad1.right_stick_y);
    }
}
