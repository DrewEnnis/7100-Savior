package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
@Autonomous(name="Robot: 2 Spec Auto w/ wall", group="Robot")
public class SpecAuto3 extends RobotLinearOpMode{
    public DcMotor  leftFrontDriveMotor   = null; //the left front drivetrain motor
    public DcMotor  rightFrontDriveMotor  = null; //the right front drivetrain motor
    public DcMotor  rightBackDriveMotor  = null; //the right back drivetrain motor
    public DcMotor  leftBackDriveMotor  = null; //the left back drivetrain motor
    public DcMotor  armMotor    = null; //the arm motor
    public DcMotor  VSlide   = null; //the left arm motor
    public CRServo intake1 = null;
    public CRServo intake2 = null;
    private ElapsedTime     runtime = new ElapsedTime();


    static final double     FW_SPEED = 0.5;
    static final double     TURN_SPEED    = 0.6;

    @Override
    public void runOpMode() {
        leftFrontDriveMotor = hardwareMap.get(DcMotor.class, "left_front_drive"); //the left front drivetrain motor
        rightFrontDriveMotor = hardwareMap.get(DcMotor.class, "right_front_drive"); //the right front drivetrain motor
        leftBackDriveMotor = hardwareMap.get(DcMotor.class, "left_back_drive"); //the left drivetrain motor
        rightBackDriveMotor = hardwareMap.get(DcMotor.class, "right_back_drive"); //the left drivetrain motor
        armMotor = hardwareMap.get(DcMotor.class, "arm_motor"); //the arm motor
        VSlide = hardwareMap.get(DcMotor.class, "vslide");
        intake1 = hardwareMap.get(CRServo.class, "intake_1");
        intake2 = hardwareMap.get(CRServo.class, "intake_2");

        leftFrontDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDriveMotor.setDirection(DcMotor.Direction.REVERSE);
        leftBackDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        rightBackDriveMotor.setDirection(DcMotor.Direction.REVERSE);
        leftFrontDriveMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontDriveMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackDriveMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackDriveMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setDirection(DcMotor.Direction.FORWARD);
        intake1.setDirection(CRServo.Direction.FORWARD);
        intake2.setDirection(CRServo.Direction.REVERSE);
        VSlide.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

//             encoderDrive(0.5, 10, MOVEMENT_DIRECTION.FORWARD);
//             encoderTurn(0.5, 180, TURN_DIRECTION.TURN_LEFT);
//             encoderSlideUp(0.5, 3, MOVEMENT_DIRECTION.FORWARD);

        while (opModeIsActive()) {
            encoderDrive(FW_SPEED,6.5, MOVEMENT_DIRECTION.STRAFE_LEFT);
//            encoderDrive(FW_SPEED,5,MOVEMENT_DIRECTION.REVERSE);
            encoderDrive(FW_SPEED, 22, MOVEMENT_DIRECTION.FORWARD);
            VSlide.setPower(.7);
            sleep(400); // was 350
            VSlide.setPower(0);
            encoderDrive(0.3,12.5,MOVEMENT_DIRECTION.FORWARD); //push into wall to center
            encoderDrive(0.3,5,MOVEMENT_DIRECTION.REVERSE);
            VSlide.setPower(.7);
            sleep(700); // changed from 800
            encoderDrive(0.3, 3.25, MOVEMENT_DIRECTION.FORWARD);
            VSlide.setPower(-.9);//hooked 1st spec, changed from 800
            sleep(200);


            encoderDrive(FW_SPEED, 10, MOVEMENT_DIRECTION.REVERSE);
            VSlide.setPower(0);
            encoderDrive(FW_SPEED, 23,MOVEMENT_DIRECTION.STRAFE_RIGHT);
            encoderDrive(0.3, 7,MOVEMENT_DIRECTION.STRAFE_RIGHT); //soften wall hit
            encoderDrive(FW_SPEED, 7,MOVEMENT_DIRECTION.STRAFE_LEFT); //move off of wall
            encoderTurn(TURN_SPEED,180,TURN_DIRECTION.TURN_RIGHT);
            sleep(1500);//waiting in front of human player, can save time if needed

            encoderDrive(FW_SPEED,20,MOVEMENT_DIRECTION.FORWARD);
            VSlide.setPower(.7);
            sleep(200); //picked up 2nd spec from wall, was 400

            encoderDrive(FW_SPEED,20,MOVEMENT_DIRECTION.REVERSE);
            VSlide.setPower(-.9);
//            sleep(800); //can prob save time here
            encoderTurn(TURN_SPEED,180,TURN_DIRECTION.TURN_RIGHT);
            VSlide.setPower(0);

//            encoderDrive(FW_SPEED, 10, MOVEMENT_DIRECTION.STRAFE_RIGHT);

            encoderDrive(FW_SPEED, 22.5,MOVEMENT_DIRECTION.STRAFE_LEFT);//in front of bar, was 32

//            encoderDrive(FW_SPEED, 25, MOVEMENT_DIRECTION.REVERSE);//reset against wall

//            encoderDrive(FW_SPEED, 18, MOVEMENT_DIRECTION.FORWARD);//was 22
            VSlide.setPower(.7);
            sleep(400); //was 350
            VSlide.setPower(0);
            encoderDrive(FW_SPEED,17.5,MOVEMENT_DIRECTION.FORWARD); //push into wall to center, maybe
            encoderDrive(FW_SPEED,5,MOVEMENT_DIRECTION.REVERSE);
            VSlide.setPower(.7);
            sleep(700); // changed from 800
            encoderDrive(0.3, 3.25, MOVEMENT_DIRECTION.FORWARD);
            VSlide.setPower(-.9);
            sleep(900);
            VSlide.setPower(0); //hooked 2nd spec, can prob save time here

            encoderDrive(FW_SPEED, 24, MOVEMENT_DIRECTION.REVERSE);
            encoderDrive(FW_SPEED,30,MOVEMENT_DIRECTION.STRAFE_RIGHT); //parked







            VSlide.setPower(0);

            sleep(30000);


            //encoderDrive(FORWARD_SPEED, 15, MOVEMENT_DIRECTION.FORWARD); // drive to basket
            // encoderDrive(FORWARD_SPEED, 25, MOVEMENT_DIRECTION.STRAFE_RIGHT); // strafe
//             encoderTurn(TURN_SPEED, 45, TURN_DIRECTION.TURN_LEFT); // turn to face
//             encoderDrive(FORWARD_SPEED, 1, MOVEMENT_DIRECTION.REVERSE); // drive to basket
////
//             armMotor.setPower(0.3);// raise arm
//             runtime.reset();
//             while (opModeIsActive() && (runtime.seconds() < 2.0)) {
//                 telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runtime.seconds());
//                 telemetry.update();
//             }
//             armMotor.setPower(0);
//
//             intake.setPower(-1);// output sample in basket
//             runtime.reset();
//             while (opModeIsActive() && (runtime.seconds() < 1.0)) {
//                 telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runtime.seconds());
//                 telemetry.update();
//             }
//             intake.setPower(0);// turn output off
//
//             armMotor.setPower(-0.3);// retract arm
//             runtime.reset();
//             while (opModeIsActive() && (runtime.seconds() < 2.0)) {
//                 telemetry.addData("Path", "Leg 1: %4.1f S Elapsed", runtime.seconds());
//                 telemetry.update();
//             }
//             armMotor.setPower(0);

            stop();
        }
    }
}
