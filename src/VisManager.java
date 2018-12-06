import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by dande_000 on 12/5/2018.
 */
public class VisManager extends Pane {
	
    private static VisManager man = null;
    private WebView view = new WebView();
    private Set<Page> nodes = new HashSet<>();
    public static VisManager getManager()
    {
        if (man == null)
            man = new VisManager();
        return man;

    }

    public VisManager()
    {
    	view.setPrefWidth(1000);
        this.getChildren().add(view);
    }

    public static void addPage(Page p)
    {
        getManager().nodes.add(p);
    }

    public static void refresh()
    {
        getManager().view.getEngine().loadContent(getManager().prepareHTML());
    }

    public static void resetManager()
    {
    	Page.currId = 0;
        getManager().nodes.clear();
        refresh();
    }

    private static String makeNode(Page p)
    {
        return "{id: " + p.id +", label: \"" + Scraper.getTitle(p.url) +"\"}";
    }

    private static String makeEdge(Page p)
    {
        return "{from: " + p.parent.id + ", to: "+ p.id + "}";
    }

    private String prepareHTML()
    {
        System.out.println("Preparing " + nodes.size() + " Nodes.");
        File f = new File("visjsinterface.html");
        String content = "";
        try (Scanner fin = new Scanner(f))
        {
            while(fin.hasNext())
                content += fin.nextLine();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return e.getMessage();
        }
        content += "\n<script type=\"text/javascript\">\n var nodes = new vis.DataSet([\n";
        for (Page n : nodes)
        {
            content += "\n" + makeNode(n) + ",";
        }
        content = content.substring(0, content.length()-1) + "\n";
        content += "]);";
        content += "\n var edges = new vis.DataSet([\n";
        for (Page n : nodes)
        {
        	if (n.parent != null)
                content += "\n" + makeEdge(n) + ",";
        }
        content = content.substring(0, content.length()-1) + "\n";
        content += "]);\nvar data = {\n" +
                "    nodes: nodes,\n" +
                "    edges: edges\n" +
                "  };\n";
        content += "var options = {layout: {improvedLayout: true}};\n" +
                " \n" +
                "    var container = document.querySelector('.network');\n" +
                " \n" +
                "    network = new vis.Network(container, data, options);\n" +
                " \n" +
                "</script>";
        System.out.println("Prepared " + nodes.size() + " Nodes.");
        File out = new File("out.html");
        try (FileWriter fout = new FileWriter(out)) {
            fout.write(content);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return content;
    }

}
