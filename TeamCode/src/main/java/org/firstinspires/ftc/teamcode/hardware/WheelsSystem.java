package org.firstinspires.ftc.teamcode.hardware;

public abstract class WheelsSystem {
    // A modifier for much power the wheels run with (0.0 - 1.0)
    protected double wheelPower = 1.0;
    
    public WheelsSystem() {

    }

    /**
     * 
     * @return A DcMotor[] that contains every DcMotor included by the Wheels
     */
    public abstract DcMotor[] getAllMotors();
}
