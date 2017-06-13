package br.com.pdasolucoes.checklist.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

import br.com.pdasolucoes.checklist.model.Form;
import checklist.pdasolucoes.com.br.checklist.R;

/**
 * Created by PDA on 08/02/2017.
 */

public class SpinnerAdapterForm extends ArrayAdapter<Form> {

    private Context context;
    private List<Form> values;

    public SpinnerAdapterForm(Context context, int textViewResourceId, List<Form> values) {
        super(context,textViewResourceId,values);
        this.context=context;
        this.values=values;
    }

    public int getCount(){
        return values.size();
    }

    public Form getItem(int position){
        return values.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(context, R.layout.spinner_item_start,null);
        TextView text = (TextView) v.findViewById(R.id.text1);

        text.setText(values.get(position).getNomeFom());

        return text;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.spinner_item_start,null);
        TextView label = (TextView) v.findViewById(R.id.text1);

        label.setText(values.get(position).getNomeFom());

        return label;
    }
}
