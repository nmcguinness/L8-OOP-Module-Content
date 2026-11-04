package t11_generics_1.demos.de01;

class BoxObject {
    private final Object value;

    public BoxObject(Object value) {
        this.value = value;
    }

    public Object value() {
        return value;
    }
}