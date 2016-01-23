package be.plutus.android.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import be.plutus.android.R;

public class ConfirmationDialog extends BaseDialog
{

    /**
     * Determines if the reset button is shown
     */
    private boolean reset;

    /**
     * The listener that is called when the positive button is pressed
     */
    private OnPositiveListener listener;

    /**
     * Sets the show state of the reset button
     *
     * @param reset The show state of the reset button
     */
    public void setReset( boolean reset )
    {
        this.reset = reset;
    }

    /**
     * Sets the positive listener of the dialog
     *
     * @param listener The positive listener of the dialog
     */
    public void setListener( OnPositiveListener listener )
    {
        this.listener = listener;
    }

    @Override
    public void after( AlertDialog dialog )
    {
        if ( reset )
        {
            Button positiveButton = dialog.getButton( AlertDialog.BUTTON_POSITIVE );
            positiveButton.setTextColor( ContextCompat.getColor( getActivity(), R.color.plutus_red ) );
        }

        Button negativeButton = dialog.getButton( AlertDialog.BUTTON_NEGATIVE );
        negativeButton.setTextColor( ContextCompat.getColor( getActivity(), R.color.text_clickable ) );
    }

    @Override
    public void notifyPositive( DialogInterface dialog )
    {
        listener.onPositive();
    }

    @Override
    public void notifyNeutral( DialogInterface dialog )
    {

    }

    @Override
    public void notifyNegative( DialogInterface dialog )
    {

    }

    public interface OnPositiveListener extends BaseDialog.OnPositiveListener
    {
        void onPositive();
    }

}
