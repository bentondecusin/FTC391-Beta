/*
 * Author: Benton Li '19
 * Version: 1.0
 *
 * */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
@Disabled
@Autonomous(name="B Climb Up ")

public class BClimbUp extends OpMode{
    private DcMotor[] drive = new DcMotor[2];
    private DcMotor bentonSucks = null;
    private int counter = 0;
    
    @Override
    public void init() {
        drive[0] = hardwareMap.get(DcMotor.class, "mot0");
        drive[1] = hardwareMap.get(DcMotor.class, "mot1");
        bentonSucks = hardwareMap.get(DcMotor.class, "mot2");
        drive[0].setDirection(DcMotor.Direction.FORWARD);
        drive[1].setDirection(DcMotor.Direction.FORWARD);
        bentonSucks.setDirection(DcMotor.Direction.REVERSE);
        telemetry.addData("Status", "Initialized");

    }

    @Override
    public void init_loop() {
        
    }

    @Override
    public void start() {
        
    }
    public ElapsedTime runTime = new ElapsedTime();


    @Override
    public void loop() {
if(runTime.time() < 14 )
{
 bentonSucks.setPower(0.75);
}
else{
bentonSucks.setPower(0);}

       
    }
}
