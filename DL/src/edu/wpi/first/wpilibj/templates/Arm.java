/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import frc.io.util.EMS22;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;

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
    
    //TODO: These currently mean nothing!
    public static final int floorPreset = 0;
    public static final int scorePreset = 50;
    public static final int trussPreset = 75;
    
    // TODO: work out what we may need to do to handle the possibility of a wrap
    // around since if we move 120 degrees at 4.5:1 it is 1.09 revolutions.

    /** Scale factor to convert encoder position to degrees.
     * The sprocket ratio is 4.5 to 1.
     */
    private static final double ARM_ENCODER_SCALE = 1.0;
       
    public Arm(int armTal1, int armTal2, int rollTal, int armEnc1, int armEnc2, int gripSolenoid, int touch, int topTouch, int bottomTouch)
    {
        armTalon1 = new Talon(armTal1);
        armTalon2 = new Talon(armTal2);
        rollerTalon = new Talon(rollTal);

        armEncoder = new EMS22(
            IOConfig.SIDECAR_SLOT,
            IOConfig.SPI_SCLK,
            IOConfig.SPI_MOSI,
            IOConfig.SPI_MISO,
            IOConfig.SPI_CS1);
        armEncoder.setScaleFactor(ARM_ENCODER_SCALE);
        
        gripperSolenoid = new Solenoid(gripSolenoid);
        
        gripperTouchSensor = new DigitalInput(touch);
        topArmLimit = new DigitalInput(topTouch);
        bottomArmLimit = new DigitalInput(bottomTouch);
    }
    
    public void setArmMotors(double arm)
    {
        armTalon2.set(arm);
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
    
    public int getArmEncoder()
    {
        EMS22.EncoderResult result = armEncoder.get();
        return result.rawValue;
    }
    
    //THIS MUST BE TESTED
    public void moveArmTo(int targetPosition)
    {
        double p = 0.05; //TODO:This needs to be tuned
        double error = targetPosition - getArmEncoder();
        
        error *= p;
        
        setArmMotors(error);
    }
}