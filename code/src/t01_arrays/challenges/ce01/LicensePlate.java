package t01_arrays.challenges.ce01;

public class LicensePlate{
    /*
    191 L 12345
    11 L 12345
    131 L 12345
    132 L 12345
     */

    //region Fields
    private String year;
    private String county;
    private String number;
    //endregion

    public LicensePlate(String rawLicensePlate)
    {
        //TODO - deal with no string, test parts for null
        String[] parts = rawLicensePlate.split("\\s");

        if(parts.length == 3)
        {
            this.year = parts[0];  //"131"
            this.county = parts[1];
            this.number = parts[2];
        }
    }

    public boolean validate(String numberRegex, String countyRegex, int yearTargetSum ) {
        boolean isTargetCounty
                = this.county.matches(countyRegex);

        boolean isNumberValid
                = this.number.matches(numberRegex);

        int sum = 0;
        for(int i = 0; i < this.year.length(); i++)
        {
            //convert the digit from a char to an int e.g. 9 (ASCII = 57) becomes 57-48 = 9
            int yearPart = this.year.charAt(i) - 48;
            sum += yearPart;
        }
        return isTargetCounty && isNumberValid && (sum == yearTargetSum);
    }

    //region Getters & Setters
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    //endregion

    //region Overrides
    @Override
    public String toString() {
        return "LicensePlate{" +
                "year='" + year + '\'' +
                ", county='" + county + '\'' +
                ", number=" + number +
                '}';
    }
    //endregion
}
