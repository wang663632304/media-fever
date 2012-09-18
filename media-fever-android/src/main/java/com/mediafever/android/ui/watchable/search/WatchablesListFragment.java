package com.mediafever.android.ui.watchable.search;

import java.io.Serializable;
import java.util.List;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jdroid.android.fragment.AbstractListFragment;
import com.mediafever.R;
import com.mediafever.android.ui.Reloadable;
import com.mediafever.android.ui.watchable.WatchableAdapter;
import com.mediafever.domain.watchable.Watchable;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchablesListFragment extends AbstractListFragment<Watchable> implements Reloadable<Watchable> {
	
	private static final String WATCHABLES_EXTRA = "watchables";
	private List<Watchable> watchables;
	
	public WatchablesListFragment() {
	}
	
	public WatchablesListFragment(List<Watchable> watchables) {
		this.watchables = watchables;
		
		Bundle bundle = new Bundle();
		bundle.putSerializable(WATCHABLES_EXTRA, (Serializable)watchables);
		setArguments(bundle);
	}
	
	/**
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_fragment, container, false);
	}
	
	/**
	 * @see android.app.Fragment#onCreate(android.os.Bundle)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		Bundle args = getArguments();
		if (args != null) {
			watchables = (List<Watchable>)args.getSerializable(WATCHABLES_EXTRA);
		}
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		reload(watchables);
	}
	
	/**
	 * @see com.mediafever.android.ui.Reloadable#reload(java.util.List)
	 */
	@Override
	public void reload(List<Watchable> watchables) {
		this.watchables = watchables;
		setListAdapter(new WatchableAdapter(WatchablesListFragment.this.getActivity(), watchables));
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractListFragment#onItemSelected(java.lang.Object)
	 */
	@Override
	public void onItemSelected(Watchable watchable) {
		WatchableAdapter.onItemClick(this.getActivity(), watchable);
	}
}
