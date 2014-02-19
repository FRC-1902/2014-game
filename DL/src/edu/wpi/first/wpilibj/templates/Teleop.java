

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.*;
/**
 *
 * @author Developer
 */
public class Teleop 
{
    
    ControlStation controlStation;
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
    
    public Teleop(ControlStation cs, Drivetrain dt, Shooter shoot, Arm a)
    {
        controlStation = cs;
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
        drivetrain.setPower(controlStation.getLeftJoystick(), controlStation.getRightJoystick());
        
        //Shifting code
        if(controlStation.getTrigger() == ControlStation.PRESSED)
        {
            drivetrain.shift(true);
            shiftMessage = false;
        }
        else if(controlStation.getTrigger() == ControlStation.NOT_PRESSED)
        {
            drivetrain.shift(false);
            shiftMessage = true;
        }
        
        //Move arm with joystick     
        arm.setArmMotors(controlStation.getArmJoystick());
        
        //Move arm with presets
        //These numbers must be tested
        if(controlStation.getFloorPreset() == ControlStation.PRESSED)
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
        
        //Fire!!
        if(!hasFired)
        {
            if(controlStation.fireButton == ControlStation.PRESSED)
            {
                hasFired = true;
                fireStartTimeMS = System.currentTimeMillis();
                shooter.setFireSolenoid(true);
            }
        }
        else
        {
            if(controlStation.fireButton == ControlStation.NOT_PRESSED)
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
        if(controlStation.getGripperSwitch() == ControlStation.UP)
        {
            arm.setGripperSolenoid(true);
        }
        
        //TODO Opperate the intake rollers
        if(controlStation.getIntakeSwitch()== ControlStation.LEFT)
        {
            arm.setIntakeRollers(1.0);
        } 
        else if(controlStation.getIntakeSwitch() == ControlStation.MIDDLE)
        {
            arm.setIntakeRollers (0.0);
        }
        else if(controlStation.getIntakeSwitch() == ControlStation.RIGHT)
        {
            arm.setIntakeRollers (-1.0);
        }
        
        chargedMessage = shooter.getWinchTouchSensor();
        ballMessage = arm.getGripperTouchSensor();
        
        userMessages.setDriveMessage(drivetrain.getDistance(), shiftMessage);
        userMessages.setArmMessage(arm.getArmEncoder(), chargedMessage, ballMessage);
        userMessages.update();
        
    }
}
