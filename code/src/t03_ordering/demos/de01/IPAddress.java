package t03_ordering.demos.de01;

public class IPAddress
{
    private static final String DELIMITER = "."; //static == shared
    private int ipA;
    private int ipB;

    public int getIpD() {
        return ipD;
    }

    public int getIpC() {
        return ipC;
    }

    public int getIpB() {
        return ipB;
    }

    public int getIpA() {
        return ipA;
    }

    private int ipC;
    private int ipD;

    public IPAddress(String strIPAddress)
    {
        strIPAddress.trim(); //white space
        String[] parts = strIPAddress.split("\\.");
        initializeParts(parts);
    }

    @Override
    public String toString()
    {
        return "IPAddress[" + ipA + "."
                            + ipB + "."
                            + ipC + "."
                            + ipD + "]";
    }

    private void initializeParts(String[] parts)
    {
        if(parts == null || parts.length != 4)
            return;

        this.ipA = clampIP(Integer.parseInt(parts[0]));
        this.ipB = clampIP(Integer.parseInt(parts[1]));
        this.ipC = clampIP(Integer.parseInt(parts[2]));
        this.ipD = clampIP(Integer.parseInt(parts[3]));
    }

    public int clampIP(int value)
    {
        /*
        boolean canEnter = age > 21 ? true : false;
        String name = "jack";
        String str = name.length() != 0 ? "valid" : "invalid";
        ternary (boolean expression) ? <true value> : <false value>
         */
        return (value >= 0 && value <= 255) ? value : 0;

        /*
        * Unary operators:
        *  ++, --
        *
        * Binary operators:
        *   * / + - %
        *   3 + 2
        *   3, 2 are operands
        *   + is the operator
        *
        * Ternary operators:
        *   (boolean exp) ? <true> : <false>
        * */
    }

    //toString
    //getters (immutable)
}
