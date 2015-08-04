package com.dcy.psychology.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dcy.psychology.R;
import com.dcy.psychology.model.QuestionModel;
import com.dcy.psychology.view.QuestionView;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ThoughtReadingUtils {
	public static enum QuestionType {
		Type_Single, Type_Multiple, Type_Grow
	}

	public static final int QuestionNum = 5;
	public static final String RightAnswerKey = "com.dcy.psychology.rightAnswer";
	public static final String MineAnswerKey = "com.dcy.psychology.wrongAnswer";
	public static final String QuestionAnswer = "com.dcy.psychology.question.answer";
	public static final String QuestionModelList = "com.dcy.psychology.question.model.list";
	public static final String ThemeTitle = "com.dcy.psychology.themetitle";
	public static final String ThemeIndex = "com.dcy.psychology.themeindex";
	
	//special test
	public static final String IsThoughtReadingMode = "com.dcy.psychology.isThoughtReading";
	public static final String IsSpecialMode = "com.dcy.psychology.isspecial";
	public static final String GrowThemeIndex = "com.dcy.psychology.growthemeindex";
	public static final String GrowGroupIndex = "com.dcy.psychology.growgroupindex";
	
	//homePage test
	public static final String GrowBeanData = "com.dcy.psychology.GrowBeanData";
	public static final String DNATest = "com.dcy.psychology.DNATest";
	public static final String PointResult = "com.dcy.psychology.PointResult";
	
	public static ArrayList<QuestionModel> getRandomList(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray dataArray = new JSONArray(jsonObject.getString("data"));
			int length = dataArray.length();
			JSONObject themeJSON = dataArray
					.getJSONObject((int) (Math.random() * length));
			return getQuestionList(themeJSON.getString("question"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<QuestionModel>();
	}

	public static ArrayList<QuestionModel> getThemeList(String json , int index){
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONObject themeJSON = (new JSONArray(jsonObject.getString("data"))).
					getJSONObject(index);
			return getQuestionList(themeJSON.getString("question"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static ArrayList<QuestionModel> getQuestionList(String jsonArray) {
		ArrayList<QuestionModel> questionList = new ArrayList<QuestionModel>();
		try {
			JSONArray questionArray = new JSONArray(jsonArray);
			int length = questionArray.length();
			ArrayList<Integer> randomList = new ArrayList<Integer>();
			for (int i = 0; i < QuestionNum; i++) {
				int random = (int) (Math.random() * length);
				while (randomList.contains(random))
					random = (int) (Math.random() * length);
				randomList.add(random);
				questionList.add(getItemQuestion(questionArray
						.getJSONObject(random)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return questionList;
	}

	private static QuestionModel getItemQuestion(JSONObject questionJSON)
			throws JSONException {
		QuestionModel itemModel = new QuestionModel();
		itemModel.setQuestionId(questionJSON.getInt("id"));
		itemModel.setQuestionType("single".equals(questionJSON
				.getString("type")) ? QuestionType.Type_Single
				: QuestionType.Type_Multiple);
		itemModel.setQuestionTitle(questionJSON.getString("title"));
		JSONArray optionJSON = new JSONArray(questionJSON.getString("option"));
		ArrayList<String> optionList = new ArrayList<String>();
		for (int i = 0; i < optionJSON.length(); i++)
			optionList.add(optionJSON.getString(i));
		itemModel.setOptionList(optionList);
		itemModel.setAnswer(questionJSON.getString("answer"));
		itemModel.setExplain(questionJSON.getString("explain"));
		return itemModel;
	}

	public static class QuestionAdapter extends PagerAdapter {
		private ArrayList<QuestionView> questionList;

		public QuestionAdapter(ArrayList<QuestionView> dataList) {
			questionList = dataList;
		}

		@Override
		public int getCount() {
			return questionList.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(questionList.get(position));
			return questionList.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(questionList.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	public static class QuestionResultAdapter extends BaseAdapter {
		private Context mContext;
		private List<HashMap<String, String>> dataList;
		private LayoutInflater mInflater;
		private Resources mResources;
		private ArrayList<QuestionModel> mQuestionModels;

		public QuestionResultAdapter(Context context,
				List<HashMap<String, String>> dataList) {
			mContext = context;
			this.dataList = dataList;
			mInflater = LayoutInflater.from(context);
			mResources = mContext.getResources();
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return dataList.get(arg0);
		}

		public void setQuestionModels(ArrayList<QuestionModel> models) {
			mQuestionModels = models;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View view, ViewGroup pareent) {
			Holder mHolder;
			if (view == null) {
				view = mInflater.inflate(R.layout.question_answer_item, null);
				mHolder = new Holder();
				mHolder.indexText = (TextView) view.findViewById(R.id.question_index_tv);
				mHolder.resultView = (ImageView) view.findViewById(R.id.question_answer_tv);
				mHolder.rightAnswer = (TextView) view.findViewById(R.id.right_answer_tv);
				mHolder.mineAnswer = (TextView) view.findViewById(R.id.mine_answer_tv);
				mHolder.reasonImage = (ImageView) view.findViewById(R.id.answer_explain_iv);
				view.setTag(mHolder);
			} else {
				mHolder = (Holder) view.getTag();
			}
//			mHolder.indexText.setText(String.format(
//							mResources.getString(R.string.question_index),position + 1));
			mHolder.indexText.setText(String.valueOf(position+1));
			HashMap<String, String> answerMap = dataList.get(position);
			String rightAnswer = answerMap.get(RightAnswerKey);
			String mineAnswer = answerMap.get(MineAnswerKey);
//			mHolder.rightAnswer.setText(String.format(
//					mResources.getString(R.string.answer_right), rightAnswer));
			mHolder.rightAnswer.setText(rightAnswer);
			mHolder.mineAnswer.setText(":" + mineAnswer);
			boolean isRight = rightAnswer.trim().equals(mineAnswer) || rightAnswer.trim().equals(mineAnswer.replace(",", ""));
			mHolder.resultView.setImageResource(isRight? 
					R.drawable.icon_right : R.drawable.icon_wrong);
			mHolder.reasonImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if (mQuestionModels == null)
						return;
					QuestionModel mItemModel = mQuestionModels.get(position);
					View dialogView = mInflater.inflate(R.layout.question_explain_dialog, null);
					((TextView)dialogView.findViewById(R.id.dialog_title)).setText(mItemModel.getQuestionTitle());
					((TextView)dialogView.findViewById(R.id.dialog_explain)).setText(mItemModel.getExplain());
					Builder mBuilder = new Builder(mContext);
					mBuilder.setView(dialogView);
					AlertDialog dialog = mBuilder.create();
					dialog.setCanceledOnTouchOutside(true);
					dialog.show();
				}
			});
			return view;
		}

		private class Holder {
			TextView indexText;
			ImageView resultView;
			TextView rightAnswer, mineAnswer;
			ImageView reasonImage;
		}

	}
}
