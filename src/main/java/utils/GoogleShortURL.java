package utils;

import org.apache.http.entity.ContentType;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Abbot
 * Date: 2017-11-02
 * Time: 14:58
 * Description:
 * Google short url
 */
public class GoogleShortURL
{
    /**
     * 基于Google short url 实现短链接
     * 优点，速度快，功能多。
     *
     * @param longUrl
     * @return
     */
    public static String createShortUrl(String longUrl)
    {

        String post = HttpClientUtil.post("https://www.googleapis.com/urlshortener/v1/url?" +
                "key=AIzaSyAdkwBkPoXODWAH3NPf8ZI8lMzR9TlyDRo", "{\"longUrl\":\" " + longUrl + "\"}", ContentType
                .APPLICATION_JSON);
        JSONObject obj = new JSONObject(post);
        String shortUrl = obj.getString("id");
        return shortUrl;
    }


    public static void main(String[] args)
    {
        String shortUrl = createShortUrl("https://github.com/zkydrx/ShortURLTool");

        System.out.println(shortUrl);
    }
}
