import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Stock {
    public static void getData(String stock) {
        String text = "";
        String mojJson = "[";
        PrintWriter out;
        try {
            URL url = null;
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            StringBuffer buffer = new StringBuffer();
            String line = "";
            if (stock=="Oil")
                url = new URL("https://www.quandl.com/api/v1/datasets/CHRIS/CME_CL1.json");
            if(stock=="Gold")
                url = new URL("https://www.quandl.com/api/v1/datasets/LBMA/GOLD.json");
            if (stock=="Aluminium")
                url = new URL("https://www.quandl.com/api/v1/datasets/LME/PR_AL.json");
            if (stock=="Silver")
                url = new URL("https://www.quandl.com/api/v1/datasets/LBMA/SILVER.json");
            connection = (HttpURLConnection) url.openConnection();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String stringBuffer = buffer.toString();
            JSONObject mainObject = new JSONObject(stringBuffer);
            JSONArray array = mainObject.getJSONArray("data");
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Date beginningDate = sdf.parse("2017-9-30");
            Date endingDate = sdf.parse("2018-04-01");
            for (int i=array.length()-1;i>0;i--){
                JSONArray value = array.getJSONArray(i);
                String stringDate =value.getString(0);
                Date date = sdf.parse(stringDate);
                String open=value.getString(1);
                if (date.before(endingDate) && date.after(beginningDate))
                    mojJson=mojJson+ "{\""+stock+"\": " + open + "},";
            }
            mojJson=mojJson.substring(0, mojJson.length() - 1)+"]";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
        }
        try {
            if (stock=="Oil")
                out = new PrintWriter("Oil.json");
            else if (stock=="Gold")
                out = new PrintWriter("Gold.json");
            else if (stock=="Aluminium")
                out = new PrintWriter("Aluminium.json");
            else if (stock=="Silver")
                out = new PrintWriter("Silver.json");
            else out = new PrintWriter("JakCzegosNieNapiszeToSieNieOdczepiszPrawda.json");
            out.println(mojJson);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
