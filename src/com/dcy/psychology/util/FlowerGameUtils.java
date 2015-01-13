package com.dcy.psychology.util;

import com.dcy.psychology.R;

public class FlowerGameUtils {

	public static final String PetalShape = "com.dcy.psychology.flower.petalshape";
	public static final String TimeLevel = "com.dcy.psychology.flower.timelevel";
	public static final String ColorLevel = "com.dcy.psychology.flower.colorlevel";
	public static final String AnswerOrder = "com.dcy.psychology.flower.answerorder";
	public static final String MineOrder = "com.dcy.psychology.flower.mineorder";

	public static int[][] levelFlower = {
			{ R.drawable.flower_one_01, R.drawable.flower_one_02,
					R.drawable.flower_one_03, R.drawable.flower_one_04,
					R.drawable.flower_one_05, R.drawable.flower_one_06,
					R.drawable.flower_one_07, R.drawable.flower_one_08,
					R.drawable.flower_one_09, R.drawable.flower_one_10 },
			{ R.drawable.flower_two_01, R.drawable.flower_two_02,
					R.drawable.flower_two_03, R.drawable.flower_two_04,
					R.drawable.flower_two_05, R.drawable.flower_two_06,
					R.drawable.flower_two_07, R.drawable.flower_two_08,
					R.drawable.flower_two_09, R.drawable.flower_two_10 } };

	public static int[][] flowerShape = {
			{ R.drawable.petal_shape_one_01, R.drawable.petal_shape_one_02,
					R.drawable.petal_shape_one_03,
					R.drawable.petal_shape_one_04 },
			{ R.drawable.petal_shape_two_01, R.drawable.petal_shape_two_02,
					R.drawable.petal_shape_two_03,
					R.drawable.petal_shape_two_04 } };
}
