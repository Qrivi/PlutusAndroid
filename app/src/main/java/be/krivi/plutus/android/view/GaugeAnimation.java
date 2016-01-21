package be.krivi.plutus.android.view;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

/**
 * Created by Krivi on 26/12/15.
 */
public class GaugeAnimation extends Animation{
    private ProgressBar gauge;
    private float from;
    private float to;

    public GaugeAnimation( ProgressBar gauge, float from, float to ){
        super();
        this.gauge = gauge;
        this.from = from;
        this.to = to;
    }

    @Override
    protected void applyTransformation( float interpolatedTime, Transformation t ){
        super.applyTransformation( interpolatedTime, t );
        float value = from + ( to - from ) * interpolatedTime;
        gauge.setProgress( (int)value );
    }

}