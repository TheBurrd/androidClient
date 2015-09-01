package com.dgaf.happyhour.Model.Queries;

import android.content.Context;

import com.dgaf.happyhour.Model.ModelUpdater;

/**
 * Created by Adam on 8/5/2015.
 *
 * The query interface is intended to be implemented by specific types of queries that
 * can be kicked off to the server. Due to the asynchronous nature of these calls,
 * a ModelUpdater callback is passed to execute when the result returns.
 */
public interface Query<T> {
    void fetch(Context context, final ModelUpdater<T> modelUpdater);
}
