package com.dcy.psychology.view;

import java.util.ArrayList;

import com.dcy.psychology.R;
import com.dcy.psychology.util.ThoughtReadingUtils.QuestionType;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class QuestionView extends RelativeLayout{

	private Context mContext;
	private QuestionType mQuestionType;
	private Resources mResources;

	private final String TAG_SINGLE = "com.dcy.psychology.view.single.choose";
	private final String TAG_MULTIPLE = "com.dcy.psychology.view.multiple.choose";
	private final String TAG_CHECKBOX = "com.dcy.psychology.view.checkbox";
	
	private ArrayList<Integer> pointList;
	private OnCheckedChangeListener mCheckedChangeListener;
	private boolean isDnaTest = false;
	
	public QuestionView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mResources = context.getResources();
	}

	public QuestionView(Context context) {
		this(context, null);
	}

	public void setQuestionType(QuestionType questonType) {
		mQuestionType = questonType;
	}
	
	public void setOnCheckedListener(OnCheckedChangeListener listener){
		this.mCheckedChangeListener = listener;
	}
	
	public void setIsDna(){
		isDnaTest = true;
	}
	
	public void setDate(int index, String title, ArrayList<String> optionList) {
		LinearLayout questionLayout = new LinearLayout(mContext);
		questionLayout.setOrientation(LinearLayout.VERTICAL);
		questionLayout.setGravity(Gravity.CENTER);
		TextView titleView = new TextView(mContext);
		titleView.setText("        " + index + "." + title);
		titleView.setLineSpacing(0, 1.2f);// (add , mul)
		// titleView.setTextScaleX(0.5f);
		int fontSize = mResources.getDimensionPixelSize(R.dimen.txt_size_20);
		titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
		questionLayout.addView(titleView);
		android.view.ViewGroup.LayoutParams buttonParams = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 
				mResources.getDimensionPixelSize(R.dimen.title_height));
		LinearLayout.LayoutParams dnaParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		dnaParams.topMargin = mResources.getDimensionPixelSize(R.dimen.title_height);
		RadioGroup.LayoutParams rightParams = new RadioGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		rightParams.leftMargin = mResources.getDimensionPixelSize(R.dimen.title_height);
		int itemColor = getResources().getColor(R.color.color_orange_gray_selector);
		switch (mQuestionType) {
		case Type_Grow:
		case Type_Single:
			RadioGroup radioGroup = new RadioGroup(mContext);
			radioGroup.setTag(TAG_SINGLE);
			if(mCheckedChangeListener != null){
				radioGroup.setOnCheckedChangeListener(mCheckedChangeListener);
			}
			if(isDnaTest){
				radioGroup.setOrientation(LinearLayout.HORIZONTAL);
				radioGroup.setGravity(Gravity.CENTER);
				radioGroup.setLayoutParams(dnaParams);
			}
			for (int i = 0; i < optionList.size(); i++) {
				RadioButton rb = new RadioButton(mContext);
				rb.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
				if(isDnaTest){
					rb.setButtonDrawable(null);
					rb.setText(i == 0 ? R.string.yes : R.string.no);
					rb.setGravity(Gravity.CENTER);
					rb.setTextColor(itemColor);
					rb.setBackgroundResource(R.drawable.bg_circle_check_selector);
					if(i == 1){
						rb.setLayoutParams(rightParams);
					}
				} else {
					rb.setText(optionList.get(i));
					rb.setLayoutParams(buttonParams);
				}
				radioGroup.addView(rb);
			}
			questionLayout.addView(radioGroup);
			break;
		case Type_Multiple:
			LinearLayout mulOptionLayout = new LinearLayout(mContext);
			mulOptionLayout.setOrientation(LinearLayout.VERTICAL);
			mulOptionLayout.setTag(TAG_MULTIPLE);
			for (int i = 0; i < optionList.size(); i++) {
				LinearLayout itemLayout = new LinearLayout(mContext);
				itemLayout.setOrientation(LinearLayout.HORIZONTAL);
				CheckBox itemBox = new CheckBox(mContext);
				itemBox.setTag(TAG_CHECKBOX);
				TextView itemText = new TextView(mContext);
				itemText.setText(optionList.get(i));
				itemLayout.addView(itemBox);
				itemLayout.addView(itemText);
				mulOptionLayout.addView(itemLayout);
			}
			questionLayout.addView(mulOptionLayout);
			break;
		default:
			break;
		}
		this.addView(questionLayout);
	}

	public void setDate(int index, String title, ArrayList<String> optionList , ArrayList<Integer> pointList){
		this.pointList = pointList;
		this.setDate(index, title, optionList);
	}

	public int getPoint(){
		RadioGroup radioGroup = (RadioGroup) findViewWithTag(TAG_SINGLE);
		for(int i = 0 ; i < radioGroup.getChildCount() ; i++){
			if(((RadioButton)radioGroup.getChildAt(i)).isChecked())
				return pointList.get(i);
		}
		return 0;
	}
	
	public String getAnswerString(){
		switch (mQuestionType) {
		case Type_Single:
			RadioGroup radioGroup = (RadioGroup) findViewWithTag(TAG_SINGLE);
			for(int i = 0 ; i < radioGroup.getChildCount() ; i++){
				if(((RadioButton)radioGroup.getChildAt(i)).isChecked()){
					return String.format("%c", Character.toUpperCase(65+i));
				}
			}
			break;
		case Type_Multiple:
			StringBuffer sb = new StringBuffer();
			LinearLayout mulAnswer = (LinearLayout) findViewWithTag(TAG_MULTIPLE);
			for(int i = 0 ; i < mulAnswer.getChildCount() ; i++){
				if(((CheckBox)mulAnswer.getChildAt(i).findViewWithTag(TAG_CHECKBOX)).isChecked())
					sb.append(String.format("%c", Character.toUpperCase(65+i))).append(" ");
			}
			return sb.toString().trim();
		case Type_Grow:
			break;
		default:
			break;
		}
		return null;
	}
	
	public boolean hasChooseAnswer(){
		RadioGroup radioGroup = (RadioGroup) findViewWithTag(TAG_SINGLE);
		if(radioGroup == null){
			return false;
		}
		return radioGroup.getCheckedRadioButtonId() != -1;
	}
}
