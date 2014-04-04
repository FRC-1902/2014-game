/*
 * @author Exploding Bacon
 *
 * Free to use under the terms of the FIRST Robotics License.
 */

package frc.io.util;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * @author Exploding Bacon
 */
abstract class AbsoluteEncoder implements PIDSource, LiveWindowSendable
{
    /**
     * Container for encoder result as public properties.
     */
    public static class Result
    {
        /** The raw position value */
        public int     rawValue         = 0;
        /** True if increase in magnitude */
        public boolean isIncrease       = false;
        /** True if decrease in magnitude */
        public boolean isDecrease       = false;
        /** True if any error has occurred */
        public boolean isError          = false;
    }

    private int     m_maxCount;     // max possible raw value
    private int     m_offsetCount;  // min expected raw value
    private int     m_fullCount;    // max expected raw value
    private boolean m_negate = false;
    private double  m_range;        // full scale - offset for calc.
    private double  m_scaledMin;
    private double  m_scaledMax;
    private double  m_scaledRange;
    private Result  m_result = new Result();
    private ITable  m_table;
    private String  m_units = "Scaled";

    /**
     * Constructor.
     * @param maxCount      Counts per revolution for the encoder.
     */
    public AbsoluteEncoder(int maxCount)
    {
        initialize(maxCount, 0.0, 1.0);
    }

    /**
     * Constructor.
     * @param maxCount      Counts per revolution for the encoder.
     * @param minScale      Scaled value represented by the minimum count.
     * @param maxScale      Scaled value represented by the maximum count.
     */
    public AbsoluteEncoder(int maxCount, double minScale, double maxScale)
    {
        initialize(maxCount, minScale, maxScale);
    }

    /**
     * Initializes the encoder.
     * @param maxCount      Counts per revolution for the encoder.
     * @param minScale      Scaled value represented by the minimum count.
     * @param maxScale      Scaled value represented by the maximum count.
     */
    private void initialize(int maxCount, double minScale, double maxScale)
    {
        if (maxCount <= 0)
        {
            throw new IllegalArgumentException("Absolute encoder max count must be > 0.");
        }
        m_maxCount = maxCount;
        setOffsetCount(0);
        setFullCount(maxCount);
        setRange(minScale, maxScale);
    }

    /**
     * To be implemented by the extending class which implements a device-
     * specific driver. Returns the current absolute encoder position result.
     * @return Current absolute encoder result.
     */
    abstract protected Result readPosition();

    /**
     * To be implemented by the extending class which implements a device-
     * specific driver. Returns the current absolute encoder position result.
     * @return Current absolute encoder result.
     */
    public Result getPosition()
    {
        Result result = readPosition();
        if (m_negate)
        {
             result.rawValue = m_maxCount - result.rawValue;
        }
        return result;
    }

    /**
     * Implements PIDSource.pidGet(). Returns the value for PIDController use.
     * Reads the current raw value and returns a scaled value.
     * @return Current scaled value.
     */
    public double pidGet()
    {
        return getScaled();
    }

    /**
     * Reads the current value and returns a scaled value calculated by 
     * using the ratio of the raw value vs. the offset and full scale applied
     * against the range.
     * @return Current scaled value.
     */
    public double getScaled()
    {
        return scaleValue(getRaw());
    }

    /**
     * Reads the current value and returns the current absolute raw count.
     * @return Current absolute count from the encoder.
     */
    public int getRaw()
    {
        m_result = getPosition();
        return m_result.rawValue;
    }

    /**
     * Returns the last position result following a call to getPosition().
     * @return Latest position result.
     */
    public Result getLastResult()
    {
        return m_result;
    }

    /**
     * Using the last position, checks if the encoder value is increasing.
     * @return True if it is increasing; false if decreasing or steady.
     */
    public boolean isIncrease()
    {
        return m_result.isIncrease;
    }
    
    /**
     * Using the last position, checks if the encoder value is decreasing.
     * @return True if it is decreasing; false if increasing or steady.
     */
    public boolean isDecrease()
    {
        return m_result.isDecrease;
    }
    
    /**
     * Checks if the last reading produced an error.
     * @return True if an error occurred.
     */
    public boolean isError()
    {
        return m_result.isError;
    }
    
    /**
     * Sets the negate flag to reverse the count so that 0 is full count and
     * vice versa.
     * @param negate    True to reverse the raw value sense.
     */
    public void setNegate(boolean negate)
    {
        m_negate = negate;
    }
    
    /**
     * Gets the negate setting.
     * @return Current negate setting. 
     */
    public boolean getNegate()
    {
        return m_negate;
    }
    
    /**
     * Sets the minimum count expected. This defaults to zero, but due to
     * mechanical limits the encoder may not reach the zero value. This value
     * will be subtracted from the raw encoder value so that when this value is
     * read from the encoder the corresponding scaled value will be at minimum.
     * @param value     The minimum (offset) count.
     */
    public void setOffsetCount(int value)
    {
        m_offsetCount = value;
        m_range = (double)(m_fullCount - m_offsetCount);
    }
    
    /**
     * Gets the current offset value that will be applied in calculations.
     * @return The offset in encoder counts.
     */
    public int getOffsetCount()
    {
        return m_offsetCount;
    }

    /**
     * Calibrates the offset count. Call when position is at the minimum limit.
     */
    public void calibrateOffsetCount()
    {
        setOffsetCount(getRaw());
    }
    
    /**
     * Sets the maximum count expected. Defaults to encoder max code, but due to
     * mechanical limits the encoder may not reach the full value. This value
     * will used to scale the raw encoder value so that when this value is
     * read from the encoder the corresponding scaled value will be at maximum.
     * @param value     The maximum (full scale) count.
     */
    public void setFullCount(int value)
    {
        m_fullCount = value;
        m_range = (double)(m_fullCount - m_offsetCount);
    }

    /**
     * Gets the current full scale value that will be applied in calculations.
     * @return The full scale in encoder counts.
     */
    public int getFullCount()
    {
        return m_fullCount;
    }

    /**
     * Calibrates the full count. Call when position is at the maximum limit.
     */
    public void calibrateFullCount()
    {
        setFullCount(getRaw());
    }
    
    /**
     * Returns a scaled value calculated by using the ratio of the raw value vs.
     * the offset and full scale applied against the scaled range.
     * @param rawValue      The raw encoder value
     * @return The scaled value
     */
    public double scaleValue(int rawValue)
    {
        double value = (double)(rawValue - m_offsetCount) / m_range * m_scaledRange + m_scaledMin;
        value = Math.min(value, m_scaledMax);
        value = Math.max(value, m_scaledMin);
        return value;
    }

    /**
     * Sets the scaled range for calculating a scaled value based on the raw
     * value.
     * @param min   The minimum value of the scaled range.
     * @param max   The maximum value of the scaled range.
     */
    public final void setRange(double min, double max)
    {
        m_scaledMin = min;
        m_scaledMax = max;
        m_scaledRange = max - min;
    }

    /**
     * Gets the maximum count (counts per revolution) of the encoder.
     * @return The maximum count.
     */
    public int getMaxCount()
    {
        return m_maxCount;
    }

    /**
     * Sets the scaled units text for display.
     * @param units     The units string for scaled values.
     */
    public void setScaledUnits(String units)
    {
        m_units = units;
    }

    /*
     * Live Window code, only does anything if live window is activated.
     */
    public String getSmartDashboardType()
    {
        return "Absolute Encoder";
    }
    
    /**
     * {@inheritDoc}
     */
    public void initTable(ITable subtable)
    {
        m_table = subtable;
        updateTable();
    }
    
    /**
     * {@inheritDoc}
     */
    public ITable getTable()
    {
        return m_table;
    }
    
    /**
     * {@inheritDoc}
     */
    public void updateTable()
    {
        if (m_table != null)
        {
            int rawValue = getRaw();
            m_table.putNumber("Count", rawValue);
            m_table.putNumber(m_units, scaleValue(rawValue));
        }
    }

    /**
     * {@inheritDoc}
     */
    public void startLiveWindowMode() {}
    
    /**
     * {@inheritDoc}
     */
    public void stopLiveWindowMode() {}
}

