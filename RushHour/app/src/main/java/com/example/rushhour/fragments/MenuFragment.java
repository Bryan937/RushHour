package com.example.rushhour.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.rushhour.R;
import com.example.rushhour.databinding.FragmentMenuBinding;

public class MenuFragment extends Fragment {
    private FragmentMenuBinding menuBinding;


    /**
     * Overrides the onCreateView method to inflate the layout for the MenuFragment using the provided
     * LayoutInflater and binding.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        menuBinding = FragmentMenuBinding.inflate(inflater, container, false);
        return menuBinding.getRoot();
    }


    /**
     * Called after the view creation of the MenuFragment.
     * - Sets up click listeners for buttons in the menu.
     *   - Play button navigates to the GameViewFragment.
     *   - Exit button exits the application.
     *   - About button opens the about window.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menuBinding.buttonPlay.setOnClickListener(menuView ->
                NavHostFragment.findNavController(MenuFragment.this)
                        .navigate(R.id.action_MenuFragment_to_gameViewFragment));

        menuBinding.buttonExit.setOnClickListener(menuView -> {
            Activity activity = getActivity();
            if (activity != null) activity.finish();
        });

        menuBinding.buttonAbout.setOnClickListener(menuView -> openAboutWindow());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        menuBinding = null;
    }


    /**
     * Opens the about window, displaying information about the application.
     * Shows the dialog and sets up a click listener for the close button to dismiss the window.
     */
    private void openAboutWindow() {
        if (!isAdded()) return;

        requireActivity().runOnUiThread(() -> {
            Context context = getContext();
            if (context == null) return;

            Dialog aboutWindow = new Dialog(context);
            aboutWindow.setContentView(R.layout.window_about);
            Button closeWindowButton = aboutWindow.findViewById(R.id.closeButton);

            aboutWindow.show();
            aboutWindow.setCancelable(false);
            aboutWindow.setCanceledOnTouchOutside(false);

            closeWindowButton.setOnClickListener(aboutWindowView -> aboutWindow.dismiss());
        });
    }

}