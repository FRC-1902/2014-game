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
    
    DriverStation control;
    
    private final int fireButton = 1;//digital
    private final int winchButton = 2;//digital
    private final int clawButton = 3;//digital
    private final int floorPreset = 4;//digital
    private final int scorePreset = 5;//digital
    private final int trussPreset = 6;//digital
    
    private int gripperSwitch = 1;//analog
    
    Joystick leftJoystick;
    Joystick rightJoystick;
    Joystick armJoystickR;
    Joystick armJoystickL;
    
    
    
    public static final int NOT_PRESSED = 0;
    public static final int PRESSED = 1;
    
    public static final int UP = 1;
    public static final int DOWN = 0;
    
    //Check these values!
    public static final int LEFT = -1;
    public static final int MIDDLE = 0;
    public static final int RIGHT = 1;
    
    public ControlStation(int lJoy, int rJoy, int aJoy, int aJoy2)
    {
        leftJoystick = new Joystick(lJoy);
        rightJoystick = new Joystick(rJoy);
        armJoystickR = new Joystick(aJoy);
        armJoystickL = new Joystick(aJoy2);
    }
    
    public void init()
    {
        control = DriverStation.getInstance();
    }
    
    //Joystick forward returns a negative value
    //Backwards returns a positive value
    
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
        return armJoystickR.getY();
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
        if(armJoystickR.getTrigger() && armJoystickL.getTrigger())
        {
            System.out.println("Pressing fire button");
            return PRESSED;
        }
        else
        {
            return NOT_PRESSED;
        }
    }
    
    public int getWinchButton()
    {
        if(armJoystickL.getRawButton(4))
        {
            return PRESSED;
        }
        else
        {
            return NOT_PRESSED;
        }
    }
    
//    public int getFloorPreset()
//    {
//        return control.getDigitalIn(floorPreset)? PRESSED : NOT_PRESSED;
//    }
//    
    public int getScorePreset()
    {
        return armJoystickL.getRawButton(2) ? PRESSED : NOT_PRESSED;
    }
    
    public int getTrussPreset()
    {
        return armJoystickL.getRawButton(3) ? PRESSED : NOT_PRESSED;
    }
    
    public int getHotKeyPreset()
    {
        return armJoystickL.getRawButton(5) ? PRESSED : NOT_PRESSED;
    }
       
    public int setHotKeyPreset()
    {
        return armJoystickR.getRawButton(4) ? PRESSED : NOT_PRESSED;
    }
    
    public int getGripperSwitch()
    {
        if(armJoystickR.getRawButton(5))
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
        //double switchValue = control.getAnalogIn(gripperSwitch);
        if(armJoystickR.getRawButton(3))
        {
            return LEFT;
        }
        else if(armJoystickR.getRawButton(2))
        {
            return RIGHT;
        }
        else 
        {
            return MIDDLE;
        }
        //return MIDDLE;
    }
    
    public int enablePID()
    {
        if(armJoystickR.getRawButton(6))
        {
            return PRESSED;
        }
        else
        {
            return NOT_PRESSED;
        }
    }
    
    public int disablePID()
    {
        if(armJoystickR.getRawButton(7))
        {
            return PRESSED;
        }
        else
        {
            return NOT_PRESSED;
        }
    }
}
