package com.mediafever.android.ui.watchable.search;

import java.util.List;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.fragment.UseCaseFragment;
import com.jdroid.android.utils.AndroidUtils;
import com.mediafever.android.ui.Reloadable;
import com.mediafever.android.ui.watchable.details.WatchableActivity;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.domain.watchable.WatchableType;
import com.mediafever.usecase.SearchWatchablesUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class SearchableActivity extends AbstractFragmentActivity {
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getContentView()
	 */
	@Override
	public int getContentView() {
		return R.layout.fragment_container_activity;
	}
	
	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			handleIntent(getIntent());
		}
	}
	
	/**
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}
	
	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			loadUseCaseFragment(null, SearchWatchablesUseCaseFragment.class);
		} else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			// Handles a click on a search suggestion; launches activity to show watchable details
			Intent watchableIntent = new Intent(this, WatchableActivity.class);
			watchableIntent.setData(intent.getData());
			WatchableType watchableType = WatchableType.find(intent.getStringExtra(SearchManager.EXTRA_DATA_KEY));
			watchableIntent.putExtra(WatchableActivity.WATCHABLE_TYPE_EXTRA, watchableType);
			startActivity(watchableIntent);
			finish();
		}
	}
	
	public static class SearchWatchablesUseCaseFragment extends UseCaseFragment<SearchWatchablesUseCase> {
		
		/**
		 * @see com.jdroid.android.fragment.UseCaseFragment#getUseCaseClass()
		 */
		@Override
		protected Class<SearchWatchablesUseCase> getUseCaseClass() {
			return SearchWatchablesUseCase.class;
		}
		
		@Override
		protected void intializeUseCase(SearchWatchablesUseCase useCase) {
			SearchableActivity activity = (SearchableActivity)getActivity();
			String query = activity.getIntent().getStringExtra(SearchManager.QUERY);
			useCase.setQuery(query);
		}
		
		/**
		 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
		 */
		@Override
		@SuppressWarnings("unchecked")
		public void onFinishUseCase() {
			executeOnUIThread(new Runnable() {
				
				@Override
				public void run() {
					SearchableActivity activity = (SearchableActivity)getActivity();
					Reloadable<Watchable> reloadable = (Reloadable<Watchable>)activity.getSupportFragmentManager().findFragmentById(
						R.id.fragmentContainer);
					List<Watchable> watchables = getUseCase().getWatchables();
					if (reloadable == null) {
						reloadable = AndroidUtils.isLargeScreenOrBigger() ? WatchablesGridFragment.instance(watchables)
								: WatchablesListFragment.instance(watchables);
						activity.commitFragment((Fragment)reloadable);
					} else {
						reloadable.reload(watchables);
					}
					dismissLoading();
				}
			});
		}
	}
}
