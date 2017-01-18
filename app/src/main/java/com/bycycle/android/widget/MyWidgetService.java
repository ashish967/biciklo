package com.bycycle.android.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bycycle.android.R;
import com.bycycle.android.datatypes.Station;
import com.bycycle.android.utils.AppProvider;
import com.bycycle.android.utils.Logger;

import java.util.ArrayList;
import java.util.List;


public class MyWidgetService extends RemoteViewsService {




    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class MyWidgetRemoteViewsFactory implements RemoteViewsFactory {

        private List<Station> mWidgetItems = new ArrayList<Station>();
        private Context mContext;
        private int mAppWidgetId;

        public MyWidgetRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {

            Cursor cursor = mContext.getContentResolver().query(AppProvider.getStationUri(null), null,null,
                    null, null);

            if(cursor!=null) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++){

                    Station station=Station.getStation(cursor);
                    mWidgetItems.add(station);
                    Logger.log("station name "+station.getName());
                    cursor.moveToNext();
                }
            }


        }

        @Override
        public void onDataSetChanged() {
            final long identityToken = Binder.clearCallingIdentity();
            mWidgetItems.clear();
            onCreate();
            Binder.restoreCallingIdentity(identityToken);

        }

        @Override
        public void onDestroy() {
            mWidgetItems.clear();

        }

        @Override
        public int getCount() {
            return mWidgetItems.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            // position will always range from 0 to getCount() - 1.

            // We construct a remote views item based on our widget item xml file, and set the
            // text based on the position.
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.stations_widget_list_item);
            rv.setTextViewText(R.id.tv_station_name, mWidgetItems.get(position).getName());

            // Next, we set a fill-intent which will be used to fill-in the pending intent template
            // which is set on the collection view in StackWidgetProvider.
            Bundle extras = new Bundle();
            extras.putInt(SimpleWidgetProvider.EXTRA_ITEM, position);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            rv.setOnClickFillInIntent(R.id.list_item, fillInIntent);

            // You can do heaving lifting in here, synchronously. For example, if you need to
            // process an image, fetch something from the network, etc., it is ok to do it here,
            // synchronously. A loading view will show up in lieu of the actual contents in the
            // interim.


            // Return the remote views object.
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
