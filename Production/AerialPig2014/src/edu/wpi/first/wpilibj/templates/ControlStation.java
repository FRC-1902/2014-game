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
    Joystick armJoystick;
    
    
    
    public static final int NOT_PRESSED = 0;
    public static final int PRESSED = 1;
    
    public static final int UP = 1;
    public static final int DOWN = 0;
    
    //Check these values!
    public static final int LEFT = -1;
    public static final int MIDDLE = 0;
    public static final int RIGHT = 1;
    
    public ControlStation(int lJoy, int rJoy, int aJoy)
    {
        leftJoystick = new Joystick(lJoy);
        rightJoystick = new Joystick(rJoy);
        armJoystick = new Joystick(aJoy);
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
        if(armJoystick.getTrigger() && armJoystick.getRawButton(6))
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
        if(armJoystick.getRawButton(10))
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
//    public int getScorePreset()
//    {
//        return control.getDigitalIn(scorePreset)? PRESSED : NOT_PRESSED;
//    }
//    
//    public int getTrussPreset()
//    {
//        return control.getDigitalIn(trussPreset)? PRESSED : NOT_PRESSED;
//    }
    
    public int getGripperSwitch()
    {
        if(armJoystick.getRawButton(5))
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
        if(armJoystick.getRawButton(3))
        {
            return LEFT;
        }
        else if(armJoystick.getRawButton(2))
        {
            return RIGHT;
        }
        else 
        {
            return MIDDLE;
        }
        //return MIDDLE;
    }
}
