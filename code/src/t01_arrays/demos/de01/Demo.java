package t01_arrays.demos.de01;

public class Demo
{
    public static void run()
    {
        int[] nums = {3, 5, 8, 12, 21, 67, 34, 56, 77};

        System.out.println("SD: " + standardDeviation(nums));

        //forward with span
        int span = 2;
        for(int i = 0; i < nums.length; i+=span)
            System.out.println(nums[i]);

        //backward with span
        for(int i = nums.length-1; i > 0; i--) {
            System.out.println(nums[i]);
        }

        System.out.println("Sum: " + sum(nums));
        System.out.println("Average: " + average(nums));
    }

    public static int sum(int[] nums)
    {
        if(nums == null)
            return Integer.MIN_VALUE;  //throw exception

        int sum = 0;

        for(int i = 0; i < nums.length; i++)
            sum += nums[i];

        return sum;
    }

    public static float average(int[] nums)
    {
        return (float)sum(nums)/nums.length;  //5/9 = 0
    }

    public static float standardDeviation(int[] nums)
    {
        if(nums == null || nums.length == 0)  //A
            return 0;     //throw exception

        //get average
        float average = average(nums);

        //get sum of differences
        double sumOfDifferences = 0;
        for(int i = 0; i < nums.length; i++)
        {
            sumOfDifferences += Math.pow(nums[i] - average, 2);
        }

        //divide by N-1
        sumOfDifferences /=  (nums.length-1);

        //sqrt and return
        float result = (float)Math.sqrt(sumOfDifferences);
        return result;
    }

}
