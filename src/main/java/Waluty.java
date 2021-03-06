import java.io.*;
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
            else if (typdaty==1)
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
                mojJson = mojJson + "{\""+waluta+"\":\"" + cena +"\"}";
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
    public static void pair(String currency1, String currency2){
        File file1 = new File(currency1+".json");
        File file2 = new File(currency2+".json");
        String pairjson="[";
        BufferedReader reader1 = null;
        BufferedReader reader2 = null;
        try {
            reader1 = new BufferedReader(new FileReader(file1));
            reader2 = new BufferedReader(new FileReader(file2));
            String text = null;
            StringBuffer buffer1 = new StringBuffer();
            StringBuffer buffer2 = new StringBuffer();
            while ((text = reader1.readLine()) != null) {
               buffer1.append(text);
            }
            while ((text = reader2.readLine()) != null) {
                buffer2.append(text);
            }
            String string1=buffer1.toString();
            String string2=buffer2.toString();
            JSONArray array1 = new JSONArray(string1);
            JSONArray array2 = new JSONArray(string2);
            for(int i=0;i<array1.length();i++){
                JSONObject currency11 = array1.getJSONObject(i);
                JSONObject currency22 = array2.getJSONObject(i);
               double c= Double.parseDouble(currency11.get(currency1).toString()) /
                        Double.parseDouble(currency22.get(currency2).toString());
                pairjson=pairjson+"{pair:"+c + "}";
            }
            pairjson=pairjson+"]";
            System.out.print(pairjson);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (reader1 != null) {
                    reader1.close();
                }
            } catch (IOException e) {
            }
        }

    }
}
