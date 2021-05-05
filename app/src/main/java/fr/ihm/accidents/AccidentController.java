package fr.ihm.accidents;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class AccidentController extends Observable {

    private int last = 0;
    private final AccidentModel model;
    private final Handler handler;
    private final Handler mainHandler;

    public AccidentController(AccidentModel model) {
        this.model = model;
        HandlerThread handlerThread = new HandlerThread("MyLocationThread");
        handlerThread.setDaemon(true);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void refresh() {
        handler.postDelayed(() -> {
            try
            {
                URL Url = new URL("https://domino.zdimension.fr/web/ihm.php?after=" + last);
                URLConnection urlConnection = Url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String str = bufferedReader.readLine();
                JSONArray obj = new JSONArray(str);
                last += obj.length();
                //Log.i("SERVICE", obj.toString());
                List<Accident> newAccidents = new ArrayList<>();
                for (int i = 0; i < obj.length(); i++)
                {
                    // JSONObject item = obj.getJSONObject(i);
                    // NotificationHelper.sendAccidentNotif(this, item.getInt("distance"),
                    // item.getString("description"));
                    JSONObject item = obj.getJSONObject(i);
                    Accident accident = new Accident(item.getDouble("latitude"), item.getDouble("longitude"));
                    model.addAccident(accident);
                    DemarageAplication.accidentsNotications.add(item);
                    newAccidents.add(accident);
                }
                this.mainHandler.post(() -> {
                    this.setChanged();
                    this.notifyObservers(newAccidents);
                });
                //return str;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }, 1);
    }

}
