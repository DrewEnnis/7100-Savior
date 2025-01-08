/* -------------------------------------------------------
   Copyright (c) [2024] FASNY
   All rights reserved
   -------------------------------------------------------
   Generic configuration
   ------------------------------------------------------- */

package org.firstinspires.ftc.teamcode.configurations;

/* System includes */
import java.util.HashMap;
import java.util.Map;

abstract public class Configuration {
    // Map to store hardware components by reference name
    public final Map<String, Motor>      m_motors = new HashMap<>();
    public final Map<String, Imu>        m_imus   = new HashMap<>();
    public final Map<String, ServoMotor> m_servos = new HashMap<>();

    public static Configuration s_Current = new Test();

    // Method to retrieve a motor by its reference name
    public Motor getMotor(String name) {
        if (m_motors.containsKey(name)) { return m_motors.get(name); }
        else                            { return null;               }
    }

    // Method to retrieve an imu by its reference name
    public Imu getImu(String name) {
        if (m_imus.containsKey(name)) { return m_imus.get(name); }
        else                          { return null;             }
    }

    // Method to retrieve a servo by its reference name
    public ServoMotor getServo(String name) {
        if (m_servos.containsKey(name)) { return m_servos.get(name); }
        else                            { return null;               }
    }

    // Abstract method for initializing specific configurations
    protected abstract void initialize();

    // Constructor
    public Configuration() {
        initialize();
    }
}
