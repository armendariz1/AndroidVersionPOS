package cacean.sorteos;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MenuSorteoActivity extends AppCompatActivity {

    private Button btnVta;
    private Button btnTicket;
    private Button btnPremio;
    private Button btnReporteVenta;
    private Button btnSalir;
    private Context context;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_sorteo);
        context=this;
        btnVta=(Button)findViewById(R.id.btnVentas);
        btnTicket=(Button)findViewById(R.id.btnConsTicket);
        btnPremio=(Button)findViewById(R.id.btnConsPremio);
        btnReporteVenta=(Button)findViewById(R.id.btnReporteVenta);
        btnSalir=(Button)findViewById(R.id.btnSalir);
        //Se establece listener para nuestros botones
        btnVta.setOnClickListener(listenerVta);
        btnTicket.setOnClickListener(listenerTicket);
        btnPremio.setOnClickListener(listenerPremio);
        btnReporteVenta.setOnClickListener(listenerReporteVenta);
        btnSalir.setOnClickListener(listenerSalir);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_sorteo, menu);
        return true;
    }

    private View.OnClickListener listenerSalir = new View.OnClickListener()
    {
        public void onClick(View arg0) {
            //	Cerramos la aplicacion
            Intent intent = new Intent(Intent.ACTION_MAIN);
            finish();
        }
    };

    private View.OnClickListener listenerVta = new View.OnClickListener()
    {
        public void onClick(View arg0) {
            //	Lanzamos pantalla de Venta
            startActivity(new Intent(context,VentaActivity.class));
        }
    };

    private View.OnClickListener listenerTicket = new View.OnClickListener()
    {
        public void onClick(View arg0) {
            //	Lanzamos pantalla de Consultar Ticket
            startActivity(new Intent(context,TicketActivity.class));
        }
    };

    private View.OnClickListener listenerPremio = new View.OnClickListener()
    {
        public void onClick(View arg0) {
            //	Lanzamos la pantalla de Verifica Premio
            startActivity(new Intent(context,PremioActivity.class));
        }
    };

    private View.OnClickListener listenerReporteVenta = new View.OnClickListener()
    {
        public void onClick(View arg0) {
            //	Lanzamos la pantalla de Reporte de venta
            new RevisaPermisos().execute("");
            pd = ProgressDialog.show(context, "Por favor espere","Validando permisos", true, false);
            //startActivity(new Intent(context,ReporteVentas.class));
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class RevisaPermisos extends AsyncTask<String, Void, String>
    {

        @SuppressLint("WrongThread")
        protected String doInBackground(String... args) {
            String res;
            CargaDatosWS ws=new CargaDatosWS();
            res=ws.getPermisoReporte(Usuario.user);
            return res;
        }

        protected void onPostExecute(String result) {
            pd.dismiss();
            if (result.equals("1")){
                startActivity(new Intent(context,ReporteVentas.class));
            } else {
                Toast.makeText(context,"Sin permiso para revisar ventas",Toast.LENGTH_LONG).show();
            }
        }
    }
}
