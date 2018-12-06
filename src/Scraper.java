
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scraper extends Application
{
	static String random = "https://en.wikipedia.org/wiki/Special:Random";

    static String randRegex = "class=\\\"firstHeading\\\" lang=\"en\">([A-Za-z_\\-%.\\d\\s]+)<";
	static String regex = "href=\"(\\/wiki\\/[A-Za-z_\\-%.\\d]+)\"";

    static String prefix = "https://en.wikipedia.org";

    static String hitler = "https://en.wikipedia.org/wiki/Adolf_Hitler";

	static String mainPage = "https://en.wikipedia.org/wiki/Main_Page";

	static String WhatLinksHere = "/w/index.php?title=Special:WhatLinksHere/";
    static String WhatLinks5k = "&limit=5000";
    static Pattern p = Pattern.compile(regex);
    static Pattern pRand = Pattern.compile(randRegex);

	static public boolean containsLink(String line)
	{
        Matcher m = p.matcher(line);
        return m.find();
	}
	
	static public ArrayList<String> stripLink(String line)
	{
	    ArrayList<String> s = new ArrayList<>();
        Matcher m = p.matcher(line);
        while(m.find())
            s.add(prefix + m.group(1));
        return s;
	}
	
	static public String reverseLink(String link)
	{
		int i = link.lastIndexOf("/")+1;
		return (link.substring(0, link.lastIndexOf("/wiki/")) + WhatLinksHere + link.substring(i) + WhatLinks5k);
	}

	static String makeLinkFromTitle(String title)
    {
        try {
            return prefix + "/wiki/" + URLEncoder.encode(title.replace(' ', '_'), StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    static String getTitle(String url)
    {
        return url.substring(url.lastIndexOf("/")+1);
    }

	static String getRandomPage()
    {
        URL link = null;
        try
        {
            link = new URL(random);

            InputStream is = link.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                Matcher m = pRand.matcher(line);
                if (m.find())
                    return makeLinkFromTitle(m.group(1));
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getRandomPage(); // :)
    }
	
	static public Set<String> scrape(String url)
	{

		Set<String> urls = new HashSet<>();
		try {
            Thread.sleep(100); //respect wikipedia
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            URL link = new URL(url);
            InputStream is = link.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                if (containsLink(line))
                    urls.addAll(stripLink(line));
            }

		}
		catch (Exception e) {
            e.printStackTrace();
            return urls;
        }
        urls.remove(mainPage);
		return urls;
	}

    public static void main(String[] args)
	{
        /*ArrayList<String> path = Searches.BiBFS (getRandomPage(), getRandomPage());
        if (path == null)
        {
            System.out.println("No path found!");
        }
        else
        {
            System.out.println(path);
        }*/
        launch(args);
	}

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane p = new BorderPane();
        primaryStage.setTitle("Wikipedia Game Bot");
        primaryStage.setScene(new Scene(p, 845, 600));


        Pane startPane = new Pane();
        TextField startField = new TextField();
        startField.setPrefWidth(300);

        ToggleGroup startG = new ToggleGroup();
        RadioButton textStart = new RadioButton("Text");
        RadioButton randStart = new RadioButton("Random");

        textStart.setToggleGroup(startG);
        randStart.setToggleGroup(startG);
        randStart.setSelected(true);

        Text startText = new Text("Start:");

        startPane.setMinHeight(150);
        startField.setTranslateY(15);
        textStart.setTranslateY(55);
        textStart.setTranslateX(0);
        randStart.setTranslateY(55);
        randStart.setTranslateX(100);

        startPane.getChildren().addAll(startText, startField, textStart, randStart);

        Pane stopPane = new Pane();
        TextField stopField = new TextField();
        stopField.setPrefWidth(300);

        ToggleGroup stopG = new ToggleGroup();
        RadioButton textStop = new RadioButton("Text");
        RadioButton randStop = new RadioButton("Random");
        RadioButton hitlerStop = new RadioButton("Hitler");


        textStop.setToggleGroup(stopG);
        randStop.setToggleGroup(stopG);
        hitlerStop.setToggleGroup(stopG);
        randStop.setSelected(true);

        Text stopText = new Text("Stop:");

        stopPane.setMinHeight(150);
        stopField.setTranslateY(15);
        textStop.setTranslateY(55);
        textStop.setTranslateX(0);
        randStop.setTranslateY(55);
        randStop.setTranslateX(100);
        hitlerStop.setTranslateY(55);
        hitlerStop.setTranslateX(200);

        stopPane.getChildren().addAll(stopText, stopField, textStop, randStop, hitlerStop);

        Pane optionPane = new Pane();

        SplitMenuButton options = new SplitMenuButton();
        MenuItem mBFS = new MenuItem("BFS");
        final String sBFS = "Search! BFS  ";
        mBFS.setOnAction(event -> options.setText(sBFS));

        MenuItem mIDDFS = new MenuItem("IDDFS");
        final String sIDDFS = "Search! IDDFS";
        mIDDFS.setOnAction(event -> options.setText(sIDDFS));

        MenuItem mBi = new MenuItem("BiBFS");
        final String sBi = "Search! BiBFS";
        mBi.setOnAction(event -> options.setText(sBi));

        options.getItems().addAll(mBFS, mIDDFS, mBi);
        options.setText(sBFS);
        options.setTranslateY(15);
        options.setPrefWidth(145);

        options.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String startURL;
                String stopURL;
                if (textStart.isSelected())
                {
                    startURL = makeLinkFromTitle(startField.getText());
                }
                else
                {
                    startURL = getRandomPage();
                }

                if (textStop.isSelected())
                {
                    stopURL = makeLinkFromTitle(stopField.getText());
                }
                else if (randStop.isSelected())
                {
                    stopURL = getRandomPage();
                }
                else
                {
                    stopURL = hitler;
                }

                ArrayList<String> path;
                VisManager.resetManager();
                switch (options.getText())
                {
                    case sBFS:
                        path = Searches.BFS(startURL, stopURL);
                        break;
                    case sIDDFS:
                        path = Searches.IDDFS(startURL, stopURL);
                        break;
                    case sBi:
                        path = Searches.BiBFS(startURL, stopURL);
                        break;
                    default:
                        path = null;
                }
                if (path == null)
                {
                    System.out.println("no");
                }
                else
                {
                	Page oldP = new Page(path.get(0), null);
                	VisManager.addPage(oldP);
					for (int i =1; i < path.size(); i++)
					{
						oldP = new Page(path.get(i), oldP);
						VisManager.addPage(oldP);
					}
                	VisManager.refresh();
                    System.out.println(path);
                }
            }
        });

        optionPane.getChildren().addAll(options);

        FlowPane bot = new FlowPane();

        p.setCenter(VisManager.getManager());
        bot.setHgap(50);
        bot.getChildren().addAll(startPane, optionPane, stopPane);
        p.setBottom(bot);

        primaryStage.show();

    }
}