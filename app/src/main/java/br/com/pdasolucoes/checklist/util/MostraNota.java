package br.com.pdasolucoes.checklist.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.plattysoft.leonids.ParticleSystem;

import java.util.ArrayList;

import checklist.pdasolucoes.com.br.checklist.R;

/**
 * Created by PDA on 09/08/2017.
 */

public class MostraNota {

    public static View MostraNotaView(final Context context, float nota) {

        View view = View.inflate(context, R.layout.view_nota, null);
        Button btOk = (Button) view.findViewById(R.id.btOk);
        PieChart chart = (PieChart) view.findViewById(R.id.pieChart);
        createPie(chart, nota);

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) context;
                activity.finish();
            }
        });

        return view;
    }

    private static void setData(int count, float range, PieChart mChart, float valor) {

        ArrayList<PieEntry> values = new ArrayList<PieEntry>();


        //Nessa parte ele irá setar a porcentagem e colocará a legenda embaixo
        for (int i = 0; i < count; i++) {
            values.add(new PieEntry(valor));
            valor = range - valor;
        }

        PieDataSet dataSet = new PieDataSet(values, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(new int[]{rgb("#50c732"), rgb("#ff0000")});

        PieData data = new PieData(dataSet);
        data.setDrawValues(false);
        mChart.setData(data);

        mChart.invalidate();
    }

    public static PieChart createPie(PieChart mChart, float nota) {

        mChart.setBackgroundColor(Color.WHITE);

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);


        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(true);

        mChart.setMaxAngle(180f); // HALF CHART
        mChart.setRotationAngle(180f);

        Legend legend = mChart.getLegend();
        legend.setEnabled(false);

        mChart.setCenterTextSize(30);
        mChart.setCenterTextColor(R.color.colorPrimary);
        mChart.setCenterTextOffset(0, 10);

        String formatIndicadorGeral = String.format("%.0f", nota);
        mChart.setCenterText(formatIndicadorGeral + "% Indicador Geral");


        setData(2, 100, mChart, nota);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);


        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setDrawEntryLabels(false);
        mChart.setDrawSliceText(false);
        mChart.setEntryLabelTextSize(12f);

        return mChart;
    }

    public static int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }
}
