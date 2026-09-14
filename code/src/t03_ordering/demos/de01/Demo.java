package t03_ordering.demos.de01;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Demo {

    public static void run()
    {
        NetworkConnection nc1 = new NetworkConnection(
                ProtocolType.HTTP,
                new IPAddress("192.1.2.3"),
                TransportProtocolType.TCP);

        System.out.println(nc1);

        ArrayList<IPAddress> ipList = new ArrayList<>();
        ipList.add(new IPAddress("192.1.2.3"));
        ipList.add(new IPAddress("168.1.2.3"));
        ipList.add(new IPAddress("192.1.2.3"));
        ipList.add(new IPAddress("192.10.1.3"));
        ipList.add(new IPAddress("192.10.5.3"));
        ipList.add(new IPAddress("168.1.2.3"));

        //version 1
        Comparator<IPAddress> compIP1 = new Comparator<IPAddress>() {
            public int compare(IPAddress o1, IPAddress o2) {
                return o1.getIpA() - o2.getIpA();
            }
        };
        ipList.sort(compIP1);

        //version 2 - lambda expression form
        ipList.sort((IPAddress a, IPAddress b) -> a.getIpA() - b.getIpA());
        System.out.println(ipList);

        //version 3 - anonymous function
        ipList.sort(new Comparator<IPAddress>() {
            @Override
            public int compare(IPAddress o1, IPAddress o2) {
                return o1.getIpA() - o2.getIpB();
            }
        });

        //version 4 - user-defined class-level method
        ipList.sort(Demo::mySorter);
        System.out.println(ipList);

        //version 5
        ipList.sort( Comparator.comparing(IPAddress::getIpA)
                .thenComparing(IPAddress::getIpB)
                .thenComparing(IPAddress::getIpC)
                .thenComparing(IPAddress::getIpD));

        //using lambdas for a better print (See topic 3 notes)
        ipList.forEach((IPAddress ip) -> System.out.println(ip));
    }

    public static int mySorter(IPAddress a, IPAddress b)
    {
        return a.getIpA() - b.getIpA();
    }
}
