package com.dcy.psychology.view;

import android.content.Context;
import android.widget.TextView;

import com.dcy.psychology.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.Utils;

public class MyMarkerView extends MarkerView {
	private TextView tvContent;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tvContent);
    }
	
	@Override
	public void refreshContent(Entry e, int dataSetIndex) {
		if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
            tvContent.setText("" + Utils.formatNumber(e.getVal(), 0, true));
        }
	}

	@Override
	public int getXOffset() {
		return -(getWidth() / 2);
	}

	@Override
	public int getYOffset() {
		return -getHeight();
	}

}
