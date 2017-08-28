package br.com.pdasolucoes.checklist.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import br.com.pdasolucoes.checklist.dao.LogoDao;
import checklist.pdasolucoes.com.br.checklist.R;

/**
 * Created by PDA on 10/08/2017.
 */

public class MostraLogoCliente {

    public static void mostra(Context context, View view) {

        try {
            ImageView imageView = (ImageView) view.findViewById(R.id.logo_cliente);
            LogoDao logoDao = new LogoDao(context);
            Bitmap bmp = BitmapFactory.decodeByteArray(logoDao.logo().getImagem(), 0, logoDao.logo().getImagem().length);
            imageView.setImageBitmap(bmp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
