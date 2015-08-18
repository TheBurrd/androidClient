package com.dgaf.happyhour.Model;

import java.util.List;

/**
 * Created by Adam on 8/6/2015.
 * A ModelUpdater serves the purpose of acting as a callback when information in the data model
 * needs to change. Typically they will be used to handle the results of server requests.
 * The generic nature of the ModelUpdater allows for different types of data objects to be returned
 */
public interface ModelUpdater<T> {
    void onDataModelUpdate(List<T> data, Exception e);
}
