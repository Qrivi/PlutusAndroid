package be.krivi.plutus.android.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import be.krivi.plutus.android.R;

/**
 * Created by Jan on 28/12/2015.
 */
public class EditTextDialog extends BaseDialog{

    public static EditTextDialog newInstance(String type, String message) {
        EditTextDialog dialog = new EditTextDialog();
        dialog.setDialogType( type );
        dialog.initializeMessage( message );
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ){
        super.onCreateDialog( savedInstanceState );

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity(), R.style.Plutus_Dialog );
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate( R.layout.dialog_edittext, null );

        builder.setView( dialogView );
        setTitle( builder, getType() );
        builder.setMessage( message );
        setPositiveButton( builder, getString( R.string.ok) );
        setNegativeButton( builder, getString( R.string.cancel) );

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener( new DialogInterface.OnShowListener(){
            @Override
            public void onShow( DialogInterface dialogInterface ){
                dialog.getButton( AlertDialog.BUTTON_NEGATIVE ).setTextColor( ContextCompat.getColor( getActivity(), R.color.text_clickable ) );
            }
        } );

        return dialog;
    }

}
