package com.example.myweather.ui.citySelection;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myweather.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

public class CitySelectionScreen extends Fragment {
    private ImageView iViewCurrent;
    private ImageView iViewFavourites;
    private TextView tViewCurrentCity;
    private TextView tViewtFavouritesCity;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_city_selection_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SharedPreferences defaultPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        setRetainInstance(true);
        findView(view);
        readFromPreference(defaultPrefs);
        setOniViewCurrent();
    }

    private void readFromPreference(SharedPreferences preferences) {
        String textKeyCurrentPlace = "current_place";
        String textKeyDefaultCity = "edit_text_preference_1";
        boolean currentPlace = preferences.getBoolean(textKeyCurrentPlace, false);
        String textCity = preferences.getString(textKeyDefaultCity, "");
        if (currentPlace) {
            tViewCurrentCity.setText(textCity);
        }
    }

    private void saveToPreference(SharedPreferences preferences) {
        String text = tViewtFavouritesCity.getText().toString();
        if (text != null) {
            String textKeyCurrentPlace = "current_place";
            String textKeyDefaultCity = "edit_text_preference_1";
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(textKeyDefaultCity, text);
            editor.putBoolean(textKeyCurrentPlace, true);
            editor.apply();
        }
    }

    private void setOniViewCurrent() {
        iViewCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext().getApplicationContext(),getString(R.string.current_place), Toast.LENGTH_SHORT).show();
            }
        });
        iViewFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences defaultPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                saveToPreference(defaultPrefs);
//                FragmentMain nextFrag= new FragmentMain();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.nav_host_fragment, nextFrag, "")
//                        .commit();
                // как закрыть этот фрагмент и открыть другой домашний.
            }
        });
    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }


    private void findView(View view) {
        iViewCurrent = view.findViewById(R.id.iViewCurrent);
        iViewFavourites = view.findViewById(R.id.iViewFavourites);
        tViewCurrentCity = view.findViewById(R.id.tViewCurrentCity);
        tViewtFavouritesCity = view.findViewById(R.id.tViewtFavouritesCity);
    }

}