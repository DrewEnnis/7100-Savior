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
@Disabled
@Autonomous(name="Robot: Park Auto 2", group="Robot")

public class ParkAuto extends RobotLinearOpMode {

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


    static final double     FORWARD_SPEED = 0.6;
    static final double     TURN_SPEED    = 0.5;

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
            encoderDrive(.3, 45, MOVEMENT_DIRECTION.FORWARD);
            sleep(30000);
            stop();
        }
    }
}
