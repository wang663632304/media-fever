package com.mediafever.android.ui.session;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.actionbarsherlock.view.MenuItem;
import com.jdroid.android.fragment.AbstractGridFragment;
import com.jdroid.java.utils.StringUtils;
import com.mediafever.R;
import com.mediafever.domain.session.MediaSelection;
import com.mediafever.domain.session.MediaSession;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSelectionsFragment extends AbstractGridFragment<MediaSelection> {
	
	public static final String MEDIA_SESSION_EXTRA = "mediaSessionExtra";
	
	private MediaSession mediaSession;
	
	public static MediaSelectionsFragment instance(Bundle bundle) {
		MediaSelectionsFragment fragment = new MediaSelectionsFragment();
		fragment.setArguments(bundle);
		return fragment;
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		mediaSession = getArgument(MEDIA_SESSION_EXTRA);
		
		setHasOptionsMenu(true);
	}
	
	/**
	 * @see android.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.media_selections_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractGridFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		// Header
		TextView header = findView(R.id.header);
		if (mediaSession.acceptOnlyMovies()) {
			header.setText(getString(R.string.mediaSelectionsHeader, getString(R.string.movie).toLowerCase()));
		} else if (mediaSession.acceptOnlySeries()) {
			header.setText(getString(R.string.mediaSelectionsHeader, getString(R.string.series).toLowerCase()));
		} else {
			header.setText(getString(R.string.mediaSelectionsHeader, getString(R.string.movieOrSeries)));
		}
		
		// Footer
		TextView footer = findView(R.id.footer);
		String dateTime = MediaSessionAdapter.getDateString(mediaSession);
		if (StringUtils.isNotEmpty(dateTime)) {
			footer.setText(getString(R.string.mediaSelectionsFooter, dateTime));
			footer.setVisibility(View.VISIBLE);
		} else {
			footer.setVisibility(View.GONE);
		}
		
		refresh();
	}
	
	/**
	 * @see com.jdroid.android.activity.BaseActivity#onOptionsItemSelected(com.actionbarsherlock.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.editMediaSessionItem:
				MediaSessionActivity.start(getActivity(), mediaSession.getId());
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.AbstractGridFragment#onItemSelected(java.lang.Object)
	 */
	@Override
	public void onItemSelected(MediaSelection mediaSelection) {
		if (mediaSelection.getWatchable() != null) {
			MediaSelectionDialogFragment.show(this, mediaSession, mediaSelection);
		} else {
			MediaSelectionPickerDialogFragment.show(this, mediaSession);
		}
	}
	
	public void addMediaSelection(MediaSelection mediaSelection) {
		mediaSession.addSelection(mediaSelection);
		refresh();
	}
	
	public void refresh() {
		setListAdapter(new MediaSelectionAdapter(MediaSelectionsFragment.this.getActivity(),
				mediaSession.getSelections()));
	}
}
