package pro.chinasoft.listener;

import org.xmpp.client.util.FaceConversionUtil;

import pro.chinasoft.activity.InChatActivity;
import pro.chinasoft.activity.R;
import pro.chinasoft.model.InSmiley;
import android.content.Context;
import android.text.SpannableString;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

public class SmileyOnItemClickListener implements AdapterView.OnItemClickListener{

	public SmileyOnItemClickListener(){
		
	}
	
	private InChatActivity context;
	public SmileyOnItemClickListener(Context context) {
		this.context=(InChatActivity) context;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long row) {
		EditText editText = (EditText) context.findViewById(R.id.et_sendmessage);
		if (position==20) {
			int selection = editText.getSelectionStart();
			String text = editText.getText().toString();
			if (selection > 0) {
				String text2 = text.substring(selection - 1);
				if ("]".equals(text2)) {
					int start = text.lastIndexOf("[");
					int end = selection;
					editText.getText().delete(start, end);
					return;
				}
				editText.getText().delete(selection - 1, selection);
			}
		}else{
			InSmiley smiley=(InSmiley) adapterView.getItemAtPosition(position);
			SpannableString spannableString = FaceConversionUtil.getInstace().addFace(context, smiley.getId(), smiley.getCharacter());
			editText.append(spannableString);
		}
		
	}

}
