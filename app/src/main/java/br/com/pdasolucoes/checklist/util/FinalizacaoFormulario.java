package br.com.pdasolucoes.checklist.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import org.json.JSONException;

import br.com.pdasolucoes.checklist.activities.HomeActivity;
import br.com.pdasolucoes.checklist.dao.RespostaDao;
import br.com.pdasolucoes.checklist.model.Resposta;


/**
 * Created by PDA on 17/05/2017.
 */

public class FinalizacaoFormulario {

    public static AlertDialog dialog;
    private static Context context;
    private static int status = 0;
    private static RespostaDao dao;
    private static int id;


    public static void DialogFinalizacao(Context c, int statusForm, int idFormItem) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setMessage("Finalizar Formulário ?")
                .setNegativeButton("Não", dialogClickListener)
                .setPositiveButton("Sim", dialogClickListener);

        context = c;
        status = statusForm;
        id=idFormItem;

        dao = new RespostaDao(context);
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
                    } else {
                        Toast.makeText(context, "Formulário Finalizado", Toast.LENGTH_SHORT).show();
                        AsyncEnviarResposta task = new AsyncEnviarResposta();
                        task.execute();

                    }
                    Intent i = new Intent(context, HomeActivity.class);
                    context.startActivity(i);

                    Activity activity = (Activity) context;
                    activity.finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        }
    };


    public static class AsyncEnviarResposta extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] objects) {


            try {
                for (Resposta r : dao.listarResposta(id)) {
                    ServiceRespostaPost.post(r);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
