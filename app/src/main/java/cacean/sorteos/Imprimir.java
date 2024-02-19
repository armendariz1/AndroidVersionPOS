package cacean.sorteos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.minipos.device.Printer;
import com.minipos.device.SDK;
import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.graphics.internal.ZebraImageAndroid;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.EnumMap;
import java.util.Map;
import java.lang.Object;


public class Imprimir {
    private String resultado;
    private static final byte[] CMD_LINE_FEED = { 0x0a };
    private static final byte[] CMD_INIT = { 0x1B, 0x40 };
    private static final byte[] CMD_ALIGN_CENTER = { 0x1B, 0x61, 1 };
    private static final byte[] CMD_FEED_AND_CUT = { 0x0A, 0x0A, 0x0A, 0x0A, 0x1D, 0x56, 0x01 };


    public String PrintStr(String texto, String ventana, String ticket, String user)  //metodo co el que se ejecuta la impresion
    {
        texto = "  " + texto;
        doConnectionText(texto, ventana, ticket,user);
        return resultado;
    }

    private void doConnectionText(String texto, String act, String code, String user) {
            enviaImpresion(texto, act, code,user);
    }


    private void enviaImpresion(String texto, String ventana, String ticket, String user)
    {

        //CargaDatosWS d = new CargaDatosWS();
        //String imprimelogo = d.ImprimeLogo(user);

        try {

            ImprimeLogo(ventana);


            String nuevo = texto;

            //eliminar el texto Don beto para las otras sucursales
            nuevo = nuevo.replace("\r\r","\r\n");
            nuevo = nuevo.replace("\r\n\n","\r\n");
            nuevo = nuevo.replace("\r\n","@");
            nuevo = nuevo.replace("\r","@");
            nuevo = nuevo.replace("@","\r\n");
            nuevo = nuevo.replace("   #","#");

            nuevo = nuevo.replace("Sorteo\r\n     ","Sorteo\r\n   ");
            nuevo = nuevo.replace("Consecutivo\r\n       ","Consecutivo\r\n   ");
            nuevo = nuevo.replace("   \r\n","\r\n");

            nuevo = nuevo.concat("\r\n\r\n");

            nuevo = "\r\n\r\n" + nuevo;



            //Inicia codigo nuevo

            Printer p = null;
            try {
                p = Printer.newInstance();
                p.getOutputStream().write(nuevo.getBytes());

             } catch (Throwable e) {
                e.printStackTrace();
            } finally {

                if(p != null) {
                    p.close();
                }
            }


            enviaImpresionCode(ticket);


            final byte[] CMD_CUT = { '\n', '\n', '\n', '\n', 0x1D, 0x56, 0x01};
            p = null;
            try {
                p = Printer.newInstance();
                p.getOutputStream().write(CMD_CUT);
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                if(p != null) {
                    p.close();
                }
            }

        } finally {


        }
    }

    private void ImprimeLogo(String ventana){

        Printer p = null;
        try {
            p = Printer.newInstance();
            OutputStream os = p.getOutputStream();
            Bitmap logo;
            if (ventana.equals("VENTA"))
            {
                logo = BitmapFactory.decodeResource(VentaActivity.aux,R.drawable.logo3);
            }else{
                logo = BitmapFactory.decodeResource(EditarActivity.aux,R.drawable.logo3);
            }

            String textoX = "O";
            String error = "Error al codificar imagen";
            os.write(textoX.getBytes());
            if (!(logo==null)){
                byte[] data = genBitmapCode(logo, false, false);
                os.write(data);
            } else {
                os.write(error.getBytes());
            }

        } catch (Throwable e) {
            e.printStackTrace();
            p.close();
        } finally {
            if(p != null) {
                p.close();
            }
        }
    }

    private void enviaImpresionCode(String texto)
    {
        try {

            String barcode_data = texto;
            Bitmap bitmap;
            try {
                bitmap = encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 400, 100);
                Printer p = null;
                try {
                    p = Printer.newInstance();
                    OutputStream os = p.getOutputStream();

                    byte[] data = genBitmapCode(bitmap, false, false);
                    os.write(data);

                } catch (Throwable e) {
                    e.printStackTrace();
                } finally {
                    if(p != null) {
                        p.close();
                    }
                }

                resultado = "00";
            } catch (WriterException e) {
                e.printStackTrace();
                resultado = e.getMessage();
            }

        } finally {

        }
    }



    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    public static final int MAX_BIT_WIDTH = 576;

    private static byte[] genBitmapCode(Bitmap bm, boolean doubleWidth, boolean doubleHeight) {
        int w = bm.getWidth();
        int h = bm.getHeight();
        if(w > MAX_BIT_WIDTH)
            w = MAX_BIT_WIDTH;
        int bitw = ((w+7)/8)*8;
        int bith = h;
        int pitch = bitw / 8;
        byte[] cmd = {0x1D, 0x76, 0x30, 0x00, (byte)(pitch&0xff), (byte)((pitch>>8)&0xff), (byte) (bith&0xff), (byte) ((bith>>8)&0xff)};
        byte[] bits = new byte[bith*pitch];


        if(doubleWidth)
            cmd[3] |= 0x01;

        if(doubleHeight)
            cmd[3] |= 0x02;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int color = bm.getPixel(x, y);
                if ((color&0xFF) < 128) {
                    bits[y * pitch + x/8] |= (0x80 >> (x%8));
                }
            }
        }
        ByteBuffer bb = ByteBuffer.allocate(cmd.length+bits.length);
        bb.put(cmd);
        bb.put(bits);
        return bb.array();
    }

    private void delay(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
