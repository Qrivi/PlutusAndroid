package be.plutus.android.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import be.plutus.android.R;

public class EditTextDialog extends BaseDialog implements DialogInterface.OnClickListener
{

    private View dialogView;
    private onPositiveListener listener;

    public static EditTextDialog create( Context context, String title, String message, onPositiveListener listener )
    {
        EditTextDialog dialog = new EditTextDialog();

        dialog.setTitle( title );
        dialog.setMessage( message );
        dialog.setListener( listener );
        dialog.setPositiveButton( context.getString( R.string.ok ), dialog );
        dialog.setNegativeButton( context.getString( R.string.cancel ), null );

        return dialog;
    }

    public void setListener( onPositiveListener listener )
    {
        this.listener = listener;
    }

    @Override
    public AlertDialog build()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity(), R.style.Plutus_Dialog );
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate( R.layout.dialog_edittext, null );

        builder.setView( dialogView );
        builder.setTitle( title );
        builder.setMessage( message );
        builder.setPositiveButton( positiveButton.first, positiveButton.second );
        builder.setNegativeButton( negativeButton.first, negativeButton.second );

        return builder.create();
    }

    @Override
    public AlertDialog after( AlertDialog dialog )
    {
        dialog.getButton( AlertDialog.BUTTON_NEGATIVE )
                .setTextColor( ContextCompat.getColor( getActivity(), R.color.text_clickable ) );
        return dialog;
    }

    @Override
    public void onClick( DialogInterface dialog, int which )
    {
        EditText editText = (EditText) dialogView.findViewById( R.id.dialog_edit );
        listener.success( editText.getText()
                .toString() );
    }

    public interface onPositiveListener
    {
        void success( String value );
    }

}
