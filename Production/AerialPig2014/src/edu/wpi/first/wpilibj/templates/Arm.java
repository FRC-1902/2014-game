/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Solenoid;
import frc.io.util.EMS22;

/**
 *
 * @author Developer
 */
public class Arm 
{
    public static final double startPreset     =  86.0;
    public static final double floorPreset     = -20.0;
    public static final double scorePreset     =  25.5;
    public static final double trussPreset     =  57.0;
    public static final double autoScorePreset =  21.0;
    
    public double hotKeyPreset = 90.0;

    // The physical angles. These should not change.
    public static final double ARM_ANGLE_MIN = -22.93;
    public static final double ARM_ANGLE_MAX =  90.0;
    public static final double ARM_ANGLE_RANGE = ARM_ANGLE_MAX - ARM_ANGLE_MIN;
    // The tolerance on the arm angle in degrees.
    public static final double ARM_ANGLE_TOL =  1.0; // degrees tolerance
    // Maximum energy with which we want to drive the motor.
    public static final double ANGLE_MOTOR_PWM_MIN = -0.50;
    public static final double ANGLE_MOTOR_PWM_MAX =  0.50;
    // Proportional, Integral, and Derivative constants (must be tuned).
    private static final double Kp = 0.3;
    private static final double Ki = 0.1;
    private static final double Kd = 0.0;

    public boolean useEncoder = false;
    public boolean usePID     = false;
    
    private final Talon angleTalon;
    private final Talon rollerTalon;
    private final Solenoid gripperSolenoid;
    private final DigitalInput gripperTouchSensor;
    private final DigitalInput topArmLimit;
    private final DigitalInput bottomArmLimit;
    private final EMS22 positionEncoder;
    private final AnalogPotentiometer positionSensor;
    private final PIDController anglePID;
    
    private double potMinV = 1.96;
    private double potMaxV = 2.90;
    private double potVRange = potMaxV - potMinV;

    private double  holdPosition = startPreset;
    private boolean angleControlEnabled = true;
       
    public Arm(int angleTal, int rollTal, int gripSolenoid, int touch, int topTouch, int bottomTouch, int pot)
    {
        loadCalibration();
        
        angleTalon  = new Talon(angleTal);
        rollerTalon = new Talon(rollTal);
        
        gripperSolenoid = new Solenoid(gripSolenoid);
        
        gripperTouchSensor = new DigitalInput(touch);
        topArmLimit        = new DigitalInput(topTouch);
        bottomArmLimit     = new DigitalInput(bottomTouch);

        PIDSource anglePIDSource = null;
        if (useEncoder)
        {
            positionEncoder = new EMS22(
                IOConfig.SIDECAR_SLOT,
                IOConfig.SPI_SCLK,
                IOConfig.SPI_MOSI,
                IOConfig.SPI_MISO,
                IOConfig.SPI_CS1,
                ARM_ANGLE_MIN,
                ARM_ANGLE_MAX
            );
            positionEncoder.setScaledUnits("Degrees");
            anglePIDSource = positionEncoder;
            positionSensor = null;
        }
        else
        {
            // Use potentiometer
            positionEncoder = null;
            positionSensor = new AnalogPotentiometer(pot);
            anglePIDSource = positionSensor;
        }
        if (usePID)
        {
            // Initialize the PID Controller; must be after the the source has been done.
            anglePID = new PIDController(Kp, Ki, Kd, anglePIDSource, angleTalon);
            anglePID.setInputRange(ARM_ANGLE_MIN, ARM_ANGLE_MAX);
            anglePID.setAbsoluteTolerance(ARM_ANGLE_TOL);
            anglePID.setOutputRange(ANGLE_MOTOR_PWM_MIN, ANGLE_MOTOR_PWM_MAX);
            if (angleControlEnabled)
            {
                // it will drive to the ini
                anglePID.enable();
            }
        }
        else
        {
            anglePID = null;
        }
    }
    
    public void setArmMotors(double arm)
    {
        angleTalon.set(-arm);
    }
    
    public void setIntakeRollers(double roll)
    {
        rollerTalon.set(roll);
    }
    
    public boolean getGripperTouchSensor()
    {
        return gripperTouchSensor.get();
    }
    
    public boolean getTopArmLimit()
    {
        return topArmLimit.get();
    }
    
    public boolean getBottomArmLimit()
    {
        return bottomArmLimit.get();
    }
    
    public void setGripperSolenoid(boolean gripper)
    {
        gripperSolenoid.set(gripper);
        System.out.println("Gripper set: " + (gripper ? "Open" : "Closed"));
    }
    
    public String getStatus()
    {
        if (useEncoder)
        {
            // absolute encoder
            return positionEncoder.isError() ? "Error" : "OK";
        }
        else
        {
            // potentiometer
            return "OK";
        }
    }
    
    public double getRawPosition()
    {
        if (useEncoder)
        {
            // absolute encoder
            return (double)positionEncoder.getRaw();
        }
        else
        {
            // potentiometer
            return positionSensor.get();
        }
    }
    
    public double getArmPosition()
    {
        if (useEncoder)
        {
            // absolute encoder
            return positionEncoder.getScaled();
        }
        else
        {
            // potentiometer
            double angleRatio = (positionSensor.get() - potMinV) / potVRange;
            return angleRatio * ARM_ANGLE_RANGE + ARM_ANGLE_MIN;
        }
    }
    
    public void holdArmPosition()
    {
        setHoldPosition(getArmPosition());
    }
    
    public double getHoldPosition()
    {
        return holdPosition;
    }
    
    public void setHoldPosition(double angle)
    {
        if (angle > ARM_ANGLE_MAX)
        {
            angle = ARM_ANGLE_MAX;
        }
        if (angle < ARM_ANGLE_MIN)
        {
            angle = ARM_ANGLE_MIN;
        }
        holdPosition = angle;
        if (usePID)
        {
            anglePID.setSetpoint(holdPosition);
        }
        else
        {
            moveArmTo(holdPosition);
        }
        
        System.out.println("Hold Position: " + angle);
    }
    
    public void setHotKeyPreset()
    {
        hotKeyPreset = getArmPosition();
    }
    
    /**
     * Enables active control of the arm angle using the position sensor and
     * automatically driving the angle motor to hold setpoints.
     */
    public void enableAngleControl()
    {
        angleControlEnabled = true;
        if (usePID)
        {
            anglePID.enable();
        }
    }
    
    /**
     * Disables active control of the arm angle. Manual control of the angle
     * motor will have to be used.
     */
    public void disableAngleControl()
    {
        angleControlEnabled = false;
        if (usePID)
        {
            anglePID.disable();
        }
        setArmMotors(0.0);
    }
    
    /**
     * Called from main loop. Required when not using PID and using simple
     * proportional control.
     */
    public void updateArmPosition()
    {
        System.out.println("updateArmPosition - enabled: " + angleControlEnabled + " hold: " + holdPosition + " angle: " + getArmPosition());
        if (angleControlEnabled && !usePID)
        {
            double currentPosition = getArmPosition();
            final double tolerance = ARM_ANGLE_TOL;
            if (currentPosition > (holdPosition + tolerance) || currentPosition < (holdPosition - tolerance))
            {
                moveArmTo(holdPosition);
            }
            else
            {
                setArmMotors(0.0);
            }
        }
    }
    
    /**
     * Moves the arm to the specified position, which should be within the range
     * limits. This does not update the hold position when simple proportional
     * is used.
     * @param targetPosition 
     */
    public void moveArmTo(double targetPosition)
    {
        targetPosition = constrainPosition(targetPosition);
        if (usePID)
        {
            anglePID.setSetpoint(targetPosition);
        }
        else
        {
            simpleProportional(targetPosition);
        }
    }

    /**
     * Constrains the given position according to the range limits.
     * @param position  The position angle to be constrained.
     * @return 
     */
    public double constrainPosition(double position)
    {
        position = Math.max(position, ARM_ANGLE_MIN);
        position = Math.min(position, ARM_ANGLE_MAX);
        return position;
    }

    /**
     * Calibrates the max range value. The arm should be positioned fully up
     * when this is called.
     */
    public void calibrateMaxPosition()
    {
        if (useEncoder)
        {
            positionEncoder.calibrateFullCount();
            System.out.println("Encoder fullscale: " + positionEncoder.getFullCount());
        }
        else
        {
            potMaxV = positionSensor.get(); // potentiometer
            potVRange = potMaxV - potMinV;
            System.out.println("Potentiometer Vmax: " + potMaxV);
        }
        saveCalibration();
    }
    
    /**
     * Calibrates the max range value. The arm should be positioned fully down
     * when this is called.
     */
    public void calibrateMinPosition()
    {
        if (useEncoder)
        {
            positionEncoder.calibrateOffsetCount();
            System.out.println("Encoder offset: " + positionEncoder.getOffsetCount());
        }
        else
        {
            potMinV = positionSensor.get(); // potentiometer
            potVRange = potMaxV - potMinV;
            System.out.println("Potentiometer Vmin: " + potMinV);
        }
        saveCalibration();
    }
    
    /**
     * 
     * @param targetPosition 
     */
    private void simpleProportional(double targetPosition)
    {
        double p = 0.05; //TODO:This needs to be tuned
        double error = targetPosition - getArmPosition();
        
        error *= p;
        error = Math.max(error, ANGLE_MOTOR_PWM_MIN);
        error = Math.min(error, ANGLE_MOTOR_PWM_MAX);
        
        if (Math.abs(error) < 0.03)
        {
            error = 0;
        }
        
        setArmMotors(error);
    }
    
    /**
     * Save calibration factors to non-volatile memory.
     */
    private void saveCalibration()
    {
        // TODO: Implement save to flash memory
    }
    
    /**
     * Load calibration factors from non-volatile memory.
     */
    private void loadCalibration()
    {
        // TODO: Implement read from flash memory
    }
}