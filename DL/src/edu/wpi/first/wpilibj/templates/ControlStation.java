/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author Developer
 */

public class ControlStation 
{
    
 /*   DriverStation control;
    
    int fireButton = 1;//digital
    int winchButton = 2;//digital
    int clawButton = 3;//digital
    int floorPreset = 4;//digital
    int scorePreset = 5;//digital
    int trussPreset = 6;//digital
    
    int gripperSwitch = 1;//analog
    
    Joystick leftJoystick;
    Joystick rightJoystick;
    Joystick armJoystick;
    
    
    
    public static final int NOT_PRESSED = 0;
    public static final int PRESSED = 1;
    
    public static final int UP = 1;
    public static final int DOWN = 0;
    
    //Check these values!
    public static final int LEFT = -1;
    public static final int MIDDLE = 0;
    public static final int RIGHT = 1;
    
    public void init()
    {
        control = DriverStation.getInstance();
    }
    
    public double getLeftJoystick()
    {
        return leftJoystick.getY();
    }
    
    public double getRightJoystick()
    {
        return rightJoystick.getY();
    }
    
    public double getArmJoystick()
    {
        return armJoystick.getY();
    }
    
    public int getTrigger()
    {
        if(leftJoystick.getTrigger() || rightJoystick.getTrigger())
        {
            return PRESSED;
        }
        else
        {
            return NOT_PRESSED;
        }
    }
    
    public int getFireButton()
    {
        if(control.getDigitalIn(fireButton))
        {
            return PRESSED;
        }
        else
        {
            return NOT_PRESSED;
        }
    }
    
    public int getWinchButton()
    {
        if(control.getDigitalIn(winchButton))
        {
            return PRESSED;
        }
        else
        {
            return NOT_PRESSED;
        }
    }
    
    public int getFloorPreset()
    {
        return control.getDigitalIn(floorPreset)? PRESSED : NOT_PRESSED;
    }
    
    public int getScorePreset()
    {
        return control.getDigitalIn(scorePreset)? PRESSED : NOT_PRESSED;
    }
    
    public int getTrussPreset()
    {
        return control.getDigitalIn(trussPreset)? PRESSED : NOT_PRESSED;
    }
    
    public int getGripperSwitch()
    {
        if(control.getDigitalIn(clawButton))
        {
            return UP;
        }
        else
        {
            return DOWN;
        }
    }
    
    public int getIntakeSwitch()
    {
        //We need to check these values when we get the drivers station running
        double switchValue = control.getAnalogIn(gripperSwitch);
        if(switchValue < 0.7)
        {
            return LEFT;
        }
        else if(switchValue > 0.8 && switchValue < 2.0)
        {
            return MIDDLE;
        }
        else if(switchValue > 2.5)
        {
            return RIGHT;
        }
        return MIDDLE;
    }
    */
}
