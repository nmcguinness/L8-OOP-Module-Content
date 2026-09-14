package t13_design_patterns_1.exercises.ex03;

public class Exercise
{
    public static void run()
    {
        Task task = new Task("Export leaderboard");

        Command fast = new ExecuteTaskCommand(task, new FastExecution());
        Command safe = new ExecuteTaskCommand(task, new SafeExecution());

        System.out.println("--- FAST ---");
        fast.execute();

        System.out.println("--- SAFE ---");
        safe.execute();
    }
}

interface Command
{
    void execute();
}

class ExecuteTaskCommand implements Command
{
    private Task _task;
    private TaskExecutionStrategy _strategy;

    public ExecuteTaskCommand(Task task, TaskExecutionStrategy strategy)
    {
        _task = task;
        _strategy = strategy;
    }

    @Override
    public void execute()
    {
        _strategy.execute(_task);
    }
}

interface TaskExecutionStrategy
{
    void execute(Task task);
}

class FastExecution implements TaskExecutionStrategy
{
    @Override
    public void execute(Task task)
    {
        task.runUnchecked();
    }
}

class SafeExecution implements TaskExecutionStrategy
{
    @Override
    public void execute(Task task)
    {
        task.validate();
        task.run();
    }
}

class Task
{
    private String _name;

    public Task(String name)
    {
        _name = name;
    }

    public void validate()
    {
        System.out.println("Validating: " + _name);
    }

    public void run()
    {
        System.out.println("Running: " + _name);
    }

    public void runUnchecked()
    {
        System.out.println("Running unchecked: " + _name);
    }
}
