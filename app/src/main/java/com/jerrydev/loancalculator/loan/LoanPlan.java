package com.jerrydev.loancalculator.loan;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class LoanPlan extends ViewModel {
    double base;
    int nMonths;
    double rate; // This is the monthly rate
    Double[] paymentPlan;

    // String components
    private final MutableLiveData<String> totalInterest;
    private final MutableLiveData<String> firstPay;

    /**
     * LoanPlan base class constructor
     * @param base The base loan amount (principle)
     * @param nMonths Number of months
     * @param annualRate Annual interest (rate)
     */
    LoanPlan(double base, int nMonths, double annualRate) {
        totalInterest = new MutableLiveData<>();
        firstPay = new MutableLiveData<>();
        updatePlan(base, nMonths, annualRate);
    }

    LoanPlan() {
        totalInterest = new MutableLiveData<>();
        firstPay = new MutableLiveData<>();
        updatePlan(0, 1, 0);
    }

    /**
     * Update repayment plan
     * @param base The base loan amount (principle)
     * @param nMonths Number of months
     * @param annualRate Annual interest (rate) as percentage
     */
    public void updatePlan(double base, int nMonths, double annualRate) {
        this.base = base;
        this.nMonths = nMonths;
        this.rate = annualRate / 1200;

        generateFullPlan();

        // Update strings
        totalInterest.setValue(String.format(Locale.CHINA, "%.2f", getTotalInterest()));
        try {
            firstPay.setValue(String.format(Locale.CHINA, "%.2f", paymentPlan[0]));
        } catch (ArrayIndexOutOfBoundsException e) {
            firstPay.setValue("--");
        }
    }

    /**
     * calcMonthPayment
     * @param monthIdx is the n-th month after loan (start from 0)
     * @return payment needed for the selected month.
     */
    public abstract double calcMonthPayment(int monthIdx);

    private void generateFullPlan() {
        paymentPlan = new Double[nMonths];
        for (int i = 0; i < nMonths; i++) {
            paymentPlan[i] = calcMonthPayment(i);
        }
    }

    /**
     * getTotalInterest
     * @return the total interest to pay over the entire period
     */
    public double getTotalInterest() {
        double total = 0;
        if (rate == 0 || nMonths == 0) {
            // Shortcut
            return 0;
        }
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
        return paymentPlan;
    }

    /**
     *
     * @return Payment of the 1st month as a string
     */
    public LiveData<String> getFirstPayStr() {
        return firstPay;
    }

    /**
     *
     * @return Total interest as a string
     */
    public LiveData<String> getInterestStr() {
        return totalInterest;
    }
}
