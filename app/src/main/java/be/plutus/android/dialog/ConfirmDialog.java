package be.plutus.android.dialog;

import android.app.AlertDialog;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import be.plutus.android.R;

/**
 * Dialog implementation that supports a reset button
 */
public class ConfirmDialog extends BaseDialog
{

    /**
     * Determines if the reset button is shown
     */
    private boolean reset;

    /**
     * Sets the show state of the reset button
     *
     * @param reset The show state of the reset button
     */
    public void setReset( boolean reset )
    {
        this.reset = reset;
    }

    @Override
    protected void after( AlertDialog dialog )
    {
        if ( reset )
        {
            Button positiveButton = dialog.getButton( AlertDialog.BUTTON_POSITIVE );
            positiveButton.setTextColor( ContextCompat.getColor( getActivity(), R.color.plutus_red ) );
        }

        Button negativeButton = dialog.getButton( AlertDialog.BUTTON_NEGATIVE );
        negativeButton.setTextColor( ContextCompat.getColor( getActivity(), R.color.text_clickable ) );
    }

}
