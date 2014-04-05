

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Watchdog;

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
    boolean hasFired = false;
    long fireStartTimeMS = 0;
    int rechargeTime = 1000;
    int doubleRunCheck = 1;
    
    //User Message Variables
    boolean shiftMessage = false;
    boolean chargedMessage = false;
    boolean ballMessage = false;
    boolean isCharging = false;
    
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
        drivetrain.startDrivetrainEncoder(true);
        drivetrain.resetDrivetrainEncoder();
        Watchdog.getInstance().setEnabled(true);
        drivetrain.compressor.start();
        System.out.println("Made it into teleop init " + doubleRunCheck);
        doubleRunCheck++;
        arm.setHoldPosition(Arm.startPreset);
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
        double joystickPosition = controlStation.getArmJoystick();
        arm.setArmMotors(joystickPosition);
        
//        if(joystickPosition < 0.0 && )
//        if(joystickPosition > 0.1 && joystickPosition < -0.1)
//        {
//            arm.updateArmPosition();
//        }
//        else
//        {
//            arm.holdArmPosition();
//        }
        
        //System.out.println("Arm Position: " + arm.getArmPosition());
        
        //Move arm with presets
        //These numbers must be tested
//        if(controlStation.getFloorPreset() == ControlStation.PRESSED)
//        {
//            arm.moveArmTo(Arm.floorPreset);
//        }
        if(controlStation.getScorePreset() == ControlStation.PRESSED)
        {
            arm.setHoldPosition(Arm.scorePreset);
            
        }
        if(controlStation.getTrussPreset() == ControlStation.PRESSED)
        {
            arm.setHoldPosition(Arm.trussPreset);
        }
        if(controlStation.getHotKeyPreset() == ControlStation.PRESSED)
        {
            arm.setHoldPosition(arm.hotKeyPreset);
        }
        
        if(controlStation.setHotKeyPreset() == ControlStation.PRESSED)
        {
            arm.setHotKeyPreset();
        }
        
        //Fire!!
        if(!hasFired)
        {
            if(controlStation.getFireButton() == ControlStation.PRESSED)
            {
//                hasFired = true;
                //fireStartTimeMS = System.currentTimeMillis();
                shooter.shoot();
            }
        }
        else
        {
//            if(controlStation.getFireButton() == ControlStation.NOT_PRESSED)
//            {
//                long elapsedTime = System.currentTimeMillis() - fireStartTimeMS;
//                if(elapsedTime > rechargeTime)
//                {
//                    shooter.setFireSolenoid(false);
//                    hasFired = false;
//                    
//                }
//            }
        }
        //System.out.println("Touch Sensor: " + shooter.winchTouchSensor.get());
        
        //Move the claw
        //Open
        if(controlStation.getGripperSwitch() == ControlStation.UP)
        {
            arm.setGripperSolenoid(true);
        }
        else
        {
            arm.setGripperSolenoid(false);
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
        
        if(controlStation.getWinchButton() == ControlStation.PRESSED)
        {
            if(!isCharging)
            {
                isCharging = true;
                shooter.startChargeShooter();
            }
        }
        else
        {         
            isCharging = false;
            shooter.stopChargeShooter();
        }
        
        chargedMessage = shooter.getWinchTouchSensor();
        ballMessage = arm.getGripperTouchSensor();
        userMessages.setArmMessage(arm.getArmPosition(), false, false);
//        System.out.println("Arm Position: " + arm.getArmPosition());
        
        if(controlStation.enablePID() == ControlStation.PRESSED)
        {
            arm.enableAngleControl();
        }
        
        else if(controlStation.disablePID() == ControlStation.PRESSED)
        {
            arm.disableAngleControl();
        }
        
        shooter.process();
        
        //userMessages.setDriveMessage(drivetrain.getDistance(), shiftMessage);
        //userMessages.setArmMessage(arm.getArmEncoder(), chargedMessage, ballMessage);
        //userMessages.update();
    }
}
