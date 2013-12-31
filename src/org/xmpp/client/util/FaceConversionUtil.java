package org.xmpp.client.util;

import java.util.ArrayList;
import java.util.List;


import pro.chinasoft.activity.R;
import pro.chinasoft.model.InSmiley;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

/**
 * 
 ****************************************** 
 * @author 廖乃波
 * @文件名称 : FaceConversionUtil.java
 * @创建时间 : 2013-1-27 下午02:34:09
 * @文件描述 : 表情轉換工具
 ****************************************** 
 */
public class FaceConversionUtil {
	
	private static FaceConversionUtil mFaceConversionUtil;
	private List<InSmiley> emojis = new ArrayList<InSmiley>();
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
					smileyEntry.setCharacter(fileName);
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
}