package com.example.taskschedulerproject;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private ImageView userBadgeImageView;
    private TextView usernameTextView;
    private EditText usernameEditText;
    private TextView userPointsTextView;
    private TextView userLevelTextView;


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

        initUI(view);
    }

    private void initUI(View view) {
        userBadgeImageView = view.findViewById(R.id.userBadgeImageView);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        usernameEditText = view.findViewById(R.id.usernameEditText);
        userPointsTextView = view.findViewById(R.id.userPointsTextView);
        userLevelTextView = view.findViewById(R.id.userLevelTextView);

        final UserBoard userBoard = UserBoard.getUserBoard(getContext());

        setUserBadge(userBoard.getBadge());
        usernameTextView.setText(userBoard.getUsername());
        usernameEditText.setText(userBoard.getUsername());
        userPointsTextView.setText("Points : " + userBoard.getPoints());
        userLevelTextView.setText("Level : " + userBoard.getLevel());

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
                imm.showSoftInput(usernameEditText, InputMethodManager.SHOW_IMPLICIT);

                usernameEditText.setCursorVisible(true);
            }
        });

        usernameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId & EditorInfo.IME_MASK_ACTION) == EditorInfo.IME_ACTION_DONE) {
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
                    return false;
                }
            }
        });
    }

    private void setUserBadge(String badge) {

    }
}
