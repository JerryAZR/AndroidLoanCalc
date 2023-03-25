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
import androidx.fragment.app.Fragment;

import com.jerrydev.loancalculator.databinding.FragmentHomeBinding;
import com.jerrydev.loancalculator.Utilities;

import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private final TextWatcher loanValueWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

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
        public void afterTextChanged(Editable editable) {

        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView loanCH = binding.loanCH;
        final EditText loanArabic = binding.inputLoan;
        loanArabic.addTextChangedListener(loanValueWatcher);
        loanCH.setText("0");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}