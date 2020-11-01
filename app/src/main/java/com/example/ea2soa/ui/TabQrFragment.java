package com.example.ea2soa.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ea2soa.data.GenerarQrService;
import com.example.ea2soa.R;
import com.example.ea2soa.data.GenerarQrServiceCallbackInterface;

public class TabQrFragment extends Fragment {
    EditText textoQR;
    Button buttonGenerarQR;
    ImageView qr_imagen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qr_fragment,container,false);

        textoQR = view.findViewById(R.id.textoQR);
        buttonGenerarQR = view.findViewById(R.id.generar_qr);
        qr_imagen = view.findViewById(R.id.imagen_qr);

        buttonGenerarQR.setOnClickListener(v -> {
            GenerarQrService generarQrService = new GenerarQrService(view.getContext());
            buttonGenerarQR.setEnabled(false);
            generarQrService.registerReceiver(new GenerarQrServiceCallbackInterface() {
                @Override
                public void handle(Bitmap image) {
                    buttonGenerarQR.setEnabled(true);
                    qr_imagen.setImageBitmap(image);
                }
            });
            generarQrService.generarQR(textoQR.getText().toString());

        });

        return view;
    }
}
