/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import frc.io.util.EMS22;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import java.lang.Math;

/**
 *
 * @author Developer
 */
public class Arm 
{
    Talon armTalon1;
    Talon armTalon2;
    Talon rollerTalon;
    Solenoid gripperSolenoid;
    EMS22 armEncoder;
    DigitalInput gripperTouchSensor;
    DigitalInput topArmLimit;
    DigitalInput bottomArmLimit;
    AnalogPotentiometer positionSensor;
    
    //TODO: These currently mean nothing!
    public static final double floorPreset = 0.0;
    public static final double scorePreset = 34.0;
    public static final double trussPreset = 57.0;
    public static final double autoScorePreset = 39.0;
    
    // TODO: work out what we may need to do to handle the possibility of a wrap
    // around since if we move 120 degrees at 4.5:1 it is 1.09 revolutions.

    /** Scale factor to convert encoder position to degrees.
     * The sprocket ratio is 4.5 to 1.
     */
    private static final double ARM_ENCODER_SCALE = 1.0;
    
    private double holdPosition = 90;
       
    public Arm(int armTal2, int rollTal, int gripSolenoid, int touch, int topTouch, int bottomTouch, int pot)
    {
        armTalon2 = new Talon(armTal2);
        rollerTalon = new Talon(rollTal);

//        armEncoder = new EMS22(
//            IOConfig.SIDECAR_SLOT,
//            IOConfig.SPI_SCLK,
//            IOConfig.SPI_MOSI,
//            IOConfig.SPI_MISO,
//            IOConfig.SPI_CS1);
//        armEncoder.setScaleFactor(ARM_ENCODER_SCALE);
        
        gripperSolenoid = new Solenoid(gripSolenoid);
        
        gripperTouchSensor = new DigitalInput(touch);
        topArmLimit = new DigitalInput(topTouch);
        bottomArmLimit = new DigitalInput(bottomTouch);
        
        positionSensor = new AnalogPotentiometer(pot);
    }
    
    public void setArmMotors(double arm)
    {
        armTalon2.set(-arm);
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
    }
    
    public void startArmEncoder(boolean setEnc)
    {
    }
    
    public void resetArmEncoder()
    {
    }
    
    public int getArmPosition()
    {
        final double minV = 1.34;
        final double maxV = 3.12;
        final double vRange = maxV - minV;
        final double angleOffSet = -22.93;
        final double angleRange = 90 - angleOffSet;
        double angleRatio = (positionSensor.get() - minV) / vRange;
        return (int)(angleRatio * angleRange + angleOffSet);
    }
    
    public void holdArmPosition()
    {
        holdPosition = getArmPosition();
    }
    
    public double getHoldPosition()
    {
        return holdPosition;
    }
    
    public void setHoldPosition(double angle)
    {
        holdPosition = angle;
        moveArmTo(holdPosition);
    }
    
    public void updateArmPosition()
    {
        int currentPosition = getArmPosition();
        final int tolerance = 1;
        if(currentPosition > (holdPosition + tolerance) || currentPosition < (holdPosition - tolerance))
        {
            moveArmTo(holdPosition);
        }
        else
        {
            setArmMotors(0.0);
        }
        
    }
    
    
    
//    public int getArmEncoder()
//    {
//        EMS22.EncoderResult result = armEncoder.get();
//        return result.rawValue;
//    }
    
    //THIS MUST BE TESTED
    public void moveArmTo(double targetPosition)
    {
        double p = 0.05; //TODO:This needs to be tuned
        double error = targetPosition - getArmPosition();
        
        error *= p;
        if(error > 1.0)
        {
            error = 1.0;
        }
        else if(error < -1.0)
        {
            error = -1.0;
        }
        
        if(Math.abs(error) < 0.1)
        {
            error = 0;
        }
        
        setArmMotors(error);
    }
}