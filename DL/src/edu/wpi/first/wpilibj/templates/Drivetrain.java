package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.*;

public class Drivetrain
{    
    Talon talonL1;
    Talon talonL2;
    Talon talonR1;
    Talon talonR2;
    Solenoid solenoidShift;
    Encoder encoderL;
    Encoder encoderR;
    Compressor compressor;
    
    public Drivetrain(int tL1, int tL2, int tR1, int tR2, int encL1, int encL2, int encR1, int encR2, int solShift1, int compSpike, int compSwitch)
    {
        talonL1 = new Talon(tL1);
        talonL2 = new Talon(tL2);
        talonR1 = new Talon(tR1);
        talonR2 = new Talon(tR2);
        
        encoderL = new Encoder(encL1, encL2);
        encoderR = new Encoder(encR1, encR2);
        
        solenoidShift = new Solenoid(solShift1);
        compressor = new Compressor(compSwitch, compSpike);
    }
    
    public void setPower(double valL, double valR) 
    {
        talonL1.set(-valL);
        talonL2.set(-valL);
        talonR1.set(valR);
        talonR2.set(valR);
    }

    public double getDistance()
    {
        return ((getEncL() * 1) +  (-getEncR() * 1)) / 2;
        // switch 1's to conversion factors after testing
    }

    public void resetDrivetrainEncoder()
    {
        encoderR.reset();
        encoderL.reset();
    }

    public void startDrivetrainEncoder(boolean setEnc)
    {
        if (setEnc)
        {
            encoderR.start();
            encoderL.start();           
        }
        else if (!setEnc)
        {
            encoderR.stop();
            encoderL.stop();  
        }
    }

    public int getEncR()
    {
        return encoderR.get();
    }

    public int getEncL()
    {
        return encoderL.get();
    }

    public void shift(boolean shift)
    {
        solenoidShift.set(shift);
    }
    
    public void startCompressor()
    {
        compressor.start();
    }
    
    //TODO: This must be tested and tuned
    public void moveTo(int targetPosition)
    {
        double p = 0.05;
        double error = targetPosition - getDistance();
        
        error *= p;
        
        setPower(error, error);
    }
}

            
            
            