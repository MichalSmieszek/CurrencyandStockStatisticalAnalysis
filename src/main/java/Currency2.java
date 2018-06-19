import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;

public class Currency2 {
    public static String twocurrencies(String currencyy) {
        String mojJson = null;
        try {
            URL url = new URL("http://api.nbp.pl/api/exchangerates/rates/A/" + currencyy + "/2018-04-01/2018-05-16/?format=json");
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            StringBuffer buffer = new StringBuffer();
            String line = "";
            connection = (HttpURLConnection) url.openConnection();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String stringBuffer = buffer.toString();
            mojJson = "[";
            JSONObject mainObject = new JSONObject(stringBuffer);
            JSONArray rates = mainObject.getJSONArray("rates");
            for (int i = 0; i < rates.length(); i++) {
                System.out.print(rates.length());
                JSONObject currency = rates.getJSONObject(i);
                double cena = currency.getDouble("mid");
                String date = currency.getString("effectiveDate");
                mojJson = mojJson + "{\"" + currencyy + "\":\"" + cena + "\"}";
                if (i + 1 < rates.length())
                    mojJson = mojJson + ",";
            }
            mojJson = mojJson + "]";


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (mojJson);
    }

    public static void Write(String waluta) {
        String json1 = twocurrencies(waluta);
        try {
            PrintWriter out = new PrintWriter(waluta + "2.json");
            out.println(json1);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static String Pair(String currency1, String currency2){
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
                pairjson=pairjson+"{\"pair\":"+c + "}";
                if (i<array1.length()-1)
                    pairjson=pairjson+",";
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
                if (reader1 != null)
                    reader1.close();

                if (reader2 != null)
                        reader2.close();

                } catch (IOException e) {
            }
        }
    return(pairjson);
    }
    public static void PairToJson(String currency1,String currency2){
        String json1 = Pair(currency1,currency2);
        try {
            PrintWriter out = new PrintWriter(currency1+currency2+ ".json");
            out.println(json1);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}