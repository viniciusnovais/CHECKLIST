package br.com.pdasolucoes.checklist.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.com.pdasolucoes.checklist.model.Form;
import br.com.pdasolucoes.checklist.model.FormItem;
import checklist.pdasolucoes.com.br.checklist.R;

/**
 * Created by PDA on 04/11/2016.
 */

public final class QueryAdapter extends RecyclerView.Adapter<QueryAdapter.MyViewHolder> {
    private Context context;
    private Typeface tf;
    private List<FormItem> lista;
    private LayoutInflater mLayoutInflater;
    private ItemClick itemClick;

    public interface ItemClick {
        void Click(int position);
    }

    public void ItemClickListener(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public QueryAdapter(List<FormItem> lista, Context context) {
        this.lista = lista;
        this.context = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tf = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.query_list_item, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);

        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tvId.setText(String.valueOf(lista.get(position).getIdItem()));
        holder.tvIdForm.setText(lista.get(position).getIdForm().getNomeFom());

        if (lista.get(position).getStatus() == 0) {
            holder.imageView.setImageResource(R.drawable.exclamation);
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public Object getItem(int position) {
        return lista.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvId, tvIdForm;
        public ImageView imageView;


        public MyViewHolder(View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.tvIdFormItem);
            tvIdForm = (TextView) itemView.findViewById(R.id.tvNomeForm);
            imageView = (ImageView) itemView.findViewById(R.id.imageStatus);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClick.Click(getAdapterPosition());
        }
    }

}
