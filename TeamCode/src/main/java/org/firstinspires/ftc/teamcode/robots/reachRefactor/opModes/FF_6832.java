package org.firstinspires.ftc.teamcode.robots.reachRefactor.opModes;

import org.ejml.simple.SimpleMatrix;
import org.firstinspires.ftc.teamcode.robots.reachRefactor.utils.Constants;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robots.reachRefactor.subsystems.Robot;
import org.firstinspires.ftc.teamcode.robots.reachRefactor.utils.ExponentialSmoother;
import org.firstinspires.ftc.teamcode.robots.reachRefactor.utils.StickyGamepad;

/** Controls
 * Pregame
 * x - set alliance to blue
 * b - set alliance to red
 * a - toggle FTC dashboard
 * y - toggle drivetrain smoothing
 * dpad_up - toggle debug telemetry
 * left bumper - decrement state
 * right bumper - increment state
 *
 * Tele-Op
 * right stick y - forward
 * left stick x - rotate
 * guide - emergency stop
 */
@TeleOp(name = "refactored FF_6832")
public class FF_6832 extends OpMode {
    private Robot robot;

    // global state
    private boolean active;
    private Constants.GameState gameState;
    private int gameStateIndex;
    private StickyGamepad stickyGamepad1, stickyGamepad2;

    // TPM Calibration state
    private boolean TPMCalibrationInitialized;
    private SimpleMatrix TPMCalibrationStartingTicks;
    private double averageTPMCalibrationTicksTraveled;
    private double calculatedTPM;

    // timing
    private long lastLoopClockTime, loopTime;
    private double averageLoopTime;
    private ExponentialSmoother loopTimeSmoother;

    // Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        // global state
        active = true;
        gameState = Constants.GameState.TELE_OP;

        // TPM calibration state
        TPMCalibrationInitialized = false;
        TPMCalibrationStartingTicks = new SimpleMatrix(1, 3);

        // timing
        lastLoopClockTime = System.nanoTime();
        loopTimeSmoother = new ExponentialSmoother(Constants.AVERAGE_LOOP_TIME_SMOOTHING_FACTOR);

        // gamepads
        stickyGamepad1 = new StickyGamepad(gamepad1);
        stickyGamepad2 = new StickyGamepad(gamepad2);

        robot = new Robot(hardwareMap, telemetry, Constants.DEFAULT_DASHBOARD_ENABLED);
    }

    private void handleStateSwitch() {
        if (!active) {
            if (stickyGamepad1.left_bumper)
                gameStateIndex -= 1;
            if (stickyGamepad1.right_bumper)
                gameStateIndex += 1;

            gameStateIndex %= Constants.GameState.getNumGameStates();
        }

        if (stickyGamepad1.start)
            active = !active;
    }

    private void handlePregameControls() {
        if(stickyGamepad1.x)
            robot.setAlliance(Constants.Alliance.BLUE);
        if(stickyGamepad1.b)
            robot.setAlliance(Constants.Alliance.RED);

        if(stickyGamepad1.a)
            robot.toggleIsDashboardEnabled();
        if(stickyGamepad1.y)
            robot.driveTrain.toggleSmoothingEnabled();
    }

    // Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    @Override
    public void init_loop() {
        handleStateSwitch();

        if (active)
            handlePregameControls();

        update();
    }

    @Override
    public void start() {
        lastLoopClockTime = System.nanoTime();
    }

    private void handleTeleOpDrive() {
        double forward = Math.pow(-gamepad1.right_stick_y, 3) * Constants.FORWARD_SCALING_FACTOR;
        double rotate = Math.pow(gamepad1.left_stick_x, 3) * Constants.ROTATE_SCALING_FACTOR;

        robot.driveTrain.drive(forward, rotate);
    }

    private void handleEmergencyStop() {
        if(stickyGamepad1.guide || stickyGamepad2.guide)
            robot.stop();
    }

    private void handleTeleOp() {
        handleTeleOpDrive();
        handleEmergencyStop();
    }

    private void handleTPMCalibration() {
        handleTeleOpDrive();
        handleEmergencyStop();

        // initializing TPM calibration state
        if(!TPMCalibrationInitialized) {
            TPMCalibrationStartingTicks = robot.driveTrain.getWheelTicks().cols(0, 1);
            TPMCalibrationInitialized = true;
        }

        // calculating average wheel ticks traveled
        SimpleMatrix ticks = robot.driveTrain.getWheelTicks().cols(0, 1);
        SimpleMatrix ones = new SimpleMatrix(new double[][] {{1, 1, 1}});
        averageTPMCalibrationTicksTraveled = ones.mult(ticks.minus(TPMCalibrationStartingTicks)).divide(3).get(0);
    }

    @Override
    public void loop() {
        handleStateSwitch();

        if (active)
            switch(gameState) {
                case TELE_OP:
                    handleTeleOp();
                    break;
                case AUTONOMOUS:
                    if (robot.getAlliance().equals(Constants.Alliance.RED)
                            && robot.articulate(Robot.Articulation.AUTONOMOUS_RED)) {
                        active = false;
                        gameState = Constants.GameState.TELE_OP;
                        gameStateIndex = Constants.GameState.indexOf(Constants.GameState.TELE_OP);
                    } else if (robot.getAlliance().equals(Constants.Alliance.BLUE)
                            && robot.articulate(Robot.Articulation.AUTONOMOUS_BLUE)) {
                        active = false;
                        gameState = Constants.GameState.TELE_OP;
                        gameStateIndex = Constants.GameState.indexOf(Constants.GameState.TELE_OP);
                    }
                    break;
                case TPM_CALIBRATION:
                    handleTPMCalibration();
                    break;
            }

        update();
    }

    private void updateTiming() {
        long loopClockTime = System.nanoTime();
        loopTime = loopClockTime - lastLoopClockTime;
        averageLoopTime = loopTimeSmoother.update(loopTime);
        lastLoopClockTime = loopClockTime;
    }

    private void update() {
        stickyGamepad1.update();
        stickyGamepad2.update();

        updateTiming();

        if(robot.isTelemetryDebugEnabled()) {
            robot.addTelemetryData("Average Loop Time", String.format("%d ms (%d hz)", (int) (averageLoopTime * 1e-6), (int) (1 / (averageLoopTime * 1e-9))));
            robot.addTelemetryData("Last Loop Time", String.format("%d ms (%d hz)", (int) (loopTime * 1e-6), (int) (1 / (loopTime * 1e-9))));
        }
        robot.addTelemetryData("Active", active);
        robot.addTelemetryData("State", String.format("(%d): %s", gameStateIndex, gameState));
        robot.addTelemetryData("Smoothing Enabled", robot.driveTrain.isSmoothingEnabled());
        robot.addTelemetryData("Dashboard Enabled", robot.isDashboardEnabled());

        switch(gameState) {
            case TELE_OP:
                break;
            case AUTONOMOUS:
                break;
            case TPM_CALIBRATION:
                robot.addTelemetryData("Average Ticks Traveled", averageTPMCalibrationTicksTraveled);
                break;
        }

        robot.update();
    }
}
