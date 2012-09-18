package com.mediafever.android.ui.watchable.details;

import java.util.List;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.jdroid.android.adapter.BaseHolderArrayAdapter;
import com.mediafever.R;
import com.mediafever.android.ui.watchable.details.SeasonAdapter.SeasonHolder;
import com.mediafever.domain.watchable.Season;

/**
 * 
 * @author Maxi Rosson
 */
public class SeasonAdapter extends BaseHolderArrayAdapter<Season, SeasonHolder> {
	
	public SeasonAdapter(Activity context, List<Season> items) {
		super(context, items, android.R.layout.simple_spinner_item);
	}
	
	@Override
	protected void fillHolderFromItem(Season season, SeasonHolder holder) {
		holder.text1.setText(getContext().getString(R.string.season, season.getSeasonNumber().toString()));
	}
	
	@Override
	protected SeasonHolder createViewHolderFromConvertView(View convertView) {
		SeasonHolder holder = new SeasonHolder();
		holder.text1 = findView(convertView, android.R.id.text1);
		return holder;
	}
	
	public static class SeasonHolder {
		
		protected TextView text1;
	}
	
}
