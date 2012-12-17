package com.mediafever.android.ui.session;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jdroid.android.fragment.AbstractGridFragment;
import com.mediafever.R;
import com.mediafever.domain.session.MediaSelection;
import com.mediafever.usecase.MediaSessionSetupUseCase;

/**
 * 
 * @author Maxi Rosson
 */
public class MediaSelectionsFragment extends AbstractGridFragment<MediaSelection> {
	
	/**
	 * @see com.jdroid.android.fragment.AbstractFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
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
		setListAdapter(new MediaSelectionAdapter(MediaSelectionsFragment.this.getActivity(),
				getMediaSessionSetupUseCase().getSelections()));
	}
	
	public MediaSessionSetupUseCase getMediaSessionSetupUseCase() {
		return ((MediaSessionActivity)getActivity()).getMediaSessionSetupUseCase();
	}
	
}
