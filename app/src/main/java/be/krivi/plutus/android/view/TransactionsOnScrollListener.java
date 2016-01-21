package be.krivi.plutus.android.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import be.krivi.plutus.android.application.Config;

/**
 * Created by Krivi on 25/12/15.
 * Merry Christmas
 */
public abstract class TransactionsOnScrollListener extends RecyclerView.OnScrollListener{
    private int previousTotal = Config.APP_DEFAULT_LIST_SIZE; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of transaction to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_set = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public TransactionsOnScrollListener( LinearLayoutManager linearLayoutManager ){
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled( RecyclerView recyclerView, int dx, int dy ){
        super.onScrolled( recyclerView, dx, dy );

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if( loading ){
            if( totalItemCount > previousTotal ){
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if( !loading && ( totalItemCount - visibleItemCount )
                <= ( firstVisibleItem + visibleThreshold ) ){

            onLoadMore( current_set++ );
            loading = true;
        }
    }

    public abstract void onLoadMore( int current_page );
}
