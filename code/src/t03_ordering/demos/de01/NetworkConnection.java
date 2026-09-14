package t03_ordering.demos.de01;

public class NetworkConnection
{
    private ProtocolType protocolType;                      //value type
    private TransportProtocolType transportProtocolType;    //value type  e.g. 1 for UDP
    private float pingMS;                                   //value type e.g. 34.5
    private IPAddress ipAddress;                            //reference type

    public NetworkConnection(ProtocolType protocolType, IPAddress ipAddress,
                             TransportProtocolType transportProtocolType) {
        this.protocolType = protocolType;
        this.ipAddress = ipAddress;
        this.transportProtocolType = transportProtocolType;

        //TransportProtocolType.TCP => int => 4 bytes
        //"TCP"                     => string => "TCP/0" => ASCII TABLE ==> 5 bytes
    }

    @Override
    public String toString() {
        return "NetworkConnection{" +
                "protocolType=" + protocolType +
                ", transportProtocolType=" + transportProtocolType +
                ", pingMS=" + pingMS +
                ", ipAddress=" + ipAddress +
                '}';
    }


}
