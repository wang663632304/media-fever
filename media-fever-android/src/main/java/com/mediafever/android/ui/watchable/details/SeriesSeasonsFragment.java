package com.mediafever.android.ui.watchable.details;

import java.util.List;
import roboguice.inject.InjectView;
import android.annotation.TargetApi;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import com.jdroid.android.fragment.AbstractListFragment;
import com.jdroid.android.pager.OnPageSelectedListener;
import com.jdroid.android.tabs.TabAction;
import com.jdroid.android.utils.AndroidUtils;
import com.google.ads.AdSize;
import com.jdroid.java.collections.Lists;
import com.mediafever.R;
import com.mediafever.domain.UserWatchable;
import com.mediafever.domain.watchable.Episode;
import com.mediafever.domain.watchable.Season;
import com.mediafever.domain.watchable.Series;
import com.mediafever.usecase.MarkAsWachedUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class SeriesSeasonsFragment extends AbstractListFragment<UserWatchable<Episode>> implements
		OnItemSelectedListener, OnPageSelectedListener {
	
	private static final String USER_WATCHABLE_EXTRA = "userWatchable";
	private MarkAsWachedUseCase markAsWachedUseCase;
	
	@InjectView(R.id.seasonsSpinner)
	private Spinner spinner;
	
	private UserWatchable<Series> userWatchable;
	private Object actionMode;
	private List<UserWatchable<Episode>> episodesUserWatchables = Lists.newArrayList();
	
	public SeriesSeasonsFragment() {
	}
	
	public SeriesSeasonsFragment(UserWatchable<Series> userWatchable) {
		this.userWatchable = userWatchable;
		
		Bundle bundle = new Bundle();
		bundle.putSerializable(USER_WATCHABLE_EXTRA, userWatchable);
		setArguments(bundle);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onCreate(android.os.Bundle)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		Bundle args = getArguments();
		if (args != null) {
			userWatchable = (UserWatchable<Series>)args.getSerializable(USER_WATCHABLE_EXTRA);
		}
		
		if (markAsWachedUseCase == null) {
			markAsWachedUseCase = getInstance(MarkAsWachedUseCase.class);
			markAsWachedUseCase.setUserId(getUser().getId());
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		onResumeUseCase(markAsWachedUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		onPauseUseCase(markAsWachedUseCase, this);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return AndroidUtils.isLargeScreenOrBigger() ? AdSize.IAB_BANNER : AdSize.SMART_BANNER;
	}
	
	/**
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.episodes_list_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		((WatchableActivity)getActivity()).setOnPageSelectedListener(this);
		
		Series series = userWatchable.getWatchable();
		
		// FIXME The series selections are not kept when we rotate the screen
		SeasonAdapter adapter = new SeasonAdapter(getActivity(), series.getSeasons());
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		
		refresh(series.getSeasons().get(0));
		
		if (AndroidUtils.supportsContextualActionBar()) {
			setupActionMode();
		} else {
			registerForContextMenu(getListView());
		}
	}
	
	private void refresh(Season season) {
		setListAdapter(new EpisodeAdapter(getActivity(), season.getEpisodesUserWatchables()));
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onFinishUseCase()
	 */
	@TargetApi(11)
	@Override
	public void onFinishUseCase() {
		executeOnUIThread(new Runnable() {
			
			@Override
			public void run() {
				episodesUserWatchables.clear();
				if (AndroidUtils.supportsContextualActionBar()) {
					((ActionMode)actionMode).finish();
				}
				Season season = (Season)spinner.getSelectedItem();
				refresh(season);
				dismissLoading();
			}
		});
	}
	
	@TargetApi(11)
	private void setupActionMode() {
		ListView listView = getListView();
		listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
		listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
			
			@Override
			public boolean onCreateActionMode(android.view.ActionMode mode, android.view.Menu menu) {
				// Inflate the menu for the CAB
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.episode_menu, menu);
				
				spinner.setEnabled(false);
				actionMode = mode;
				return true;
			}
			
			@Override
			public boolean onPrepareActionMode(android.view.ActionMode mode, android.view.Menu menu) {
				return false;
			}
			
			@Override
			public boolean onActionItemClicked(android.view.ActionMode mode, android.view.MenuItem item) {
				// Respond to clicks on the actions in the CAB
				switch (item.getItemId()) {
					case R.id.addToWatchedItem:
						addToWatchedSelectedEpisodes(true);
						return true;
					case R.id.removeFromWatchedItem:
						addToWatchedSelectedEpisodes(false);
						return true;
					default:
						return false;
				}
			}
			
			@Override
			public void onDestroyActionMode(android.view.ActionMode mode) {
				episodesUserWatchables.clear();
				spinner.setEnabled(true);
				actionMode = null;
			}
			
			@SuppressWarnings("unchecked")
			@Override
			public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {
				UserWatchable<Episode> episodesUserWatchable = (UserWatchable<Episode>)getListView().getItemAtPosition(
					position);
				if (checked) {
					episodesUserWatchables.add(episodesUserWatchable);
				} else {
					episodesUserWatchables.remove(episodesUserWatchable);
				}
				mode.setTitle(getString(R.string.selected, episodesUserWatchables.size()));
			}
		});
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateContextMenu(android.view.ContextMenu, android.view.View,
	 *      android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.episode_menu, menu);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (!AndroidUtils.supportsContextualActionBar()) {
			UserWatchable<Episode> userWatchable = getMenuItem(item);
			episodesUserWatchables.add(userWatchable);
		}
		switch (item.getItemId()) {
			case R.id.addToWatchedItem:
				addToWatchedSelectedEpisodes(true);
				return true;
			case R.id.removeFromWatchedItem:
				addToWatchedSelectedEpisodes(false);
				return true;
			default:
				return false;
		}
	}
	
	@TargetApi(11)
	@Override
	public void onPageSelected(TabAction action) {
		if (actionMode != null) {
			((ActionMode)actionMode).finish();
		}
	}
	
	private void addToWatchedSelectedEpisodes(Boolean watched) {
		markAsWachedUseCase.setEpisodesUserWatchables(episodesUserWatchables);
		markAsWachedUseCase.setWatched(watched);
		executeUseCase(markAsWachedUseCase);
	}
	
	/**
	 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView,
	 *      android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		Season season = ((Season)parent.getItemAtPosition(pos));
		refresh(season);
	}
	
	/**
	 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
	 */
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// Do Nothing
	}
}