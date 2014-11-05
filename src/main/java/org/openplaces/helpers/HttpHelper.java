package org.openplaces.helpers;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HttpHelper {
	
	HttpHost proxy = null;
	
	public void setHttpProxy(String host, int port){
		this.proxy = new HttpHost(host, port);
	}
	
	
	private HttpClient buildClient() {

        HttpClient client = null;

        if (proxy != null) {
            // WORKS in java version but not android
            HttpClientBuilder clientBuilder = HttpClientBuilder.create();

            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            clientBuilder.setRoutePlanner(routePlanner);

            client = clientBuilder.build();
        } else {
            client = new DefaultHttpClient();
        }

        return client;
    }

	public String encodeString(String string){
		try {
			return URLEncoder.encode(string, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return string;
	}
	
	public String doGET(String url){
		System.out.println("Calling: " + url);
		
		HttpClient client = this.buildClient();
		HttpGet request = new HttpGet(url);
		

		// add request header
		try {
			HttpResponse response = client.execute(request);

			System.out.println("Response Code : " 
		                + response.getStatusLine().getStatusCode());
		 
			BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
		 
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
			return result.toString();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}