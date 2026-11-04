package t11_generics_1.exercises.ex02;

public class Exercise {

    public static void run() {
        UndoStack<String> actions = new UndoStack<>();
        actions.push("MoveLeft");
        actions.push("Jump");
        actions.push("UsePotion");

        System.out.println("Size: " + actions.size());
        System.out.println("Pop: " + actions.pop());
        System.out.println("Peek: " + actions.peek());
        actions.pop();
        actions.pop();
        System.out.println("Pop when empty: " + actions.pop());

        UndoStack<Integer> scores = new UndoStack<>();
        scores.push(10);
        scores.push(20);
        System.out.println("Integer pop: " + scores.pop());
    }
}
