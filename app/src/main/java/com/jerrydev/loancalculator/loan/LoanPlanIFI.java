package com.jerrydev.loancalculator.loan;

public class LoanPlanIFI extends LoanPlan {
    @Override
    public double calcMonthPayment(int monthIdx) {
        if (monthIdx < nMonths - 1) return base * rate;
        else return base * rate + base;
    }
}
