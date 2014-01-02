package org.xmpp.client.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import pro.chinasoft.activity.R;
import pro.chinasoft.model.InSmiley;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;

public class FaceConversionUtil {
	
	private static FaceConversionUtil mFaceConversionUtil;
	private FaceConversionUtil() {

	}

	public static FaceConversionUtil getInstace() {
		if (mFaceConversionUtil == null) {
			mFaceConversionUtil = new FaceConversionUtil();
		}
		return mFaceConversionUtil;
	}


	
	public List<InSmiley> parseData(int start,int end, Context context) {
		List<InSmiley> list =new ArrayList<InSmiley>();
		try {
			for (int i=start;i<end;i++) {
				String fileName="smiley_"+i;
				int resID = context.getResources().getIdentifier(fileName,
						"drawable", context.getPackageName());
				if (resID != 0) {
					InSmiley smileyEntry= new InSmiley();
					smileyEntry.setId(resID);
					smileyEntry.setCharacter("["+fileName+"]");
					smileyEntry.setFaceName(fileName);
					list.add(smileyEntry);
				}
			}
			InSmiley smileyEntry= new InSmiley();
			smileyEntry.setId(R.drawable.face_del_icon);
			list.add(smileyEntry);
		} catch (Exception e) {
			return null;
		}
		return list;
	}
	
	public SpannableString addFace(Context context, int imgId,
			String spannableString) {
		if (TextUtils.isEmpty(spannableString)) {
			return null;
		}
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				imgId);
		bitmap = Bitmap.createScaledBitmap(bitmap, 35, 35, true);
		ImageSpan imageSpan = new ImageSpan(context, bitmap);
		SpannableString spannable = new SpannableString(spannableString);
		spannable.setSpan(imageSpan, 0, spannableString.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}
	
	public SpannableString getExpressionString(Context context, String str) {
		SpannableString spannableString = new SpannableString(str);
		System.out.println(str+" here is getExpressionString");
		// 正则表达式比配字符串里是否含有表情，如： 我好[开心]啊
		String zhengze = "\\[[^\\]]+\\]";
		// 通过传入的正则表达式来生成一个pattern
		Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
		try {
			dealExpression(context, spannableString, sinaPatten, 0);
		} catch (Exception e) {
			Log.e("dealExpression", e.getMessage());
		}
		return spannableString;
	}
	
	private void dealExpression(Context context,
			SpannableString spannableString, Pattern patten, int start)
			throws Exception {
		Matcher matcher = patten.matcher(spannableString);
		while (matcher.find()) {
			String key = matcher.group();
			key=key.replace("[", "").replace("]", "");
			System.out.println(key+" key:");
			// 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
			if (matcher.start() < start) {
				continue;
			}
			if (TextUtils.isEmpty(key)) {
				continue;
			}
			int resId = context.getResources().getIdentifier(key, "drawable",
					context.getPackageName());
			// 通过上面匹配得到的字符串来生成图片资源id
			// Field field=R.drawable.class.getDeclaredField(value);
			// int resId=Integer.parseInt(field.get(null).toString());
			if (resId != 0) {
				Bitmap bitmap = BitmapFactory.decodeResource(
						context.getResources(), resId);
				bitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
				// 通过图片资源id来得到bitmap，用一个ImageSpan来包装
				ImageSpan imageSpan = new ImageSpan(bitmap);
				// 计算该图片名字的长度，也就是要替换的字符串的长度
				int end = matcher.start() + key.length()+2;
				// 将该图片替换字符串中规定的位置中
				spannableString.setSpan(imageSpan, matcher.start(), end,
						Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				if (end < spannableString.length()) {
					// 如果整个字符串还未验证完，则继续。。
					dealExpression(context, spannableString, patten, end);
				}
				break;
			}
		}
	}

}