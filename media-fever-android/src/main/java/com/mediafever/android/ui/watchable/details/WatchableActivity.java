package com.mediafever.android.ui.watchable.details;

import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.fragment.OnItemSelectedListener;
import com.jdroid.android.fragment.UseCaseFragment;
import com.jdroid.android.pager.ContextualItemsPagerAdapter;
import com.jdroid.android.pager.OnPageSelectedListener;
import com.jdroid.android.utils.AndroidUtils;
import com.mediafever.R;
import com.mediafever.domain.UserWatchable;
import com.mediafever.domain.watchable.Watchable;
import com.mediafever.domain.watchable.WatchableType;
import com.mediafever.usecase.UserWatchableUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchableActivity extends AbstractFragmentActivity implements
		OnItemSelectedListener<WatchableContextualItem> {
	
	public final static String WATCHABLE_TYPE_EXTRA = "WatchableTypeExtra";
	
	public static class WatchableUseCaseFragment extends UseCaseFragment<UserWatchableUseCase> {
		
		/**
		 * @see com.jdroid.android.fragment.UseCaseFragment#getUseCaseClass()
		 */
		@Override
		protected Class<UserWatchableUseCase> getUseCaseClass() {
			return UserWatchableUseCase.class;
		}
		
		/**
		 * @see com.jdroid.android.fragment.UseCaseFragment#intializeUseCase(com.jdroid.android.usecase.DefaultAbstractUseCase)
		 */
		@Override
		protected void intializeUseCase(UserWatchableUseCase watchableUseCase) {
			watchableUseCase.setUserId(getUser().getId());
			watchableUseCase.setWatchableId(Long.valueOf(getActivity().getIntent().getDataString()));
		}
		
		/**
		 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
		 */
		@Override
		public void onFinishUseCase() {
			executeOnUIThread(new Runnable() {
				
				@Override
				public void run() {
					FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
					
					UserWatchable<Watchable> userWatchable = getUseCase().getUserWatchable();
					
					if (AndroidUtils.isLargeScreenOrBigger()) {
						
						WatchableContextualItem defaultWatchableContextualItem = WatchableContextualItem.OVERVIEW;
						fragmentTransaction.add(R.id.contextualFragmentContainer,
							WatchableContextualFragment.instance(userWatchable));
						fragmentTransaction.add(R.id.detailsFragmentContainer,
							defaultWatchableContextualItem.createFragment(userWatchable),
							defaultWatchableContextualItem.getName());
					} else {
						loadHandsetUI();
					}
					
					fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					fragmentTransaction.commit();
					dismissLoading();
				}
			});
		}
		
		public void loadHandsetUI() {
			UserWatchable<Watchable> userWatchable = getUseCase().getUserWatchable();
			if (userWatchable != null) {
				final List<WatchableContextualItem> items = WatchableContextualItem.getWatchableContextualItems(userWatchable);
				
				ContextualItemsPagerAdapter pagerAdapter = new ContextualItemsPagerAdapter(
						getActivity().getSupportFragmentManager(), items, userWatchable);
				ViewPager viewPager = findViewOnActivity(R.id.pager);
				viewPager.setOnPageChangeListener(new SimpleOnPageChangeListener() {
					
					@Override
					public void onPageSelected(int position) {
						if (((WatchableActivity)getActivity()).onPageSelectedListener != null) {
							((WatchableActivity)getActivity()).onPageSelectedListener.onPageSelected(items.get(position));
						}
					}
				});
				viewPager.setAdapter(pagerAdapter);
			}
		}
	}
	
	private WatchableType watchableType;
	
	private OnPageSelectedListener onPageSelectedListener;
	
	public static void start(Context context, Watchable watchable) {
		start(context, watchable.getId(), WatchableType.find(watchable));
	}
	
	public static void start(Context context, Long watchableId, WatchableType watchableType) {
		Intent intent = new Intent(context, WatchableActivity.class);
		intent.setData(Uri.parse(watchableId.toString()));
		intent.putExtra(WatchableActivity.WATCHABLE_TYPE_EXTRA, watchableType);
		context.startActivity(intent);
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getContentView()
	 */
	@Override
	public int getContentView() {
		return AndroidUtils.isLargeScreenOrBigger() ? R.layout.contextual_activity
				: com.mediafever.R.layout.watchable_activity;
	}
	
	/**
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		watchableType = getExtra(WatchableActivity.WATCHABLE_TYPE_EXTRA);
		setTitle(watchableType.getResourceId());
		loadUseCaseFragment(savedInstanceState, WatchableUseCaseFragment.class);
		
		if ((savedInstanceState != null) && !AndroidUtils.isLargeScreenOrBigger()) {
			WatchableUseCaseFragment useCase = (WatchableUseCaseFragment)getUseCaseUseCaseFragment(WatchableUseCaseFragment.class);
			useCase.loadHandsetUI();
		}
		
	}
	
	/**
	 * @see com.jdroid.android.fragment.OnItemSelectedListener#onItemSelected(java.lang.Object)
	 */
	@Override
	public void onItemSelected(WatchableContextualItem item) {
		Fragment oldFragment = getSupportFragmentManager().findFragmentById(R.id.detailsFragmentContainer);
		if (!oldFragment.getTag().equals(item.getName())) {
			
			WatchableUseCaseFragment useCase = (WatchableUseCaseFragment)getUseCaseUseCaseFragment(WatchableUseCaseFragment.class);
			UserWatchable<Watchable> userWatchable = useCase.getUseCase().getUserWatchable();
			
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.detailsFragmentContainer, item.createFragment(userWatchable),
				item.getName());
			fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fragmentTransaction.commit();
		}
	}
	
	public void setOnPageSelectedListener(OnPageSelectedListener onPageSelectedListener) {
		this.onPageSelectedListener = onPageSelectedListener;
	}
	
	/**
	 * @see com.jdroid.android.activity.AbstractFragmentActivity#getMenuResourceId()
	 */
	@Override
	public int getMenuResourceId() {
		int menuResourceId = super.getMenuResourceId();
		if (!AndroidUtils.isLargeScreenOrBigger()) {
			menuResourceId = R.menu.watchable_menu;
		}
		return menuResourceId;
	}
}
