

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.*;
/**
 *
 * @author Developer
 */
public class Teleop 
{
    
    //ControlStation controlStation;
    Drivetrain drivetrain;
    Shooter shooter;
    Arm arm;
    UserMessages userMessages;
    
    //Variables
    boolean hasFired = true;
    long fireStartTimeMS = 0;
    int rechargeTime = 1000;
    
    //User Message Variables
    boolean shiftMessage = false;
    boolean chargedMessage = false;
    boolean ballMessage = false;
    Joystick lJoy = new Joystick(1);
    Joystick rJoy = new Joystick(2);
    Joystick aJoy = new Joystick(3);
    
    public Teleop(ControlStation cs, Drivetrain dt, Shooter shoot, Arm a)
    {
        //controlStation = cs;
        drivetrain = dt;
        shooter = shoot;
        arm = a;
        userMessages = new UserMessages();
    }
    
    public void init()
    {
        //drivetrain.startDrivetrainEncoder(false);
    }
    
    public void runTeleop()
    {
        //Drive robot with joysticks
        drivetrain.setPower(lJoy.getY(), rJoy.getY());
        
        //Shifting code
        if(lJoy.getTrigger())
        {
            drivetrain.shift(true);
            shiftMessage = false;
        }
        else if(!lJoy.getTrigger())
        {
            drivetrain.shift(false);
            shiftMessage = true;
        }
        
        //Move arm with joystick     
        arm.setArmMotors(aJoy.getY()* 0.5);
        
        //Move arm with presets
        //These numbers must be tested
       /* if(controlStation.getFloorPreset() == ControlStation.PRESSED)
        {
            arm.moveArmTo(Arm.floorPreset);
        }
        if(controlStation.getScorePreset() == ControlStation.PRESSED)
        {
            arm.moveArmTo(Arm.scorePreset);
        }
        if(controlStation.getTrussPreset() == ControlStation.PRESSED)
        {
            arm.moveArmTo(Arm.trussPreset);
        }
       */ 
        //Fire!!
        if(!hasFired)
        {
            if(aJoy.getTrigger() && aJoy.getRawButton(3))
            {
                hasFired = true;
                fireStartTimeMS = System.currentTimeMillis();
                shooter.setFireSolenoid(true);
            }
        }
        else
        {
            if(!aJoy.getTrigger())
            {
                long elapsedTime = System.currentTimeMillis() - fireStartTimeMS;
                if(elapsedTime > rechargeTime)
                {
                    shooter.setFireSolenoid(false);
                    hasFired = false;
                    shooter.chargeShooter();
                }
            }
        }
        
        //Move the claw
        //Open
        if(aJoy.getRawButton(2))
        {
            arm.setGripperSolenoid(true);
        }
        
        //TODO Opperate the intake rollers
        if(aJoy.getRawButton(5))
        {
            arm.setIntakeRollers(1.0);
        } 
      //  else if(controlStation.getIntakeSwitch() == ControlStation.MIDDLE)
      //  {
      //      arm.setIntakeRollers (0.0);
      //  }
        else if(aJoy.getRawButton(4))
        {
            arm.setIntakeRollers (-1.0);
        }
        else
        {
            arm.setIntakeRollers(0.0);
        }
        
        chargedMessage = shooter.getWinchTouchSensor();
        ballMessage = arm.getGripperTouchSensor();
        
        userMessages.setDriveMessage(drivetrain.getDistance(), shiftMessage);
        userMessages.setArmMessage(arm.getArmEncoder(), chargedMessage, ballMessage);
        userMessages.update();
        
    }
}
