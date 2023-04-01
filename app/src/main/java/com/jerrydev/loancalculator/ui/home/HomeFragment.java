package com.jerrydev.loancalculator.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jerrydev.loancalculator.R;
import com.jerrydev.loancalculator.databinding.FragmentHomeBinding;
import com.jerrydev.loancalculator.Utilities;
import com.jerrydev.loancalculator.loan.LoanPlan;
import com.jerrydev.loancalculator.loan.LoanPlanEMI;
import com.jerrydev.loancalculator.loan.LoanPlanEPI;
import com.jerrydev.loancalculator.loan.LoanPlanIFI;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private LoanPlan planEMI;
    private LoanPlan planEPI;
    private LoanPlan planIFI;

    private final TextWatcher loanValueWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            final TextView loanCH = binding.loanCH;
            try {
                String loanStr = Utilities.num2Chinese(charSequence.toString());
                loanCH.setText(loanStr);
            } catch (NumberFormatException e) {
                loanCH.setText("--");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    private final TextWatcher loanTimeWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            int numYears, numMonths;
            int totalMonths;
            String hint;
            try {
                numYears = Integer.parseInt(binding.inputYear.getText().toString());
            } catch (NumberFormatException e) {
                numYears = 0;
            }
            try {
                numMonths = Integer.parseInt(binding.inputMonth.getText().toString());
            } catch (NumberFormatException e) {
                numMonths = 0;
            }
            totalMonths = Utilities.calcTotalMonths(numYears, numMonths);
            hint = getString(R.string.tmpl_total_months);
            binding.totalMonths.setText(String.format(hint, totalMonths));
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final EditText loanDecimal = binding.inputLoan;
        final EditText loanYear = binding.inputYear;
        final EditText loanMonth = binding.inputMonth;
        final EditText loanAnnualRate = binding.inputAnnualRate;
        final Button confirmBtn = binding.confirmInputBtn;
        final TextView EMIFirstPay = binding.emiMonth0Payment;
        final TextView EMIInterest = binding.emiTotalInterest;
        final TextView EPIFirstPay = binding.epiMonth0Payment;
        final TextView EPIInterest = binding.epiTotalInterest;
        final TextView IFIFirstPay = binding.intFirstMonth0Payment;
        final TextView IFIInterest = binding.intFirstTotalInterest;

        loanDecimal.addTextChangedListener(loanValueWatcher);
        loanYear.addTextChangedListener(loanTimeWatcher);
        loanMonth.addTextChangedListener(loanTimeWatcher);

        confirmBtn.setOnClickListener(view -> {
            double base, annualRate;
            int nMonths, nYears, totalMonths;
            try {
                base = Double.parseDouble(loanDecimal.getText().toString());
            } catch (NumberFormatException e) {
                base = 0;
            }
            try {
                annualRate = Double.parseDouble(loanAnnualRate.getText().toString());
            } catch (NumberFormatException e) {
                annualRate = 0;
            }
            try {
                nMonths = Integer.parseInt(loanMonth.getText().toString());
            } catch (NumberFormatException e) {
                nMonths = 0;
            }
            try {
                nYears = Integer.parseInt(loanYear.getText().toString());
            } catch (NumberFormatException e) {
                nYears = 0;
            }
            totalMonths = Utilities.calcTotalMonths(nYears, nMonths);
            planEMI.updatePlan(base, totalMonths, annualRate);
            planEPI.updatePlan(base, totalMonths, annualRate);
            planIFI.updatePlan(base, totalMonths, annualRate);
        });

        planEMI = new ViewModelProvider(this).get(LoanPlanEMI.class);
        planEMI.getFirstPayStr().observe(getViewLifecycleOwner(), EMIFirstPay::setText);
        planEMI.getInterestStr().observe(getViewLifecycleOwner(), EMIInterest::setText);

        planEPI = new ViewModelProvider(this).get(LoanPlanEPI.class);
        planEPI.getFirstPayStr().observe(getViewLifecycleOwner(), EPIFirstPay::setText);
        planEPI.getInterestStr().observe(getViewLifecycleOwner(), EPIInterest::setText);

        planIFI = new ViewModelProvider(this).get(LoanPlanIFI.class);
        planIFI.getFirstPayStr().observe(getViewLifecycleOwner(), IFIFirstPay::setText);
        planIFI.getInterestStr().observe(getViewLifecycleOwner(), IFIInterest::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}