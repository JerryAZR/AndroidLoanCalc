package com.jerrydev.loancalculator;

import java.util.ArrayList;
import java.util.List;

public abstract class LoanPlan {
    double base;
    int nMonths;
    double rate; // This is the monthly rate
    List<Double> paymentPlan;

    /**
     * LoanPlan base class constructor
     * @param base The base loan amount (principle)
     * @param nMonths Number of months
     * @param annualRate Annual interest (rate)
     */
    LoanPlan(double base, int nMonths, double annualRate) {
        this.base = base;
        this.nMonths = nMonths;
        this.rate = annualRate / 12;

        generateFullPlan();
    }

    /**
     * calcMonthPayment
     * @param loanBase is the base loan amount
     * @param monthIdx is the n-th month after loan (start from 0)
     * @param monthlyRate is Annual rate / 12
     * @return payment needed for the selected month.
     */
    public abstract double calcMonthPayment(double loanBase, int monthIdx, double monthlyRate);

    private void generateFullPlan() {
        paymentPlan = new ArrayList<>(nMonths);
        for (int i = 0; i < nMonths; i++) {
            paymentPlan.set(i, calcMonthPayment(base, i, rate));
        }
    }

    /**
     * getTotalInterest
     * @return the total interest to pay over the entire period
     */
    public double getTotalInterest() {
        double total = 0;
        for (double monthly: paymentPlan) {
            total += monthly;
        }
        return total - base;
    }

    /**
     * getFullPlan
     * @return the full repayment plan in an array of Doubles,
     * with the first element being the payment of the first month and so on.
     */
    public Double[] getFullPlan() {
        return paymentPlan.toArray(new Double[0]);
    }
}
