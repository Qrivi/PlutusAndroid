package be.plutus.android.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import be.plutus.android.R;

public class EditTextDialog extends BaseDialog
{

    /**
     * The listener that is called when the positive button is pressed
     */
    private OnPositiveListener listener;

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
    public AlertDialog.Builder build()
    {
        AlertDialog.Builder builder = super.build();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate( R.layout.dialog_edittext, null );

        builder.setView( view );

        return builder;
    }

    @Override
    public void after( AlertDialog dialog )
    {
        Button negativeButton = dialog.getButton( AlertDialog.BUTTON_NEGATIVE );
        negativeButton.setTextColor( ContextCompat.getColor( getActivity(), R.color.text_clickable ) );
    }

    @Override
    public void notifyPositive( DialogInterface dialog )
    {
        Window window = ( (AlertDialog) dialog ).getWindow();
        EditText editText = (EditText) window.findViewById( R.id.dialog_edit );
        listener.onPositive( editText.getText()
                .toString() );
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
        /**
         * @param value The value of the text field
         */
        void onPositive( String value );
    }
}
