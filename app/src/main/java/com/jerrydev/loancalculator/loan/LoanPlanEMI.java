package com.jerrydev.loancalculator.loan;

public class LoanPlanEMI extends LoanPlan {
    @Override
    public double calcMonthPayment(int monthIdx) {
        // The following function applies only if interest is greater than 0
        //              R * (1 + R) ^ i
        // X = base * -------------------
        //              (1 + R) ^ i - 1
        if (rate == 0) return base / nMonths;
        double power = Math.pow(1 + rate, nMonths);
        return base * (rate * power) / (power - 1);
    }
}
