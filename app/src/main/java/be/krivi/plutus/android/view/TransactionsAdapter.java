package be.krivi.plutus.android.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import be.krivi.plutus.android.R;
import be.krivi.plutus.android.application.Config;
import be.krivi.plutus.android.model.Transaction;
import butterknife.Bind;
import butterknife.ButterKnife;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Krivi on 23/12/15.
 */
public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>{

    private  TransactionsOnClickListener clickListener;
    private Context context;
    private LayoutInflater inflater;
    private List<RowData> rowData;

    private DecimalFormat df;

    public TransactionsAdapter( Context context, List<Transaction> transactions, TransactionsOnClickListener clickListener ){
        this.clickListener = clickListener;
        this.context = context;
        this.inflater = LayoutInflater.from( context );
        this.rowData = formatData( transactions );

        this.df = new DecimalFormat( "#0.00", DecimalFormatSymbols.getInstance( Locale.getDefault() ) );
    }

    public void setRowData( List<Transaction> transactions ){
        this.rowData = formatData( transactions );
        notifyDataSetChanged();
        // Log.v( "ADAPTER", "Loaded transactions: " + transactions.size() );
        // Log.v( "ADAPTER", "Loaded rowData: " + rowData.size() );
    }

    @Override
    public TransactionViewHolder onCreateViewHolder( ViewGroup parent, int viewType ){
        return new TransactionViewHolder( inflater.inflate( R.layout.row_transaction, parent, false ) );
    }

    @Override
    public void onBindViewHolder( TransactionViewHolder holder, int position ){

        RowData data = rowData.get( position );

        if( position < 1)
            holder.mSeparator.setVisibility( View.GONE );

        switch( data.type ){
            case 0: // header
                holder.mRowHeader.setVisibility( View.VISIBLE );
                holder.mRowData.setVisibility( View.GONE );

                holder.mHeader.setText( data.header );

                break;
            case 1: // transaction
                holder.mRowHeader.setVisibility( View.GONE );
                holder.mRowData.setVisibility( View.VISIBLE );

                Transaction t = data.transaction;

                holder.mMonth.setText( t.getMonth( "short" ) );
                holder.mDay.setText( t.getDay() + "" );

                holder.mTitle.setText( t.getTitle() );
                holder.mLocation.setText( t.getLocation().getName() );

                holder.setTransactionType( t.getType() );
                holder.mAmount.setText( Config.API_DEFAULT_CURRENCY_SYMBOL + " " + df.format( t.getAmount() ) );

                holder.transaction = t;
                break;
        }
    }

    @Override
    public int getItemCount(){
        return rowData.size();
    }

    private List<RowData> formatData( List<Transaction> list ){
        String currentMonth = "not set";
        List<RowData> currentData = new LinkedList<>();

        for( Transaction t : list ){
            if( !t.getMonth( "full" ).equals( currentMonth ) ){
                currentMonth = t.getMonth( "full" );
                currentData.add( new RowData( currentMonth + " " + t.getYear() ) );
            }
            currentData.add( new RowData( t, list.indexOf( t ) ) );
        }
        return currentData;
    }

    class RowData{
        public String header;
        public Transaction transaction;
        public int index;
        public int type;

        public RowData( String header ){
            this.header = capitalize( header );
            this.index = -1;
            this.type = 0;
        }

        public RowData( Transaction transaction, int index ){
            this.transaction = transaction;
            this.index = index;
            this.type = 1;
        }

        private String capitalize( String original ){
            if( original == null || original.length() == 0 )
                return original;
            return original.substring( 0, 1 ).toUpperCase() + original.substring( 1 );
        }
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind( R.id.tr_rowHeader )
        LinearLayout mRowHeader;

        @Bind( R.id.tr_separator )
        View mSeparator;

        @Bind( R.id.tr_rowData )
        RelativeLayout mRowData;

        @Bind( R.id.tr_header )
        TextView mHeader;

        @Bind( R.id.tr_wrapperDate )
        RelativeLayout mWrapperDate;

        @Bind( R.id.tr_month )
        TextView mMonth;

        @Bind( R.id.tr_day )
        TextView mDay;

        @Bind( R.id.tr_title )
        TextView mTitle;

        @Bind( R.id.tr_location )
        TextView mLocation;

        @Bind( R.id.tr_amount )
        TextView mAmount;

        public Transaction transaction;

        public TransactionViewHolder( View view ){
            super( view );
            ButterKnife.bind( this, view );
            mRowData.setOnClickListener( this );
        }

        public void setTransactionType( String transactionType ){
            switch( transactionType ){
                case "expense":
                    mAmount.setTextColor( ContextCompat.getColor( context, R.color.transaction_expense ) );
                    break;
                case "topup":
                    mAmount.setTextColor( ContextCompat.getColor( context, R.color.transaction_topup ) );
                    break;
            }
        }

        @Override
        public void onClick( View view ){
            if( transaction != null )
                clickListener.onTransactionClicked( view, transaction );
        }
    }
}
