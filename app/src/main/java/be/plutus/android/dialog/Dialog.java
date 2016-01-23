package be.plutus.android.dialog;

import android.content.Context;
import be.plutus.android.R;

import java.util.List;

public class Dialog
{

    public static ConfirmationDialog confirm( Context context, String title, String message, ConfirmationDialog.OnPositiveListener listener )
    {
        ConfirmationDialog dialog = new ConfirmationDialog();

        dialog.setTitle( title );
        dialog.setMessage( message );
        dialog.setReset( false );
        dialog.setListener( listener );
        dialog.setPositiveButton( context.getString( R.string.ok ), null );

        return dialog;
    }

    public static ConfirmationDialog reset( Context context, String title, String message, ConfirmationDialog.OnPositiveListener listener )
    {
        ConfirmationDialog dialog = new ConfirmationDialog();

        dialog.setTitle( title );
        dialog.setMessage( message );
        dialog.setReset( true );
        dialog.setListener( listener );
        dialog.setPositiveButton( context.getString( R.string.reset ), null );
        dialog.setNegativeButton( context.getString( R.string.cancel ), null );

        return dialog;
    }

    public static EditTextDialog text( Context context, String title, String message, EditTextDialog.OnPositiveListener listener )
    {
        EditTextDialog dialog = new EditTextDialog();

        dialog.setTitle( title );
        dialog.setMessage( message );
        dialog.setListener( listener );
        dialog.setPositiveButton( context.getString( R.string.ok ), null );
        dialog.setNegativeButton( context.getString( R.string.cancel ), null );

        return dialog;
    }

    public static RadioButtonDialog radio( Context context, String type, String message, int current, List<Integer> optionsList, RadioButtonDialog.OnPositiveListener listener )
    {
        RadioButtonDialog dialog = new RadioButtonDialog();

        dialog.setTitle( type );
        //dialog.setMessage( message ); // AlertDialog doesn't support this
        dialog.setListener( listener );
        dialog.setContext( context );
        dialog.setCurrent( current );
        dialog.setOptions( optionsList );
        dialog.setPositiveButton( context.getString( R.string.ok ), null );
        dialog.setNegativeButton( context.getString( R.string.cancel ), null );

        return dialog;
    }

}
