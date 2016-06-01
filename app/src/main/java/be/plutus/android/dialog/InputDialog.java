package be.plutus.android.dialog;

import android.app.AlertDialog;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import be.plutus.android.R;

/**
 * Dialog implementation that supports an input field
 */
public class InputDialog extends BaseDialog
{

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
    protected void after( AlertDialog dialog )
    {
        Button negativeButton = dialog.getButton( AlertDialog.BUTTON_NEGATIVE );
        negativeButton.setTextColor( ContextCompat.getColor( getActivity(), R.color.text_clickable ) );
    }

    /**
     * @return The text that is inputted
     */
    public String getValue()
    {
        Window window = dialog.getWindow();
        EditText editText = (EditText) window.findViewById( R.id.dialog_edit );
        return editText.getText()
                .toString();
    }


}
