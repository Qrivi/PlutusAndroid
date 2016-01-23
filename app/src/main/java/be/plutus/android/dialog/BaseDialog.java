package be.plutus.android.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

public abstract class BaseDialog extends android.support.v4.app.DialogFragment
{

    protected String title;
    protected String message;

    protected Pair<String, DialogInterface.OnClickListener> positiveButton;
    protected Pair<String, DialogInterface.OnClickListener> negativeButton;

    public void setTitle( String title )
    {
        this.title = title;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    public void setPositiveButton( String text, DialogInterface.OnClickListener listener )
    {
        this.positiveButton = new Pair<>( text, listener );
    }

    public void setNegativeButton( String text, DialogInterface.OnClickListener listener )
    {
        this.negativeButton = new Pair<>( text, listener );
    }

    public abstract AlertDialog build();

    public abstract AlertDialog after( AlertDialog dialog );

    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState )
    {
        AlertDialog dialog = build();
        dialog.setOnShowListener( d -> after( dialog ) );
        return dialog;
    }

}
