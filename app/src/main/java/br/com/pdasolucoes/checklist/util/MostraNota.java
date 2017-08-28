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

    public static View MostraNotaView(Context context, float nota) {
        View view = View.inflate(context, R.layout.view_nota, null);

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.addView(createPie(context, nota));

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

    public static PieChart createPie(Context context, float nota) {

        PieChart mChart = new PieChart(context);

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

        mChart.setCenterTextSize(19);
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

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = 400;
        lp.height = 400;
        lp.gravity = Gravity.CENTER;
        mChart.setLayoutParams(lp);

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
