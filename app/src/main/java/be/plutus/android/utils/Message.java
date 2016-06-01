package be.plutus.android.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import be.plutus.android.R;
import be.plutus.android.dialog.Dialog;

public abstract class Message
{

    public static void toast( Context context, String message )
    {
        Toast.makeText( context, message, Toast.LENGTH_LONG )
                .show();
    }

    public static void snack( View view, String message )
    {
        Snackbar snack = Snackbar.make( view, message, Snackbar.LENGTH_LONG );

        ViewGroup group = (ViewGroup) snack.getView();
        TextView text = (TextView) group.findViewById( android.support.design.R.id.snackbar_text );

        group.setBackgroundColor( ContextCompat.getColor( view.getContext(), R.color.ucll_light_blue ) );
        text.setTextColor( ContextCompat.getColor( view.getContext(), R.color.ucll_dark_blue ) );

        snack.show();
    }

    public static void obtrusive( Context context, String message )
    {
        Dialog.confirm( context, context.getString( R.string.something_went_wrong ), message, null )
                .build()
                .show();
    }
}
