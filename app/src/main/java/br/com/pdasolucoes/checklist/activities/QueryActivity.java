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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.pdasolucoes.checklist.adapter.AppointmentAdapter;
import br.com.pdasolucoes.checklist.adapter.QueryAdapter;
import br.com.pdasolucoes.checklist.adapter.Question2Adapter;
import br.com.pdasolucoes.checklist.dao.FormDao;
import br.com.pdasolucoes.checklist.dao.FormItemDao;
import br.com.pdasolucoes.checklist.dao.OpcaoRespostaDao;
import br.com.pdasolucoes.checklist.dao.PerguntaDao;
import br.com.pdasolucoes.checklist.dao.RespostaDao;
import br.com.pdasolucoes.checklist.dao.TodoDao;
import br.com.pdasolucoes.checklist.model.Form;
import br.com.pdasolucoes.checklist.model.FormItem;
import br.com.pdasolucoes.checklist.model.OpcaoResposta;
import br.com.pdasolucoes.checklist.model.Pergunta;
import br.com.pdasolucoes.checklist.model.Resposta;
import br.com.pdasolucoes.checklist.util.MostraLogoCliente;
import checklist.pdasolucoes.com.br.checklist.R;

/**
 * Created by PDA on 01/11/2016.
 */

public class QueryActivity extends AppCompatActivity {

    private PieChart mChart;
    private ImageButton imageButtonAppointment, imageButtonTodo, imageButtonLogout;
    private TextView tvText, tvRfe, tvRde, tvAde, tvAfe, tvNomeFormAction, tvTempo;
    private RespostaDao dao;
    private OpcaoRespostaDao opcaoRespostaDao;
    private FormItemDao formItemDao;
    private TodoDao todoDao;
    private Button btAbrir;
    private String nomeForm;
    private PerguntaDao perguntaDao;
    private float resultado = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_formulario);
        MostraLogoCliente.mostra(this, getSupportActionBar().getCustomView());
        tvNomeFormAction = (TextView) findViewById(R.id.nomeForm);
        nomeForm = getIntent().getExtras().getString("nomeForm");
        setContentView(R.layout.query_activity);
        tvNomeFormAction.setText(nomeForm);

        btAbrir = (Button) findViewById(R.id.buttonAbrir);
        tvText = (TextView) findViewById(R.id.tvText);
        tvRfe = (TextView) findViewById(R.id.tvRfe);
        tvRde = (TextView) findViewById(R.id.tvRde);
        tvAde = (TextView) findViewById(R.id.tvAde);
        tvAfe = (TextView) findViewById(R.id.tvAfe);
        tvTempo = (TextView) findViewById(R.id.tvTempo);

        dao = new RespostaDao(this);
        perguntaDao = new PerguntaDao(this);
        opcaoRespostaDao = new OpcaoRespostaDao(this);
        todoDao = new TodoDao(this);
        formItemDao = new FormItemDao(this);

        resultado = Float.parseFloat(String.valueOf(dao.contadorResposta(getIntent().getExtras().getInt("idItem")))) /
                Float.parseFloat(String.valueOf(perguntaDao.QtdePergunta(getIntent().getExtras().getInt("idSetor"))));
        resultado = resultado * 100;

        String porcentFormat = String.format("%.2f", resultado);
        tvText.setText(porcentFormat + "% Concluído");


        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
        Date d = new Date();

        if (formItemDao.listarItemForm(getIntent().getExtras().getInt("idItem")).get(0).getHoraFim() != null) {

            try {
                d = sdfHora.parse(formItemDao.listarItemForm(getIntent().getExtras().getInt("idItem")).get(0).getHoraFim());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Calendar c = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();


        c.set(Calendar.HOUR, Integer.parseInt(sdfHora.format(d).substring(0, 2)));
        c.set(Calendar.MINUTE, Integer.parseInt(sdfHora.format(d).substring(3, 5)));


        String data = formItemDao.listarItemForm(getIntent().getExtras().getInt("idItem")).get(0).getHoraInicio();
        cal.set(Calendar.HOUR, Integer.parseInt(data.substring(0, 2)));
        cal.set(Calendar.MINUTE, Integer.parseInt(data.substring(3, 5)));

        long resultado = c.getTimeInMillis() - cal.getTimeInMillis();
        long hours = (60 * 60 * 1000);
        long diffHoras = resultado / hours;
        long diffHorasMinutos = (resultado % hours) / (60 * 1000);

//        if (!formItemDao.listarItemForm(getIntent().getExtras().getInt("idItem")).get(0).getHoraFim().equals("")) {
//            String hora = formItemDao.listarItemForm(getIntent().getExtras().getInt("idItem")).get(0).getHoraFim();
//            if (Integer.parseInt(hora.substring(0, 1)) < 9) {
//                hora = "0" + hora;
//            }
//            tvTempo.setText("Tempo: " + hora.substring(0, 2) + " horas e " + hora.substring(2, 4) + " minutos");
//        } else {

            tvTempo.setText("Tempo: " + diffHoras + " horas e " + diffHorasMinutos + " minutos");
        //}

        mChart = (PieChart) findViewById(R.id.chart);
        createPie(mChart);
        imageButtonAppointment = (ImageButton) findViewById(R.id.appointmentBook);
        imageButtonTodo = (ImageButton) findViewById(R.id.toDo);
        imageButtonLogout = (ImageButton) findViewById(R.id.logout);

        tvRfe.setText(dao.respostaNaoConforme(getIntent().getExtras().getInt("idItem")) + "");
        tvRde.setText(dao.respostaConforme(getIntent().getExtras().getInt("idItem")) + "");
        tvAde.setText(todoDao.contadorTodo(getIntent().getExtras().getInt("idItem")) + "");
        tvAfe.setText(todoDao.acaoFC(getIntent().getExtras().getInt("idItem"), getSharedPreferences(MainActivity.PREF_NAME, MODE_PRIVATE).getInt("idUsuario", 0)) + "");

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
                Intent i = new Intent(QueryActivity.this, ListaRespostaFaltaTodo.class);
                startActivity(i);
                finish();
            }
        });


        btAbrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QueryActivity.this, QuestionsActivity.class);
                i.putExtra("form", nomeForm);
                i.putExtra("ItemId", getIntent().getExtras().getInt("idItem"));
                i.putExtra("idForm", getIntent().getExtras().getInt("idForm"));
                i.putExtra("idSetor", getIntent().getExtras().getInt("idSetor"));
                i.putExtra("QueryActivity", "QueryActivity");
                startActivity(i);
                finish();
            }
        });

    }

    private void setData(int count, float range, PieChart mChart, float valor) {

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

//    private void moveOffScreen(PieChart mChart) {
//
//        Display display = getWindowManager().getDefaultDisplay();
//        int height = display.getHeight();  // deprecated
//
//        int offset = (int) (height * 0.65); /* percent to move */
//
//        LinearLayout.LayoutParams rlParams =
//                (LinearLayout.LayoutParams) mChart.getLayoutParams();
//        rlParams.setMargins(0, 0, 0, -offset);
//        mChart.setLayoutParams(rlParams);
//    }

    private void createPie(PieChart mChart) {
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

        String formatIndicadorGeral = String.format("%.0f", indicadorGeral(perguntaDao.listar(getIntent().getExtras().getInt("idSetor"))) * 100);
        mChart.setCenterText(formatIndicadorGeral + "% Indicador Geral");


        setData(2, 100, mChart, indicadorGeral(perguntaDao.listar(getIntent().getExtras().getInt("idSetor"))) * 100);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);


        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setDrawEntryLabels(false);
        mChart.setDrawSliceText(false);
        mChart.setEntryLabelTextSize(12f);
    }


    public float indicadorGeral(List<Pergunta> pergunta) {
        float rIndicador = 0, indicador = 0, totalTodasPerguntasForm = 0;


        for (Pergunta p : pergunta) {
            if (p.getTipoPergunta() == 2) {
                for (OpcaoResposta op : opcaoRespostaDao.listarTodasOpcoesForm(getIntent().getExtras().getInt("idItem"), p.getIdPergunta())) {
                    totalTodasPerguntasForm += op.getValor();
                }
            } else if (p.getTipoPergunta() == 4 || p.getTipoPergunta() == 8) {
                for (OpcaoResposta op : opcaoRespostaDao.listarTodasOpcoesFormMultiplo(getIntent().getExtras().getInt("idItem"), p.getIdPergunta())) {
                    totalTodasPerguntasForm += op.getValor();
                }
            }
            for (Resposta r : dao.respotaPeloIdPergunta(p.getIdPergunta(),
                    getIntent().getExtras().getInt("idItem"))) {

                rIndicador += p.getPeso() * r.getValor();
            }
        }
        indicador = rIndicador / totalTodasPerguntasForm;

        return indicador;
    }

    public static int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }

}


