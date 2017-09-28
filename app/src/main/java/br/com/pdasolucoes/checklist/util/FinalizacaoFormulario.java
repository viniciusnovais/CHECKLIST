package br.com.pdasolucoes.checklist.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.plattysoft.leonids.ParticleSystem;

import org.json.JSONException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.pdasolucoes.checklist.activities.HomeActivity;
import br.com.pdasolucoes.checklist.activities.QueryActivity;
import br.com.pdasolucoes.checklist.activities.QuestionsActivity;
import br.com.pdasolucoes.checklist.dao.ComplementoRespostaDao;
import br.com.pdasolucoes.checklist.dao.FormItemDao;
import br.com.pdasolucoes.checklist.dao.LogoDao;
import br.com.pdasolucoes.checklist.dao.OpcaoRespostaDao;
import br.com.pdasolucoes.checklist.dao.PerguntaDao;
import br.com.pdasolucoes.checklist.dao.RespostaDao;
import br.com.pdasolucoes.checklist.dao.TodoDao;
import br.com.pdasolucoes.checklist.model.ComplementoResposta;
import br.com.pdasolucoes.checklist.model.FormItem;
import br.com.pdasolucoes.checklist.model.Logo;
import br.com.pdasolucoes.checklist.model.OpcaoResposta;
import br.com.pdasolucoes.checklist.model.Pergunta;
import br.com.pdasolucoes.checklist.model.Resposta;
import checklist.pdasolucoes.com.br.checklist.R;


/**
 * Created by PDA on 17/05/2017.
 */

public class FinalizacaoFormulario {

    public static AlertDialog dialog;
    private static Context context;
    private static int status = 0;
    private static RespostaDao dao;
    private static TodoDao todoDao;
    private static FormItemDao formItemDao;
    private static PerguntaDao perguntaDao;
    private static OpcaoRespostaDao opcaoRespostaDao;
    private static int id;
    private static AlertDialog dialogProgress;
    private static ProgressBar progressBar;


    public static void DialogFinalizacao(Context c, int statusForm, int idFormItem) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage("Finalizar Formulário ?")
                .setNegativeButton("Não", dialogClickListener)
                .setPositiveButton("Sim", dialogClickListener);

        context = c;
        status = statusForm;
        id = idFormItem;

        dao = new RespostaDao(context);
        formItemDao = new FormItemDao(context);
        todoDao = new TodoDao(context);
        opcaoRespostaDao = new OpcaoRespostaDao(context);
        perguntaDao = new PerguntaDao(context);

        //alert progressBar
        AlertDialog.Builder builderProgress = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.progress_bar, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        builderProgress.setView(view);
        dialogProgress = builderProgress.create();

        dialog = builder.create();
        dialog.show();

    }

    static DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:

                    if (status == 0) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                        builder2.setMessage("Formulário possui pendências! Deseja completar ?")
                                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        Intent intent = new Intent(context, HomeActivity.class);
                                        context.startActivity(intent);
                                        Activity activity = (Activity) context;
                                        activity.finish();
                                    }
                                })
                                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(context, QuestionsActivity.class);
                                        intent.putExtra("pendencias", "pendencias");
                                        intent.putExtra("idSetor", formItemDao.idSetor(id));
                                        context.startActivity(intent);
                                        Activity activity = (Activity) context;
                                        activity.finish();
                                    }
                                }).create().show();


                    } else {
                        FormItem f = new FormItem();
                        Calendar c = Calendar.getInstance();
                        c.setTime(new Date());
                        String data = c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
                        String hora = c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE);
                        f.setIdItem(id);
                        f.setDataFim(data);
                        f.setHoraFim(hora);
                        formItemDao.alterarDataHoraFim(f);
                        Toast.makeText(context, "Finalizando formulário...", Toast.LENGTH_SHORT).show();
                        AsyncEnviarResposta task = new AsyncEnviarResposta();
                        task.execute();
                    }
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        }
    };


    public static class AsyncEnviarResposta extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (VerificaConexao.isNetworkConnected(context)) {
                dialogProgress.show();
            }

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            ComplementoRespostaDao dao2 = new ComplementoRespostaDao(context);
            if (VerificaConexao.isNetworkConnected(context)) {
                try {
                    SharedPreferences preferences = context.getSharedPreferences("MainActivityPreferences", context.MODE_PRIVATE);
                    ServiceRespostaPost.postResposta(dao.listarResposta(id, preferences.getInt("idUsuario", 0)));
                    ServiceComplementoResposta.postResposta(dao2.imagem());
                    ServiceTodoPost.postTodo(todoDao.listarTodo(id));
                    formItemDao.alterarStatusSync(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (dialogProgress.isShowing()) {
                dialogProgress.dismiss();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Sua Nota: ");
            final AlertDialog dialog = builder.setView(MostraNota.MostraNotaView(context, indicadorGeral(perguntaDao.listar(formItemDao.buscarIdSetor(id)), id) * 100)).create();

            dialog.show();

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialog.dismiss();

                    Activity activity = (Activity) context;
                    activity.finish();
                }
            });

        }
    }

    public static float indicadorGeral(List<Pergunta> pergunta, int id) {
        float rIndicador = 0, indicador = 0, totalTodasPerguntasForm = 0;


        for (Pergunta p : pergunta) {
            if (p.getTipoPergunta() == 2) {
                for (OpcaoResposta op : opcaoRespostaDao.listarTodasOpcoesForm(id, p.getIdPergunta())) {
                    totalTodasPerguntasForm += op.getValor();
                }
            } else if (p.getTipoPergunta() == 4 || p.getTipoPergunta() == 8) {
                for (OpcaoResposta op : opcaoRespostaDao.listarTodasOpcoesFormMultiplo(id, p.getIdPergunta())) {
                    totalTodasPerguntasForm += op.getValor();
                }
            }
            for (Resposta r : dao.respotaPeloIdPergunta(p.getIdPergunta(), id)) {

                rIndicador += p.getPeso() * r.getValor();
            }
        }
        indicador = rIndicador / totalTodasPerguntasForm;

        return indicador;
    }
}
