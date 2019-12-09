package yess.barmimass.csabilusta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.csabilusta.R;

import java.util.ArrayList;

public class KeresoAdapter  extends ArrayAdapter<String> {



    public KeresoAdapter(Context context, ArrayList<String> words) {
        super(context, 0, words);

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }


        String currentWord = getItem(position);

        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.default_text_view);

        defaultTextView.setText(currentWord);



        View textContainer = listItemView.findViewById(R.id.text_container);





        return listItemView;
    }
}

