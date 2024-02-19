package cacean.sorteos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.minipos.device.SDK;

import org.ksoap2.serialization.SoapObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import cacean.sorteos.adapter.SorteoAdapter;

//import cacean.sorteos.adapter.SorteoAdapter;


public class VentaActivity extends Activity implements OnClickListener {

    private Spinner lvw;
    private GridView gvw;
    private EditText numeroTxt;
    static Resources aux;
    static String reprint;
    private EditText lugar1;
    private EditText lugar2;
    private EditText lugar3;
    private EditText CopiaTicket;
    private String sorteoId;
    private String apuestaId;
    private TextView usuarioTxt;
    private TextView fechaTxt;
    private TextView strUltTicket;
    private TextView total;
    private TextView cantAp;
    private Button limpiar;
    private Button imprimir;
    private Button agregar;
    private Button cancelar;
    private Button salir;
    private Button copiar;
    private String res;
    private ProgressDialog pd;
    private Context con;
    private String strNumero;
    private String strTicket;
    private String ErrorApuesta;
    private Boolean blnNumero;
    private Boolean editApuesta;
    private String strLugar1;
    private String Ticket;
    private Boolean blnLugar1;
    private String strLugar2;
    private Boolean blnLugar2;
    private String strLugar3;
    private Boolean blnLugar3;
    private List<String> strApuestas;

    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta2);
        aux = getResources();
        usuarioTxt = (TextView)findViewById(R.id.tvwUsuario);
        usuarioTxt.append(Usuario.nombre);
        fechaTxt = (TextView)findViewById(R.id.tvwFecha);
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        fechaTxt.append(dateFormat.format(date));
        total = (TextView)findViewById(R.id.txtTotal);
        strUltTicket = (TextView)findViewById(R.id.txtUltTicket);
        cantAp = (TextView)findViewById(R.id.txtCantAp);
        limpiar = (Button)findViewById(R.id.btnLimpiar);
        limpiar.setOnClickListener(this);
        agregar = (Button)findViewById(R.id.btnAgregar);
        agregar.setOnClickListener(this);
        imprimir = (Button)findViewById(R.id.btnImprimir);
        imprimir.setOnClickListener(this);

        copiar = (Button)findViewById(R.id.btnCopiar);
        copiar.setOnClickListener(this);

        cancelar = (Button)findViewById(R.id.btnCancelar);
        cancelar.setOnClickListener(this);
        salir = (Button)findViewById(R.id.btnSalir);
        salir.setOnClickListener(this);
        editApuesta = false;
        con = this;
        numeroTxt = (EditText)findViewById(R.id.txtNumero);
        lugar1 = (EditText)findViewById(R.id.txtLugar1);
        lugar2 = (EditText)findViewById(R.id.txtLugar2);
        lugar3 = (EditText)findViewById(R.id.txtLugar3);
        CopiaTicket = (EditText)findViewById(R.id.txtCopiar) ;

        imprimir.setEnabled(false);
        lvw = (Spinner)findViewById(R.id.lvwSorteos);
        gvw = (GridView)findViewById(R.id.gvApuestas);
        new DownloadTasklvw().execute("");
        new DownloadTaskLimpia().execute("");

        lvw.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                sorteoId = ((SorteoLvw) arg0.getItemAtPosition(arg2)).getId();

                Log.e("Selected item : ", sorteoId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        gvw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                apuestaId = strApuestas.get(position -(position % 5));
                strNumero = strApuestas.get(position - (position % 5) + 1);
                strLugar1 = strApuestas.get(position - (position % 5) + 2);
                strLugar2 = strApuestas.get(position - (position % 5) + 3);
                strLugar3 = strApuestas.get(position - (position % 5) + 4);

                AlertDialog.Builder builder = new AlertDialog.Builder(con);
                builder.setMessage("Seleccione la operación para la apuesta: " + apuestaId + ".")
                        .setTitle("Atención!!")
                        .setCancelable(true)
                        .setNegativeButton("Editar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Ejecutar editar Apuesta(apuestaId);
                                        editApuesta = true;
                                        EditarApuesta(apuestaId, strNumero, strLugar1, strLugar2, strLugar3);
                                    }
                                })
                        .setPositiveButton("Borrar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        new DownloadTaskDelete().execute(""); // metodo que se debe implementar
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                //Toast.makeText(getApplicationContext(), "Has selecccionado: "  + position + ";" + strApuestas.get(position -(position % 5)) + ";" + strApuestas.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onClick(View v){
        //respond to clicks
        if(v.getId()==R.id.btnSalir){
            //Salir
            Intent intent = new Intent(Intent.ACTION_MAIN);
            finish();
        }
        if(v.getId()==R.id.btnImprimir){
            imprimir.setEnabled(false);
            copiar.setEnabled(true);
            try {
                SDK.init(this);
            } catch (Throwable e1) {
                e1.printStackTrace();
                Toast.makeText(this, e1.getMessage(), Toast.LENGTH_SHORT).show();
            }
            new DownloadTaskFin().execute("");

        }
        if(v.getId()==R.id.btnLimpiar){
           LimpiaApuesta();
        }
        if(v.getId()==R.id.btnCopiar){
            imprimir.setEnabled(true);
            copiar.setEnabled(false);

            strTicket = CopiaTicket.getText().toString();
            new DownloadTaskCopiar().execute("");


            //Salir

        }
        if(v.getId()==R.id.btnAgregar){
            imprimir.setEnabled(true);
            strNumero = numeroTxt.getText().toString();
            if(editApuesta){
                new DownloadTaskModificar().execute("");
                //Limpiar el Número
                numeroTxt.setText("");
                //Asignarle el foco al Número
                numeroTxt.setFocusable(true);
                numeroTxt.requestFocus();

            }else{
                new DownloadTaskAgregar().execute("");
                //Limpiar el Número
                numeroTxt.setText("");
                //Asignarle el foco al Número
                numeroTxt.setFocusable(true);
                numeroTxt.requestFocus();
            }
        }
        if(v.getId()==R.id.btnCancelar){
            copiar.setEnabled(true);
            new DownloadTaskLimpia().execute("");
        }
    }

    //Tarea en Background
    private class DownloadTasklvw extends AsyncTask<String, Void, LinkedList<SorteoLvw>>
    {

        protected LinkedList<SorteoLvw> doInBackground(String... args) {
            CargaDatosWS ws=new CargaDatosWS();
            //Se invoca nuestro metodo
            //return ws.getSorteoActivos(con);
            return ws.getSorteoActivosNew(con,Usuario.user);
//            return 1;
        }

        protected void onPostExecute(LinkedList<SorteoLvw> result) {
            //Creamos el adaptador
            ArrayAdapter<SorteoLvw> spinner_adapter = new ArrayAdapter<SorteoLvw>(VentaActivity.this,android.R.layout.simple_spinner_item,result);
            //Añadimos el layout para el menú y se lo damos al spinner
            spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lvw.setAdapter(spinner_adapter);
        }
    }

    //Tarea en Background
    private class DownloadTaskAgregar extends AsyncTask<String, Void, MyWrapper>
    {

        @SuppressLint("WrongThread")
        protected MyWrapper doInBackground(String... args) {
            MyWrapper res = null;
            //String validaapuesta1,validaapuesta2,validaapuesta3;
            ErrorApuesta="";
            String strTotal = null;
            String msg = null;
            CargaDatosWS ws=new CargaDatosWS();

//            String Primero = lugar1.getText().toString();
//            String Segundo = lugar2.getText().toString();
//            String Tercero = lugar3.getText().toString();
//
//            if (!Primero.equals(""))
//            {
//                validaapuesta1 = ws.validaApuesta(sorteoId,strNumero,Primero,"1");
//                if (!validaapuesta1.equals("00"))
//                {
//                    ErrorApuesta=validaapuesta1;
//                    return res;
//                }
//            }
//            if (!Segundo.equals(""))
//            {
//                validaapuesta2 = ws.validaApuesta(sorteoId,strNumero,Segundo,"2");
//                if (!validaapuesta2.equals("00"))
//                {
//                    ErrorApuesta=validaapuesta2;
//                    return res;
//                }
//            }
//            if (!Tercero.equals(""))
//            {
//                validaapuesta3 = ws.validaApuesta(sorteoId,strNumero,Tercero,"3");
//                if (!validaapuesta3.equals("00"))
//                {
//                    ErrorApuesta=validaapuesta3;
//                    return res;
//                }
//            }

            //Se invoca nuestro metodo
            res = ws.agregaApuesta(sorteoId, strNumero, lugar1.getText().toString(), lugar2.getText().toString(), lugar3.getText().toString(), Usuario.user, con);

            return res;

        }

        protected void onPostExecute(MyWrapper result) {

            //No hacer nada

            if( result==null)
            {
                Toast.makeText(con,ErrorApuesta, Toast.LENGTH_LONG).show();

            }
            else
            {
                LinkedList<Apuesta> apuestas = new LinkedList<Apuesta>();
                ArrayAdapter<String> adaptador;
                strApuestas= new ArrayList<>();

                if (result.code.equals("00")) {
                    if (result.cant > 0) {
                        for (int i = 0; i < result.cant; i++) {

                            SoapObject so = new SoapObject();
                            so = (SoapObject) result.soap.getProperty(i);
                            apuestas.add(new Apuesta(so.getProperty("IdApuesta").toString(), so.getProperty("Numero").toString(), so.getProperty("MontoPrimero").toString(), so.getProperty("MontoSegundo").toString(), so.getProperty("MontoTercero").toString()));
                            strApuestas.add(so.getProperty("IdApuesta").toString());
                            strApuestas.add(so.getProperty("Numero").toString());
                            strApuestas.add(so.getProperty("MontoPrimero").toString());
                            strApuestas.add(so.getProperty("MontoSegundo").toString());
                            strApuestas.add(so.getProperty("MontoTercero").toString());
                        }
                        //Creamos el adaptador
                        //SorteoAdapter adapter = new SorteoAdapter(VentaActivity.this,apuestas);
                        adaptador = new ArrayAdapter<String>(con,android.R.layout.simple_list_item_1,strApuestas);

                        gvw.setAdapter(adaptador);

                        total.setText(result.data);
                        cantAp.setText(result.cant.toString());

                        strNumero = "";
                    }
                    else
                    {

                        Toast.makeText(con, result.message, Toast.LENGTH_LONG).show();
                    }
                }
                else
                {

                    Toast.makeText(con, result.message, Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    protected void LimpiaApuesta()
    {
        //Limpiar los elementos superiores
        strNumero = "";
        numeroTxt.setText(strNumero);
        strLugar1 = "";
        lugar1.setText(strLugar1);
        strLugar2 = "";
        lugar2.setText(strLugar2);
        strLugar3 = "";
        lugar3.setText(strLugar3);
        //Asignarle el foco al Número
        numeroTxt.setFocusable(true);
        numeroTxt.requestFocus();
        CopiaTicket.setText("");
    }
    //Tarea en Background
    private class DownloadTaskModificar extends AsyncTask<String, Void, MyWrapper>
    {

        @SuppressLint("WrongThread")
        protected MyWrapper doInBackground(String... args) {
            MyWrapper res = null;
            String strTotal = null;
            String msg = null;
            CargaDatosWS ws=new CargaDatosWS();
            //Se invoca nuestro metodo
            res = ws.modificarApuestaTicket(sorteoId,apuestaId, strNumero, lugar1.getText().toString(), lugar2.getText().toString(), lugar3.getText().toString(), Usuario.user, con);

            return res;
        }

        protected void onPostExecute(MyWrapper result) {

            //No hacer nada
            LinkedList<Apuesta> apuestas = new LinkedList<Apuesta>();
            ArrayAdapter<String> adaptador;
            strApuestas= new ArrayList<>();

            if (result.code.equals("00")) {
                if (result.cant > 0) {
                    for (int i = 0; i < result.cant; i++) {

                        SoapObject so = new SoapObject();
                        so = (SoapObject) result.soap.getProperty(i);
                        apuestas.add(new Apuesta(so.getProperty("IdApuesta").toString(), so.getProperty("Numero").toString(), so.getProperty("MontoPrimero").toString(), so.getProperty("MontoSegundo").toString(), so.getProperty("MontoTercero").toString()));
                        strApuestas.add(so.getProperty("IdApuesta").toString());
                        strApuestas.add(so.getProperty("Numero").toString());
                        strApuestas.add(so.getProperty("MontoPrimero").toString());
                        strApuestas.add(so.getProperty("MontoSegundo").toString());
                        strApuestas.add(so.getProperty("MontoTercero").toString());
                    }
                    //Creamos el adaptador
                    //SorteoAdapter adapter = new SorteoAdapter(VentaActivity.this,apuestas);
                    adaptador = new ArrayAdapter<String>(con,android.R.layout.simple_list_item_1,strApuestas);

                    gvw.setAdapter(adaptador);

                    total.setText(result.data);
                    cantAp.setText(result.cant.toString());

                    strNumero = "";
                }
                else
                {
                    Toast.makeText(con, result.message, Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(con, result.message, Toast.LENGTH_LONG).show();
            }
            editApuesta = false;
        }
    }

    private void EditarApuesta(String apuesta, String numero, String primero, String segundo, String tercero){
        numeroTxt.setText(numero);
        lugar1.setText(primero);
        lugar2.setText(segundo);
        lugar3.setText(tercero);
    }

    //Tarea en Background
    private class DownloadTaskFin extends AsyncTask<String, Void, String>
    {

        protected String doInBackground(String... args) {
            CargaDatosWS ws=new CargaDatosWS();
            String strTicket;

            //Se invoca nuestro metodo



            strTicket=ws.crearTicket(sorteoId.toString(),Usuario.user);

            return strTicket;
        }

        protected void onPostExecute(String result) {
            //revisar respuesta del servicio web
            String strCode = "";
            if (result.length() > 2)
            {
                strCode = result.substring(0,2);
                result = result.substring(2);
            }
            if (strCode.equals("00")) {
                strUltTicket.setText(result);
                gvw.setAdapter(null);
                total.setText("0");
                cantAp.setText("0");
                LimpiaApuesta();
            }else
            {
                Toast.makeText(con, result, Toast.LENGTH_LONG).show();
            }
            //CAVL-INI-MAY.14.2015-
//            new DownloadTaskLimpia().execute("");
//            LimpiaApuesta();
            //CAVL-FIN-
        }
     }

    private class DownloadTaskLimpia extends AsyncTask<String, Void, String>
    {

        protected String doInBackground(String... args) {
            CargaDatosWS ws=new CargaDatosWS();
            String strRes = "";

            //Se invoca nuestro metodo
            strRes=ws.limpiaTablaTemp(Usuario.user);
            return strRes;
        }

        protected void onPostExecute(String result) {
            //Limpiar spinner
            gvw.setAdapter(null);
            total.setText("0");
            cantAp.setText("0");
        }
    }

    //Tarea en Background
    private class DownloadTaskDelete extends AsyncTask<String, Void, MyWrapper>
    {

        protected MyWrapper doInBackground(String... args) {
            CargaDatosWS ws=new CargaDatosWS();
            //Se invoca nuestro metodo
            return ws.eliminaApuesta(apuestaId, Usuario.user,con);
//            return 1;
        }

        protected void onPostExecute(MyWrapper result) {
            LinkedList<Apuesta> apuestas = new LinkedList<Apuesta>();
            ArrayAdapter<String> adaptador;
            strApuestas= new ArrayList<>();

            if (result.code.equals("00")) {
                if (result.cant > 0) {
                    for (int i = 0; i < result.cant; i++) {

                        SoapObject so = new SoapObject();
                        so = (SoapObject) result.soap.getProperty(i);
                        apuestas.add(new Apuesta(so.getProperty("IdApuesta").toString(), so.getProperty("Numero").toString(), so.getProperty("MontoPrimero").toString(), so.getProperty("MontoSegundo").toString(), so.getProperty("MontoTercero").toString()));
                        strApuestas.add(so.getProperty("IdApuesta").toString());
                        strApuestas.add(so.getProperty("Numero").toString());
                        strApuestas.add(so.getProperty("MontoPrimero").toString());
                        strApuestas.add(so.getProperty("MontoSegundo").toString());
                        strApuestas.add(so.getProperty("MontoTercero").toString());
                    }
                    //Creamos el adaptador
                    //SorteoAdapter adapter = new SorteoAdapter(VentaActivity.this,apuestas);
                    adaptador = new ArrayAdapter<String>(con,android.R.layout.simple_list_item_1,strApuestas);

                    gvw.setAdapter(adaptador);

                    total.setText(result.data);
                    cantAp.setText(result.cant.toString());
                }
                else
                {
                    gvw.setAdapter(null);
                    total.setText("0");
                    cantAp.setText("0");
                }
            }

        }
    }


    private class DownloadTaskCopiar extends AsyncTask<String, Void, MyWrapper>
    {

        @SuppressLint("WrongThread")
        protected MyWrapper doInBackground(String... args) {
            MyWrapper res = null;
            String strTotal = null;
            String msg = null;
            CargaDatosWS ws=new CargaDatosWS();
            //Se invoca nuestro metodo
            res = ws.CopiaApuestas( strTicket,Usuario.user, con);

            return res;
        }

        protected void onPostExecute(MyWrapper result) {

            //No hacer nada
            LinkedList<Apuesta> apuestas = new LinkedList<Apuesta>();
            ArrayAdapter<String> adaptador;
            strApuestas= new ArrayList<>();

            if (result.code.equals("00")) {
                if (result.cant > 0) {
                    for (int i = 0; i < result.cant; i++) {

                        SoapObject so = new SoapObject();
                        so = (SoapObject) result.soap.getProperty(i);
                        apuestas.add(new Apuesta(so.getProperty("ID").toString(), so.getProperty("NUMERO").toString(), so.getProperty("MONTO_APUESTA").toString(), so.getProperty("APUESTA_SEGUNDO").toString(), so.getProperty("APUESTA_TERCERO").toString()));

                        strApuestas.add(so.getProperty("ID").toString());
                        strApuestas.add(so.getProperty("NUMERO").toString());
                        strApuestas.add(so.getProperty("MONTO_APUESTA").toString());
                        strApuestas.add(so.getProperty("APUESTA_SEGUNDO").toString());
                        strApuestas.add(so.getProperty("APUESTA_TERCERO").toString());
                    }
                    //Creamos el adaptador
                    //SorteoAdapter adapter = new SorteoAdapter(VentaActivity.this,apuestas);
                    adaptador = new ArrayAdapter<String>(con,android.R.layout.simple_list_item_1,strApuestas);

                    gvw.setAdapter(adaptador);

                    total.setText(result.data);
                    cantAp.setText(result.cant.toString());

                    strNumero = "";
                }
                else
                {

                    Toast.makeText(con, result.message, Toast.LENGTH_LONG).show();
                }
            }
            else
            {

                Toast.makeText(con, result.message, Toast.LENGTH_LONG).show();
            }

        }
    }

}

