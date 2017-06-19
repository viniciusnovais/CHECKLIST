package br.com.pdasolucoes.checklist.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;

import br.com.pdasolucoes.checklist.activities.HomeActivity;
import br.com.pdasolucoes.checklist.activities.QuestionsActivity;
import br.com.pdasolucoes.checklist.dao.ComplementoRespostaDao;
import br.com.pdasolucoes.checklist.dao.FormItemDao;
import br.com.pdasolucoes.checklist.dao.RespostaDao;
import br.com.pdasolucoes.checklist.model.ComplementoResposta;
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
    private static FormItemDao formItemDao;
    private static ComplementoRespostaDao daoComplementoR;
    private static int id;
    private static AlertDialog dialogProgress;
    private static Handler handler = new Handler();
    private static int progressStatus = 0;
    private static int progress;
    private static ProgressBar progressBar;


    public static void DialogFinalizacao(Context c, int statusForm, int idFormItem) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage("Finalizar Formulário ?")
                .setNegativeButton("Não", dialogClickListener)
                .setPositiveButton("Sim", dialogClickListener);

        context = c;
        status = statusForm;
        id=idFormItem;

        dao = new RespostaDao(context);
        daoComplementoR = new ComplementoRespostaDao(context);
        formItemDao = new FormItemDao(context);

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
                        Toast.makeText(context, "Ainda não finalizou o Formulário", Toast.LENGTH_SHORT).show();
                        //mudar status formItem
                        Intent i = new Intent(context, HomeActivity.class);
                        context.startActivity(i);

                        Activity activity = (Activity) context;
                        activity.finish();
                    } else {
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
                new Thread(new Runnable() {
                    public void run() {
                        progressStatus = doSomeWork();
                        handler.post(new Runnable() {
                            public void run() {
                                dialogProgress.show();
                                progressBar.setProgress(progressStatus);
                            }
                        });
                    }

                    private int doSomeWork() {
                        try {
                            // ---simulate doing some work---
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return ++progress;
                    }
                }).start();
            }

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            if (VerificaConexao.isNetworkConnected(context)) {
                try {
                    for (Resposta r : dao.listarResposta(id)) {
                        ServiceRespostaPost.post(r);
                    }
                    formItemDao.alterarStatusSync(id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (dialogProgress.isShowing()){
                dialogProgress.dismiss();
            }

            Toast.makeText(context,"Formulário finalizado",Toast.LENGTH_SHORT).show();

            Intent i = new Intent(context, HomeActivity.class);
            context.startActivity(i);

            Activity activity = (Activity) context;
            activity.finish();
        }
    }
}
