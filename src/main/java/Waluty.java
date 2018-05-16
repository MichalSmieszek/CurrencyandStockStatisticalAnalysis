import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.*;

public class Waluty {
    public static String WypiszJSON(String waluta,int typdaty) {
        String text="";
        String mojJson="";

        try {
            URL url =null;
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            StringBuffer buffer = new StringBuffer();
            String line = "";
            if (typdaty==0)
                 url = new URL("http://api.nbp.pl/api/exchangerates/rates/A/" + waluta + "/2018-01-01/2018-03-31/?format=json");
            else
                 url = new URL("http://api.nbp.pl/api/exchangerates/rates/A/" + waluta + "/2017-10-01/2017-12-31/?format=json");
            connection = (HttpURLConnection) url.openConnection();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String stringBuffer=buffer.toString();
            mojJson="[";
            JSONObject mainObject= new JSONObject(stringBuffer);
            JSONArray rates = mainObject.getJSONArray("rates");
            for(int i=0;i<rates.length();i++) {
                JSONObject currency = rates.getJSONObject(i);
                double cena = currency.getDouble("mid");
                String date = currency.getString("effectiveDate");
                mojJson = mojJson + "{\""+date+"\": " + cena + "}";
                if (i + 1 < rates.length())
                    mojJson = mojJson + ",";
            }
                mojJson=mojJson+"]";

        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return(mojJson);
    }
    public static void napisz(String waluta){
        String json1=WypiszJSON(waluta,1);
        String json2=WypiszJSON(waluta,0);
        json1=json1.substring(0, json1.length() - 1);
        json2=json2.substring(1);
        String mojJson=json1+","+json2;
        try {
            PrintWriter out = new PrintWriter(waluta+ ".json");
            out.println(mojJson);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
