package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.DriverStationLCD;



public class UserMessages {
    StringBuffer errorMessage = new StringBuffer("");
    StringBuffer armMessage   = new StringBuffer("");
    StringBuffer driveMessage = new StringBuffer("");
    String logMessageLine1    = "";
    String logMessageLine2    = "";
    String logMessageLine3    = "";
    
    DriverStationLCD messageScreen = DriverStationLCD.getInstance();
    
    public void setErrorMessage(String message)
    {
        errorMessage = new StringBuffer("ERROR: ").append(message);
    }
    
    public void clearError()
    {
        errorMessage = new StringBuffer("Everything is OK");
    }
    
    public void setArmMessage(int armAngle, boolean charged, boolean ball)
    {
        armMessage = new StringBuffer("Arm: ").append(armAngle);
        
        if(charged)
        {
            armMessage.append(" Charged");
            if(ball)
            {
                armMessage.append(" Ball");
            }
        }
    }
    
    public void setDriveMessage(double driveDistance, boolean shift)
    {
        driveMessage = new StringBuffer("Drv: ").append(driveDistance).append("ft Shift: ");
        
        if(!shift)
        {
            driveMessage.append("High");
        }
        else
        {
            driveMessage.append("Low");
        }
    }
    
    public void setLogMessage(String message)
    {
        logMessageLine1 = logMessageLine2;
        logMessageLine2 = logMessageLine3;
        logMessageLine3 = message;
    }
    
    public void update()
    {
        messageScreen.println(DriverStationLCD.Line.kUser1, 1, errorMessage);
        messageScreen.println(DriverStationLCD.Line.kUser2, 1, armMessage);
        messageScreen.println(DriverStationLCD.Line.kUser3, 1, driveMessage);
        messageScreen.println(DriverStationLCD.Line.kUser4, 1, logMessageLine1);
        messageScreen.println(DriverStationLCD.Line.kUser5, 1, logMessageLine2);
        messageScreen.println(DriverStationLCD.Line.kUser6, 1, logMessageLine3);
        messageScreen.updateLCD();
    }
}
