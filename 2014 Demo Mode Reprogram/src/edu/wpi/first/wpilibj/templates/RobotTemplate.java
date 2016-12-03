/*
    ____                          _    __     ___      
   / __ )____  _  _____  _____   | |  / /    |__ \     
  / __  / __ \| |/_/ _ \/ ___/   | | / /     __/ /     
 / /_/ / /_/ />  </  __/ /       | |/ /     / __/      
/_____/\____/_/|_|\___/_/        |___(_)   /____/      
                                                           
                                                        
Written by Veron A. and Ryan Shavell.

This program is a complete code rewrite for team 1902's 2014 "Aerial Assist" robot.

Credit to Tyler Felsted, Phillip Parker, Bryce Dere, Anderson Perkins, Garry Little,
and Sebastian Hedge for writing the original code which this code referenced.

*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class RobotTemplate extends IterativeRobot {
    Joystick joystick;
    Button wheelBButton, wheelFButton, intakeButton, shiftButton;
    
    Talon fRight, bRight, fLeft, bLeft, arm, intake;
    Compressor compressor = new Compressor(9, 1);
    Solenoid solenoid, shift;
    
    Autonomous auto;
    
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        joystick = new Joystick(1);
        
        wheelFButton = new JoystickButton(joystick, 8);
        wheelBButton = new JoystickButton(joystick, 7);
        intakeButton = new JoystickButton(joystick, 2);
        shiftButton = new JoystickButton(joystick, 3);
        
        fRight = new Talon(Map.RIGHT_MOTOR_1);
        bRight = new Talon(Map.RIGHT_MOTOR_2);
        fLeft = new Talon(Map.LEFT_MOTOR_1);
        bLeft = new Talon(Map.LEFT_MOTOR_2);
        arm = new Talon(Map.ARM);
        intake = new Talon(Map.INTAKE);
        
        solenoid = new Solenoid(Map.INTAKE_ARM);
        shift = new Solenoid(Map.SHIFT);
        
        auto = new Autonomous();
        
        compressor.start();
        
        System.out.println("Boxer initialized!");
    }

    public void autonomousInit() {
        auto.start();
    }
    
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        getWatchdog().feed();
    }

    /**
     * This function is called periodically during operator control.
     */
    public void teleopPeriodic() {
        double x, y;
        boolean armStopped = false;
        
        if(Math.abs(joystick.getY()) > 0.1){
            x = joystick.getY();
        } else x = 0;
        
        if(Math.abs(joystick.getX()) > 0.1){
            y = joystick.getX();
        } else y = 0;
        
        //TODO: left/right is reversed
        setPower(-(y - x)/2, fLeft, bLeft);
        setPower(-(y + x)/2, fRight, bRight);
        
        double armSpeed = joystick.getRawAxis(4);
        
        
        if(!armStopped && Math.abs(armSpeed) > 0.1){
            arm.set(armSpeed);
        } else {
            arm.set(0);
        }
        
        //TODO: Make intake a toggle
        if(intakeButton.get())
        {
            solenoid.set(true);
        } else solenoid.set(false);
        
        if(wheelFButton.get()) {
            intake.set(1);
        } else if(wheelBButton.get()){
            intake.set(-1);
        } else intake.set(0);
        
        if(shiftButton.get()){
            shift.set(true);
        } else shift.set(false);
        
        getWatchdog().feed();
    }
    
    /**
     * This function is called periodically during test mode.
     */
    public void testPeriodic() {
        getWatchdog().feed();
    }
    
    public void setPower(double power, Talon right, Talon left){
        right.set(power);
        left.set(power);
    }
}