
/**
 * Author: Benton Li '19
 * Version: 2.0
 *
 * */
package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

@Disabled
@Autonomous(name="Imu")

public class IMUNavigating extends LinearOpMode{
    BNO055IMU   imu;
    Orientation lastAngles = new Orientation();
    double direction;
    @Override
    public void runOpMode() throws InterruptedException{
        initIMU();
        turnCounterClockwise(50);
        turnClockwise(250);


    }
    public void turnClockwise(double dA){
        double targetAngle, currentAngle ;
        currentAngle = getCurrentAngle();
        targetAngle = getTargetAngle(dA);

        //setPowerX
        while (opModeIsActive()&&(currentAngle>targetAngle) )
        {   currentAngle = getCurrentAngle();
            telemetry.addData("Current Angle1",currentAngle );
            telemetry.addData("Target Angle", targetAngle);

            telemetry.update();
        }

        while (opModeIsActive()&&(currentAngle<targetAngle) )
        {   currentAngle = getCurrentAngle();
            telemetry.addData("Current Angle2",currentAngle );
            telemetry.addData("Target Angle", targetAngle);
            telemetry.update();
        }
        //setPower0
    }
    public void turnCounterClockwise(double dA){
        double targetAngle, currentAngle ;
        currentAngle = getCurrentAngle();
        targetAngle = getTargetAngle(-dA);

        //setPowerX


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
        //setPower0
    }

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


}