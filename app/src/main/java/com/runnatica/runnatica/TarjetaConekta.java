package com.runnatica.runnatica;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.runnatica.runnatica.PDF.PlantillaPDF;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;

import io.conekta.conektasdk.Card;
import io.conekta.conektasdk.Conekta;
import io.conekta.conektasdk.Token;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TarjetaConekta#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TarjetaConekta extends Fragment implements Token.CreateToken{
    View vista;
    EditText numeroTarjeta, nombre, mes, ano, cvv;
    Button btnPagar;

    Calendar calendar = Calendar.getInstance();
    final String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;

    public TarjetaConekta() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TarjetaConekta.
     */
    // TODO: Rename and change types and number of parameters
    public static TarjetaConekta newInstance(String param1, String param2, Context context) {
        TarjetaConekta fragment = new TarjetaConekta();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // ------------------------------> Iniciar el servicio Conekta
        Conekta.setPublicKey("key_eYvWV7gSDkNYXsmr");
        Conekta.setApiVersion("2.0.0");
        Conekta.collectDevice(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_tarjeta_conekta, container, false);
        numeroTarjeta = (EditText)vista.findViewById(R.id.etnoTarjeta);
        nombre = (EditText)vista.findViewById(R.id.etnombreTarjeta);
        mes = (EditText)vista.findViewById(R.id.etmesTarjeta);
        ano = (EditText)vista.findViewById(R.id.etanoTarjeta);
        cvv = (EditText)vista.findViewById(R.id.etcvv);
        btnPagar = (Button)vista.findViewById(R.id.btnPagarConConekta);

        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tokenizarTarjeta();

            }
        });
        return vista;
    }

    private void tokenizarTarjeta() {
        Card card = new Card(nombre.getText().toString(), numeroTarjeta.getText().toString(), cvv.getText().toString(),
                mes.getText().toString(), ano.getText().toString());

        Token token = new Token(getActivity());
        token.onCreateTokenListener(this);
        token.create(card);
    }

    @Override
    public void onCreateTokenReady(JSONObject data) {
        try {
            //Send the id to the webservice.
            Log.d("Token", data.getString("id"));
            Toast.makeText(getActivity(), "Token creado", Toast.LENGTH_SHORT).show();
        } catch (Exception err) {
            Log.e("Error en el token", err.toString());
            Toast.makeText(getActivity(), "Token creado", Toast.LENGTH_SHORT).show();
            crearPDF(currentDate);
            //Do something on error
        }
    }

    private void crearPDF(String Fecha) {
        PlantillaPDF plantillaPDF = new PlantillaPDF(getActivity());
        plantillaPDF.abrirArchivo();
        plantillaPDF.addMetadata("Carrera", "Inscripcion", "Runnatica");
        plantillaPDF.addHeaders("Nombre de la carrera", "Marca", Fecha);
        plantillaPDF.addParagraph("Código QR");
        plantillaPDF.addParagraph("Nombre usuario");
        plantillaPDF.addParagraph("Ubicación del evento");
        plantillaPDF.addParagraph("Nomre del organizador");
        plantillaPDF.cerrarDocumento();
        plantillaPDF.sendPDF(getActivity());
    }
}
