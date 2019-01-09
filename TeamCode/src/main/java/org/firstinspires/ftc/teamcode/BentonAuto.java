package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="Benton Auto")

public class BentonAuto extends LinearOpMode {
    private DcMotor[] drive = new DcMotor[2];
    private DcMotor lift = null;
    private int counter = 0;

    @Override
    public void runOpMode() {
        drive[0] = hardwareMap.get(DcMotor.class, "mot0");
        drive[1] = hardwareMap.get(DcMotor.class, "mot1");
        lift = hardwareMap.get(DcMotor.class, "mot2");
        drive[0].setDirection(DcMotor.Direction.FORWARD);
        drive[1].setDirection(DcMotor.Direction.FORWARD);
        lift.setDirection(DcMotor.Direction.REVERSE);
        telemetry.addData("Status", "Initialized");
        ElapsedTime runTime = new ElapsedTime();
        if (runTime.time() < 15.2) {
            lift.setPower(-0.75);//lefting mech extending;robot lowering down
        }
        if (runTime.time() > 15.2 && runTime.time() < 18.2) {
            lift.setPower(0);//lifting mech stops
        }
        if (runTime.time() > 18.2 && runTime.time() < 25.2) {
            lift.setPower(0.75);//lifting mech retracts
        }
        if (runTime.time() > 25.2) {
            lift.setPower(0);//lifting mech stops
        }


        if (runTime.time() > 15.2 && runTime.time() < 16.2) {

            drive[0].setPower(-5);
            drive[1].setPower(-5);//counter clockwise turning
        }
        if (runTime.time() > 16.2 && runTime.time() < 17.2) {

            drive[0].setPower(+5);
            drive[1].setPower(+5);//moveforward
        }
        if (runTime.time() > 17.2 && runTime.time() < 23) {

            drive[0].setPower(0);//stopping moving

            drive[1].setPower(0);//stopping moving
        }
        telemetry.addData("Status", "Initialized");
        telemetry.update();
// Wait for the game to start (driver presses PLAY) waitForStart();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Running");
            telemetry.update();
        }


    }
}