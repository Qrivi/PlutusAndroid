package be.plutus.android.dialog;

import android.app.AlertDialog;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.ListView;
import be.plutus.android.R;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.List;

/**
 * Dialog implementation that supports an option field
 */
public class OptionDialog extends BaseDialog
{

    /**
     * The current selected option index
     */
    private int current;

    /**
     * The list containing all the available options
     */
    private String[] options;


    /**
     * Sets the index of the selected item
     *
     * @param current The index of the selected item
     */
    public void setCurrent( int current )
    {
        this.current = current;
    }

    /**
     * Sets the list containing all the available options
     *
     * @param optionsList The list containing all the available options
     */
    public void setOptions( List<Integer> optionsList )
    {
        this.options = listToArray( optionsList );
    }

    /**
     * Converts a list to an array
     *
     * @param optionsList The list to be converted
     * @return An array containing all the objects from the array
     */
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
    protected void after( AlertDialog dialog )
    {
        Button negativeButton = dialog.getButton( AlertDialog.BUTTON_NEGATIVE );
        negativeButton.setTextColor( ContextCompat.getColor( getActivity(), R.color.text_clickable ) );
    }

    /**
     * @return The index of the selected option
     */
    public int getValue()
    {
        ListView lw = dialog.getListView();
        Integer selected = lw.getCheckedItemPosition();
        if ( selected != current )
            return selected;
        return -1;
    }

}
