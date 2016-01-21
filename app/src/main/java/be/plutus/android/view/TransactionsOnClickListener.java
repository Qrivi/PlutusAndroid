package be.plutus.android.view;

import android.view.View;
import be.plutus.android.model.Transaction;

/**
 * Created by Krivi on 27/12/15.
 */
public interface TransactionsOnClickListener{

    void onTransactionClicked( View view, Transaction transaction );
}
