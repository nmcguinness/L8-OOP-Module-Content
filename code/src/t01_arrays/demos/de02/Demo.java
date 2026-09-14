package t01_arrays.demos.de02;

import java.util.ArrayList;

public class Demo {
    public static void run()
    {
        //2d array demo
        // Rectangular: same columns in every row
        int[][] rect = new int[3][4]; // 3 rows, 4 cols each

        // Jagged: different columns per row
        int[][] jag = new int[3][];       // rows allocated, inner rows null
        jag[0] = new int[2];              // row 0 has 2 columns
        jag[1] = new int[5];              // row 1 has 5 columns
        jag[2] = new int[3];              // row 2 has 3 columns

    //    print(rect, "Rectangular array:");
      //  print(jag, "*****\tJagged array\t****");

        int[][] years = new int[][]
                {
                        {1999, 2004, 2011},
                        {1985, 1945, 2025, 2017, 2018},
                        {1963}
                };
      //  print(years, "\n--- Years array ----\n");

        String[][] students = new String[][]{
                {"aman", "bea", "ciaran"},
                {"fatima", "aleksei"},
                {"mykola","gabe","dorota","tomas","bob"}
        };

        print(students, "*** Names array ***");

        Integer[][] myIntObjArray = {
                {1,2,4,2},
                {45,7,8,4},
                {32,3,5,6,7}};
        print(myIntObjArray, "Bla bla");
         Double[][] myVars = {{23.4}, {567.34, 3434.8}};
    }

    /// Converts array from 2D to 1D
    public static int[] flatten(int[][] array2d)
    {
        //input:
            //1,2,3
            //4,5,6
        //output:
            //1,2,3,4,5,6

        int size = 0;
        for (int[] row : array2d)
            for (int cell : row)
                size++;

        int[] flattened = new int[size];

        int index = 0;
        for (int[] row : array2d)
            for (int cell : row) {
                flattened[index] = cell;
                index++;
            }
        return flattened;
    }

    public static <E> void print(ArrayList<E> stdList)
    {

    }



    // generic method in java - see w3schools
    public static <T> void print(T [][] array, String headerStr)
    {

        if(array == null || array.length == 0)
            return;

        System.out.println(headerStr);
        for(int row = 0; row < array.length; row++)
        {
            for(int col = 0; col < array[row].length; col++)
            {
                System.out.print(array[row][col] + "\t");
            }
            System.out.println();
        }
    }


    public static void print(int[][] array, String headerStr)
    {
        if(array == null || array.length == 0)
            return;

        System.out.println(headerStr);
        for(int row = 0; row < array.length; row++)
        {
            for(int col = 0; col < array[row].length; col++)
            {
              //  System.out.print(array[row][col] + "\t");
                System.out.printf("%6d", array[row][col]);
            }
            System.out.println();
        }
    }


    public static void print(String[][] array, String headerStr)
    {
        if(array == null || array.length == 0)
            return;

        System.out.println(headerStr);
        for(int row = 0; row < array.length; row++)
        {
            for(int col = 0; col < array[row].length; col++)
            {
                //  System.out.print(array[row][col] + "\t");
                System.out.printf("%12s", array[row][col]);
            }
            System.out.println();
        }
    }
}
