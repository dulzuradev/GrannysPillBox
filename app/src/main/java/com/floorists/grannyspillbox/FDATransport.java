package com.floorists.grannyspillbox;
import android.util.Log;
import com.floorists.grannyspillbox.classes.Medication;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FDATransport {

    public static Future<Medication> getMedInfo(final String medName) {


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Medication> callable = new Callable<Medication>() {
            @Override
            public Medication call() throws Exception {
                try {
                    URL url = new URL("https://api.fda.gov/drug/ndc.json?search=generic_name:" + medName);
                    URLConnection urlConnection = url.openConnection();
                    InputStream is = new BufferedInputStream(urlConnection.getInputStream());
                    JSONObject json = getJsonObject(is);

                    JSONArray results = json.getJSONArray("results");
                    JSONArray packageInfo = results.getJSONObject(0).getJSONArray("packaging");
                    String description = packageInfo.getJSONObject(0).get("description").toString();
                    String name = results.getJSONObject(0).get("generic_name").toString();
                    String imageUrl = getImageUrl(medName);

                    Medication med = new Medication();
                    med.setDescription(description);
                    med.setImageUrl(imageUrl);
                    med.setName(name);

                    return med;

                } catch(IOException | JSONException e) {
                    Log.e("getMedInfo", "request failed", e);
                    return null;
                }
            }

        };

        Future<Medication> future = executor.submit(callable);
        executor.shutdown();
        return future;
    }


    public static String getImageUrl(String medName) {

        try {
            URL url = new URL("https://rximage.nlm.nih.gov/api/rximage/1/rxnav?name=" + medName);
            URLConnection urlConnection = url.openConnection();
            InputStream is = new BufferedInputStream(urlConnection.getInputStream());
            JSONObject json = getJsonObject(is).getJSONArray("nlmRxImages").getJSONObject(0);
            return json.get("imageUrl").toString();
        } catch (Exception e) {
            Log.e("getImageUrl", "request failed", e);
            return null;
        }
    }

    //Returns a json object from an input stream
    private static JSONObject getJsonObject(InputStream inputStream){

        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());

            //returns the json object
            return jsonObject;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // if something went wrong, return null
        return null;
    }
}
