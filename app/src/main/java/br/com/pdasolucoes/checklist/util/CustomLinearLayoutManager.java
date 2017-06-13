package br.com.pdasolucoes.checklist.util;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by PDA on 23/01/2017.
 */

public class CustomLinearLayoutManager extends LinearLayoutManager{
    public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);


    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }
}
