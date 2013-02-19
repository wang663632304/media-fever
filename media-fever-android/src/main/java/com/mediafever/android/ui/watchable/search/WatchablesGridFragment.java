package com.mediafever.android.ui.watchable.search;

import java.io.Serializable;
import java.util.List;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jdroid.android.fragment.AbstractGridFragment;
import com.mediafever.R;
import com.mediafever.android.ui.Reloadable;
import com.mediafever.android.ui.watchable.WatchableAdapter;
import com.mediafever.android.ui.watchable.details.WatchableActivity;
import com.mediafever.domain.watchable.Watchable;

/**
 * 
 * @author Maxi Rosson
 */
public class WatchablesGridFragment extends AbstractGridFragment<Watchable> implements Reloadable<Watchable> {
	
	private static final String WATCHABLES_EXTRA = "watchables";
	private List<Watchable> watchables;
	
	public static WatchablesGridFragment instance(List<Watchable> watchables) {
		WatchablesGridFragment fragment = new WatchablesGridFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(WATCHABLES_EXTRA, (Serializable)watchables);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	/**
	 * @see android.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		watchables = getArgument(WATCHABLES_EXTRA);
	}
	
	/**
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.grid_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractGridFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		reload(watchables);
	}
	
	/**
	 * @see com.mediafever.android.ui.Reloadable#reload(java.util.List)
	 */
	@Override
	public void reload(List<Watchable> watchables) {
		this.watchables = watchables;
		setListAdapter(new WatchableAdapter(WatchablesGridFragment.this.getActivity(), watchables));
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractGridFragment#onItemSelected(java.lang.Object)
	 */
	@Override
	public void onItemSelected(Watchable watchable) {
		WatchableActivity.start(getActivity(), watchable);
	}
}
