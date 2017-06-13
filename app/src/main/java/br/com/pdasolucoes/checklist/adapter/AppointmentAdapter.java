package br.com.pdasolucoes.checklist.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import br.com.pdasolucoes.checklist.activities.AppointmentActivity;
import br.com.pdasolucoes.checklist.activities.StartActivity;
import br.com.pdasolucoes.checklist.model.Form;
import checklist.pdasolucoes.com.br.checklist.R;

/**
 * Created by PDA on 04/11/2016.
 */

public final class AppointmentAdapter extends BaseAdapter{
    private List<Form> listaForm;
    private Context context;
    private Typeface tf;

    public AppointmentAdapter(List<Form> listaForm, Context context) {
        this.listaForm = listaForm;
        this.context = context;
        this.tf = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    public int getCount() {
        return listaForm.size();
    }

    @Override
    public Object getItem(int position) {
        return listaForm.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v= View.inflate(context,R.layout.appointment_item_listview,null);


            TextView tvDayNumber = (TextView) v.findViewById(R.id.tvDayNumber);

            String number = listaForm.get(position).getDataForm();
            if (number.contains("09")) {
                number = "09";
            }
            tvDayNumber.setText(number);
            tvDayNumber.setTypeface(tf);


            TextView tvDayWeek = (TextView) v.findViewById(R.id.tvDayWeek);
            String dayWeek = listaForm.get(position).getDataForm();
            if (dayWeek.contains("08")) {
                dayWeek = "SEG";
            }
            tvDayWeek.setText(dayWeek);
            tvDayWeek.setTypeface(tf);
            TextView tvLocation = (TextView) v.findViewById(R.id.tvLocation);
            tvLocation.setText("#" + listaForm.get(position).getNomeLoja());
            tvLocation.setTypeface(tf);
            TextView tvHour = (TextView) v.findViewById(R.id.tvHour);
            tvHour.setText(listaForm.get(position).getHora() + " | ");
            tvHour.setTypeface(tf);
            TextView tvForms = (TextView) v.findViewById(R.id.tvForm);
            tvForms.setText(listaForm.get(position).getNomeFom());
            tvForms.setTypeface(tf);

            v.setTag(listaForm.get(position).getIdForm());

        return v;
    }



}
