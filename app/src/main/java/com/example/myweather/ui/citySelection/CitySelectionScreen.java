package com.example.myweather.ui.citySelection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.myweather.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CitySelectionScreen extends Fragment {
    private ImageView iViewCurrent;
    private ImageView iViewFavourites;
//    private TextView tViewCurrentCity;
//    private TextView tViewtFavouritesCity;
//    final static String currentPointKey1 = "currentPointKey1";


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_city_selection_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);
        findView(view);
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_city_selection_screen);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        findView();
//        showBackBtn();
//        setOniViewCurrent();
////        showDataFromFirstActivity();
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId()== android.R.id.home) {
//            Intent intentCity = new Intent();
//            String strDate = tViewtFavouritesCity.getText().toString();
//            if (! strDate.equals("")) {
//                intentCity.putExtra(currentPointKey1, strDate);
//            } else {
//                intentCity.putExtra(currentPointKey1, tViewCurrentCity.getText().toString());
//            }
//            setResult(RESULT_OK, intentCity);
//            finish();
//        }
//        return true;
//    }

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
                Toast.makeText(getContext().getApplicationContext(),getString(R.string.favourites), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findView(View view) {
        iViewCurrent = view.findViewById(R.id.iViewCurrent);
        iViewFavourites = view.findViewById(R.id.iViewFavourites);
//        tViewCurrentCity = view.findViewById(R.id.tViewCurrentCity);
//        tViewtFavouritesCity = view.findViewById(R.id.tViewtFavouritesCity);
    }

//    private void showBackBtn(){
//        try {
//            Objects.requireNonNull(getContext().getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        } catch (NullPointerException e){
//            e.printStackTrace();
//        }
//    }

//    private void showDataFromFirstActivity(){
//        tViewCurrentCity.setText(getIntent().getStringExtra(MainActivity.currentPointKey));
//    }
}