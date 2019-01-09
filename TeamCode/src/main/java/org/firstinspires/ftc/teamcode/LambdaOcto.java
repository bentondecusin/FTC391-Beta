package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Lambda-Octo")

public class LambdaOcto extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    //a bunch of motors
    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightBack = null;
    int p = 1; //default power
    float x = 0;//default x-coord
    float y = 0;//default y-coord
    String direction = "static";//default direction
    public void regulateSpeed(){

    }
    public String directionSetting(float x, float y/*the parameters are x and y*/){

        if (x >0.4 & y<-0.4/*downright*/){
            return direction = "RB";
        }
        if (x<-0.4 & y<-0.4/*downleft*/){
            return direction = "LB";
        }
        if (x> 0.4 & y> 0.4/*upright*/){
            return direction = "RF";
        }
        if (x<-0.4 & y>0.4/*upleft*/){
            return direction = "LF";
        }

        if (x > 0.4 & y<0.4 & y>-0.4/*right*/){
            return direction = "RR";
        }
        if (x <-0.4 & y <0.4 & y>-0.4 /*left*/){
            return direction = "LL";
        }
        if (x<0.4 & x > -0.4 & y >0.4/*up*/){
            return direction = "FF";
        }
        if (x<0.4 & x>-0.4 & y<-0.4/*down*/){
            return direction ="BB";
        }
        if (y <0.4 & y > -0.4 & x < 0.4 & x > -0.4/*null*/){
            return direction = "static";
        }

        if (gamepad1.dpad_left){
            return direction = "CO";//stands for counter clockwise
        }
        if (gamepad1.dpad_right){
            return direction = "CL";//stands for clockwise
        }


        return direction;
    }
    public void move(){

        if (direction == "static"){
            leftFront.setPower(0);
            leftBack.setPower(0);
            rightFront.setPower(0);
            rightBack.setPower(0);
        }
        if (direction == "LF"){
            leftFront.setPower(0);
            leftBack.setPower(p);
            rightFront.setPower(0);
            rightBack.setPower(p);
        }
        if (direction == "RF"){
            leftFront.setPower(p);
            leftBack.setPower(0);
            rightFront.setPower(p);
            rightBack.setPower(0);
        }
        if (direction == "LB"){
            leftFront.setPower(-p);
            leftBack.setPower(0);
            rightFront.setPower(-p);
            rightBack.setPower(0);
        }
        if (direction == "RB"){
            leftFront.setPower(0);
            leftBack.setPower(-p);
            rightFront.setPower(0);
            rightBack.setPower(-p);
        }
        if (direction == "RR"){
            leftFront.setPower(p);
            leftBack.setPower(-p);
            rightFront.setPower(p);
            rightBack.setPower(-p);
        }
        if (direction == "LL"){
            leftFront.setPower(-p);
            leftBack.setPower(p);
            rightFront.setPower(-p);
            rightBack.setPower(p);
        }
        if (direction == "FF"){
            leftFront.setPower(.7*p);
            leftBack.setPower(.7*p);
            rightFront.setPower(.7*p);
            rightBack.setPower(.7*p);}
        if (direction == "BB"){
            leftFront.setPower(-.7*p);
            leftBack.setPower(-.7*p);
            rightFront.setPower(-.7*p);
            rightBack.setPower(-.7*p);
        }

        if (direction == "CO"){
            leftFront.setPower(-.7*p);
            leftBack.setPower(-.7*p);
            rightFront.setPower(+.7*p);
            rightBack.setPower(+.7*p);
        }

        if (direction == "CC"){
            leftFront.setPower(+.7*p);
            leftBack.setPower(+.7*p);
            rightFront.setPower(-.7*p);
            rightBack.setPower(-.7*p);
        }

    }

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        //configuration
        leftFront = hardwareMap.get(DcMotor.class, "mot0");
        leftBack = hardwareMap.get(DcMotor.class, "mot2");
        rightFront = hardwareMap.get(DcMotor.class, "mot1");
        rightBack = hardwareMap.get(DcMotor.class, "mot3");

        //set rotational direction
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        rightBack.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()/*Following expressions will keep being executed*/) {
            float horizontal = gamepad1.right_stick_x;//parameter horizontal
            float vertical = - gamepad1.right_stick_y;//parameter vertical and sign conventional***
            direction = directionSetting(horizontal,vertical);//input
            move();
        }
        // Show the elapsed game time and wheel power.


        telemetry.update();

    }
}
