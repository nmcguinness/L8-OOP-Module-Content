package t11_generics_1.exercises.ex07;

public class Exercise {

    public static void run() {
        MessageBus<String> bus = new MessageBus<>();
        bus.enqueue("PlayerJumped");
        bus.enqueue("CoinCollected");
        bus.enqueue("LevelComplete");
        // bus.enqueue(123);  <- does not compile: int cannot be converted to String

        System.out.println("Size: " + bus.size());
        String message = bus.dequeue();
        while (message != null) {
            System.out.println("Handled: " + message);
            message = bus.dequeue();
        }
        System.out.println("Empty: " + bus.isEmpty());
    }
}
