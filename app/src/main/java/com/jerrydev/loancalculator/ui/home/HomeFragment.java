package com.jerrydev.loancalculator.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.jerrydev.loancalculator.R;
import com.jerrydev.loancalculator.databinding.FragmentHomeBinding;
import com.jerrydev.loancalculator.Utilities;
import com.jerrydev.loancalculator.loan.LoanPlan;
import com.jerrydev.loancalculator.loan.LoanPlanEMI;
import com.jerrydev.loancalculator.loan.LoanPlanEPI;
import com.jerrydev.loancalculator.loan.LoanPlanIFI;

import java.util.Locale;
import java.util.Objects;

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

    private final AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            LoanPlan[] allPlans = {planEMI, planEPI, planIFI};
            showPlan(allPlans[i]);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
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
        final Spinner planSpinner = binding.planSpinner;

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

            LoanPlan[] allPlans = {planEMI, planEPI, planIFI};

            showPlan(allPlans[planSpinner.getSelectedItemPosition()]);
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

        planSpinner.setOnItemSelectedListener(spinnerListener);

        return root;
    }

    private void showPlan(LoanPlan plan) {
        Double[] payments = plan.getFullPlan();
        Double[] remaining = plan.getRemaining();
        binding.fullPlanTable.removeAllViews();
        try {
            LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
            for (int i = 0; i < payments.length; i++) {
                @SuppressLint("InflateParams") View rowView = inflater.inflate(
                        R.layout.pay_table_row, null);
                ((TextView) rowView.findViewById(R.id.monthIdx)).setText(
                        String.format(Locale.CHINA, "%d", i+1));
                ((TextView) rowView.findViewById(R.id.payment)).setText(
                        String.format(Locale.CHINA, "%.2f", payments[i]));
                ((TextView) rowView.findViewById(R.id.remaining)).setText(
                        String.format(Locale.CHINA, "%.2f", remaining[i]));
                if ((i & 1) == 1) rowView.setBackgroundResource(
                        com.google.android.material.R.color.material_dynamic_neutral90);
                binding.fullPlanTable.addView(rowView);
            }
        } catch (NullPointerException ignored) {
            // TODO: proper error handling
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}