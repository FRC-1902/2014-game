/*
 * Driver for Bourns EMS22 absolute encoder.
 * http://www.bourns.com/pdfs/EMS22A.pdf
 */

package frc.io.util;

import edu.wpi.first.wpilibj.SPIDevice;

/** Bourns EMS22 Absolute Encoder
* @author Exploding Bacon
*
* Synchronous serial interface with active high clock (SCLK), active
* low chip select (CS), 16-bit frames. No data input. 1MHz transfer rate.
* 
* Pin-out (numbered left to right when viewed from back)
* ------------------------------------------------------
* Pin 1 DI  Digital Input
* Pin 2 CLK Clock
* Pin 3 GND Ground
* Pin 4 DO  Digital Output
* Pin 5 VCC Power (+5V or +3.3V depending on variant)
* Pin 6 CS  Chip Select
*
* Data Output Details
* -------------------
* D9-D0 - Absolute angular position data (0-1023 or 0x0-0x3ff)
* OCF   - End of offset compensation algorithm
* COF   - Cordic overflow indicating an error in cordic part
* LIN   - Linearity alarm
* INC   - Increase in magnitude (CW for A, CCW for B)
* DEC   - Decrease in magnitude (CCW for A, CW for B)
* PAR   - Even parity bit
*
* Data Frame
* ----------
* bit:   15 14 13 12 11 10  9  8  7  6   5   4   3   2   1   0
*        -----------------------------------------------------
* data: |D9|D8|D7|D6|D5|D4|D3|D2|D1|D0|OCF|COF|LIN|INC|DEC|PAR|
*        -----------------------------------------------------
*/
public class EMS22 extends AbsoluteEncoder
{
    /**
     * Container for encoder result as public properties. This extends the
     * AbsoluteEncoder.Result class, adding device specific properties.
     */
    public static class DeviceResult extends AbsoluteEncoder.Result
    {
        /** End of offset compensation algorithm flag */
        public boolean isEndOfOffset    = false;
        /** Cordic overflow indicating an error in cordic part */
        public boolean isCordicOverflow = false;
        /** Linearity alarm */
        public boolean isLinearityAlarm = false;
        /** True if a parity error has occurred */
        public boolean isParityError    = false;
    }

    private static final int    MAX_COUNT = 1023;
    private static final long   SPI_OUT_DONT_CARE = 0; // encoder input only for daisy chain
    private static final int    SPI_FRAME_BITS  = 16;
    private static final int    NUM_STATUS_BITS = 6;
    private static final double CLOCK_RATE_HZ = 20000; // 20KHz
    // Bitmasks for extracting fields from SPI frame
    private static final long   BITMASK_OCF  = 0x20;
    private static final long   BITMASK_COF  = 0x10;
    private static final long   BITMASK_LIN  = 0x08;
    private static final long   BITMASK_INC  = 0x04;
    private static final long   BITMASK_DEC  = 0x02;

    private final SPIDevice m_spi;
    private int m_lastGoodValue = 0;
    
   /**
     * Constructor.
     * @param slot      Slot number for IO (e.g. CRio slot for Digital Sidecar)
     * @param chSCLK    I/O channel for SCLK SPI signal
     * @param chMOSI    I/O channel for MOSI SPI signal
     * @param chMISO    I/O channel for MISO SPI signal
     * @param chCS      I/O channel for CS SPI signal
     */
//    public EMS22(int slot, int chSCLK, int chMOSI, int chMISO, int chCS)
//    {
//        // Initialize AbsoluteEncoder base.
//        super(MAX_COUNT);
//        m_spi = createSPI(slot, chSCLK, chMOSI, chMISO, chCS);
//    }
    
    /**
     * Constructor.
     * @param slot      Slot number for IO (e.g. CRio slot for Digital Sidecar)
     * @param chSCLK    I/O channel for SCLK SPI signal
     * @param chMOSI    I/O channel for MOSI SPI signal
     * @param chMISO    I/O channel for MISO SPI signal
     * @param chCS      I/O channel for CS SPI signal
     * @param minRange  Scaled value represented by the minimum count.
     * @param maxRange  Scaled value represented by the maximum count.
     */
    public EMS22(int slot, int chSCLK, int chMOSI, int chMISO, int chCS, double minRange, double maxRange)
    {
        // Initialize AbsoluteEncoder base.
        super(MAX_COUNT, minRange, maxRange);
        m_spi = createSPI(slot, chSCLK, chMOSI, chMISO, chCS);
    }
    
   /**
     * Creates and initializes the SPI driver.
     * @param slot      Slot number for IO (e.g. CRio slot for Digital Sidecar)
     * @param chSCLK    I/O channel for SCLK SPI signal
     * @param chMOSI    I/O channel for MOSI SPI signal
     * @param chMISO    I/O channel for MISO SPI signal
     * @param chCS      I/O channel for CS SPI signal
     */
    private SPIDevice createSPI(int slot, int chSCLK, int chMOSI, int chMISO, int chCS)
    {
        SPIDevice spi = new SPIDevice(slot, chSCLK, chMOSI, chMISO, chCS, SPIDevice.CS_ACTIVE_LOW);
        spi.setBitOrder(SPIDevice.BIT_ORDER_MSB_FIRST);
        spi.setClockPolarity(SPIDevice.CLOCK_POLARITY_ACTIVE_HIGH);
        spi.setClockRate(CLOCK_RATE_HZ);
        spi.setDataOnTrailing(SPIDevice.DATA_ON_TRAILING_EDGE);
        spi.setFrameMode(SPIDevice.FRAME_MODE_CHIP_SELECT);
        return spi;
    }
    
    /**
     * To be implemented by extending class. Returns the current encoder result.
     * @return Current absolute encoder result.
     */
    protected AbsoluteEncoder.Result readPosition()
    {
        return (AbsoluteEncoder.Result)readDevicePosition();
    }

    /**
     * Read the encoder to get the current position and other information. This
     * result is specific to the device.
     * @return The result as an object with properties.
     */
    public DeviceResult readDevicePosition()
    {
        DeviceResult result = new DeviceResult();
        long data = m_spi.transfer(SPI_OUT_DONT_CARE, SPI_FRAME_BITS);
        System.out.println("EMS22: " + data);
        result.isEndOfOffset    = (data & BITMASK_OCF) != 0;
        result.isCordicOverflow = (data & BITMASK_COF) != 0;
        result.isLinearityAlarm = (data & BITMASK_LIN) != 0;
        result.isIncrease       = (data & BITMASK_INC) != 0;
        result.isDecrease       = (data & BITMASK_DEC) != 0;
        result.isParityError    = !checkParity(data);
        result.isError = result.isCordicOverflow ||
                         result.isLinearityAlarm ||
                         result.isParityError;
        result.rawValue = (int)(data >>> NUM_STATUS_BITS); // unsigned shift
        if (!result.isError)
        {
            m_lastGoodValue = result.rawValue;
        }
        else if (result.isParityError)
        {
            result.rawValue = m_lastGoodValue;
        }
        return result;
    }
    
    /**
     * Checks the parity of the data frame.
     * @param data  The data frame to be checked
     * @return True if the parity is even.
     */
    private boolean checkParity(long data)
    {
        boolean isEven = true;
        // Note unsigned shift
        for (int i = 0; i < SPI_FRAME_BITS; ++i, data = data >>> 1)
        {
            if ((data & 0x1) != 0)
            {
                isEven = !isEven;
            }
        }
        return isEven;
    }
}
