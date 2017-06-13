package br.com.pdasolucoes.checklist.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

import br.com.pdasolucoes.checklist.adapter.AppointmentAdapter;
import br.com.pdasolucoes.checklist.adapter.QueryAdapter;
import br.com.pdasolucoes.checklist.adapter.Question2Adapter;
import br.com.pdasolucoes.checklist.dao.FormDao;
import br.com.pdasolucoes.checklist.dao.FormItemDao;
import br.com.pdasolucoes.checklist.dao.OpcaoRespostaDao;
import br.com.pdasolucoes.checklist.dao.PerguntaDao;
import br.com.pdasolucoes.checklist.dao.RespostaDao;
import br.com.pdasolucoes.checklist.model.Form;
import br.com.pdasolucoes.checklist.model.FormItem;
import br.com.pdasolucoes.checklist.model.OpcaoResposta;
import br.com.pdasolucoes.checklist.model.Pergunta;
import br.com.pdasolucoes.checklist.model.Resposta;
import checklist.pdasolucoes.com.br.checklist.R;

/**
 * Created by PDA on 01/11/2016.
 */

public class QueryActivity extends AppCompatActivity {

    private PieChart mChart;
    private ImageButton imageButtonAppointment,imageButtonTodo,imageButtonLogout;
    private TextView tvText, tvNomeForm, tvRfe, tvRde;
    private RespostaDao dao;
    private Button btAbrir;
    private String nomeForm;
    private PerguntaDao perguntaDao;
    private float resultado=0;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        setContentView(R.layout.query_activity);

        nomeForm=getIntent().getExtras().getString("nomeForm");

        btAbrir= (Button) findViewById(R.id.buttonAbrir);
        tvText= (TextView) findViewById(R.id.tvText);
        tvNomeForm=(TextView)findViewById(R.id.tvNomeFormAction);
        tvRfe = (TextView) findViewById(R.id.tvRfe);
        tvRde = (TextView) findViewById(R.id.tvRde);

        dao=new RespostaDao(this);
        perguntaDao=new PerguntaDao(this);

        tvNomeForm.setText(nomeForm);

        resultado=Float.parseFloat(String.valueOf(dao.contadorResposta(getIntent().getExtras().getInt("idItem"))))/
                Float.parseFloat(String.valueOf(perguntaDao.QtdePergunta(getIntent().getExtras().getInt("idForm"))));
        resultado=resultado*100;
        String porcentFormat = String.format("%.2f",resultado);
        tvText.setText(porcentFormat+"% Concluído");

        mChart= (PieChart) findViewById(R.id.chart);
        createPie(mChart);
        imageButtonAppointment= (ImageButton) findViewById(R.id.appointmentBook);
        imageButtonTodo= (ImageButton) findViewById(R.id.toDo);
        imageButtonLogout= (ImageButton) findViewById(R.id.logout);

        tvRfe.setText(dao.respostaNaoConforme(getIntent().getExtras().getInt("idItem"))+"");
        tvRde.setText(dao.respostaConforme(getIntent().getExtras().getInt("idItem"))+"");

        imageButtonAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QueryActivity.this, AppointmentActivity.class);
                startActivity(i);
                finish();
            }
        });
        imageButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QueryActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

        imageButtonTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QueryActivity.this, FrameTodoActivity.class);
                startActivity(i);
                finish();
            }
        });


        btAbrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QueryActivity.this, QuestionsActivity.class);
                i.putExtra("form",nomeForm);
                i.putExtra("ItemId",getIntent().getExtras().getInt("idItem"));
                i.putExtra("idForm",getIntent().getExtras().getInt("idForm"));
                i.putExtra("QueryActivity","QueryActivity");
                startActivity(i);
                finish();
            }
        });

    }

    private void setData(int count, float range, PieChart mChart) {

        ArrayList<PieEntry> values = new ArrayList<PieEntry>();


        //Nessa parte ele irá setar a porcentagem e colocará a legenda embaixo
        for (int i = 0; i < count; i++) {
            values.add(new PieEntry((float) ((Math.random() * range) + range / 5)));

//            values.add(new PieEntry(10));
//            values.add(new PieEntry());
//            values.add(new PieEntry());
//            values.add(new PieEntry());
        }

        PieDataSet dataSet = new PieDataSet(values,"");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        //data.setValueFormatter(new PercentFormatter());
        //data.setValueTextSize(11f);
        //data.setValueTextColor(Color.WHITE);
        //data.setValueTypeface(mTfLight);
        data.setDrawValues(false);
        mChart.setData(data);

        mChart.invalidate();
    }

    private void moveOffScreen(PieChart mChart) {

        Display display = getWindowManager().getDefaultDisplay();
        int height = display.getHeight();  // deprecated

        int offset = (int)(height * 0.65); /* percent to move */

        LinearLayout.LayoutParams rlParams =
                (LinearLayout.LayoutParams)mChart.getLayoutParams();
        rlParams.setMargins(0, 0, 0, -offset);
        mChart.setLayoutParams(rlParams);
    }

    private void createPie(PieChart mChart){
        mChart.setBackgroundColor(Color.WHITE);

        moveOffScreen(mChart);

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
        mChart.setCenterTextOffset(0, -20);

        setData(4, 100,mChart);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);


        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setDrawEntryLabels(false);
        mChart.setDrawSliceText(false);
        mChart.setEntryLabelTextSize(12f);
    }

}
