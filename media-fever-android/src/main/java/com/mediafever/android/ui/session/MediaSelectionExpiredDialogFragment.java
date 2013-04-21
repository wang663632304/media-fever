package com.mediafever.android.ui.session;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.jdroid.android.dialog.AbstractDialogFragment;
import com.mediafever.R;
import com.mediafever.android.ui.session.MediaSelectionAdapter.MediaSelectionHolder;
import com.mediafever.android.ui.watchable.details.WatchableActivity;
import com.mediafever.domain.session.MediaSelection;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSelectionExpiredDialogFragment extends AbstractDialogFragment {
	
	private static final String MEDIA_SELECTION_EXTRA = "mediaSelectionExtra";
	
	private MediaSelection mediaSelection;
	
	public static void show(Fragment fragment, MediaSelection mediaSelection) {
		FragmentManager fm = fragment.getActivity().getSupportFragmentManager();
		MediaSelectionExpiredDialogFragment dialogFragment = new MediaSelectionExpiredDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(MEDIA_SELECTION_EXTRA, mediaSelection);
		dialogFragment.setArguments(bundle);
		dialogFragment.show(fm, MediaSelectionExpiredDialogFragment.class.getSimpleName());
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mediaSelection = getArgument(MEDIA_SELECTION_EXTRA);
	}
	
	/**
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 *      android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.media_session_expired_dialog_fragment, container, false);
	}
	
	/**
	 * @see com.jdroid.android.dialog.AbstractDialogFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		getDialog().setTitle(R.string.mediaSessionWinner);
		
		MediaSelectionHolder holder = new MediaSelectionHolder();
		holder.name = findView(R.id.name);
		holder.image = findView(R.id.image);
		holder.addNew = findView(R.id.addNew);
		holder.userContainer = findView(R.id.userContainer);
		holder.userImage = findView(R.id.userImage);
		holder.fullName = findView(R.id.fullName);
		holder.thumbsUp = findView(R.id.thumbsUp);
		holder.thumbsUpIcon = findView(R.id.thumbsUpIcon);
		holder.thumbsDown = findView(R.id.thumbsDown);
		holder.thumbsDownIcon = findView(R.id.thumbsDownIcon);
		
		MediaSelectionAdapter.doFillHolderFromItem(mediaSelection, holder);
		
		findView(R.id.mediaSelection).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WatchableActivity.start(getActivity(), mediaSelection.getWatchable());
			}
		});
	}
	
}