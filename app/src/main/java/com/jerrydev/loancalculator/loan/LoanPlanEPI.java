package com.jerrydev.loancalculator.loan;

public class LoanPlanEPI extends LoanPlan {
    @Override
    public double calcMonthPayment(int monthIdx) {
        double monthlyPrinciple = base / nMonths;
        return (base - (monthlyPrinciple * monthIdx)) * rate + monthlyPrinciple;
    }
}
