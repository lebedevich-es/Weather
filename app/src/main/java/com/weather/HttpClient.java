package com.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {

    public String get(final String url) {
        String response = null;
        try {
            final URL reqUrl = new URL(url);
            final HttpURLConnection connection = ((HttpURLConnection) reqUrl.openConnection());
            connection.setRequestMethod("GET");
            final InputStream inputStream = connection.getInputStream();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            final StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            response = stringBuilder.toString();
            inputStream.close();
            reader.close();
            connection.disconnect();

        } catch (final IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
