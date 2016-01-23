package be.plutus.android.dialog;

import android.app.AlertDialog;
import android.support.v4.content.ContextCompat;
import be.plutus.android.R;

public class ConfirmationDialog extends BaseDialog
{

    private boolean reset;
    private onPositiveListener listener;

    public static ConfirmationDialog create( String type, String message, onPositiveListener listener )
    {
        return create( type, message, false, listener );
    }

    public static ConfirmationDialog create( String type, String message, boolean reset, onPositiveListener listener )
    {
        ConfirmationDialog dialog = new ConfirmationDialog();

        dialog.setTitle( type );
        dialog.setMessage( message );
        dialog.setReset( reset );
        dialog.setListener( listener );

        return dialog;
    }

    public void setReset( boolean reset )
    {
        this.reset = reset;
    }

    public void setListener( onPositiveListener listener )
    {
        this.listener = listener;
    }

    @Override
    public AlertDialog build()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity(), R.style.Plutus_Dialog );

        builder.setTitle( title );
        builder.setMessage( message );
        // todo add reset button
        builder.setPositiveButton( "OK", ( dialog, which ) -> listener.success() );

        return builder.create();
    }

    @Override
    public AlertDialog after( AlertDialog dialog )
    {
        if ( reset )
        {
            dialog.getButton( AlertDialog.BUTTON_POSITIVE )
                    .setTextColor( ContextCompat.getColor( getActivity(), R.color.plutus_red ) );
            dialog.getButton( AlertDialog.BUTTON_NEGATIVE )
                    .setTextColor( ContextCompat.getColor( getActivity(), R.color.text_clickable ) );
        }
        return dialog;
    }

    public interface onPositiveListener
    {
        void success();
    }

}
