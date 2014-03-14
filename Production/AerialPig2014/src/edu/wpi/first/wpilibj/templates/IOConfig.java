/*
 * Configuration values for the Robot
 */

package edu.wpi.first.wpilibj.templates;

import frc.io.util.Digital;

/**
 * Container for configuration values. Consolidate all application-specific 
 * hardware port assignments etc. in this class.
 * 
 * @author Michael Holman
 */
public class IOConfig
{
    public static final int SIDECAR_SLOT = 2;

    // --- Arm ---
    public static final int SPI_MOSI            = Digital.GPIO1;
    public static final int SPI_MISO            = Digital.GPIO2;
    public static final int SPI_SCLK            = Digital.GPIO3;
    public static final int SPI_CS1             = Digital.GPIO4;
    public static final int SHOOTER_TOUCH       = Digital.GPIO11;
    public static final int GRIPPER_TOUCH       = Digital.GPIO10;
    public static final int ARM_LIMIT_TOP       = Digital.GPIO12;
    public static final int ARM_LIMIT_BOTTOM    = Digital.GPIO13;
    public static final int SHOOTER_PWM_1       = Digital.PWM5;
    public static final int SHOOTER_PWM_2       = Digital.PWM6;
    public static final int SHOOTER_FIRE        = Digital.RELAY2;
    public static final int SHOOTER_FIRE_SAFE   = Digital.RELAY4;

    // --- Drivetrain ---
    public static final int DRIVE_ENCODER_L1    = Digital.GPIO5;
    public static final int DRIVE_ENCODER_L2    = Digital.GPIO6;
    public static final int DRIVE_ENCODER_R1    = Digital.GPIO7;
    public static final int DRIVE_ENCODER_R2    = Digital.GPIO8;
    public static final int DRIVE_PWM_L1        = Digital.PWM1;
    public static final int DRIVE_PWM_L2        = Digital.PWM2;
    public static final int DRIVE_PWM_R1        = Digital.PWM3;
    public static final int DRIVE_PWM_R2        = Digital.PWM4;
    public static final int DRIVE_SHIFT         = Digital.RELAY1;

    // --- Compressor ---
    public static final int COMPRESSOR_PRESSURE = Digital.GPIO9;
    public static final int COMPRESSOR_RELAY    = Digital.RELAY1;
}
