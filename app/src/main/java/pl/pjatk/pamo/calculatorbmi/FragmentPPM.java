package pl.pjatk.pamo.calculatorbmi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class FragmentPPM extends Fragment {

    float bmi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adult_calc_result, container, false);
        ImageView food = (ImageView) view.findViewById(R.id.food_image);

        AdultCalcResultActivity activity = (AdultCalcResultActivity) getActivity();
        bmi = activity.getBmi();

        if(bmi<=18.5) food.setImageResource(R.drawable.pizza);
        else if(bmi>18.5 && bmi<=24.9) food.setImageResource(R.drawable.pizza);
        else if(bmi>24.9 && bmi<=29.9) food.setImageResource(R.drawable.dinner);
        else if(bmi>29.9 && bmi<=34.9) food.setImageResource(R.drawable.dinner);
        else if(bmi>34.9 && bmi<=39.9) food.setImageResource(R.drawable.water);
        else if(bmi>39.9) food.setImageResource(R.drawable.water);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}


