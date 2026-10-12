package t01_arrays.challenges.ce01;

import common.FileUtils;

import java.util.ArrayList;

public class Exercise {

    public static void run()
    {
            //helper to tell you what directory to put your data file in
            System.out.println("Put your data file in: " + System.getProperty("user.dir"));

            long startTimeNS = System.nanoTime();

            //read all file contents
            char dataDelimiter = ',';
            String filePath = "data/ce01/ce01_data.txt";
            ArrayList<String> rawLicensePlateList = FileUtils.readDelimitedFile(filePath, dataDelimiter);

            //exit if fail
            if(rawLicensePlateList == null)
                return;

            //exit if empty
            if(rawLicensePlateList.isEmpty())
                return;

            //conditions for success
            String numberRegex = ".*6.*6.*";    //anything, then a 6, then anything, then another 6, then anything
            String countyRegex = ".*L.*";       //anything, then an L, then anything
            int yearTargetSum = 10;

            //parse contents into LicensePlate
            for(String licensePlate : rawLicensePlateList) {
                LicensePlate l1 = new LicensePlate(licensePlate);

                boolean isValid = l1.validate(numberRegex,
                        countyRegex, yearTargetSum);

                //we found a match!
                if (isValid)
                    System.out.println("Found: " + l1);
            }

        long finishTimeNS = System.nanoTime();
        System.out.println("Execution time (ms): " + (finishTimeNS - startTimeNS)/1E6);

    }
}
