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
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Slide Presentation", group="Robot")
//@Disabled
public class SlideTest extends RobotLinearOpMode {

    /* Declare OpMode members. */
    public DcMotor  slide1   = null;
    public DcMotor  slide2  = null;
    public DcMotor  slide3  = null;
    public DcMotor  slide4  = null;
    public Servo link = null;


    @Override
    public void runOpMode() {


        boolean is1Up = false;
        boolean is2Up = false;
        boolean is3Up = false;
        boolean is4Up = false;
        boolean is5Up = false;
        
        float motorSpeed = 0.8f;
        long waitTime = 500;
        float holdSpeed = 0.2f;

        /* Define and Initialize Motors */
        slide1  = hardwareMap.get(DcMotor.class, "slide1");
        slide2 = hardwareMap.get(DcMotor.class, "slide2");
        slide3  = hardwareMap.get(DcMotor.class, "slide3");
        slide4  = hardwareMap.get(DcMotor.class, "slide4");
        link  = hardwareMap.get(Servo.class, "link");


        slide1.setDirection(DcMotor.Direction.FORWARD);
        slide2.setDirection(DcMotor.Direction.FORWARD);
        slide3.setDirection(DcMotor.Direction.FORWARD);
        slide4.setDirection(DcMotor.Direction.FORWARD);
        slide1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide3.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide4.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        /* Send telemetry message to signify robot waiting */
        telemetry.addLine("Robot Ready.");
        telemetry.update();

        /* Wait for the game driver to press play */
        waitForStart();

        /* Run until the driver presses stop */
        while (opModeIsActive()) {

            if (gamepad1.a) {
                if (!is1Up) {
                    slide1.setPower(motorSpeed);
                    sleep(waitTime);
                    slide1.setPower(holdSpeed);
                    is1Up = true;
                } else {
                    slide1.setPower(-motorSpeed);
                    sleep(waitTime);
                    slide1.setPower(0);
                    is1Up = false;
                }
            } else if (gamepad1.b) {
                if (!is2Up) {
                    slide2.setPower(motorSpeed);
                    sleep(waitTime);
                    slide2.setPower(holdSpeed);
                    is2Up = true;
                } else {
                    slide2.setPower(-motorSpeed);
                    sleep(waitTime);
                    slide2.setPower(0);
                    is2Up = false;
                }
            } else if (gamepad1.x) {
                if (!is3Up) {
                    slide3.setPower(motorSpeed);
                    sleep(waitTime);
                    slide3.setPower(holdSpeed);
                    is3Up = true;
                } else {
                    slide3.setPower(-motorSpeed);
                    sleep(waitTime);
                    slide3.setPower(0);
                    is3Up = false;
                }
            } else if (gamepad1.y) {
                if (!is4Up) {
                    slide4.setPower(motorSpeed);
                    sleep(waitTime);
                    slide4.setPower(holdSpeed);
                    is4Up = true;
                } else {
                    slide4.setPower(-motorSpeed);
                    sleep(waitTime);
                    slide4.setPower(0);
                    is4Up = false;
                }
            } else if (gamepad1.right_bumper) {
                if (!is5Up) {
                    link.setPosition(0);
                    is5Up = true;
                } else {
                    link.setPosition(0);
                    is5Up = false;
                }
            }


            telemetry.addData("left trigger: ", gamepad1.left_trigger);
            telemetry.addData("Right trigger: ", gamepad1.right_trigger);
            telemetry.addData("------", "");
            telemetry.addData("------", "");
            telemetry.addData("left Stick x: ", gamepad1.left_stick_x);
            telemetry.addData("Right Stick x: ", gamepad1.right_stick_x);
            telemetry.addData("left Stick y: ", gamepad1.left_stick_y);
            telemetry.addData("Right Stick y: ", gamepad1.right_stick_y);
            telemetry.update();

        }
    }
}
