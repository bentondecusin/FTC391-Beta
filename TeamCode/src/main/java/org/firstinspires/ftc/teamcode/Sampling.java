/*
 * Author: Benton Li '19
 * Version: 1.0
 *
 * */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCharacteristics;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


@Autonomous(name = "Just Sampling", group = "Beta")

public class Sampling extends LinearOpMode {
    //preparation for these cool vuforia stuffs
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private static final String VUFORIA_KEY = "AUrb6t//////AAABmZ7sUnVME0wvu2pmOKRP5ilgE5gzg4vWVqHNhc0ef2FEwf9NlosWkTS81UmRvZ0UTHFjPeQYLKL6iY60ZJQcJFcMftURUv/1nA/9YELScRwzltxrUAFpfMA/VE9VTaNPTQQYUfm1Z1wUwY6fAJBwDvZJP+UBqPD0AJxz0Gf8jgcdCVgu4A7VtVdk1PRMTSUkHdOEm+VmXzpjxL9X4d/v81mx3aqJbVc6+qhUD53umiep/wCgl9WxHYY6ZEM2tuS7Eih3TexL24HLFvdEu79t24yTzCFz6du/hB12nfyySO78UWbdlusHuHIv0ZI5/IWh4RigF057FaLWc4F+EluGBkO0c6ygIaciN5fHPS9l7dtj";
    String goldLocation;
    int goldMineralX = -1;
    int silverMineral1X = -1;
    int silverMineral2X = -1;

    public void sample() {

        initVuforia();
        initTfod();
        tfod.activate();

        goldLocation = "N";
        while (opModeIsActive() ) {
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    telemetry.addData("goldlocation",goldLocation );

                    if (updatedRecognitions.size() == 2) {

                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                goldMineralX = (int) recognition.getTop();
                            } else if (silverMineral1X == -1) {
                                silverMineral1X = (int) recognition.getTop();
                            } else {
                                silverMineral2X = (int) recognition.getTop();
                            }
                        }
                        if (silverMineral1X != -1 && silverMineral2X != -1) {
                            telemetry.addData("Gold Mineral Position", "Left");
                            goldLocation = "L";
                            break;
                        }
                        if (goldMineralX != -1) {
                            if (goldMineralX < silverMineral1X) {
                                telemetry.addData("Gold Mineral Position", "Center");
                                goldLocation = "C";
                                break;
                            }
                        }
                        if (goldMineralX != -1) {
                            if (goldMineralX > silverMineral1X) {
                                telemetry.addData("Gold Mineral Position", "Right");
                                goldLocation = "R";
                                break;
                            }
                        }


                    }
                }
                telemetry.update();
            }
        }
        //Four endings for this story: left, right, center, nothing
    }




    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        CameraDevice.getInstance().setFocusMode(1);

    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);

    }

    @Override
    public void runOpMode() {
        sample();
    }

}



