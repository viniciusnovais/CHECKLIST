package br.com.pdasolucoes.checklist.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import checklist.pdasolucoes.com.br.checklist.R;

/**
 * Created by PDA on 23/11/2016.
 */

public class FrameTodoActivity extends AppCompatActivity implements DialogInterface.OnClickListener{

    private Button btnGravar,btnDepois,btnMaisTodo,btEnviarComment,btEnviarFoto;
    private ImageButton imageCamera,imageComment,imageBell;
    private ImageView image;
    private EditText editJustificativa;
    private int status=0;
    private AlertDialog dialogComment,dialogOpcoes,dialog;
    private final int CAMERA=0,GALERIA=1;
    private Bitmap photo;
    private Uri uriImagem;
    private Bitmap originalBitmap;
    private Bitmap resizedBitmap;
    private RelativeLayout relativeLayoutCamera, relativeLayoutComment;
    private int cntCamera=0,cntComment=0;
    private TextView tvNumeroNotificaCamera,tvNumeroNotificaComment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        setContentView(R.layout.frame_todo);
        btnGravar= (Button) findViewById(R.id.btnGravar);

        imageCamera= (ImageButton) findViewById(R.id.btnCamera);
        imageComment= (ImageButton) findViewById(R.id.btnComment);
        imageBell= (ImageButton) findViewById(R.id.btnBell);

        tvNumeroNotificaCamera= (TextView) findViewById(R.id.numeroNotificaCamera);
        tvNumeroNotificaComment= (TextView) findViewById(R.id.numeroNotificaComment);

        //Dialog com as opçoes
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.opcoes);
        String[] item = new String[2];
        item[0] = getString(R.string.camera);
        item[1] = getString(R.string.galeria);

        builder.setItems(item, this);
        dialogOpcoes = builder.create();

        editJustificativa= (EditText) findViewById(R.id.editJustificativa);
        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(FrameTodoActivity.this,"Gravado com sucesso",Toast.LENGTH_SHORT).show();
                status=1;
                btnMaisTodo.setClickable(true);

            }
        });

        btnDepois.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnMaisTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status==1){
                    editJustificativa.setEnabled(false);
                    editJustificativa.setBackgroundColor(Color.rgb(54, 54, 54));
                }

            }
        });

        imageComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewComment = getLayoutInflater().inflate(R.layout.frame_todo_comment,null);
                AlertDialog.Builder builderComment = new AlertDialog.Builder(FrameTodoActivity.this);
                builderComment.setView(viewComment);
                dialogComment=builderComment.create();
                dialogComment.show();

                btEnviarComment= (Button) viewComment.findViewById(R.id.btEnviar);
                btEnviarComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(FrameTodoActivity.this,"Gravou",Toast.LENGTH_SHORT).show();
                        cntComment=cntComment+1;
                        if(cntComment>0){
                            relativeLayoutComment.setVisibility(View.VISIBLE);
                            tvNumeroNotificaComment.setText(cntComment+"");
                        }

                        dialogComment.dismiss();
                    }
                });
            }
        });

        imageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOpcoes.show();
            }
        });
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Intent intent;
        switch (which) {
            //Abrir Camera
            case 0:
                intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA);
                break;
            //Abrir Galeria
            case 1:
                intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALERIA);
                break;
            default:
                break;


        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final View view = getLayoutInflater().inflate(R.layout.frame_image_todo, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        image = (ImageView) view.findViewById(R.id.image);
        btEnviarFoto = (Button) view.findViewById(R.id.btEnviarFoto);

        if (resultCode == MainActivity.RESULT_OK) {
            //Colocar imagem da Camera no Dialog
            switch (requestCode) {

                case CAMERA:
                    photo = (Bitmap) data.getExtras().get("data");
                    originalBitmap = photo;
                    resizedBitmap = Bitmap.createScaledBitmap(
                            originalBitmap, 530, 530, false);

                    image.setImageBitmap(resizedBitmap);
                    dialog = builder.create();
                    dialog.show();
                    dialog.getWindow().setLayout(700, 800);
                    break;
                //Trazer imagem da galeria e colocar no dialog
                case GALERIA:
                    String[] colunaCaminhoArquivo = { MediaStore.Images.Media.DATA };
                    uriImagem = data.getData();
                    Cursor cursor = getContentResolver().query(
                            uriImagem, colunaCaminhoArquivo, null, null, null);
                    cursor.moveToFirst();
                    int colunaIndex = cursor
                            .getColumnIndex(colunaCaminhoArquivo[0]);
                    String caminhoImagem = cursor.getString(colunaIndex);
                    cursor.close();
                    uriImagem = Uri.fromFile(new File(caminhoImagem));
                    try {
                        photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImagem);
                        originalBitmap = photo;
                        resizedBitmap = Bitmap.createScaledBitmap(
                                originalBitmap, 530, 530, false);
                        image.setImageBitmap(resizedBitmap);

                        dialog = builder.create();
                        dialog.show();
                        dialog.getWindow().setLayout(700, 800);
                    } catch (Exception e) {
                        //Notifica.notificaLogo(getActivity(), e.getMessage());
                    }


                    break;
            }
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOpcoes.show();
            }
        });

        btEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FrameTodoActivity.this,"Gravou",Toast.LENGTH_SHORT).show();
                cntCamera=cntCamera+1;
                if (cntCamera>0){
                    relativeLayoutCamera.setVisibility(View.VISIBLE);
                    tvNumeroNotificaCamera.setText(cntCamera+"");
                }

                dialog.dismiss();
            }
        });
    }
}
