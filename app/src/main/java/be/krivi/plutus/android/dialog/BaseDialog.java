package be.krivi.plutus.android.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by Jan on 28/12/2015.
 */
public class BaseDialog extends android.support.v4.app.DialogFragment implements DialogInterface.OnClickListener{

    protected String type;
    protected String message;

    public interface NoticeDialogListener{
        void onDialogPositiveClick( BaseDialog dialog, int id );
        void onDialogNegativeClick( BaseDialog dialog );
    }

    @Override
    public void onClick( DialogInterface dialogInterface, int id ){
        NoticeDialogListener callback = (NoticeDialogListener)getTargetFragment();
        if( id >= -1 ){
            callback.onDialogPositiveClick( this, id );
        }else{
            callback.onDialogNegativeClick( this );
            this.getDialog().cancel();
        }
    }

    public void setDialogType( String type ){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }

    protected void setTitle( AlertDialog.Builder builder, String title ){
        builder.setTitle( title );
    }

    protected void setPositiveButton( AlertDialog.Builder builder, String text ){
        builder.setPositiveButton( text, this );
    }

    protected void setNegativeButton( AlertDialog.Builder builder, String text ){
        builder.setNegativeButton( text, this );
    }

    protected void setMessage( AlertDialog.Builder builder, String message ){
        builder.setMessage( message );
    }

    protected void initializeMessage( String message ){
        this.message = message;
    }



}
