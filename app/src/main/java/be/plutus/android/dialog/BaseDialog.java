package be.plutus.android.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import be.plutus.android.R;

/**
 * Base implementation of a dialog
 */
public abstract class BaseDialog extends android.support.v4.app.DialogFragment
{

    /**
     * Title and message of the dialog
     */
    protected String title;
    protected String message;

    /**
     * Positive, neutral and negative button of the dialog
     */
    protected Pair<String, DialogInterface.OnClickListener> positiveButton;
    protected Pair<String, DialogInterface.OnClickListener> neutralButton;
    protected Pair<String, DialogInterface.OnClickListener> negativeButton;

    /**
     * Sets the title of the dialog
     *
     * @param title The new title of the dialog
     */
    public void setTitle( String title )
    {
        this.title = title;
    }

    /**
     * Sets the message of the dialog
     *
     * @param message The new message of the dialog
     */
    public void setMessage( String message )
    {
        this.message = message;
    }

    /**
     * Sets the positive button text and listener
     *
     * @param text     The new text of the positive button
     * @param listener The new listener of the positive button
     */
    public void setPositiveButton( String text, DialogInterface.OnClickListener listener )
    {
        this.positiveButton = new Pair<>( text, listener );
    }

    /**
     * Sets the neutral button text and listener
     *
     * @param text     The new text of the neutral button
     * @param listener The new listener of the neutral button
     */
    public void setNeutralButton( String text, DialogInterface.OnClickListener listener )
    {
        this.neutralButton = new Pair<>( text, listener );
    }

    /**
     * Sets the negative button text and listener
     *
     * @param text     The new text of the negative button
     * @param listener The new listener of the negative button
     */
    public void setNegativeButton( String text, DialogInterface.OnClickListener listener )
    {
        this.negativeButton = new Pair<>( text, listener );
    }

    /**
     * @return An instance of the AlertDialog builder with the default properties added
     */
    public AlertDialog.Builder build()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity(), R.style.Plutus_Dialog );

        if ( title != null )
            builder.setTitle( title );
        if ( message != null )
            builder.setMessage( message );
        if ( positiveButton != null )
            builder.setPositiveButton( positiveButton.first, ( dialog, which ) -> notifyPositive( dialog ) );
        if ( neutralButton != null )
            builder.setNeutralButton( neutralButton.first, ( dialog, which ) -> notifyNeutral( dialog ) );
        if ( negativeButton != null )
            builder.setNegativeButton( negativeButton.first, ( dialog, which ) -> notifyNegative( dialog ) );

        return builder;
    }

    /**
     * This method is called when the AlertDialog is shown
     * You can use this to give extra styles to elements of the dialog
     */
    public abstract void after( AlertDialog dialog );

    /**
     * When the dialog is created a new AlertDialog is build and the after listener is set
     */
    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        AlertDialog dialog = build().create();
        dialog.setOnShowListener( d -> after( dialog ) );
        return dialog;
    }

    /**
     * Interface for the positive action
     */
    public interface OnPositiveListener {}

    /**
     * Interface for the neutral action
     */
    public interface OnNeutralListener {}

    /**
     * Interface for the negative action
     */
    public interface OnNegativeListener {}

    /**
     * Button click notifiers
     */
    public abstract void notifyPositive( DialogInterface dialog );
    public abstract void notifyNeutral( DialogInterface dialog );
    public abstract void notifyNegative( DialogInterface dialog );

}
