/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Subsystems.ArmSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.DrivetrainSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.ErectorSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.ExtenderSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.VisionSubsystem;
import org.firstinspires.ftc.teamcode.Subsystems.WristSubsystem;
import org.firstinspires.ftc.teamcode.Utilities.Constants;

@TeleOp(name="Main TeleOp", group="Linear OpMode")
//@Disabled
public class MainTeleOp extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //Initialize Subsystems
        DrivetrainSubsystem drivetrainSubsystem = new DrivetrainSubsystem(
                hardwareMap.get(DcMotor.class, Constants.backLeftDriveID),
                hardwareMap.get(DcMotor.class, Constants.backRightDriveID),
                hardwareMap.get(DcMotor.class, Constants.frontLeftDriveID),
                hardwareMap.get(DcMotor.class, Constants.frontRightDriveID),
                hardwareMap.get(IMU.class, "imu"));

        IntakeSubsystem intakeSubsystem = new IntakeSubsystem(hardwareMap.get(DcMotor.class, Constants.intakeID), runtime);

        ArmSubsystem armSubsystem = new ArmSubsystem(hardwareMap.get(DcMotor.class, Constants.armID), hardwareMap.get(TouchSensor.class, Constants.armLimitSwitchID));

        /*
        ErectorSubsystem erectorSubsystem = new ErectorSubsystem(hardwareMap.get(DcMotor.class, Constants.extenderID));
        erectorSubsystem.initialize();
         */

        ExtenderSubsystem extenderSubsystem = new ExtenderSubsystem(hardwareMap.get(DcMotor.class, Constants.extenderID), hardwareMap.get(TouchSensor.class, Constants.extenderLimitSwitchID));

        WristSubsystem wristSubsystem = new WristSubsystem(hardwareMap.get(Servo.class, Constants.wristID), runtime);

        VisionSubsystem visionSubsystem = new VisionSubsystem(hardwareMap.get(WebcamName.class, "Webcam 1"));
        visionSubsystem.resumeStreaming();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            drivetrainSubsystem.teleOPDrive(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_y);
            armSubsystem.ManualPositionArm(gamepad2.dpad_left, gamepad2.dpad_right);
            //erectorSubsystem.teleOPErect(gamepad2.dpad_down, gamepad2.dpad_up);
            intakeSubsystem.teleOPIntake(gamepad2.right_trigger, gamepad2.left_trigger);
            wristSubsystem.manualAngleWrist(gamepad2.right_bumper, gamepad2.left_bumper);


            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Drive Motors", "frontLeft (%.2f), backLeft (%.2f), frontRight (%.2f), backRight (%.2f)",
                    drivetrainSubsystem.getFrontLeftPower(),
                    drivetrainSubsystem.getBackLeftPower(),
                    drivetrainSubsystem.getFrontRightPower(),
                    drivetrainSubsystem.getBackRightPower());
            telemetry.update();
        }
    }
}
