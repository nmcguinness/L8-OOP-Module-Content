package t03_ordering.demos.de01;

import java.util.Comparator;

public class PortComparator implements Comparator<NetworkConnection> {

    private int sortDirection = 1;

    public PortComparator(int sortDirection)
    {
        this.sortDirection = sortDirection;
    }

    @Override
    public int compare(NetworkConnection o1, NetworkConnection o2) {
        return 0;
    }
}
