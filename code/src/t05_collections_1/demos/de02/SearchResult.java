package t05_collections_1.demos.de02;

public class SearchResult {
    private int index;
    private String entryA;
    private String entryB;

    public int getIndex() {
        return index;
    }
    public String getA() {
        return entryA;
    }
    public String getB() {
        return entryB;
    }
    public String toString() {return entryA + ", " + entryB;}
    public SearchResult(int index, String entryA, String entryB) {
        this.index = index;
        this.entryA = entryA;
        this.entryB = entryB;
    }


}
