

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.*;
/**
 *
 * @author Developer
 */
public class Teleop {
    
    ControlStation controlStation;
    Drivetrain drivetrain;
    Shooter shooter;
    Arm arm;
    
    //Variables
    boolean hasFired = false;
    long fireStartTimeMS = 0;
    int rechargeTime = 1000;
    //TODO: These currently mean nothing!
    int floorPreset = 0;
    int scorePreset = 50;
    int trussPreset = 75;
    
    public Teleop(ControlStation cs, Drivetrain dt, Shooter shoot, Arm a)
    {
        controlStation = cs;
        drivetrain = dt;
        shooter = shoot;
        arm = a;
    }
    
    public void init()
    {
        drivetrain.startDrivetrainEncoder(false);
    }
    
    public void runTeleop()
    {
        //Drive robot with joysticks
        drivetrain.setPower(controlStation.getLeftJoystick(), controlStation.getRightJoystick());
        
        //Shifting code
        if(controlStation.getTrigger() == ControlStation.PRESSED)
        {
            drivetrain.shift(true);
        }
        else if(controlStation.getTrigger() == ControlStation.NOT_PRESSED)
        {
            drivetrain.shift(false);
        }
        
        //Move arm with joystick
        
        arm.setArmMotors(controlStation.getArmJoystick());
        
        //Move arm with presets
        //These numbers must be tested
        if(controlStation.getFloorPreset() == ControlStation.PRESSED)
        {
            arm.moveArmTo(floorPreset);
        }
        if(controlStation.getScorePreset() == ControlStation.PRESSED)
        {
            arm.moveArmTo(scorePreset);
        }
        if(controlStation.getTrussPreset() == ControlStation.PRESSED)
        {
            arm.moveArmTo(trussPreset);
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
        if(controlStation.getIntakeSwitch()== controlStation.LEFT)
        {
            arm.setIntakeRollers(1.0);
        } 
        else if(controlStation.getIntakeSwitch() == controlStation.MIDDLE)
        {
            arm.setIntakeRollers (0.0);
        }
        else if(controlStation.getIntakeSwitch() == controlStation.RIGHT)
        {
            arm.setIntakeRollers (-1.0);
        }
    }
}
