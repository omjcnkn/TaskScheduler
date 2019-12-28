package com.example.taskschedulerproject;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private ImageView userBadgeImageView;
    private TextView usernameTextView;
    private EditText usernameEditText;
    private TextView userPointsTextView;
    private TextView userLevelTextView;

    private ArrayList<Integer> badge;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initLogic();
        initUI(view);
    }

    private void initLogic() {
        badge = new ArrayList<>(Arrays.asList(R.drawable.b1 , R.drawable.b2 , R.drawable.b3 , R.drawable.b4,
                R.drawable.b5));
    }

    private void initUI(View view) {
        userBadgeImageView = view.findViewById(R.id.userBadgeImageView);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        usernameEditText = view.findViewById(R.id.usernameEditText);
        userPointsTextView = view.findViewById(R.id.userPointsTextView);
        userLevelTextView = view.findViewById(R.id.userLevelTextView);

        final UserBoard userBoard = UserBoard.getUserBoard(getContext());

        userBadgeImageView.setImageResource(badge.get(userBoard.getLevel() - 1));
        usernameTextView.setText(userBoard.getUsername());
        usernameEditText.setText(userBoard.getUsername());
        userPointsTextView.setText(userBoard.getPoints() + "");
        userLevelTextView.setText(userBoard.getLevel() + "" );

        usernameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameEditText.setVisibility(View.VISIBLE);
                usernameEditText.setEnabled(true);

                usernameTextView.setVisibility(View.INVISIBLE);
                usernameTextView.setEnabled(false);

                usernameEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
                usernameEditText.requestFocus();

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(usernameEditText, InputMethodManager.SHOW_FORCED);

                usernameEditText.setCursorVisible(true);
            }
        });

        usernameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (((actionId & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE) &&
                        (!usernameEditText.getText().toString().isEmpty())) {
                    String newUsername = usernameEditText.getText().toString().trim();

                    usernameEditText.setVisibility(View.INVISIBLE);
                    usernameEditText.setEnabled(false);

                    userBoard.setUsername(newUsername);

                    usernameTextView.setVisibility(View.VISIBLE);
                    usernameTextView.setEnabled(true);
                    usernameTextView.setText(newUsername);

                    return true;
                }
                else {
                    if(usernameEditText.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Username can't be empty", Toast.LENGTH_SHORT).show();
                    }

                    usernameEditText.setVisibility(View.INVISIBLE);
                    usernameEditText.setEnabled(false);

                    usernameTextView.setVisibility(View.VISIBLE);
                    usernameTextView.setEnabled(true);

                    usernameTextView.setText(userBoard.getUsername());

                    return false;
                }
            }
        });
    }
}
