import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        int selection = -1;

        while (selection != 0) {
            System.out.println("\n=====================================================");
            System.out.println("         OOP Module - Topic Exercise Runner");
            System.out.println("=====================================================");
            System.out.println("  0.  Exit");
            System.out.println("  1.  t01 - Arrays");
            System.out.println("  2.  t03 - Ordering");
            System.out.println("  3.  t04 - Equality & Hashing");
            System.out.println("  4.  t05 - Collections I (ArrayList)");
            System.out.println("  5.  t06 - Collections II (LinkedList)");
            System.out.println("  6.  t08 - Inheritance");
            System.out.println("  7.  t09 - Interfaces");
            System.out.println("  8.  t11 - Generics I");
            System.out.println("  9.  t12 - Generics II");
            System.out.println(" 10.  t13 - Design Patterns I");
            System.out.println(" 11.  t14 - Design Patterns II");
            System.out.println(" 12.  t15 - DB Connectivity / DAO");
            System.out.println(" 13.  t16 - Functional Interfaces");
            System.out.println(" 14.  t19 - Concurrency I");
            System.out.println(" 15.  t24 - Pathfinding (BFS, Dijkstra, A*)");
            System.out.println("=====================================================");
            System.out.println("  Menu numbers are just list positions; tNN is the");
            System.out.println("  topic code used in notes/topics and code/src.");
            System.out.println("  t20 (JSON) and t22 (Networking) are client-server");
            System.out.println("  topics: run their server and client classes by hand.");
            System.out.println("=====================================================");
            System.out.print("Select topic (0 to exit): ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Enter a number between 0 and 14.");
                scanner.next();
                continue;
            }

            selection = scanner.nextInt();

            switch (selection) {
                case 0:
                    System.out.println("Exiting.");
                    break;

                case 1:
                    System.out.println("\n******************** Topic:t01 - Arrays ********************\n");
                    run_t01_Arrays();
                    break;

                case 2:
                    System.out.println("\n******************** Topic:t03 - Ordering ********************\n");
                    run_t03_Ordering();
                    break;

                case 3:
                    System.out.println("\n******************** Topic:t05 - Collections I (ArrayList) ********************\n");
                    run_t05_Collections1();
                    break;

                case 4:
                    System.out.println("\n******************** Topic:t06 - Collections II (LinkedList) ********************\n");
                    run_t06_Collections2();
                    break;

                case 5:
                    System.out.println("\n******************** Topic:t04 - Equality & Hashing ********************\n");
                    run_t04_EqualityHashing();
                    break;

                case 6:
                    System.out.println("\n******************** Topic:t08 - Inheritance ********************\n");
                    run_t08_Inheritance();
                    break;

                case 7:
                    System.out.println("\n******************** Topic:t09 - Interfaces ********************\n");
                    run_t09_Interface();
                    break;

                case 8:
                    System.out.println("\n******************** Topic:t11 - Generics I ********************\n");
                    run_t11_Generics1();
                    break;

                case 9:
                    System.out.println("\n******************** Topic:t12 - Generics II ********************\n");
                    run_t12_Generics2();
                    break;

                case 10:
                    System.out.println("\n******************** Topic:t13 - Design Patterns I ********************\n");
                    run_t13_DesignPatterns1();
                    break;

                case 11:
                    System.out.println("\n******************** Topic:t14 - Design Patterns II ********************\n");
                    run_t14_DesignPatterns2();
                    break;

                case 12:
                    System.out.println("\n******************** Topic:t15 - DB Connectivity / DAO ********************\n");
                    System.out.println("NOTE: These exercises require a MySQL database.");
                    System.out.println("      Before running, create the 'car_rental' database in phpMyAdmin");
                    System.out.println("      and ensure the credentials in each Exercise.java match your setup.");
                    System.out.println();
                    run_t15_Dao();
                    break;

                case 13:
                    System.out.println("\n******************** Topic:t16 - Functional Interfaces ********************\n");
                    run_t16_FunctionalInterfaces();
                    break;

                case 14:
                    System.out.println("\n******************** Topic:t19 - Concurrency I ********************\n");
                    run_t19_Concurrency();
                    break;

                case 15:
                    System.out.println("\n******************** Topic:t24 - Pathfinding ********************\n");
                    run_t24_Pathfinding();
                    break;

                default:
                    System.out.println("Invalid selection. Enter a number between 0 and 15.");
                    break;
            }
        }

        scanner.close();
    }

    public void run_t24_Pathfinding() {
        System.out.println("\n********** Demo 01 - watch A* search a grid **********\n");
        t24_pathfinding.demos.de01.Demo.run();

        System.out.println("\n********** Demo 02 - compare BFS, Dijkstra and A* **********\n");
        t24_pathfinding.demos.de02.Demo.run();
    }

    public void runChallengeExercises() {
        System.out.println("Challenge Exercise t01...");
        t01_arrays.challenges.ce01.Exercise.run();
    }

    public void run_t01_Arrays() {
        System.out.println("\n********** Demo 01 **********\n");
        t01_arrays.demos.de01.Demo.run();

        System.out.println("\n********** Demo 02 **********\n");
        t01_arrays.demos.de02.Demo.run();

        System.out.println("\n********** Exercise 01 - Fill, sum, average **********\n");
        t01_arrays.exercises.ex01.Exercise.run();

        System.out.println("\n********** Exercise 02 - indexOf & count **********\n");
        t01_arrays.exercises.ex02.Exercise.run();

        System.out.println("\n********** Exercise 03 - Min/Max with guards **********\n");
        t01_arrays.exercises.ex03.Exercise.run();

        System.out.println("\n********** Exercise 04 - Grade histogram **********\n");
        t01_arrays.exercises.ex04.Exercise.run();

        System.out.println("\n********** Exercise 08 - Heatmap normalization **********\n");
        t01_arrays.exercises.ex08.Exercise.run();

        System.out.println("\n********** Exercise 09 - Tic-Tac-Toe checker **********\n");
        t01_arrays.exercises.ex09.Exercise.run();

        System.out.println("\n********** Exercise 11 - Steps data wrangling **********\n");
        t01_arrays.exercises.ex11.Exercise.run();
    }

    public void run_t03_Ordering() {
        System.out.println("\n********** Demo 01 - NetworkConnection **********\n");
        t03_ordering.demos.de01.Demo.run();

        System.out.println("\n********** Exercise 01 - Comparable: score ranking **********\n");
        t03_ordering.exercises.ex01.Exercise.run();

        System.out.println("\n********** Exercise 02 - Comparator: name A-Z **********\n");
        t03_ordering.exercises.ex02.Exercise.run();

        System.out.println("\n********** Exercise 03 - Anon comparator: price asc **********\n");
        t03_ordering.exercises.ex03.Exercise.run();

        System.out.println("\n********** Exercise 04 - Multi-key: rating desc, name **********\n");
        t03_ordering.exercises.ex04.Exercise.run();

        System.out.println("\n********** Exercise 05 - Stable sort demo **********\n");
        t03_ordering.exercises.ex05.Exercise.run();

        System.out.println("\n********** Exercise 06 - Nulls-last policy **********\n");
        t03_ordering.exercises.ex06.Exercise.run();

        System.out.println("\n********** Exercise 07 - Comparable: due date **********\n");
        t03_ordering.exercises.ex07.Exercise.run();

        System.out.println("\n********** Exercise 08 - Chain: price->rating->name **********\n");
        t03_ordering.exercises.ex08.Exercise.run();
    }

    public void run_t05_Collections1() {
        System.out.println("\n********** Demo 01 - ArrayList basics **********\n");
        t05_collections_1.demos.de01.Demo.run();

        System.out.println("\n********** Demo 02 - ArrayList basics **********\n");
        t05_collections_1.demos.de02.Demo.run();

        System.out.println("\n********** Demo 03 - ArrayList basics **********\n");
        t05_collections_1.demos.de03.Demo.run();

        System.out.println("\n********** Demo 04 - ArrayList basics **********\n");
        t05_collections_1.demos.de04.Demo.run();

        System.out.println("\n********** Exercise 01 - Basic ArrayList ops **********\n");
        t05_collections_1.exercises.ex01.Exercise.run();

        System.out.println("\n********** Exercise 02 - Convert between array & list **********\n");
        t05_collections_1.exercises.ex02.Exercise.run();

        System.out.println("\n********** Exercise 03 - Remove even numbers (iterator) **********\n");
        t05_collections_1.exercises.ex03.Exercise.run();

        System.out.println("\n********** Exercise 04 - Unique values, preserve order **********\n");
        t05_collections_1.exercises.ex04.Exercise.run();

        System.out.println("\n********** Exercise 05 - Time appends vs front inserts **********\n");
        t05_collections_1.exercises.ex05.Exercise.run();

        System.out.println("\n********** Exercise 06 - Find & update task by title **********\n");
        t05_collections_1.exercises.ex06.Exercise.run();

        System.out.println("\n********** Exercise 07 - Split into evens and odds **********\n");
        t05_collections_1.exercises.ex07.Exercise.run();

        System.out.println("\n********** Exercise 08 - Merge two sorted lists **********\n");
        t05_collections_1.exercises.ex08.Exercise.run();

        System.out.println("\n********** Exercise 09 - Group names by first letter **********\n");
        t05_collections_1.exercises.ex09.Exercise.run();

        System.out.println("\n********** Exercise 10 - Simple high-score leaderboard **********\n");
        t05_collections_1.exercises.ex10.Exercise.run();
    }

    public void run_t06_Collections2() {

        System.out.println("\n********** Demo 01 - LinkedList basics **********\n");
        t06_collections_2.demos.de01.Demo.run();

        System.out.println("\n********** Demo 02 - LinkedList basics **********\n");
        t06_collections_2.demos.de02.Demo.run();

        System.out.println("\n********** Demo 03 - LinkedList basics **********\n");
        t06_collections_2.demos.de03.Demo.run();

        System.out.println("\n********** Exercise 01 - Remove non-positive values **********\n");
        t06_collections_2.exercises.ex01.Exercise.run();

        System.out.println("\n********** Exercise 02 - Insert group separators **********\n");
        t06_collections_2.exercises.ex02.Exercise.run();

        System.out.println("\n********** Exercise 03 - Iterate forwards and backwards **********\n");
        t06_collections_2.exercises.ex03.Exercise.run();

        System.out.println("\n********** Exercise 04 - Job queue with inserted tasks **********\n");
        t06_collections_2.exercises.ex04.Exercise.run();

        System.out.println("\n********** Exercise 05 - Undo/redo using two stacks **********\n");
        t06_collections_2.exercises.ex05.Exercise.run();

        System.out.println("\n********** Exercise 06 - Event list with inline edits **********\n");
        t06_collections_2.exercises.ex06.Exercise.run();

        System.out.println("\n********** Exercise 07 - Josephus survivor index **********\n");
        t06_collections_2.exercises.ex07.Exercise.run();

        System.out.println("\n********** Exercise 08 - In-place merge of sorted lists **********\n");
        t06_collections_2.exercises.ex08.Exercise.run();

        System.out.println("\n********** Exercise 09 - Sliding window maximum **********\n");
        t06_collections_2.exercises.ex09.Exercise.run();

        System.out.println("\n********** Exercise 10 - Priority turn scheduler **********\n");
        t06_collections_2.exercises.ex10.Exercise.run();

    }

    public void run_t04_EqualityHashing() {

        System.out.println("\n********** Exercise 01 - Identity vs value equality **********\n");
        t04_equality_hashing.exercises.ex01.Exercise.run();

        System.out.println("\n********** Exercise 02 - equals/hashCode contract **********\n");
        t04_equality_hashing.exercises.ex02.Exercise.run();

        System.out.println("\n********** Exercise 03 - Hand-calculating a string hash **********\n");
        t04_equality_hashing.exercises.ex03.Exercise.run();

        System.out.println("\n********** Exercise 04 - Combining field hashes **********\n");
        t04_equality_hashing.exercises.ex04.Exercise.run();

        System.out.println("\n********** Exercise 05 - From hash value to bucket index **********\n");
        t04_equality_hashing.exercises.ex05.Exercise.run();

        System.out.println("\n********** Exercise 06 - equals/hashCode for Customer **********\n");
        t04_equality_hashing.exercises.ex06.Exercise.run();

        System.out.println("\n********** Exercise 07 - Hash collisions with sum hash **********\n");
        t04_equality_hashing.exercises.ex07.Exercise.run();

        System.out.println("\n********** Exercise 08 - Mutable keys in hashed collections **********\n");
        t04_equality_hashing.exercises.ex08.Exercise.run();
    }

    public void run_t08_Inheritance() 
    {
        System.out.println("\n********** Exercise 01 - is-a vs has-a & AdminUser **********\n");
        t08_inheritance.exercises.ex01.Exercise.run();

        System.out.println("\n********** Exercise 02 - Basic Entity / Player / Enemy hierarchy **********\n");
        t08_inheritance.exercises.ex02.Exercise.run();

        System.out.println("\n********** Exercise 03 - BossEnemy and constructor chaining **********\n");
        t08_inheritance.exercises.ex03.Exercise.run();

        System.out.println("\n********** Exercise 04 - Weapon polymorphism (Sword, Staff) **********\n");
        t08_inheritance.exercises.ex04.Exercise.run();

        System.out.println("\n********** Exercise 05 - Abstract GameEntity update/render loop **********\n");
        t08_inheritance.exercises.ex05.Exercise.run();

        System.out.println("\n********** Exercise 06 - Overriding vs overloading (Formatter) **********\n");
        t08_inheritance.exercises.ex06.Exercise.run();
    }

    private void run_t09_Interface() {
        System.out.println("\n********** Challenge Exercise 02 - Contact Consolidation Tool **********\n");
        t09_interface.challenges.ce02.Exercise.run();

        System.out.println("\n********** Exercise 01 - Basic Greeter interface with casual and formal implementations **********\n");
        t09_interface.exercises.ex01.Exercise.run();

        System.out.println("\n********** Exercise 02 - Interactable interface with doors, chests, and NPCs + interaction range **********\n");
        t09_interface.exercises.ex02.Exercise.run();

        System.out.println("\n********** (NOT IMPLEMENTED AS QUITE STRAIGHTFORWARD) Exercise 03 - Multiple interfaces (Moveable, Damageable) with Player and Enemy **********\n");

        System.out.println("\n********** (NOT IMPLEMENTED AS QUITE STRAIGHTFORWARD) Exercise 04 - Strategy pattern for enemy attacks (swap melee/ranged behaviour) **********\n");

        System.out.println("\n********** Exercise 05 - Named interface with Comparators for sorting by name and length **********\n");
        t09_interface.exercises.ex05.Exercise.run();

        System.out.println("\n********** Exercise 06 - Command interface for queued actions (execute() loop) **********\n");
        t09_interface.exercises.ex06.Exercise.run();

        System.out.println("\n********** Exercise 07 - XmlSerializable interface with PlayerProfile toXml()/fromXml **********\n");
        t09_interface.exercises.ex07.Exercise.run();

        System.out.println("\n********** Exercise 08 - TextFilter pipeline (trim, lowercase, regex highlight) **********\n");
        t09_interface.exercises.ex08.Exercise.run();

        System.out.println("\n********** Exercise 09 - TextFilter pipeline with profanity blacklist from CSV **********\n");
        t09_interface.exercises.ex09.Exercise.run();
    }

    private void run_t11_Generics1() {
        System.out.println("\n********** Demo 01 - BoxObject **********\n");
        t11_generics_1.demos.de01.Demo.run();

        System.out.println("\n********** Demo 02 - Box **********\n");
        t11_generics_1.demos.de02.Demo.run();

        System.out.println("\n********** Demo 03 - Diamond operator **********\n");
        t11_generics_1.demos.de03.Demo.run();

        System.out.println("\n********** Demo 04 - Generic methods (first) **********\n");
        t11_generics_1.demos.de04.Demo.run();

        System.out.println("\n********** Demo 05 - Generic methods (max) **********\n");
        t11_generics_1.demos.de05.Demo.run();

        System.out.println("\n********** Challenge Exercise - Frequency Counter **********\n");
        t11_generics_1.challenges.ce03.Exercise.run();
    }

    private void run_t12_Generics2(){
        System.out.println("\n********** Exercise 01 - Prove invariance (compile-time) **********\n");
        t12_generics_2.exercises.ex01.Exercise.run();

        System.out.println("\n********** Exercise 02 - printAll(List<?>) **********\n");
        t12_generics_2.exercises.ex02.Exercise.run();

        System.out.println("\n********** Exercise 03 - sumNumbers(List<? extends Number>) **********\n");
        t12_generics_2.exercises.ex03.Exercise.run();

        System.out.println("\n********** Exercise 04 - fill(List<? super T>, T, int) **********\n");
        t12_generics_2.exercises.ex04.Exercise.run();

        System.out.println("\n********** Exercise 05 - PECS copy(src extends T, dst super T) **********\n");
        t12_generics_2.exercises.ex05.Exercise.run();

        System.out.println("\n********** Exercise 06 - addAllEntities (extends -> super) **********\n");
        t12_generics_2.exercises.ex06.Exercise.run();

        System.out.println("\n********** Exercise 07 - Fix broken API signature (consumer list) **********\n");
        t12_generics_2.exercises.ex07.Exercise.run();

        System.out.println("\n********** Exercise 08 - Comparator<? super T> practice **********\n");
        t12_generics_2.exercises.ex08.Exercise.run();

        System.out.println("\n********** Exercise 09 - Wildcard capture: swapFirstTwo(List<?>) **********\n");
        t12_generics_2.exercises.ex09.Exercise.run();

        System.out.println("\n********** Exercise 10 - What can I add? What can I read? **********\n");
        t12_generics_2.exercises.ex10.Exercise.run();

        System.out.println("\n********** Exercise 11 - Optional: merge two producers into one consumer **********\n");
        t12_generics_2.exercises.ex11.Exercise.run();
    }

    private void run_t13_DesignPatterns1(){
        System.out.println("\n********** Exercise 01 - Combat AI attack behaviour (Strategy) **********\n");
        t13_design_patterns_1.exercises.ex01.Exercise.run();

        System.out.println("\n********** Exercise 02 - Pricing rules (Strategy) **********\n");
        t13_design_patterns_1.exercises.ex02.Exercise.run();

        System.out.println("\n********** Exercise 03 - Execute a task with a policy (Strategy + Command) **********\n");
        t13_design_patterns_1.exercises.ex03.Exercise.run();

        System.out.println("\n********** Exercise 04 - MacroCommand (commands as building blocks) **********\n");
        t13_design_patterns_1.exercises.ex04.Exercise.run();

        System.out.println("\n********** Exercise 05 - Undoable commands (undo stack) **********\n");
        t13_design_patterns_1.exercises.ex05.Exercise.run();

        System.out.println("\n********** Exercise 06 - Cafe order tickets dispatcher (Command + Strategy) **********\n");
        t13_design_patterns_1.exercises.ex06.Exercise.run();
    }

    private void run_t14_DesignPatterns2(){
        System.out.println("\n********** Exercise 01 - ParserFactory by file extension (Factory) **********\n");
        t14_design_patterns_2.exercises.ex01.Exercise.run();

        System.out.println("\n********** Exercise 02 - EnemyFactory by difficulty (Factory) **********\n");
        t14_design_patterns_2.exercises.ex02.Exercise.run();

        System.out.println("\n********** Exercise 03 - Button click listeners (Observer) **********\n");
        t14_design_patterns_2.exercises.ex03.Exercise.run();

        System.out.println("\n********** Exercise 04 - TemperatureSensor events with payload (Observer) **********\n");
        t14_design_patterns_2.exercises.ex04.Exercise.run();

        System.out.println("\n********** Exercise 05 - LegacyLoggerAdapter (Adapter) **********\n");
        t14_design_patterns_2.exercises.ex05.Exercise.run();

        System.out.println("\n********** Exercise 06 - GatewayCheckoutAdapter (Adapter) **********\n");
        t14_design_patterns_2.exercises.ex06.Exercise.run();
    }

    private void run_t15_Dao(){
        try {
            System.out.println("\n********** Exercise 01 - DB smoke test (JDBC) **********\n");
            t15_dao.exercises.ex01.Exercise.run();

            System.out.println("\n********** Exercise 02 - Car domain model + InMemoryCarDao **********\n");
            t15_dao.exercises.ex02.Exercise.run();

            System.out.println("\n********** Exercise 03 - JdbcCarDao (PreparedStatement + mapping) **********\n");
            t15_dao.exercises.ex03.Exercise.run();

            System.out.println("\n********** Exercise 04 - CarRentalService (business rules layer) **********\n");
            t15_dao.exercises.ex04.Exercise.run();

            System.out.println("\n********** Exercise 05 - Paging, search, and countByStatus **********\n");
            t15_dao.exercises.ex05.Exercise.run();
        } catch (Exception e) {
            System.out.println("t12 exercise failed: " + e.getMessage());
        }
    }

    private void run_t16_FunctionalInterfaces(){
        t16_functional_interfaces.exercises.Exercise.run();
    }

    private void run_t19_Concurrency(){
        try {
            System.out.println("\n********** Exercise 01 - DeliveryTask as Runnable **********\n");
            t19_concurrency.exercises.ex01.Exercise.run();

            System.out.println("\n********** Exercise 02 - Dispatch with ExecutorService **********\n");
            t19_concurrency.exercises.ex02.Exercise.run();

            System.out.println("\n********** Exercise 03 - Race condition: shared counter **********\n");
            t19_concurrency.exercises.ex03.Exercise.run();

            System.out.println("\n********** Exercise 04 - Callable and Future **********\n");
            t19_concurrency.exercises.ex04.Exercise.run();

            System.out.println("\n********** Exercise 05 - Full dispatch simulation **********\n");
            t19_concurrency.exercises.ex05.Exercise.run();
        } catch (Exception e) {
            System.out.println("t14 exercise failed: " + e.getMessage());
        }
    }
}
