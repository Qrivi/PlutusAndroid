package be.plutus.android.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.ListView;
import be.plutus.android.R;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.List;

public class RadioButtonDialog extends BaseDialog
{

    private int current;
    private String[] options;
    private Context context;

    /**
     * The listener that is called when the positive button is pressed
     */
    private OnPositiveListener listener;

    public void setContext( Context context )
    {
        this.context = context;
    }

    public void setCurrent( int current )
    {
        this.current = current;
    }

    public void setOptions( List<Integer> optionsList )
    {
        this.options = listToArray( optionsList );
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

    private String[] listToArray( List<Integer> optionsList )
    {
        return Stream.of( optionsList )
                .map( o -> context.getResources()
                        .getString( o ) )
                .collect( Collectors.toList() )
                .toArray( new String[ optionsList.size() ] );
    }

    @Override
    public AlertDialog.Builder build()
    {
        AlertDialog.Builder builder;
        builder = super.build();
        builder.setSingleChoiceItems( options, current, null );
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
        ListView lw = ( (AlertDialog) dialog ).getListView();
        Integer selected = lw.getCheckedItemPosition();
        if ( selected != current )
            listener.onPositive( selected );
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
         * @param value The index of the selected item
         */
        void onPositive( int value );
    }

}
