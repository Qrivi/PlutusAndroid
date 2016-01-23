package be.plutus.android.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ListView;
import be.plutus.android.R;

import java.util.List;

public class RadioButtonDialog extends BaseDialog
{

    private int current;
    private String[] options;
    private Context context;
    private onPositiveListener listener;

    public static RadioButtonDialog create( Context context, String type, String message, int current, List<Integer> optionsList, onPositiveListener listener )
    {
        RadioButtonDialog dialog = new RadioButtonDialog();

        dialog.context = context;
        dialog.setTitle( type );
        dialog.setCurrent( current );
        dialog.setOptions( optionsList );
        dialog.setMessage( message );
        dialog.setListener( listener );

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

        builder.setTitle( title );
        // setMessage( builder, message ); TODO Find a way to implement this.
        builder.setSingleChoiceItems( options, current, null );
        builder.setPositiveButton( "OK", ( dialog, which ) -> {
            ListView lw = ( (AlertDialog) dialog ).getListView();
            Integer selected = lw.getCheckedItemPosition();
            if ( selected != current )
                listener.success( selected );
        } );

        return builder.create();
    }

    @Override
    public AlertDialog after( AlertDialog dialog )
    {
        dialog.getButton( AlertDialog.BUTTON_NEGATIVE )
                .setTextColor( ContextCompat.getColor( getActivity(), R.color.text_clickable ) );
        return dialog;
    }

    public void setCurrent( int current )
    {
        this.current = current;
    }

    public void setOptions( List<Integer> optionsList )
    {
        this.options = listToArray( optionsList );
    }

    private String[] listToArray( List<Integer> optionsList )
    {
        String[] results = new String[ optionsList.size() ];

        for ( int i = 0 ; i < optionsList.size() ; i++ )
            results[ i ] = context.getResources()
                    .getString( optionsList.get( i ) );

        return results;
    }

    public interface onPositiveListener
    {
        void success( int value );
    }

}
