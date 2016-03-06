package be.plutus.android.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import be.plutus.android.R;
import com.annimon.stream.Stream;

import java.util.HashSet;
import java.util.Set;

/**
 * Base implementation of a dialog
 */
public abstract class BaseDialog extends android.support.v4.app.DialogFragment
{

    /**
     * The context of the dialog
     */
    protected Context context;


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
     * Listeners
     */
    protected Set<OnPositiveListener> positiveListeners;
    protected Set<OnNeutralListener> neutralListeners;
    protected Set<OnNegativeListener> negativeListeners;

    /**
     * Dialog object
     */
    protected AlertDialog dialog;

    /**
     * Default constructor for a Dialog
     */
    public BaseDialog()
    {
        positiveListeners = new HashSet<>();
        neutralListeners = new HashSet<>();
        negativeListeners = new HashSet<>();
    }

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
     * Sets the context of the dialog
     *
     * @param context The new context of the dialog
     */
    public void setContext( Context context )
    {
        this.context = context;
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
    protected AlertDialog.Builder build()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity(), R.style.Plutus_Dialog );

        if ( title != null )
            builder.setTitle( title );
        if ( message != null )
            builder.setMessage( message );
        if ( positiveButton != null )
            builder.setPositiveButton( positiveButton.first, ( dialog, which ) -> notifyPositive( this ) );
        if ( neutralButton != null )
            builder.setNeutralButton( neutralButton.first, ( dialog, which ) -> notifyNeutral( this ) );
        if ( negativeButton != null )
            builder.setNegativeButton( negativeButton.first, ( dialog, which ) -> notifyNegative( this ) );

        return builder;
    }

    /**
     * This method is called when the AlertDialog is shown
     * You can use this to give extra styles to elements of the dialog
     */
    protected abstract void after( AlertDialog dialog );

    /**
     * When the dialog is created a new AlertDialog is build and the after listener is set
     */
    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        this.dialog = build().create();
        this.dialog.setOnShowListener( d -> after( dialog ) );
        return dialog;
    }

    /**
     * Interface for the positive action
     */
    public interface OnPositiveListener
    {
        void onPositive( BaseDialog dialog );
    }

    /**
     * Interface for the neutral action
     */
    public interface OnNeutralListener
    {
        void onNeutral( BaseDialog dialog );
    }

    /**
     * Interface for the negative action
     */
    public interface OnNegativeListener
    {
        void onNegative( BaseDialog dialog );
    }

    /**
     * Adds a positive listener to the dialog
     *
     * @param listener The listener to add to the dialog
     * @return If the listener is added
     */
    public boolean addListener( OnPositiveListener listener )
    {
        return this.positiveListeners.add( listener );
    }

    /**
     * Adds a neutral listener to the dialog
     *
     * @param listener The listener to add to the dialog
     * @return If the listener is added
     */
    public boolean addListener( OnNeutralListener listener )
    {
        return this.neutralListeners.add( listener );
    }

    /**
     * Adds a negative listener to the dialog
     *
     * @param listener The listener to add to the dialog
     * @return If the listener is added
     */
    public boolean addListener( OnNegativeListener listener )
    {
        return this.negativeListeners.add( listener );
    }

    /**
     * Notifies all the positive listeners
     *
     * @param dialog The dialog that sends the notification
     */
    protected void notifyPositive( BaseDialog dialog )
    {
        Stream.of( positiveListeners )
                .forEach( l -> l.onPositive( dialog ) );
    }

    /**
     * Notifies all the neutral listeners
     *
     * @param dialog The dialog that sends the notification
     */
    protected void notifyNeutral( BaseDialog dialog )
    {
        Stream.of( neutralListeners )
                .forEach( l -> l.onNeutral( dialog ) );
    }

    /**
     * Notifies all the negative listeners
     *
     * @param dialog The dialog that sends the notification
     */
    protected void notifyNegative( BaseDialog dialog )
    {
        Stream.of( negativeListeners )
                .forEach( l -> l.onNegative( dialog ) );
    }

}
