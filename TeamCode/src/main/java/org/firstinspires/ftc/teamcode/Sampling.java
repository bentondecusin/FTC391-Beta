/**
 * Author: Benton Li '19
 * Version: 2.0
 *
 * */

/**
 * Introduction:
 * When Vuforia and Tensor Flow are initialized, the camera will look for the cube first.
 * The camera can only see two objects (center and right)
 * If we find the cube, we fetch the cube's x-coordinate in the image.
 *      If it's less than 650, then the cube is in the "center" (99% sure)
 *      If it's more than 650, then the cube is on the "right"  (99% sure)
 * If we can't find the cube, we look for the ball.
 *      If there are two balls, then the cube is on the "right" (99% sure)
 *      If there is only one ball, we fetch the ball's x-coordinate
 *          If it's less than 650, then the cube is on the "right" (49% sure)
 *              we assume we failed to detect the cube
 *          If it's more than 650, then the cube is in the "center" (49% sure)
 *              we assume we failed to detect the cube
 *      If there is none, we are out of our luck and we'll guess.
 *          the cube is in the "center" (33% sure)
 */

package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


@Autonomous(name = "Sampling ", group = "Beta")

public class Sampling extends LinearOpMode {
    //preparation for these cool vuforia stuffs
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private static final String VUFORIA_KEY = "AUrb6t//////AAABmZ7sUnVME0wvu2pmOKRP5ilgE5gzg4vWVqHNhc0ef2FEwf9NlosWkTS81UmRvZ0UTHFjPeQYLKL6iY60ZJQcJFcMftURUv/1nA/9YELScRwzltxrUAFpfMA/VE9VTaNPTQQYUfm1Z1wUwY6fAJBwDvZJP+UBqPD0AJxz0Gf8jgcdCVgu4A7VtVdk1PRMTSUkHdOEm+VmXzpjxL9X4d/v81mx3aqJbVc6+qhUD53umiep/wCgl9WxHYY6ZEM2tuS7Eih3TexL24HLFvdEu79t24yTzCFz6du/hB12nfyySO78UWbdlusHuHIv0ZI5/IWh4RigF057FaLWc4F+EluGBkO0c6ygIaciN5fHPS9l7dtj";
    String goldLocation = "N";
    int goldMineralX = -1;
    int silverMineral1X = -1;
    int silverMineral2X = -1;
    String confidence = "";

    public void sample() {
        initVuforia();
        initTfod();
        tfod.activate();
        goldLocation = "N";
        while (opModeIsActive()) {
            if (tfod != null) {
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    telemetry.addData("Gold Mineral Position", goldLocation);
                    telemetry.addData("Confidence", confidence);
                    if (updatedRecognitions.size() == 2 || updatedRecognitions.size() == 1 ) {
                        int goldMineralX = -1;
                        int silverMineral1X = -1;
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                goldMineralX = (int) recognition.getLeft();
                                if (goldMineralX < 650) {
                                    goldLocation = "C";
                                    confidence = "99%";
                                }
                                else if (goldMineralX > 650) {
                                    goldLocation = "R";
                                    confidence = "99%";
                                }
                            } else if (silverMineral1X == -1) {
                                silverMineral1X = (int) recognition.getLeft();
                            } else {
                                silverMineral2X = (int) recognition.getLeft();
                            }
                        }

                        if (goldMineralX ==-1) {
                            if (updatedRecognitions.size() == 2) {
                                goldLocation = "L";
                                confidence = "99%";
                            }
                            else if (silverMineral1X > 650) {
                                goldLocation = "C";
                                confidence = "49%";
                            }
                            else if(silverMineral1X != -1){
                                goldLocation = "R";
                                confidence = "49%";
                            }
                            else{
                                goldLocation = "C";
                                confidence = "33%";
                            }
                        }
                    }
                    telemetry.update();
                }
            }
        }
    }




    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.FRONT;
        parameters.fillCameraMonitorViewParent = true;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);


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




