package br.com.pdasolucoes.checklist.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.pdasolucoes.checklist.activities.SincActivity;
import br.com.pdasolucoes.checklist.model.Form;
import br.com.pdasolucoes.checklist.model.FormItem;
import br.com.pdasolucoes.checklist.model.Sync;
import br.com.pdasolucoes.checklist.util.WebService;
import checklist.pdasolucoes.com.br.checklist.R;

/**
 * Created by PDA on 21/11/2016.
 */

public class SyncAdapter extends BaseAdapter {

    private Context context;
    private List<FormItem> lista;
    private int[] vetorPosition;
    private RelativeLayout relativeLayout;

    private static ItemClickListener itemClickListener;

    public interface ItemClickListener {
        void onItemClick(int[] vetorInt);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public SyncAdapter(Context context, List<FormItem> lista) {
        this.context = context;
        this.lista = lista;
        vetorPosition = new int[getCount()];
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.sinc_item_listview2, null);

        CheckBox check = (CheckBox) v.findViewById(R.id.checkbox);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageInfo);
        relativeLayout = (RelativeLayout) v.findViewById(R.id.relative1);

        check.setVisibility(View.GONE);

        if (lista.get(position).getStatus() == 1) {
            imageView.setImageResource(R.drawable.checked);
        }

        TextView tvLocation = (TextView) v.findViewById(R.id.tvLocation);
        tvLocation.setText(lista.get(position).getIdForm().getNomeLoja() + "");

        TextView tvHour = (TextView) v.findViewById(R.id.tvHour);
        tvHour.setText(lista.get(position).getIdForm().getHora() + "");

        TextView tvForm = (TextView) v.findViewById(R.id.tvForm);
        tvForm.setText(lista.get(position).getIdForm().getNomeFom() + "");

        TextView tvData = (TextView) v.findViewById(R.id.tvDayNumber);
        tvData.setText(lista.get(position).getIdForm().getDataForm() + "");


        if (SincActivity.POSITION == 1) {
            check.setVisibility(View.VISIBLE);

        } else if (SincActivity.POSITION == 2) {
            check.setVisibility(View.VISIBLE);
            check.setChecked(true);
            for (int i = 0; i < vetorPosition.length; i++) {
                vetorPosition[i] = 1;
            }
            itemClickListener.onItemClick(vetorPosition);
        }


        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    vetorPosition[position] = 1;
                } else {
                    vetorPosition[position] = 0;
                }
                itemClickListener.onItemClick(vetorPosition);
            }
        });

        return v;
    }


}
