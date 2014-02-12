/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.*;
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
    Encoder armEncoder;
    DigitalInput gripperTouchSensor;
    DigitalInput topArmLimit;
    DigitalInput bottomArmLimit;
    
    //TODO: These currently mean nothing!
    public static final int floorPreset = 0;
    public static final int scorePreset = 50;
    public static final int trussPreset = 75;
    
    public Arm(int armTal1, int armTal2, int rollTal, int armEnc1, int armEnc2, int gripSolenoid, int touch, int topTouch, int bottomTouch)
    {
        armTalon1 = new Talon(armTal1);
        armTalon2 = new Talon(armTal2);
        rollerTalon = new Talon(rollTal);
        
        armEncoder = new Encoder(armEnc1, armEnc2);
        
        gripperSolenoid = new Solenoid(gripSolenoid);
        
        gripperTouchSensor = new DigitalInput(touch);
        topArmLimit = new DigitalInput(topTouch);
        bottomArmLimit = new DigitalInput(bottomTouch);
    }
    
    public void setArmMotors(double arm)
    {
        armTalon1.set(-arm);
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
        if(setEnc)
        {
            armEncoder.start();
        }
        else if(!setEnc)
        {
            armEncoder.stop();
        }
    }
    
    public void resetArmEncoder()
    {
        armEncoder.reset();
    }
    
    public int getArmEncoder()
    {
        return armEncoder.get();
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