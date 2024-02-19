package cacean.sorteos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cacean.sorteos.Apuesta;
import cacean.sorteos.R;

/**
 * Created by CARLOSANTONIO on 07/04/2015.
 */
public class SorteoAdapter extends ArrayAdapter<Apuesta> {

    public SorteoAdapter(Context context, List<Apuesta> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        Apuesta apuesta = getItem(position);
        View v = inflater.inflate(R.layout.grid_lay, null, true);
        ((TextView)v.findViewById(R.id.rowidApuesta)).setText(apuesta.getIdApuesta());
        ((TextView)v.findViewById(R.id.rowNumero)).setText(apuesta.getNumero());
        ((TextView)v.findViewById(R.id.rowAp1)).setText(apuesta.getMontoPrimero());
        ((TextView)v.findViewById(R.id.rowAp2)).setText(apuesta.getMontoSegundo());
        ((TextView)v.findViewById(R.id.rowAp3)).setText(apuesta.getMontoTercero());


        return v;
    }
}
