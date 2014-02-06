/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.*;
import java.util.*;

/**
 *
 * @author Developer
 */
public class DriverStation {
    
    Joystick leftJoystick;
    Joystick rightJoystick;
    Joystick armJoystick;
    DigitalInput gripperButton;
    DigitalInput clawButton;
    DigitalInput winchButton;
    
    public static final int NOT_PRESSED = 0;
    public static final int PRESSED = 1;
    
    public DriverStation(int lJoy, int rJoy, int armJoy, int gripper, int claw, int winch)
    {
        leftJoystick = new Joystick(lJoy);
        rightJoystick = new Joystick(rJoy);
        armJoystick = new Joystick(armJoy);
        gripperButton = new DigitalInput(gripper);
        clawButton = new DigitalInput(claw);
        winchButton = new DigitalInput(winch);
        
        
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
        if(armJoystick.getTrigger())
        {
            return PRESSED;
        }
        else
        {
            return NOT_PRESSED;
        }
    }
}
