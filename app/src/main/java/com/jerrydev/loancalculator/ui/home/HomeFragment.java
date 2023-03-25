package com.jerrydev.loancalculator.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.jerrydev.loancalculator.R;
import com.jerrydev.loancalculator.databinding.FragmentHomeBinding;
import com.jerrydev.loancalculator.Utilities;

import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

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

        final TextView loanCH = binding.loanCH;
        final EditText loanArabic = binding.inputLoan;
        final EditText loanYear = binding.inputYear;
        final EditText loanMonth = binding.inputMonth;

        loanArabic.addTextChangedListener(loanValueWatcher);
        loanYear.addTextChangedListener(loanTimeWatcher);
        loanMonth.addTextChangedListener(loanTimeWatcher);

        // Set initial inputs
//        binding.inputLoan.setText("0");
//        binding.inputYear.setText("0");
//        binding.inputMonth.setText("0");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}