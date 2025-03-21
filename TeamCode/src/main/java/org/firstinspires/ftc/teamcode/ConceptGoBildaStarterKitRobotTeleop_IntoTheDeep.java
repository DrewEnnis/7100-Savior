/*   MIT License
 *   Copyright (c) [2024] [Base 10 Assets, LLC]
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:

 *   The above copyright notice and this permission notice shall be included in all
 *   copies or substantial portions of the Software.

 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   SOFTWARE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

/*
 * This OpMode is an example driver-controlled (TeleOp) mode for the goBILDA 2024-2025 FTC
 * Into The Deep Starter Robot
 * The code is structured as a LinearOpMode
 *
 * This robot has a two-motor differential-steered (sometimes called tank or skid steer) drivetrain.
 * With a left and right drive motor.
 * The drive on this robot is controlled in an "Arcade" style, with the left stick Y axis
 * controlling the forward movement and the right stick X axis controlling rotation.
 * This allows easy transition to a standard "First Person" control of a
 * mecanum or omnidirectional chassis.
 *
 * The drive wheels are 96mm diameter traction (Rhino) or omni wheels.
 * They are driven by 2x 5203-2402-0019 312RPM Yellow Jacket Planetary Gearmotors.
 *
 * This robot's main scoring mechanism includes an arm powered by a motor, a "wrist" driven
 * by a servo, and an intake driven by a continuous rotation servo.
 *
 * The arm is powered by a 5203-2402-0051 (50.9:1 Yellow Jacket Planetary Gearmotor) with an
 * external 5:1 reduction. This creates a total ~254.47:1 reduction.
 * This OpMode uses the motor's encoder and the RunToPosition method to drive the arm to
 * specific setpoints. These are defined as a number of degrees of rotation away from the arm's
 * starting position.
 *
 * Make super sure that the arm is reset into the robot, and the wrist is folded in before
 * you run start the OpMode. The motor's encoder is "relative" and will move the number of degrees
 * you request it to based on the starting position. So if it starts too high, all the motor
 * setpoints will be wrong.
 *
 * The wrist is powered by a goBILDA Torque Servo (2000-0025-0002).
 *
 * The intake wheels are powered by a goBILDA Speed Servo (2000-0025-0003) in Continuous Rotation mode.
 */


@TeleOp(name="FTC Strafer Kit Example Robot (INTO THE DEEP)", group="Robot")
//@Disabled
public class ConceptGoBildaStarterKitRobotTeleop_IntoTheDeep extends RobotLinearOpMode {

    /* Declare OpMode members. */
    public DcMotor  leftFrontDriveMotor   = null; //the left front drivetrain motor
    public DcMotor  rightFrontDriveMotor  = null; //the right front drivetrain motor
    public DcMotor  rightBackDriveMotor  = null; //the right back drivetrain motor
    public DcMotor  leftBackDriveMotor  = null; //the left back drivetrain motor
    public DcMotor  armMotor    = null; //the arm motor
//    public DcMotor  VSlide   = null; //the left arm motor
    public CRServo intake1 = null;
    public CRServo intake2 = null;


    /* This constant is the number of encoder ticks for each degree of rotation of the arm.
    To find this, we first need to consider the total gear reduction powering our arm.
    First, we have an external 20t:100t (5:1) reduction created by two spur gears.
    But we also have an internal gear reduction in our motor.
    The motor we use for this arm is a 117RPM Yellow Jacket. Which has an internal gear
    reduction of ~50.9:1. (more precisely it is 250047/4913:1)
    We can multiply these two ratios together to get our final reduction of ~254.47:1.
    The motor's encoder counts 28 times per rotation. So in total you should see about 7125.16
    counts per rotation of the arm. We divide that by 360 to get the counts per degree. */
    final double ARM_TICKS_PER_DEGREE = 19.7924893140647; //exact fraction is (194481/9826)


    /* These constants hold the position that the arm is commanded to run to.
    These are relative to where the arm was located when you start the OpMode. So make sure the
    arm is reset to collapsed inside the robot before you start the program.

    In these variables you'll see a number in degrees, multiplied by the ticks per degree of the arm.
    This results in the number of encoder ticks the arm needs to move in order to achieve the ideal
    set position of the arm. For example, the ARM_SCORE_SAMPLE_IN_LOW is set to
    160 * ARM_TICKS_PER_DEGREE. This asks the arm to move 160° from the starting position.
    If you'd like it to move further, increase that number. If you'd like it to not move
    as far from the starting position, decrease it. */

    final double ARM_COLLAPSED_INTO_ROBOT  = 0;
    final double ARM_COLLECT               = 250 * ARM_TICKS_PER_DEGREE;
    final double ARM_CLEAR_BARRIER         = 230 * ARM_TICKS_PER_DEGREE;
    final double ARM_SCORE_SPECIMEN        = 160 * ARM_TICKS_PER_DEGREE;
    final double ARM_SCORE_SAMPLE_IN_LOW   = 160 * ARM_TICKS_PER_DEGREE;
    final double ARM_ATTACH_HANGING_HOOK   = 120 * ARM_TICKS_PER_DEGREE;
    final double ARM_WINCH_ROBOT           = 15  * ARM_TICKS_PER_DEGREE;

    /* Variables to store the speed the intake servo should be set at to intake, and deposit game elements. */
    final double INTAKE_COLLECT    = -1.0;
    final double INTAKE_OFF        =  0.0;
    final double INTAKE_DEPOSIT    =  0.5;

    /* Variables to store the positions that the wrist should be set to when folding in, or folding out. */
    final double WRIST_FOLDED_IN   = 0.8333;
    final double WRIST_FOLDED_OUT  = 0.5;

    /* A number in degrees that the triggers can adjust the arm position by */
    final double FUDGE_FACTOR = 5 * ARM_TICKS_PER_DEGREE; //changed to 5 from 15

    /* Variables that are used to set the arm to a specific position */
    double armPosition = (int)ARM_COLLAPSED_INTO_ROBOT;
    double armPositionFudgeFactor;

    boolean blockInp = false;

    /*must delete one of these methods can only be one with the same name also this
    method can't be defined in another method but can be defined inside this class
     */
    @Override
    public void runOpMode() {

        double forward;
        double strafe;
        double rotate;

        double RFPower;
        double LFPower;
        double RBPower;
        double LBPower;



        /* Define and Initialize Motors */
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

        /* Setting zeroPowerBehavior to BRAKE enables a "brake mode". This causes the motor to slow down
        much faster when it is coasting. This creates a much more controllable drivetrain. As the robot
        stops much quicker. */
//        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        /*This sets the maximum current that the control hub will apply to the arm before throwing a flag */
//        ((DcMotorEx) armMotor).setCurrentAlert(5,CurrentUnit.AMPS);


        /* Before starting the armMotor. We'll make sure the TargetPosition is set to 0.
        Then we'll set the RunMode to RUN_TO_POSITION. And we'll ask it to stop and reset encoder.
        If you do not have the encoder plugged into this motor, it will not run in this code. */
//        armMotor.setTargetPosition(0);
//        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        /* Define and initialize servos.*/
//        intake = hardwareMap.get(CRServo.class, "intake");


        /* Make sure that the intake is off, and the wrist is folded in. */
//        intake.setPower(INTAKE_OFF);


        /* Send telemetry message to signify robot waiting */
        telemetry.addLine("Robot Ready.");
        telemetry.update();

        /* Wait for the game driver to press play */
        waitForStart();

        /* Run until the driver presses stop */
        while (opModeIsActive()) {

            /* Set the drive and turn variables to follow the joysticks on the gamepad.
            the joysticks decrease as you push them up. So reverse the Y axis. */
            forward = gamepad1.left_stick_y;
            strafe = -gamepad1.left_stick_x;
            rotate  = gamepad1.right_stick_x;


            /* Here we "mix" the input channels together to find the power to apply to each motor.
            The both motors need to be set to a mix of how much you're retesting the robot move
            forward, and how much you're requesting the robot turn. When you ask the robot to rotate
            the right and left motors need to move in opposite directions. So we will add rotate to
            forward for the left motor, and subtract rotate from forward for the right motor. */

            RFPower = forward - strafe + rotate;
            LFPower = forward + strafe - rotate;
            RBPower = forward + strafe + rotate;
            LBPower = forward - strafe - rotate;

//            RFPower = rotate - (forward - strafe);
//            LFPower = rotate + (forward + strafe);
//            RBPower = rotate + (forward - strafe);
//            LBPower = rotate - (forward + strafe);
            if (!blockInp) {
                rightFrontDriveMotor.setPower(RFPower/2);
                leftFrontDriveMotor.setPower(LFPower/2);
                rightBackDriveMotor.setPower(RBPower/2);
                leftBackDriveMotor.setPower(LBPower/2);

                armMotor.setPower(gamepad1.left_trigger-gamepad1.right_trigger);

                if (gamepad1.left_bumper && !gamepad1.right_bumper) {
                    VSlide.setPower(1);
                } else if (gamepad1.right_bumper && !gamepad1.left_bumper) {
                    VSlide.setPower(-1);
                } else {
                    VSlide.setPower(0);
                }

                if (gamepad1.b) {
                    intake1.setPower(-1);
                    intake2.setPower(-1);
                } else if (gamepad1.a) {
                    intake1.setPower(1);
                    intake2.setPower(1);
                } else {
                    intake1.setPower(0);
                    intake2.setPower(0);
                }
            }



            /* Here we handle the three buttons that have direct control of the intake speed.
            These control the continuous rotation servo that pulls elements into the robot,
            If the user presses A, it sets the intake power to the final variable that
            holds the speed we want to collect at.
            If the user presses X, it sets the servo to Off.
            And if the user presses B it reveres the servo to spit out the element.*/

            /* TECH TIP: If Else loops:
            We're using an else if loop on "gamepad1.x" and "gamepad1.b" just in case
            multiple buttons are pressed at the same time. If the driver presses both "a" and "x"
            at the same time. "a" will win over and the intake will turn on. If we just had
            three if statements, then it will set the intake servo's power to multiple speeds in
            one cycle. Which can cause strange behavior. */






            /* Here we create a "fudge factor" for the arm position.
            This allows you to adjust (or "fudge") the arm position slightly with the gamepad triggers.
            We want the left trigger to move the arm up, and right trigger to move the arm down.
            So we add the right trigger's variable to the inverse of the left trigger. If you pull
            both triggers an equal amount, they cancel and leave the arm at zero. But if one is larger
            than the other, it "wins out". This variable is then multiplied by our FUDGE_FACTOR.
            The FUDGE_FACTOR is the number of degrees that we can adjust the arm by with this function. */

//            armPositionFudgeFactor = FUDGE_FACTOR * (gamepad1.right_trigger + (-gamepad1.left_trigger));



            /* Here we implement a set of if else loops to set our arm to different scoring positions.
            We check to see if a specific button is pressed, and then move the arm (and sometimes
            intake and wrist) to match. For example, if we click the right bumper we want the robot
            to start collecting. So it moves the armPosition to the ARM_COLLECT position,
            it folds out the wrist to make sure it is in the correct orientation to intake, and it
            turns the intake on to the COLLECT mode.*/





            if (gamepad1.dpad_left) {


            }

            else if (gamepad1.dpad_right){
                /* This is the correct height to score SPECIMEN on the HIGH CHAMBER */
//                    armPosition = ARM_SCORE_SPECIMEN;

            }

            else if (gamepad1.dpad_up){
                blockInp = true;
                VSlide.setPower(.7);
                sleep(350);
                VSlide.setPower(0);
                encoderDrive(0.3,25,MOVEMENT_DIRECTION.FORWARD); //push into wall to center, maybe
                encoderDrive(0.3,5,MOVEMENT_DIRECTION.REVERSE);
                VSlide.setPower(.7);
                sleep(700); // changed from 800
                encoderDrive(0.3, 3.25, MOVEMENT_DIRECTION.FORWARD);
                VSlide.setPower(-.9);
                sleep(900);
                VSlide.setPower(0);
                blockInp = false;
            }

            else if (gamepad1.dpad_down){
                blockInp = true;
                encoderDrive(0.3,15,MOVEMENT_DIRECTION.FORWARD); //push into wall to center, maybe need to raise vslide
                encoderDrive(0.3,12,MOVEMENT_DIRECTION.REVERSE); //get ready to swing arm
                armMotor.setPower(0.8);//arm lowered
                sleep(2100);
                armMotor.setPower(-0.5);//raise it up to the bar as we drive forward
//                sleep(250); //might need to change timing, lets arm clear the lower bar
                encoderDrive(0.4,10,MOVEMENT_DIRECTION.FORWARD);
                armMotor.setPower(0);
                rightFrontDriveMotor.setPower(0.3); //drive backwards to stop from lifting submersible
                leftFrontDriveMotor.setPower(0.3);
                rightBackDriveMotor.setPower(0.3);
                leftBackDriveMotor.setPower(0.3);
                sleep(200);
                armMotor.setPower(-1);
                sleep(2000);
                rightFrontDriveMotor.setPower(0); //drive backwards to stop from lifting submersible
                leftFrontDriveMotor.setPower(0);
                rightBackDriveMotor.setPower(0);
                leftBackDriveMotor.setPower(0);
//                while (!gamepad1.dpad_down) {
//                    armMotor.setPower(-0.5);
//                }
//                armMotor.setPower(0);
//                blockInp=false;
            }

            /* Here we set the target position of our arm to match the variable that was selected
            by the driver.
            We also set the target velocity (speed) the motor runs at, and use setMode to run it.*/


//            armMotor.setTargetPosition((int) (armPosition + armPositionFudgeFactor));
//
//
//            ((DcMotorEx) armMotor).setVelocity(2100);
//            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            /* TECH TIP: Encoders, integers, and doubles
            Encoders report when the motor has moved a specified angle. They send out pulses which
            only occur at specific intervals (see our ARM_TICKS_PER_DEGREE). This means that the
            position our arm is currently at can be expressed as a whole number of encoder "ticks".
            The encoder will never report a partial number of ticks. So we can store the position in
            an integer (or int).
            A lot of the variables we use in FTC are doubles. These can capture fractions of whole
            numbers. Which is great when we want our arm to move to 122.5°, or we want to set our
            servo power to 0.5.

            setTargetPosition is expecting a number of encoder ticks to drive to. Since encoder
            ticks are always whole numbers, it expects an int. But we want to think about our
            arm position in degrees. And we'd like to be able to set it to fractions of a degree.
            So we make our arm positions Doubles. This allows us to precisely multiply together
            armPosition and our armPositionFudgeFactor. But once we're done multiplying these
            variables. We can decide which exact encoder tick we want our motor to go to. We do
            this by "typecasting" our double, into an int. This takes our fractional double and
            rounds it to the nearest whole number.
            */

            /* Check to see if our arm is over the current limit, and report via telemetry. */
//            if (((DcMotorEx) armMotor).isOverCurrent()){
//                telemetry.addLine("MOTOR EXCEEDED CURRENT LIMIT!");
//            }


            /* send telemetry to the driver of the arm's current position and target position */
//            telemetry.addData("armTarget: ", armMotor.getTargetPosition());
//            telemetry.addData("arm Encoder: ", armMotor.getCurrentPosition());
            telemetry.addData("left trigger: ", gamepad1.left_trigger);
            telemetry.addData("Right trigger: ", gamepad1.right_trigger);
            telemetry.addData("------", "");
            telemetry.addData("RFPower ",RFPower);
            telemetry.addData("LFPower ",LFPower);
            telemetry.addData("RBPower ",RBPower);
            telemetry.addData("LBPower ",LBPower);
            telemetry.addData("------", "");
            telemetry.addData("left Stick x: ", gamepad1.left_stick_x);
            telemetry.addData("Right Stick x: ", gamepad1.right_stick_x);
            telemetry.addData("left Stick y: ", gamepad1.left_stick_y);
            telemetry.addData("Right Stick y: ", gamepad1.right_stick_y);
            telemetry.update();

        }
    }
}
