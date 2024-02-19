package cacean.sorteos;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.AsyncTask;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class PremioActivity extends Activity  implements OnClickListener{

    private Button scanBtn;
    private Button verifBtn;
    private TextView contentTxt;
    private String res;
    private ProgressDialog pd;
    private Context context;


    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premio);
        scanBtn = (Button)findViewById(R.id.btnScan);
        verifBtn = (Button)findViewById(R.id.btnVerif);
        contentTxt = (TextView)findViewById(R.id.txtTicket);
        context=this;
        scanBtn.setOnClickListener(this);
        verifBtn.setOnClickListener(this);
    }


    public void onClick(View v){
        //respond to clicks
        if(v.getId()==R.id.btnScan){
            //scan
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }

        if(v.getId()== R.id.btnVerif){
            //Verificar Ticket
            if(contentTxt.length() == 0)
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

    }

    //Tarea en Background
    private class DownloadTask2 extends AsyncTask<String, Void, String>
    {

        @SuppressLint("WrongThread")
        protected String doInBackground(String... args) {
            CargaDatosWS ws=new CargaDatosWS();
            //Se invoca nuestro metodo
            res=ws.validaTicket(contentTxt.getText().toString(),Usuario.user.toString());
            return res;
        }

        protected void onPostExecute(String result) {
            //Se elimina la pantalla de por favor espere.
            pd.dismiss();
            if (res.length()> 2)
            {
                if (res.substring(0,2)== "OK")
                {
                    res = res.substring(2);
                }
            }
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

//            Toast.makeText(context,res,Toast.LENGTH_LONG).show();
//            super.onPostExecute(result);
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
            contentTxt.setText("CONTENT: " + scanContent);
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}

