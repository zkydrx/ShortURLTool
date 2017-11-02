package utils;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServlet;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Abbot
 * Date: 2017-11-02
 * Time: 15:07
 * Description:
 */
public class HttpClientUtil extends HttpServlet
{
    private static CloseableHttpClient httpClient;

    static
    {
        //设置可关闭的httpclient
        try
        {
            TrustManager manager = new X509TrustManager()
            {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException
                {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException
                {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
            };
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[]{manager}, null);
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(context, NoopHostnameVerifier
                    .INSTANCE);

            RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).build();
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create
                    ().register("http", PlainConnectionSocketFactory.INSTANCE)
                                                                                                                       .register("https", socketFactory)
                                                                                                                       .build();
            //创建ConnectionManager，添加Connection配置信息
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager
                    (socketFactoryRegistry);
            httpClient = HttpClients.custom()
                                    .setConnectionManager(connectionManager)
                                    .setDefaultRequestConfig(config)
                                    .build();
            //对请求参数进行编码后再进行发送

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 发送GET请求
     *
     * @param url 请求URL
     * @return 响应字符串
     */
    public static String get(String url)
    {
        return get(url, null, null);
    }


    public static String get(String url, List<NameValuePair> params, Map<String, String> headers)
    {
        String responseStr = null;
        try
        {
            HttpGet httpGet = new HttpGet(url);
            if (params != null && !params.isEmpty())
            {
                String paramStr = EntityUtils.toString(new UrlEncodedFormEntity(params));
                httpGet.setURI(new URI(httpGet.getURI().toString() + "?" + paramStr));
            }
            if (!CollectionUtils.isEmpty(headers))
            {
                for (String key : headers.keySet())
                {
                    httpGet.addHeader(key, headers.get(key));
                }
            }
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            responseStr = EntityUtils.toString(entity, Charset.forName("utf-8"));
            EntityUtils.consume(entity);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return responseStr;
    }

    public static String post(String url, String content, ContentType contentType)
    {
        return post(url, content, contentType, null);
    }

    public static String post(String url, String content, ContentType contentType, Map<String, String> headers)
    {
        String responseStr = null;
        try
        {
            URL urlTmp = new URL(url);
            URI uri = new URI(urlTmp.getProtocol(), null, urlTmp.getHost(), urlTmp.getPort(), urlTmp.getPath(),
                    urlTmp.getQuery(), null);
            HttpPost httpPost = new HttpPost(uri);
            if (!CollectionUtils.isEmpty(headers))
            {
                for (String key : headers.keySet())
                {
                    httpPost.addHeader(key, headers.get(key));
                }
            }
            httpPost.setEntity(new StringEntity(content, contentType));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            responseStr = EntityUtils.toString(entity, contentType.getCharset()).trim();
            EntityUtils.consume(entity);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return responseStr;
    }
}
