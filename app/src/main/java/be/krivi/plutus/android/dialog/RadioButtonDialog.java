package be.krivi.plutus.android.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import be.krivi.plutus.android.R;

import java.util.List;

/**
 * Created by Jan on 28/12/2015.
 */
public class RadioButtonDialog extends BaseDialog{

    private int current;
    private String[] options;
    private Context context;

    public static RadioButtonDialog newInstance( Context context, String type, String message, int current, List<Integer> optionsList ){
        RadioButtonDialog dialog = new RadioButtonDialog();
        dialog.context = context;
        dialog.setDialogType( type );
        dialog.setCurrent( current );
        dialog.setOptions( optionsList );
        dialog.initializeMessage( message );
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ){

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity(), R.style.Plutus_Dialog );

        setTitle( builder, getType() );
       // setMessage( builder, message ); TODO Find a way to implement this.
        builder.setSingleChoiceItems( options, current, this );
        setNegativeButton( builder, getString( R.string.cancel ) );

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener( new DialogInterface.OnShowListener(){
            @Override
            public void onShow( DialogInterface dialogInterface ){
                dialog.getButton( AlertDialog.BUTTON_NEGATIVE ).setTextColor( ContextCompat.getColor( getActivity(), R.color.text_clickable ) );
            }
        } );

        return dialog;
    }

    public void setCurrent( int current ){
        this.current = current;
    }

    public void setOptions( List<Integer> optionsList ){
        this.options = listToArray( optionsList );
    }

    private String[] listToArray( List<Integer> optionsList ){
        String[] results = new String[optionsList.size()];
        for( int i = 0; i < optionsList.size(); i++ )
            results[i] = context.getResources().getString( optionsList.get( i ) );

        return results;
    }
}
