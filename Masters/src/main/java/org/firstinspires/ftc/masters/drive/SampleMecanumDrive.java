package org.firstinspires.ftc.masters.drive;

import static org.firstinspires.ftc.masters.CSCons.clawArmTransfer;
import static org.firstinspires.ftc.masters.CSCons.clawClosed;
import static org.firstinspires.ftc.masters.CSCons.clawOpen;
import static org.firstinspires.ftc.masters.CSCons.clawTransfer;
import static org.firstinspires.ftc.masters.CSCons.openHook;
import static org.firstinspires.ftc.masters.CSCons.openMicroHook;
import static org.firstinspires.ftc.masters.drive.DriveConstants.MAX_ACCEL;
import static org.firstinspires.ftc.masters.drive.DriveConstants.MAX_ANG_ACCEL;
import static org.firstinspires.ftc.masters.drive.DriveConstants.MAX_ANG_VEL;
import static org.firstinspires.ftc.masters.drive.DriveConstants.MAX_VEL;
import static org.firstinspires.ftc.masters.drive.DriveConstants.MOTOR_VELO_PID;
import static org.firstinspires.ftc.masters.drive.DriveConstants.RUN_USING_ENCODER;
import static org.firstinspires.ftc.masters.drive.DriveConstants.TRACK_WIDTH;
import static org.firstinspires.ftc.masters.drive.DriveConstants.encoderTicksToInches;
import static org.firstinspires.ftc.masters.drive.DriveConstants.kA;
import static org.firstinspires.ftc.masters.drive.DriveConstants.kStatic;
import static org.firstinspires.ftc.masters.drive.DriveConstants.kV;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.drive.DriveSignal;
import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.followers.HolonomicPIDVAFollower;
import com.acmerobotics.roadrunner.followers.TrajectoryFollower;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.constraints.AngularVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.MinVelocityConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.ProfileAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryAccelerationConstraint;
import com.acmerobotics.roadrunner.trajectory.constraints.TrajectoryVelocityConstraint;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import org.firstinspires.ftc.masters.CSCons;
import org.firstinspires.ftc.masters.trajectorySequence.TrajectorySequence;
import org.firstinspires.ftc.masters.trajectorySequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.masters.trajectorySequence.TrajectorySequenceRunner;
import org.firstinspires.ftc.masters.util.LynxModuleUtil;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Config
public class SampleMecanumDrive extends MecanumDrive {

//    public static double ALIGN_SPEED = .3;

    public static PIDCoefficients TRANSLATIONAL_PID = new PIDCoefficients(8.5, 0, 0);
    public static PIDCoefficients HEADING_PID = new PIDCoefficients(8, 0, 0);

    public static double LATERAL_MULTIPLIER = 1;

    public static double VX_WEIGHT = 1;
    public static double VY_WEIGHT = 1;
    public static double OMEGA_WEIGHT = 1;

    protected TrajectorySequenceRunner trajectorySequenceRunner;

    private static final TrajectoryVelocityConstraint VEL_CONSTRAINT = getVelocityConstraint(MAX_VEL, MAX_ANG_VEL, TRACK_WIDTH);
    private static final TrajectoryAccelerationConstraint ACCEL_CONSTRAINT = getAccelerationConstraint(MAX_ACCEL);

    protected TrajectoryFollower follower;

    public DcMotorEx leftFront, leftRear, rightRear, rightFront;
    //private Encoder leftEncoder, rightEncoder, middleEncoder;
    private List<DcMotorEx> motors;
    DcMotor intakeSlides = null;
    DcMotor backSlides = null;
    DcMotor otherBackSlides;
    DcMotor hangingMotor = null;

    Servo planeRaise;
    Servo clawServo;
    Servo clawArm;
    Servo clawAngle;
    Servo cameraTurning;
    Servo outtakeHook;
    Servo outtakeRotation;
    Servo outtakeMovement;
    Servo microHook;

    TouchSensor touchSensor;
    RevColorSensorV3 colorSensor;

    private IMU imu;
    private VoltageSensor batteryVoltageSensor;

    private List<Integer> lastEncPositions = new ArrayList<>();
    private List<Integer> lastEncVels = new ArrayList<>();

    PIDController controller;
    PIDController icontroller;

    public static double p = 0.01, i = 0, d = 0.0001;
    public static double f = 0.05;

    private final double ticks_in_degrees = 384.5 / 180;

    public final double iticks_in_degree = 384.5 / 180;

    public static double ip = 0.01, ii = 0, iid = 0.00;
    public static double iif = 0.05;


    Telemetry telemetry;

    public SampleMecanumDrive(HardwareMap hardwareMap, Telemetry telemetry){
        this(hardwareMap);
        this.telemetry= telemetry;

    }

    public SampleMecanumDrive(HardwareMap hardwareMap) {
        super(kV, kA, kStatic, TRACK_WIDTH, TRACK_WIDTH, LATERAL_MULTIPLIER);

        follower = new HolonomicPIDVAFollower(TRANSLATIONAL_PID, TRANSLATIONAL_PID, HEADING_PID,
                new Pose2d(0.5, 0.5, Math.toRadians(5.0)), 0.5);


        LynxModuleUtil.ensureMinimumFirmwareVersion(hardwareMap);

        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next();

        for (LynxModule module : hardwareMap.getAll(LynxModule.class)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

//        // TODO: adjust the names of the following hardware devices to match your configuration
//        imu = hardwareMap.get(IMU.class, "imu");
//        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
//                DriveConstants.LOGO_FACING_DIR, DriveConstants.USB_FACING_DIR));
//        imu.initialize(parameters);

        // TODO: If the hub containing the IMU you are using is mounted so that the "REV" logo does
        // not face up, remap the IMU axes so that the z-axis points upward (normal to the floor.)
        //
        //             | +Z axis
        //             |
        //             |
        //             |
        //      _______|_____________     +Y axis
        //     /       |_____________/|__________
        //    /   REV / EXPANSION   //
        //   /       / HUB         //
        //  /_______/_____________//
        // |_______/_____________|/
        //        /
        //       / +X axis
        //
        // This diagram is derived from the axes in section 3.4 https://www.bosch-sensortec.com/media/boschsensortec/downloads/datasheets/bst-bno055-ds000.pdf
        // and the placement of the dot/orientation from https://docs.revrobotics.com/rev-control-system/control-system-overview/dimensions#imu-location
        //
        // For example, if +Y in this diagram faces downwards, you would use AxisDirection.NEG_Y.
        // BNO055IMUUtil.remapZAxis(imu, AxisDirection.NEG_Y);

        leftFront = hardwareMap.get(DcMotorEx.class, "frontLeft");
        leftRear = hardwareMap.get(DcMotorEx.class, "backLeft");
        rightRear = hardwareMap.get(DcMotorEx.class, "backRight");
        rightFront = hardwareMap.get(DcMotorEx.class, "frontRight");

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motors = Arrays.asList(leftFront, leftRear, rightRear, rightFront);

        hangingMotor = hardwareMap.dcMotor.get("hangingMotor");
        intakeSlides = hardwareMap.dcMotor.get("intakeSlides");
        backSlides = hardwareMap.dcMotor.get("backSlides");
        otherBackSlides = hardwareMap.dcMotor.get("otherBackSlides");

        planeRaise = hardwareMap.servo.get("planeRaise");
        clawServo = hardwareMap.servo.get("clawServo");
        clawArm = hardwareMap.servo.get("clawArm");
        clawAngle = hardwareMap.servo.get("clawAngle");
        //cameraTurning = hardwareMap.servo.get("cameraTurning");
        outtakeHook = hardwareMap.servo.get("outtakeHook");
        microHook = hardwareMap.servo.get("microHook");
        outtakeRotation = hardwareMap.servo.get("outtakeRotation");
        outtakeMovement = hardwareMap.servo.get("backSlideServo");
        touchSensor = hardwareMap.touchSensor.get("touchBucket");
        colorSensor = hardwareMap.get(RevColorSensorV3.class, "color");

        controller = new PIDController(p, i, d);
        controller.setPID(p, i, d);

        icontroller = new PIDController(ip, ii, iid);
        icontroller.setPID(ip, ii, iid);

        for (DcMotorEx motor : motors) {
            MotorConfigurationType motorConfigurationType = motor.getMotorType().clone();
            motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
            motor.setMotorType(motorConfigurationType);
        }

        if (RUN_USING_ENCODER) {
            setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        if (RUN_USING_ENCODER && MOTOR_VELO_PID != null) {
            setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, MOTOR_VELO_PID);
        }

        // TODO: reverse any motors using DcMotor.setDirection()

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);

        List<Integer> lastTrackingEncPositions = new ArrayList<>();
        List<Integer> lastTrackingEncVels = new ArrayList<>();

        // TODO: if desired, use setLocalizer() to change the localization method
        // for instance, setLocalizer(new ThreeTrackingWheelLocalizer(...));
        setLocalizer(new StandardTrackingWheelLocalizer(hardwareMap));

        trajectorySequenceRunner = new TrajectorySequenceRunner(
                follower, HEADING_PID, batteryVoltageSensor,
                lastEncPositions, lastEncVels, lastTrackingEncPositions, lastTrackingEncVels
        );


        hangingMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        otherBackSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        hangingMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeSlides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backSlides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        otherBackSlides.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        hangingMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        otherBackSlides.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        otherBackSlides.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void openClaw(){
        clawServo.setPosition(clawOpen);
    }

    public void transferClaw(){
        clawServo.setPosition(clawTransfer);
    }

    public void closeClaw(){
        clawServo.setPosition(clawClosed);
    }

    public void haltSlides() {
        intakeSlides.setTargetPosition(0);
        intakeSlides.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        intakeSlides.setPower(.5);
    }

    public void intakeToGround() {
        clawAngle.setPosition(CSCons.clawAngleGroundToThree);
        clawArm.setPosition(CSCons.clawArmGround);
    }


    public void intakeToTopStack() {
        clawAngle.setPosition(CSCons.clawAngleGroundToThree);
        clawArm.setPosition(CSCons.clawArm5);
    }

    //pick up pixel 3 and 4
    public void intakeToPosition3(){
        clawAngle.setPosition(CSCons.clawAngleGroundToThree);
        clawArm.setPosition(CSCons.clawArm3);
    }



    public void intakeToTransfer() {
        clawAngle.setPosition(CSCons.clawAngleTransfer);
        clawArm.setPosition(clawArmTransfer);
    }

    public void closeHook(){
        microHook.setPosition(CSCons.closeMicroHook);
        outtakeHook.setPosition(CSCons.closeHook);
    }
     public void closeSmallHook(){
        microHook.setPosition(CSCons.closeMicroHook);
     }

     public void openLargeHook(){
        outtakeHook.setPosition(openHook);
     }

     public void openSmallHook(){
        microHook.setPosition(openMicroHook);
     }

    public void outtakeToBackdrop() {
        outtakeHook.setPosition(CSCons.closeHook);
        outtakeMovement.setPosition(CSCons.outtakeMovementBackDrop);
        outtakeRotation.setPosition(CSCons.outtakeAngleFolder);
    }

    public void outtakeToTransfer() {
        outtakeHook.setPosition(CSCons.openHook);
        outtakeMovement.setPosition(CSCons.outtakeMovementTransfer);
        outtakeRotation.setPosition(CSCons.outtakeAngleTransfer);
    }

    public void dropPixel() {
        outtakeHook.setPosition(CSCons.openHook);
        microHook.setPosition(CSCons.openMicroHook);
    }

    public void alignTag(List<AprilTagDetection> currentDetections) {
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            } else {
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }   // end for() loop
    }

    public TrajectoryBuilder trajectoryBuilder(Pose2d startPose, boolean reversed) {
        return new TrajectoryBuilder(startPose, reversed, VEL_CONSTRAINT, ACCEL_CONSTRAINT);
    }

    public TrajectoryBuilder trajectoryBuilder(Pose2d startPose) {
        return new TrajectoryBuilder(startPose, false, VEL_CONSTRAINT, ACCEL_CONSTRAINT);
    }

    public TrajectoryBuilder trajectoryBuilder(Pose2d startPose, double startHeading) {
        return new TrajectoryBuilder(startPose, startHeading, VEL_CONSTRAINT, ACCEL_CONSTRAINT);
    }

    public TrajectorySequenceBuilder trajectorySequenceBuilder(Pose2d startPose) {
        return new TrajectorySequenceBuilder(
                startPose,
                VEL_CONSTRAINT, ACCEL_CONSTRAINT,
                MAX_ANG_VEL, MAX_ANG_ACCEL
        );
    }

    public void turnAsync(double angle) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(
                trajectorySequenceBuilder(getPoseEstimate())
                        .turn(angle)
                        .build()
        );
    }

    public void turn(double angle) {
        turnAsync(angle);
        waitForIdle();
    }

    public void followTrajectoryAsync(Trajectory trajectory) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(
                trajectorySequenceBuilder(trajectory.start())
                        .addTrajectory(trajectory)
                        .build()
        );
    }

    public void followTrajectory(Trajectory trajectory) {
        followTrajectoryAsync(trajectory);
        waitForIdle();
    }

    public void followTrajectorySequenceAsync(TrajectorySequence trajectorySequence) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(trajectorySequence);
    }

    public void followTrajectorySequence(TrajectorySequence trajectorySequence) {
        followTrajectorySequenceAsync(trajectorySequence);
        waitForIdle();
    }

    public Pose2d getLastError() {
        return trajectorySequenceRunner.getLastPoseError();
    }

    public void update() {
        updatePoseEstimate();
        DriveSignal signal = trajectorySequenceRunner.update(getPoseEstimate(), getPoseVelocity());
        if (signal != null) setDriveSignal(signal);
    }

    public void waitForIdle() {
        while (!Thread.currentThread().isInterrupted() && isBusy())
            update();
    }

    public boolean isBusy() {
        return trajectorySequenceRunner.isBusy();
    }

    public void setMode(DcMotor.RunMode runMode) {
        for (DcMotorEx motor : motors) {
            motor.setMode(runMode);
        }
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        for (DcMotorEx motor : motors) {
            motor.setZeroPowerBehavior(zeroPowerBehavior);
        }
    }

    public void setPIDFCoefficients(DcMotor.RunMode runMode, PIDFCoefficients coefficients) {
        PIDFCoefficients compensatedCoefficients = new PIDFCoefficients(
                coefficients.p, coefficients.i, coefficients.d,
                coefficients.f * 12 / batteryVoltageSensor.getVoltage()
        );

        for (DcMotorEx motor : motors) {
            motor.setPIDFCoefficients(runMode, compensatedCoefficients);
        }
    }

    public void setWeightedDrivePower(Pose2d drivePower) {
        Pose2d vel = drivePower;

        if (Math.abs(drivePower.getX()) + Math.abs(drivePower.getY())
                + Math.abs(drivePower.getHeading()) > 1) {
            // re-normalize the powers according to the weights
            double denom = VX_WEIGHT * Math.abs(drivePower.getX())
                    + VY_WEIGHT * Math.abs(drivePower.getY())
                    + OMEGA_WEIGHT * Math.abs(drivePower.getHeading());

            vel = new Pose2d(
                    VX_WEIGHT * drivePower.getX(),
                    VY_WEIGHT * drivePower.getY(),
                    OMEGA_WEIGHT * drivePower.getHeading()
            ).div(denom);
        }

        setDrivePower(vel);
    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {
        lastEncPositions.clear();

        List<Double> wheelPositions = new ArrayList<>();
        for (DcMotorEx motor : motors) {
            int position = motor.getCurrentPosition();
            lastEncPositions.add(position);
            wheelPositions.add(encoderTicksToInches(position));
        }
        return wheelPositions;
    }

    @Override
    public List<Double> getWheelVelocities() {
        lastEncVels.clear();

        List<Double> wheelVelocities = new ArrayList<>();
        for (DcMotorEx motor : motors) {
            int vel = (int) motor.getVelocity();
            lastEncVels.add(vel);
            wheelVelocities.add(encoderTicksToInches(vel));
        }
        return wheelVelocities;
    }

    @Override
    public void setMotorPowers(double v, double v1, double v2, double v3) {
        leftFront.setPower(v);
        leftRear.setPower(v1);
        rightRear.setPower(v2);
        rightFront.setPower(v3);
    }

    @Override
    public double getRawExternalHeading() {
        return 0;//imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }

    @Override
    public Double getExternalHeadingVelocity() {
        return (double) 0.0; //(double) imu.getRobotAngularVelocity(AngleUnit.RADIANS).zRotationRate;
    }


    public static TrajectoryVelocityConstraint getVelocityConstraint(double maxVel, double maxAngularVel, double trackWidth) {
        return new MinVelocityConstraint(Arrays.asList(
                new AngularVelocityConstraint(maxAngularVel),
                new MecanumVelocityConstraint(maxVel, trackWidth)
        ));
    }

    public static TrajectoryAccelerationConstraint getAccelerationConstraint(double maxAccel) {
        return new ProfileAccelerationConstraint(maxAccel);
    }

    public void breakFollowing() {

    }

    public void backSlidesMove(int target) {

        int slidePos = backSlides.getCurrentPosition();
        double pid = controller.calculate(slidePos, target);
        double ff = Math.cos(Math.toRadians(target / ticks_in_degrees)) * f;

        double liftPower = pid + ff;

        if (telemetry!=null) {
            telemetry.addData("liftPower", liftPower);
        }
        backSlides.setPower(liftPower);
        otherBackSlides.setPower(liftPower);
    }

    public void intakeSlidesMove(int itarget) {


        int islidePos = intakeSlides.getCurrentPosition();
        double ipid = icontroller.calculate(islidePos, itarget);
        double iff = Math.cos(Math.toRadians(itarget / iticks_in_degree)) * iif;

        double iliftPower = ipid + iff;

        intakeSlides.setPower(iliftPower);

    }

    public DcMotor getBackSlides(){
        return backSlides;
    }

    public DcMotor getIntakeSlides() {
        return intakeSlides;
    }

    public RevColorSensorV3 getColorSensor(){
        return colorSensor;
    }


}