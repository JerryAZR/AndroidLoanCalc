package com.jerrydev.loancalculator;

import java.util.Locale;

public class Utilities {
    private static final String[] chnNumChar = {"零","一","二","三","四","五","六","七","八","九"};
    private static final String[] chnUnitSection = {"", "万","亿"};
    private static final String[] chnUnitChar = {"", "十","百","千"};

    /**
     * num2Chinese(String)
     * Converts input number to Chinese representation
     * @param num Number in decimal or scientific representation
     * @return Rounded value in Chinese
     * @throws NumberFormatException if the input string is not a valid number
     */
    public static String num2Chinese(String num) throws NumberFormatException
    {
        String out;
        String[] tmpList;
        double numVal = Double.parseDouble(num);
        String scientific = String.format(Locale.CHINA,"%.1E", numVal);
        double rounded = Double.parseDouble(scientific);
        if (numVal == 0) return "零";
        if (numVal >= 1e16) return scientific;
        // Keep only the two most significant digits
        int log10, mod4, div4;
        int digit1, digit2;
        boolean trillion = false;
        tmpList = scientific.split("[.eE]");
        if (tmpList.length != 3) return scientific; // Unknown error

        out = "";
        if (rounded != numVal) out += "约 ";
        digit1 = Integer.parseInt(tmpList[0]);
        digit2 = Integer.parseInt(tmpList[1]);
        log10 = Integer.parseInt(tmpList[2]);
        if (log10 < 0) return "小于 一";
        if (log10 > 8) {
            // number is larger than a trillion
            trillion = true;
            log10 -= 8;
        }
        // Process the 1st digit
        mod4 = log10 % 4; // Get unit index
        div4 = log10 / 4; // Get section index
        if (digit1 != 1 || mod4 != 1) out += chnNumChar[digit1];
        out += chnUnitChar[mod4];
        if (mod4 == 0 || digit2 == 0) out += chnUnitSection[div4];
        // Process the 2nd digit (if non-zero)
        if (digit2 != 0) {
            if (log10 > 0) log10--;
            else out += ".";
            mod4 = log10 % 4; // Get unit index
            div4 = log10 / 4; // Get section index
            out += chnNumChar[digit2];
            out += chnUnitChar[mod4];
            out += chnUnitSection[div4];
        }
        if (trillion) out += chnUnitSection[2];
        return out;
    }

    /**
     * calcTotalMonths(int, int)
     * Calculate the equivalent number of months from the given number of years and months.
     * @param years Total number of years
     * @param months Number of months remaining after subtracting the years
     * @return Total number of months
     */
    public static int calcTotalMonths(int years, int months) {
        return years * 12 + months;
    }
}
