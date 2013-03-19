package com.mediafever.android.contentprovider;

import android.app.SearchManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.contentprovider.ReadOnlyContentProvider;
import com.jdroid.android.usecase.listener.DefaultUseCaseListener;
import com.mediafever.android.ui.watchable.WatchableAdapter;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.domain.watchable.WatchableType;
import com.mediafever.usecase.WatchablesSuggestionsUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchablesSuggestionsProvider extends ReadOnlyContentProvider implements DefaultUseCaseListener {
	
	private WatchablesSuggestionsUseCase watchablesSuggestionsUseCase;
	
	/**
	 * @see com.jdroid.android.contentprovider.ReadOnlyContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		return true;
	}
	
	/**
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String,
	 *      java.lang.String[], java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		
		if (watchablesSuggestionsUseCase == null) {
			watchablesSuggestionsUseCase = AbstractApplication.getInstance(WatchablesSuggestionsUseCase.class);
		}
		watchablesSuggestionsUseCase.addListener(this);
		watchablesSuggestionsUseCase.setSearchValue(selectionArgs[0]);
		watchablesSuggestionsUseCase.run();
		watchablesSuggestionsUseCase.removeListener(this);
		
		MatrixCursor cursor = new MatrixCursor(new String[] { BaseColumns._ID,
				SearchManager.SUGGEST_COLUMN_INTENT_DATA, SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA,
				SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_TEXT_2,
				SearchManager.SUGGEST_COLUMN_ICON_1 });
		
		if (watchablesSuggestionsUseCase.isFinishSuccessful()) {
			for (Watchable watchable : watchablesSuggestionsUseCase.getSearchResult().getResults()) {
				WatchableType watchableType = WatchableType.find(watchable);
				cursor.addRow(new String[] { watchable.getId().toString(), watchable.getId().toString(),
						watchableType.getName(), watchable.getName(),
						WatchableAdapter.getWatchableDescription(watchable), "" + watchableType.getIconId() });
			}
		}
		return cursor;
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		// Do Nothing
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onUpdateUseCase()
	 */
	@Override
	public void onUpdateUseCase() {
		// Do Nothing
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishFailedUseCase(java.lang.RuntimeException)
	 */
	@Override
	public void onFinishFailedUseCase(RuntimeException runtimeException) {
		throw runtimeException;
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		// Do Nothing
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishCanceledUseCase()
	 */
	@Override
	public void onFinishCanceledUseCase() {
		// Do Nothing
	}
}
