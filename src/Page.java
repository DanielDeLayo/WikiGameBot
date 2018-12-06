import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Page extends ImageView
{
	Page parent;
    final String url;

    static long currId = 0;
    long id = 0;
    static String imgGet = "<img[\"A-Za-z_\\-%.\\d\\s=]+src=\"([/A-Za-z_\\-%.\\d\\s]+)\"";
    static Pattern pattern = Pattern.compile(imgGet);

    public static Image getImage(String url)
    {
        //FIXME
        /*Matcher m = pattern.matcher(url);
        while (m.find()) {
            Image a = new Image(m.group(1));
            if (!a.isError())
                return a;
        }*/
        return new Image("https://cdn.mamamia.com.au/wp/wp-content/uploads/2018/04/18161417/shrug-face.jpg");// :)
    }

	Page(String url, Page parent)
	{
		id = currId++;
		this.url = url;
        this.parent = parent;
        //setImage(getImage(url));
        //VisManager.addPage(this);
	}
	Page(String url)
	{
		id = -1;
		this.url = url;
		this.parent = null;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof Page))
			return false;
		return this.url.equals(((Page)o).url);
	}

    @Override
    public int hashCode()
    {
        return url.hashCode();
    }

    @Override
    public String toString()
    {
        return url;
    }
	
	
}
