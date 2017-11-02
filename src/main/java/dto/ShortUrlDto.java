package dto;

/**
 * Created with IntelliJ IDEA.
 * User: Abbot
 * Date: 2017-11-02
 * Time: 16:28
 * Description:
 */
public class ShortUrlDto
{
    private String url_short;
    private String url_long;

    public String getUrl_short()
    {
        return url_short;
    }

    public void setUrl_short(String url_short)
    {
        this.url_short = url_short;
    }

    public String getUrl_long()
    {
        return url_long;
    }

    public void setUrl_long(String url_long)
    {
        this.url_long = url_long;
    }
}
