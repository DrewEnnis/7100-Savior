package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.drivetrain.MechDrive;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.Imu;
import org.firstinspires.ftc.teamcode.subsystems.Limelight;
import org.firstinspires.ftc.teamcode.subsystems.ThreeDeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.utils.DriverHubHelp;
import org.firstinspires.ftc.teamcode.utils.GamepadEvents;
//@Autonomous(name="BasicTimeAuto")
public class BasicTimeAuto extends LinearOpMode {
    private GamepadEvents controller;
    private MechDrive robot;
    private Limelight limelight;
    private Imu imu;
    private ThreeDeadWheelLocalizer deadwheels;
    private DriverHubHelp screen;
    double forward;
    double strafe;
    double rotate;
    @Override
    public void runOpMode() throws InterruptedException {
        robot = new MechDrive(hardwareMap);
        limelight = new Limelight(hardwareMap);
        imu = new Imu(hardwareMap);
        screen = new DriverHubHelp();
        deadwheels = new ThreeDeadWheelLocalizer(hardwareMap,0.0029);
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));

        waitForStart();

        while(opModeIsActive())
        {
                strafe = 0;
                rotate = 0;
                forward = -0.2;
                robot.drive(forward,strafe,rotate);

        }
    }
}
