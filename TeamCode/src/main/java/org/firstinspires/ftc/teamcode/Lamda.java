/*
 * Author: Benton Li '19
 * Version: 1.0
 *
 * */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@Disabled
@Autonomous(name="Lamda")

public class Lamda extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor FL = null;
    private DcMotor FR = null;
    private DcMotor BL = null;
    private DcMotor BR = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        FL  = hardwareMap.get(DcMotor.class, "mot0");
        FR  = hardwareMap.get(DcMotor.class, "mot1");
        BL = hardwareMap.get(DcMotor.class, "mot2");
        BR = hardwareMap.get(DcMotor.class, "mot3");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        FL.setDirection(DcMotor.Direction.REVERSE);
        FR.setDirection(DcMotor.Direction.FORWARD);
        BL.setDirection(DcMotor.Direction.REVERSE);
        BR.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry

                FL.setPower(gamepad1.right_stick_x);
                FR.setPower(-gamepad1.right_stick_y);
                BL.setPower(gamepad1.right_stick_y);
                BR.setPower(-gamepad1.right_stick_x);


            }




            // Show the elapsed game time and wheel power.


            telemetry.update();

    }
}
