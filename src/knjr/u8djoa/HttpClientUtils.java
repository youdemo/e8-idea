//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package knjr.u8djoa;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientUtils {
    private static final String ENCODING = "UTF-8";
    private static final int CONNECT_TIMEOUT = 6000;
    private static final int SOCKET_TIMEOUT = 6000;

    public HttpClientUtils() {
    }

    public static HttpClientResult doGet(String url) throws Exception {
        return doGet(url, (Map)null, (Map)null);
    }

    public static HttpClientResult doGet(String url, Map<String, String> params) throws Exception {
        return doGet(url, (Map)null, params);
    }

    public static HttpClientResult doGet(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URIBuilder uriBuilder = new URIBuilder(url);
        Entry httpResponse;
        if (params != null) {
            Set<Entry<String, String>> entrySet = params.entrySet();
            Iterator i$ = entrySet.iterator();

            while(i$.hasNext()) {
                httpResponse = (Entry)i$.next();
                uriBuilder.setParameter((String)httpResponse.getKey(), (String)httpResponse.getValue());
            }
        }

        HttpGet httpGet = new HttpGet(uriBuilder.build());
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(6000).setSocketTimeout(6000).build();
        httpGet.setConfig(requestConfig);
        packageHeader(headers, httpGet);
        httpResponse = null;

        HttpClientResult var8;
        try {
            var8 = getHttpClientResult(httpResponse, httpClient, httpGet);
        } finally {
            release(httpResponse, httpClient);
        }

        return var8;
    }

    public static HttpClientResult doPost(String url) throws Exception {
        return doPost(url, (Map)null, (Map)null);
    }

    public static HttpClientResult doPost(String url, Map<String, String> params) throws Exception {
        return doPost(url, (Map)null, params);
    }

    public static HttpClientResult doPost(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(6000).setSocketTimeout(6000).build();
        httpPost.setConfig(requestConfig);
        packageHeader(headers, httpPost);
        packageParam(params, httpPost);
        Object httpResponse = null;

        HttpClientResult var7;
        try {
            var7 = getHttpClientResult((CloseableHttpResponse)httpResponse, httpClient, httpPost);
        } finally {
            release((CloseableHttpResponse)httpResponse, httpClient);
        }

        return var7;
    }

    public static HttpClientResult doPut(String url) throws Exception {
        return doPut(url);
    }

    public static HttpClientResult doPut(String url, Map<String, String> params) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(6000).setSocketTimeout(6000).build();
        httpPut.setConfig(requestConfig);
        packageParam(params, httpPut);
        Object httpResponse = null;

        HttpClientResult var6;
        try {
            var6 = getHttpClientResult((CloseableHttpResponse)httpResponse, httpClient, httpPut);
        } finally {
            release((CloseableHttpResponse)httpResponse, httpClient);
        }

        return var6;
    }

    public static HttpClientResult doDelete(String url) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(6000).setSocketTimeout(6000).build();
        httpDelete.setConfig(requestConfig);
        Object httpResponse = null;

        HttpClientResult var5;
        try {
            var5 = getHttpClientResult((CloseableHttpResponse)httpResponse, httpClient, httpDelete);
        } finally {
            release((CloseableHttpResponse)httpResponse, httpClient);
        }

        return var5;
    }

    public static HttpClientResult doDelete(String url, Map<String, String> params) throws Exception {
        if (params == null) {
            params = new HashMap();
        }

        ((Map)params).put("_method", "delete");
        return doPost(url, (Map)params);
    }

    public static void packageHeader(Map<String, String> params, HttpRequestBase httpMethod) {
        if (params != null) {
            Set<Entry<String, String>> entrySet = params.entrySet();
            Iterator i$ = entrySet.iterator();

            while(i$.hasNext()) {
                Entry<String, String> entry = (Entry)i$.next();
                httpMethod.setHeader((String)entry.getKey(), (String)entry.getValue());
            }
        }

    }

    public static void packageParam(Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod) throws UnsupportedEncodingException {
        if (params != null) {
            List<NameValuePair> nvps = new ArrayList();
            Set<Entry<String, String>> entrySet = params.entrySet();
            Iterator i$ = entrySet.iterator();

            while(i$.hasNext()) {
                Entry<String, String> entry = (Entry)i$.next();
                nvps.add(new BasicNameValuePair((String)entry.getKey(), (String)entry.getValue()));
            }

            httpMethod.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        }

    }

    public static HttpClientResult getHttpClientResult(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient, HttpRequestBase httpMethod) throws Exception {
        httpResponse = httpClient.execute(httpMethod);
        if (httpResponse != null && httpResponse.getStatusLine() != null) {
            String content = "";
            if (httpResponse.getEntity() != null) {
                content = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            }

            return new HttpClientResult(httpResponse.getStatusLine().getStatusCode(), content);
        } else {
            return new HttpClientResult(500);
        }
    }

    public static void release(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) throws IOException {
        if (httpResponse != null) {
            httpResponse.close();
        }

        if (httpClient != null) {
            httpClient.close();
        }

    }
}
