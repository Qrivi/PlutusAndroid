package be.krivi.plutus.android.view;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Krivi on 26/12/15.
 */

public class CollapseAnimation extends Animation{

    public enum CollapseAnimationAction{
        COLLAPSE,
        EXPAND
    }

    private View view;
    private CollapseAnimationAction action;

    private int viewHeight;

    public CollapseAnimation( View view, CollapseAnimationAction action ){
        super();
        this.view = view;
        this.action = action;

        view.measure( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        viewHeight = view.getMeasuredHeight();

        this.setDuration( (int)( viewHeight / view.getContext().getResources().getDisplayMetrics().density ) * 3 );
    }

    @Override
    protected void applyTransformation( float interpolatedTime, Transformation t ){
        switch( action ){
            case EXPAND:
                view.setVisibility( View.VISIBLE );
                view.getLayoutParams().height = 1;
                view.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)( viewHeight * interpolatedTime );
                view.requestLayout();
                break;
            case COLLAPSE:
                if( interpolatedTime == 1 ){
                    view.setVisibility( View.GONE );
                }else{
                    view.getLayoutParams().height = viewHeight - (int)( viewHeight * interpolatedTime );
                    view.requestLayout();
                }
                break;
        }
    }

    @Override
    public boolean willChangeBounds(){
        return true;
    }
}