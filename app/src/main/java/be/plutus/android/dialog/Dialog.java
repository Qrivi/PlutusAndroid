package be.plutus.android.dialog;

import android.content.Context;
import be.plutus.android.R;

import java.util.List;

/**
 * Factory class for Dialogs
 */
public class Dialog
{

    /**
     * Creates a confirm dialog
     *
     * @param context  The context of the dialog
     * @param title    The title of the dialog
     * @param message  The message of the dialog
     * @param listener The listener that will be called when the "OK" button is clicked
     * @return A confirmation dialog with the given parameters
     */
    public static ConfirmDialog confirm( Context context, String title, String message, BaseDialog.OnPositiveListener listener )
    {
        ConfirmDialog dialog = new ConfirmDialog();

        dialog.setTitle( title );
        dialog.setMessage( message );
        dialog.setReset( false );
        dialog.addListener( listener );
        dialog.setPositiveButton( context.getString( R.string.ok ), null );

        return dialog;
    }

    /**
     * Creates a reset dialog
     *
     * @param context  The context of the dialog
     * @param title    The title of the dialog
     * @param message  The message of the dialog
     * @param listener The listener that will be called when the "Reset" button is clicked
     * @return A reset dialog with the given parameters
     */
    public static ConfirmDialog reset( Context context, String title, String message, BaseDialog.OnPositiveListener listener )
    {
        ConfirmDialog dialog = new ConfirmDialog();

        dialog.setTitle( title );
        dialog.setMessage( message );
        dialog.setReset( true );
        dialog.addListener( listener );
        dialog.setPositiveButton( context.getString( R.string.reset ), null );
        dialog.setNegativeButton( context.getString( R.string.cancel ), null );

        return dialog;
    }

    /**
     * Creates a text input dialog
     *
     * @param context  The context of the dialog
     * @param title    The title of the dialog
     * @param message  The message of the dialog
     * @param listener The listener that will be called when the "OK" button is clicked
     * @return A text input dialog with the given parameters
     */
    public static InputDialog input( Context context, String title, String message, BaseDialog.OnPositiveListener listener )
    {
        InputDialog dialog = new InputDialog();

        dialog.setTitle( title );
        dialog.setMessage( message );
        dialog.addListener( listener );
        dialog.setPositiveButton( context.getString( R.string.ok ), null );
        dialog.setNegativeButton( context.getString( R.string.cancel ), null );

        return dialog;
    }

    /**
     * Creates an option dialog
     *
     * @param context     The context of the dialog
     * @param title       The title of the dialog
     * @param message     The message of the dialog
     * @param current     The current selected option
     * @param optionsList The list containing the available options
     * @param listener    The listener that will be called when the "OK" button is clicked
     * @return An option dialog with the given parameters
     */
    public static OptionDialog option( Context context, String type, String message, int current, List<Integer> optionsList, BaseDialog.OnPositiveListener listener )
    {
        OptionDialog dialog = new OptionDialog();

        dialog.setTitle( type );
        //dialog.setMessage( message ); // AlertDialog doesn't support this
        dialog.addListener( listener );
        dialog.setContext( context );
        dialog.setCurrent( current );
        dialog.setOptions( optionsList );
        dialog.setPositiveButton( context.getString( R.string.ok ), null );
        dialog.setNegativeButton( context.getString( R.string.cancel ), null );

        return dialog;
    }

}
