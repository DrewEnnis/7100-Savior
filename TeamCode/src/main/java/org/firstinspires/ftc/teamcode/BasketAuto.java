package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * This OpMode illustrates the concept of driving a path based on time.
 * The code is structured as a LinearOpMode
 *
 * The code assumes that you do NOT have encoders on the wheels,
 *   otherwise you would use: RobotAutoDriveByEncoder;
 *
 *   The desired path in this example is:
 *   - Drive forward for 3 seconds
 *   - Spin right for 1.3 seconds
 *   - Drive Backward for 1 Second
 *
 *  The code is written in a simple form with no optimizations.
 *  However, there are several ways that this type of sequence could be streamlined,
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@Autonomous(name="Robot: Basket Auto", group="Robot")

public class BasketAuto extends RobotLinearOpMode {

    /* Declare OpMode members. */
    public DcMotor  leftFrontDriveMotor   = null; //the left front drivetrain motor
    public DcMotor  rightFrontDriveMotor  = null; //the right front drivetrain motor
    public DcMotor  rightBackDriveMotor  = null; //the right back drivetrain motor
    public DcMotor  leftBackDriveMotor  = null; //the left back drivetrain motor
    public DcMotor  armMotor    = null; //the arm motor
    public DcMotor  VSlide   = null; //the left arm motor
    public CRServo intake1 = null;
    public CRServo intake2 = null;
    private ElapsedTime     runtime = new ElapsedTime();


    static final double     FORWARD_SPEED = 0.3;
    static final double     TURN_SPEED    = 0.4;

    @Override
    public void runOpMode() {
        leftFrontDriveMotor  = hardwareMap.get(DcMotor.class, "left_front_drive"); //the left front drivetrain motor
        rightFrontDriveMotor = hardwareMap.get(DcMotor.class, "right_front_drive"); //the right front drivetrain motor
        leftBackDriveMotor  = hardwareMap.get(DcMotor.class, "left_back_drive"); //the left drivetrain motor
        rightBackDriveMotor  = hardwareMap.get(DcMotor.class, "right_back_drive"); //the left drivetrain motor
        armMotor  = hardwareMap.get(DcMotor.class, "arm_motor"); //the arm motor
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
        VSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        VSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        armMotor.setDirection(DcMotor.Direction.FORWARD);
        intake1.setDirection(CRServo.Direction.FORWARD);
        intake2.setDirection(CRServo.Direction.REVERSE);

        waitForStart();

//             encoderDrive(0.5, 10, MOVEMENT_DIRECTION.FORWARD);
//             encoderTurn(0.5, 180, TURN_DIRECTION.TURN_LEFT);
//             encoderSlideUp(0.5, 3, MOVEMENT_DIRECTION.FORWARD);

        while(opModeIsActive()){
            encoderDrive(FORWARD_SPEED, 16, MOVEMENT_DIRECTION.REVERSE);
            encoderDrive(FORWARD_SPEED, 4, MOVEMENT_DIRECTION.STRAFE_LEFT);
            encoderTurn(TURN_SPEED, 45, TURN_DIRECTION.TURN_LEFT);
            encoderDrive(FORWARD_SPEED, 2, MOVEMENT_DIRECTION.FORWARD);
            armMotor.setPower(0.6);
            sleep(1500);
            armMotor.setPower(0);
//            encoderDrive(FORWARD_SPEED, 2, MOVEMENT_DIRECTION.FORWARD);
            encoderDrive(FORWARD_SPEED, 10, MOVEMENT_DIRECTION.REVERSE);
            armMotor.setPower(-0.5);
            sleep(500);
            armMotor.setPower(0.15);
            sleep(250);
            armMotor.setPower(0);
            intake1.setPower(1);
            intake2.setPower(1);
            sleep(1800); //dropped in bucket
            intake1.setPower(0);
            intake2.setPower(0);
            encoderDrive(FORWARD_SPEED,6,MOVEMENT_DIRECTION.FORWARD);
            armMotor.setPower(-0.6);
            sleep(500);
            armMotor.setPower(0);
            encoderDrive(FORWARD_SPEED, 14, MOVEMENT_DIRECTION.FORWARD); //-6 because of forward above
            encoderTurn(TURN_SPEED, 45, TURN_DIRECTION.TURN_LEFT);

            encoderDrive(FORWARD_SPEED,2,MOVEMENT_DIRECTION.STRAFE_LEFT);
            encoderDrive(FORWARD_SPEED,1.5,MOVEMENT_DIRECTION.REVERSE);
            armMotor.setPower(0.8);
            sleep(2500);
            armMotor.setPower(0);
            intake1.setPower(1);
            intake2.setPower(1);
            sleep(200); //Picked up sample
            intake1.setPower(0);
            intake2.setPower(0);

            armMotor.setPower(-0.6);
//            sleep(1250);
            encoderTurn(TURN_SPEED,45,TURN_DIRECTION.TURN_RIGHT);
            encoderDrive(FORWARD_SPEED,10,MOVEMENT_DIRECTION.REVERSE);
            armMotor.setPower(0);

//            encoderDrive(FORWARD_SPEED, 18, MOVEMENT_DIRECTION.REVERSE);
//            encoderTurn(TURN_SPEED, 120, TURN_DIRECTION.TURN_LEFT);
//            encoderDrive(FORWARD_SPEED, 20, MOVEMENT_DIRECTION.FORWARD);
//            armMotor.setPower(1);
//            sleep(1300);
//            armMotor.setPower(0);

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
