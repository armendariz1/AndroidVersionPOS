package cacean.sorteos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.minipos.device.Printer;

public class ReporteVentas extends AppCompatActivity {

    private TextView txtNombreSucursal;
    private TextView txtVentaSucursal;
    private Context context;

    private TextView txtFecha1;
    private TextView txtSorteo1;
    private TextView txtVenta1;

    private TextView txtFecha2;
    private TextView txtSorteo2;
    private TextView txtVenta2;

    private TextView txtFecha3;
    private TextView txtSorteo3;
    private TextView txtVenta3;

    private TextView txtFecha4;
    private TextView txtSorteo4;
    private TextView txtVenta4;

    private TextView txtFecha5;
    private TextView txtSorteo5;
    private TextView txtVenta5;

    private ProgressDialog pd;



    private Button btnRefrescar;
    private Button btnImprime;

    private String reporteTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_ventas);
        context=this;
        pd = ProgressDialog.show(context, "Por favor espere","Obteniendo ventas", true, false);

        txtNombreSucursal = findViewById(R.id.txtNombreSucursal);
        txtVentaSucursal = findViewById(R.id.txtVentaSucursal);

        txtFecha1 = findViewById(R.id.txtFecha1);
        txtSorteo1 = findViewById(R.id.txtSorteo1);
        txtVenta1 = findViewById(R.id.txtVenta1);

        txtFecha2 = findViewById(R.id.txtFecha2);
        txtSorteo2 = findViewById(R.id.txtSorteo2);
        txtVenta2 = findViewById(R.id.txtVenta2);

        txtFecha3 = findViewById(R.id.txtFecha3);
        txtSorteo3 = findViewById(R.id.txtSorteo3);
        txtVenta3 = findViewById(R.id.txtVenta3);

        txtFecha4 = findViewById(R.id.txtFecha4);
        txtSorteo4 = findViewById(R.id.txtSorteo4);
        txtVenta4 = findViewById(R.id.txtVenta4);

        txtFecha5 = findViewById(R.id.txtFecha5);
        txtSorteo5 = findViewById(R.id.txtSorteo5);
        txtVenta5 = findViewById(R.id.txtVenta5);

        btnRefrescar = findViewById(R.id.btnRecargar);
        btnImprime = findViewById(R.id.btnImprime);

        btnRefrescar.setOnClickListener(listenerRefrescar);
        btnImprime.setOnClickListener(listenerImprime);

        new cargaventas().execute("");
    }

    private View.OnClickListener listenerRefrescar = new View.OnClickListener()
    {
        public void onClick(View arg0) {
            pd = ProgressDialog.show(context, "Por favor espere","Obteniendo ventas", true, false);
            new cargaventas().execute("");
        }
    };

    private View.OnClickListener listenerImprime = new View.OnClickListener()
    {

        public void onClick(View arg0) {

            Printer p = null;

            final byte[] CMD_CUT1 = { '\n', '\n', '\n', '\n', 0x1D, 0x56, 0x01};
            p = null;
            try {
                p = Printer.newInstance();
                p.getOutputStream().write(CMD_CUT1);
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                if(p != null) {
                    p.close();
                }
            }


            try {
                p = Printer.newInstance();
                p.getOutputStream().write(reporteTexto.getBytes());

            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                if(p != null) {
                    p.close();
                }
            }

            final byte[] CMD_CUT2 = { '\n', '\n', '\n', '\n', 0x1D, 0x56, 0x01};
            p = null;
            try {
                p = Printer.newInstance();
                p.getOutputStream().write(CMD_CUT2);
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                if(p != null) {
                    p.close();
                }
            }

        }

    };

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private class cargaventas extends AsyncTask<String, Void, ReporteVenta>
    {
        protected ReporteVenta doInBackground(String... args) {

            CargaDatosWS ws = new CargaDatosWS();
            ReporteVenta Reporte = ws.getReporteventas(Usuario.user);
            return Reporte;
        }

        protected void onPostExecute(ReporteVenta result) {
            pd.dismiss();
            txtNombreSucursal.setText(result.nombreSucursal);
            txtVentaSucursal.setText(result.ventaHoy);

            txtFecha1.setText(result.fechaSorteo1);
            txtSorteo1.setText(result.nombreSorteo1);
            txtVenta1.setText(result.ventaSorteo1);

            txtFecha2.setText(result.fechaSorteo2);
            txtSorteo2.setText(result.nombreSorteo2);
            txtVenta2.setText(result.ventaSorteo2);

            txtFecha3.setText(result.fechaSorteo3);
            txtSorteo3.setText(result.nombreSorteo3);
            txtVenta3.setText(result.ventaSorteo3);

            txtFecha4.setText(result.fechaSorteo4);
            txtSorteo4.setText(result.nombreSorteo4);
            txtVenta4.setText(result.ventaSorteo4);

            txtFecha5.setText(result.fechaSorteo5);
            txtSorteo5.setText(result.nombreSorteo5);
            txtVenta5.setText(result.ventaSorteo5);

            reporteTexto = result.reporteText;


        }

    }

    private void delay(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
