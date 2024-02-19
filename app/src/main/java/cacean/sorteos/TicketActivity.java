package cacean.sorteos;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TicketActivity extends Activity  implements OnClickListener {

    private Button scanBtn1;
    private Button verifBtn1;
    private Button BorraBtn;
    private TextView contentTxt1;
    private String res,ticket;
    private ProgressDialog pd;
    private Context context;

    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        scanBtn1 = (Button)findViewById(R.id.btnScan);
        verifBtn1 = (Button)findViewById(R.id.btnVerif);
        BorraBtn = (Button)findViewById(R.id.btnBorrar);
        contentTxt1 = (TextView)findViewById(R.id.txtTicket);
        context=this;
        scanBtn1.setOnClickListener(this);
        verifBtn1.setOnClickListener(this);
        BorraBtn.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public void onClick(View v){
        //respond to clicks

        ticket=contentTxt1.getText().toString();


        if(v.getId()==R.id.btnScan){
            //scan
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }

        if(v.getId()== R.id.btnVerif){
            //Verificar Ticket
            if(contentTxt1.length() == 0)
            {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Debe teclear o escanear el ticket", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            //	Usamos un AsyncTask, para poder mostrar una ventana de por favor espere, mientras se consulta el servicio web
            new DownloadTask2().execute("");
            pd = ProgressDialog.show(context, "Por favor espere","Verificando ticket", true, false);
        }

        if(v.getId()== R.id.btnBorrar){
            //Verificar Ticket

            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(TicketActivity.this);
            dialogo1.setTitle("Borrar ticket");
            dialogo1.setMessage("Se eliminará el ticket: " + ticket);
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    new BorrarTicket().execute("");
                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    cancelar();
                }
            });
            dialogo1.show();
        }
    }

    //Tarea en Background
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private class DownloadTask2 extends AsyncTask<String, Void, String>
    {

        protected String doInBackground(String... args) {
            CargaDatosWS ws=new CargaDatosWS();
            //Se invoca nuestro metodo
            res=ws.validaEditar(ticket);
            return res;
        }

        protected void onPostExecute(String result) {
            //Se elimina la pantalla de por favor espere.
            pd.dismiss();
            //Se lanza pantalla con la respuesta del servicio web
            if(result.length() > 2){
                String strResp = result.substring(0,2);
                String strTicket = result.substring(2);
                if (strResp.equals("00")) {
                    Intent i = new Intent(TicketActivity.this,EditarActivity.class);
                    i.putExtra("ticketEdit", strTicket);
                    startActivity(i);
                }
                else{
                    //Se muestra mensaje con la respuesta del servicio web
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(res)
                            .setTitle("Atención!!")
                            .setCancelable(false)
                            .setNegativeButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            //super.onPostExecute(result);
        }
    }



    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null)
        {
            //we have a result

            String scanContent = scanningResult.getContents();
            contentTxt1.setText("CONTENT: " + scanContent);
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //Tarea en Background
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
//    private class DownloadTask3 extends AsyncTask<String, Void, String>
//    {
//
//        protected String doInBackground(String... args) {
//            CargaDatosWS ws=new CargaDatosWS();
//            //Se invoca nuestro metodo
//            res=ws.eliminaTicket(ticket,"");
//            return res;
//        }
//
//        protected void onPostExecute(String result) {
//            //Se elimina la pantalla de por favor espere.
//            pd.dismiss();
//            //Se lanza pantalla con la respuesta del servicio web
//            AlertDialog.Builder builder = new AlertDialog.Builder(TicketActivity.this);
//            builder.setMessage(res)
//                    .setTitle("Atención!")
//                    .setCancelable(false)
//                    .setNegativeButton("OK",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//            AlertDialog alert = builder.create();
//            alert.show();
//
//            //super.onPostExecute(result);
//        }
//    }

    private class BorrarTicket extends AsyncTask<String, Void, String>
    {

        protected String doInBackground(String... args) {

            CargaDatosWS ws=new CargaDatosWS(TicketActivity.this);
            res=ws.eliminaTicket(ticket,"");
            return res;

        }

        protected void onPostExecute(String result) {



            //Se muestra mensaje con la respuesta del servicio web
            AlertDialog.Builder builder = new AlertDialog.Builder(TicketActivity.this);
            builder.setMessage(res)
                    .setTitle("Atención!")
                    .setCancelable(false)
                    .setNegativeButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();


        }
    }

    public void cancelar() {

    }



}
