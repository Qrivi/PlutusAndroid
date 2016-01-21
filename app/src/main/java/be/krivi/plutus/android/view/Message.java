package be.krivi.plutus.android.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.dialog.ConfirmationDialog;

/**
 * Created by Jan on 30/11/2015.
 */
public abstract class Message{

    public static void toast( Context context, String message ){
        Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
    }

    public static void snack( View view, String message ){
        Snackbar snack = Snackbar.make( view, message, Snackbar.LENGTH_LONG );

        ViewGroup group = (ViewGroup)snack.getView();
        TextView text = (TextView)group.findViewById( android.support.design.R.id.snackbar_text );

        group.setBackgroundColor( ContextCompat.getColor( view.getContext(), R.color.ucll_light_blue ) );
        text.setTextColor( ContextCompat.getColor( view.getContext(), R.color.ucll_dark_blue ) );

        snack.show();
    }

    public static void obtrusive( Context context, String message ){
        new AlertDialog.Builder( context, R.style.Plutus_Dialog )
                .setTitle( context.getString( R.string.something_went_wrong ) )
                .setMessage( message )
                .setPositiveButton( R.string.ok, new DialogInterface.OnClickListener(){
                    public void onClick( DialogInterface dialog, int which ){
                        // empty dismisses dialog
                    }
                } )
                .show();
    }
}
