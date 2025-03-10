package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
@Autonomous(name="Robot: 2 Spec Auto #2", group="Robot")
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


    static final double     FW_SPEED = 0.3;
    static final double     TURN_SPEED    = 0.3;

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

        waitForStart();

//             encoderDrive(0.5, 10, MOVEMENT_DIRECTION.FORWARD);
//             encoderTurn(0.5, 180, TURN_DIRECTION.TURN_LEFT);
//             encoderSlideUp(0.5, 3, MOVEMENT_DIRECTION.FORWARD);

        while (opModeIsActive()) {
            encoderDrive(FW_SPEED, 22, MOVEMENT_DIRECTION.FORWARD);
            VSlide.setPower(-.7);
            sleep(800);
            encoderDrive(FW_SPEED, 4.5, MOVEMENT_DIRECTION.FORWARD);
            VSlide.setPower(.9);
            sleep(1300); //hooked 1st spec, can prob save time here

            encoderDrive(FW_SPEED, 10, MOVEMENT_DIRECTION.REVERSE);
            encoderDrive(FW_SPEED, 26,MOVEMENT_DIRECTION.STRAFE_RIGHT); // edited +5 to try and reset against wall (untested)
            encoderDrive(FW_SPEED, 5,MOVEMENT_DIRECTION.STRAFE_LEFT); //move off of wall (untested)
            encoderTurn(TURN_SPEED,180,TURN_DIRECTION.TURN_RIGHT);
            sleep(3000);//waiting in front of human player, can save time if needed

            encoderDrive(FW_SPEED,20,MOVEMENT_DIRECTION.FORWARD);
            VSlide.setPower(-.7);
            sleep(800); //picked up 2nd spec from wall

            encoderDrive(FW_SPEED,20,MOVEMENT_DIRECTION.REVERSE);
            VSlide.setPower(.9);
            sleep(1300); //can prob save time here
            encoderTurn(TURN_SPEED,180,TURN_DIRECTION.TURN_RIGHT);
            encoderDrive(FW_SPEED, 26,MOVEMENT_DIRECTION.STRAFE_LEFT);//in front of bar

            encoderDrive(FW_SPEED, 30, MOVEMENT_DIRECTION.REVERSE);//reset against wall

            encoderDrive(FW_SPEED, 22, MOVEMENT_DIRECTION.FORWARD);
            VSlide.setPower(-.7);
            sleep(800);
            encoderDrive(FW_SPEED, 5.5, MOVEMENT_DIRECTION.FORWARD);
            VSlide.setPower(.9);
            sleep(1300); //hooked 2nd spec, can prob save time here

            encoderDrive(FW_SPEED, 24, MOVEMENT_DIRECTION.REVERSE);//start moving to park, runs out of time
            encoderDrive(FW_SPEED,30,MOVEMENT_DIRECTION.STRAFE_RIGHT); //parked (untested)







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
