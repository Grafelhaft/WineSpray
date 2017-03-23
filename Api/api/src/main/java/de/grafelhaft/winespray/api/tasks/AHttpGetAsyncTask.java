package de.grafelhaft.winespray.api.tasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import de.grafelhaft.grafellib.async.http.AHttpAsyncTask;
import de.grafelhaft.grafellib.async.http.HttpResponse;
import de.grafelhaft.winespray.api.Response;

/**
 * Created by Markus on 16.09.2016.
 */
public abstract class AHttpGetAsyncTask<Progress, Output> extends AHttpAsyncTask<Void, Progress, Response, Output> {

    public AHttpGetAsyncTask(Context context, String baseUrl) {
        this.context = context;
        this.baseUrl = baseUrl;
    }

    @Override
    protected synchronized boolean connect(String s) {
        if (super.checkIfConnected(this.context)) {
            try {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    final ConnectivityManager connectivityManager = (ConnectivityManager) this.context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkRequest.Builder request = new NetworkRequest.Builder();
                    request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);

                    try {
                        connectivityManager.registerNetworkCallback(request.build(), new ConnectivityManager.NetworkCallback() {

                            @Override
                            public void onAvailable(Network network) {
                                super.onAvailable(network);
                                try {
                                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                                        connectivityManager.setProcessDefaultNetwork(network);
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        connectivityManager.bindProcessToNetwork(network);
                                    }
                                } catch (IllegalStateException e) {
                                    //Fuck this shit
                                }
                            }
                        });
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        //Too many Network Requests filed
                        notifyConnectionResponse(new HttpResponse(HttpURLConnection.HTTP_BAD_GATEWAY, e.toString()));
                    }
                }

                url = new URL(baseUrl);

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    notifyConnectionResponse(new HttpResponse(httpURLConnection.getResponseCode(), httpURLConnection.getResponseMessage()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    protected Response doInBackground(Void... params) {
        if (this.connect(baseUrl)) {
            try {
                return read();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                this.disconnect();
            }
        }
        return null;
    }

    Response read() throws IOException {
        BufferedReader reader = null;
        String jsonString = null;

        InputStream inputStream = httpURLConnection.getInputStream();
        StringBuilder buffer = new StringBuilder();
        if (inputStream == null) {
            return null;
        }

        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        while ((line = reader.readLine()) != null) {
            buffer.append(line).append("\n");
        }

        if (buffer.length() == 0) {
            return null;
        }

        jsonString = buffer.toString();
        int code = httpURLConnection.getResponseCode();

        return new Response("", jsonString, code);
    }
}
