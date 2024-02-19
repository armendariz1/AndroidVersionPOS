package cacean.sorteos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CopyActivity extends Activity implements View.OnClickListener {


    private Button Copiar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy);

        Copiar = (Button)findViewById(R.id.btnCopiar);
        Copiar.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnCopiar){
            //Salir

        }
    }
}


