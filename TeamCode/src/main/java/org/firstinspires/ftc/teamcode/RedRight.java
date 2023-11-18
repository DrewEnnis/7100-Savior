package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.drive.MecanumDrive2024;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.Locale;

@Autonomous(name = "RedRight", group = "")
public class RedRight extends LinearOpMode {
    private static final int NUMLOOPS = 3 ;
    //test1

    private MecanumDrive2024 drive;
    private actuatorUtils utils;
    private Servo dump = null; //Located on Expansion Hub- Servo port 0
    private Servo gripper = null; //Located on Expansion Hub- Servo port 0
    private DcMotor arm = null;

    static final float MAX_SPEED = 1.0f;
    static final float MIN_SPEED = 0.4f;
    static final int ACCEL = 75;  // Scaling factor used in accel / decel code.  Was 100!
    public double desiredHeading;

    Orientation angles;
    Acceleration gravity;
    private OpenCvCamera webCam;
    private boolean isCameraStreaming = false;
    Pipeline2023 modifyPipeline = new Pipeline2023(false);

    private int resultROI = 3;

    private boolean done = false;

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new MecanumDrive2024(hardwareMap);
        utils = new actuatorUtils();
        arm = hardwareMap.get(DcMotor.class, "arm");
        dump = hardwareMap.get(Servo.class, "Dump");
        Pose2d startPose = new Pose2d(-55, -2,0);
        drive.setPoseEstimate(startPose);
        //TrajectorySequence aSeq = autoSeq(startPose);


        //Reverse the arm direction so it moves in the proper direction
        arm.setDirection(DcMotor.Direction.REVERSE);

        desiredHeading = getHeading();

        utils.initializeActuator(arm, gripper, dump);

        Long startTime = System.currentTimeMillis();
        Long currTime = startTime;

        initOpenCV();

        utils.dumpClose();

        waitForStart();
        currTime = System.currentTimeMillis();
        startTime = currTime;
        sleep(5000);
        if (resultROI == 3) {

            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            done = false;
            while (!done && opModeIsActive()) {
                //if (currTime - startTime < 500) {
                //    telemetry.addData("Camera: ", "Waiting to make sure valid data is incoming");
                //} else {
                telemetry.addData("Time Delta: ", (currTime - startTime));
                resultROI = modifyPipeline.getResultROI();
                if (resultROI == 0) {
                    telemetry.addData("Resulting ROI: ", "Left");
                    done = true;
                } else if (resultROI == 1) {
                    telemetry.addData("Resulting ROI: ", "Middle");
                    done = true;
                } else if (resultROI == 2) {
                    telemetry.addData("Resulting ROI: ", "Right");
                    done = true;
                } else {
                    telemetry.addData("Resulting ROI: ", "Something went wrong.");
                }
                //}
                telemetry.update();
                currTime = System.currentTimeMillis();

            }

        }
        telemetry.update();
        done = false;

        //lift arm up
        while (((currTime - startTime) < 30000) && !done && opModeIsActive()) {
            //autoSeq();
            telemetry.addData("IM at ", getHeading());
            telemetry.update();
            //if (!isStopRequested())
            //actuatorUtils.armPole(actuatorUtils.ArmLevel.ZERO,false);
            if (resultROI == 0) {
                LeftPath();
            } else if (resultROI == 1) {
                MiddlePath();
            } else {
                RightPath();
            }
            //set arm to lowest position
       /*     while (gripperSensor.getDistance(DistanceUnit.INCH)>2 && !isStopRequested())
            {
                telemetry.addData("ARM Position = ", arm.getCurrentPosition());
                telemetry.update();
                arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                arm.setPower(-0.7);
            }
            //disabling and resetting arm
            arm.setPower(0);
            arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
*/
            currTime = System.currentTimeMillis();
            done = true;
        }
    }

    private void LeftPath() {
        Pose2d pose = drive.getPoseEstimate();
        pose = new Pose2d(-30,-12,Math.toRadians(-180));
        TrajectorySequence seq = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(pose)
                .build();
        drive.followTrajectorySequence(seq);
        pose = new Pose2d(-30,-2,Math.toRadians(-180));
        seq = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(pose)
                .build();
        drive.followTrajectorySequence(seq);
        utils.dumpOpen();
        sleep(1000);
        pose = new Pose2d(-30, -6, Math.toRadians(-180));
        seq = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(pose)
                .build();
        drive.followTrajectorySequence(seq);
        sleep(1000);
        utils.dumpClose();
        sleep(1000);
        pose = new Pose2d(-52, -50, Math.toRadians(-90));
        seq = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(pose)
                .build();
        drive.followTrajectorySequence(seq);


    }
    private void MiddlePath() {
        Pose2d pose = drive.getPoseEstimate();
        pose = new Pose2d(-17,-17,Math.toRadians(-180));
        TrajectorySequence seq = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(pose)
                .build();
        drive.followTrajectorySequence(seq);
        sleep(1000);
        utils.dumpOpen();
        sleep(1000);
        pose = new Pose2d(-17, -22, Math.toRadians(-180));
        seq = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(pose)
                .build();
        drive.followTrajectorySequence(seq);
        sleep(1000);
        utils.dumpClose();
        pose = new Pose2d(-45, -22, Math.toRadians(-180));
        seq = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(pose)
                .build();
        drive.followTrajectorySequence(seq);
        pose = new Pose2d(-50, -50, Math.toRadians(-90));
        seq = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(pose)
                .build();
        drive.followTrajectorySequence(seq);

    }
    private void RightPath() {
        Pose2d pose = drive.getPoseEstimate();
        pose = new Pose2d(-28,-24.5,Math.toRadians(180));
        TrajectorySequence seq = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(pose)
                .build();
        drive.followTrajectorySequence(seq);
        sleep(1000);
        utils.dumpOpen();
        sleep(1000);
        pose = new Pose2d(-28, -28.5, Math.toRadians(180));
        seq = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(pose)
                .build();
        drive.followTrajectorySequence(seq);
        sleep(1000);
        utils.dumpClose();
        pose = new Pose2d(-45, -24, Math.toRadians(180));
        seq = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(pose)
                .build();
        drive.followTrajectorySequence(seq);
        pose = new Pose2d(-50, -50, Math.toRadians(-90));
        seq = drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .lineToLinearHeading(pose)
                .build();
        drive.followTrajectorySequence(seq);

    }

    private void initOpenCV() {
       int cameraMonitorViewId2 = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId",
                "id",
                hardwareMap.appContext.getPackageName());
        // For a webcam (uncomment below)
        webCam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId2);
        // For a phone camera (uncomment below)
        // webCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId2);
        webCam.setPipeline(modifyPipeline);
        webCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webCam.startStreaming(960, 544, OpenCvCameraRotation.UPRIGHT);
                telemetry.addData("Pipeline: ", "Initialized");
                telemetry.update();
                isCameraStreaming = true;
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("Error: ", "Something went wrong :(");
                telemetry.update();
            }
        });
    }

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees) {
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }

    public double getHeading() {
        double angle = drive.getRawExternalHeading();
        return angle;
    }
    private float convertRad(int input) {
        float x;
        x=input/180f;
        x*=Math.PI;
        x*=(1);
        return x;
    }
}


