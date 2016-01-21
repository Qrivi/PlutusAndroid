package be.krivi.plutus.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import be.krivi.plutus.android.activity.MainActivity;
import be.krivi.plutus.android.application.PlutusAndroid;

/**
 * Created by Krivi on 29/12/15.
 */
public abstract class BaseFragment extends Fragment{

    PlutusAndroid app;
    MainActivity main;

    @Override
    public void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );

        main = (MainActivity)getActivity();
        app = (PlutusAndroid)main.getApplication();
    }

    public abstract void updateView();
}
