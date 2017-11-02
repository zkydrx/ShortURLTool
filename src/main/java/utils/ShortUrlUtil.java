package utils;

import com.google.common.collect.Lists;
import dto.ShortUrlDto;

import org.springframework.util.Assert;
import com.alibaba.fastjson.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Abbot
 * Date: 2017-11-02
 * Time: 16:21
 * Description:
 * 基于新浪网的短链接工具
 */
public class ShortUrlUtil
{
    public static String convertShortUrl(String longUrl) throws Exception
    {
        return convertShortUrl(Lists.<String>newArrayList(longUrl)).get(0);
    }

    public static List<String> convertShortUrl(List<String> longUrls) throws Exception
    {
        Assert.notEmpty(longUrls);
        String urlParams = "";
        for (String longUrl : longUrls)
        {
            urlParams += "&url_long=" + longUrl;
        }
        String response = HttpClientUtil.get("http://api.t.sina.com.cn/short_url/shorten.json?source=3271760578" +
                urlParams);
        List<ShortUrlDto> shortUrlDtos = JSONObject.parseArray(response, ShortUrlDto.class);
        Assert.notEmpty(shortUrlDtos);
        List<String> shortUrls = Lists.newArrayList();
        for (ShortUrlDto shortUrlDto : shortUrlDtos)
        {
            shortUrls.add(shortUrlDto.getUrl_short());
        }
        return shortUrls;
    }


    public static void main(String[] args) throws Exception
    {
        String shortUrl = convertShortUrl("https://github.com/zkydrx/ShortURLTool");
        System.out.println(shortUrl);
    }
}
