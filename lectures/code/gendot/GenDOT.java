import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

/***
 * Excerpted from "Language Implementation Patterns",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/tpdsl for more book information.
***/
public class GenDOT {
    public static void main(String[] args) throws Exception {
        LinkViz graph = new LinkViz();
        graph.addEdge("index.html", "login.html");
        graph.addEdge("index.html", "about.html");
        graph.addEdge("login.html", "error.html");
        graph.addEdge("about.html", "news.html");
        graph.addEdge("index.html", "news.html");
        graph.addEdge("logout.html", "index.html");
        graph.addEdge("index.html", "logout.html");
        System.out.println(gen2(graph));
    }

    public static String gen(LinkViz graph) {
        STGroup templates = new STGroupFile("DOT.stg");
        ST fileST = templates.getInstanceOf("file");
        fileST.add("gname", "testgraph");
        for (LinkViz.Link x : graph.links) {
            ST edgeST = templates.getInstanceOf("edge");
            edgeST.add("from", x.from);
            edgeST.add("to", x.to);
            fileST.add("edges", edgeST);
        }
        return fileST.render(); // render (eval) template to text
    }

    public static String gen2(LinkViz graph) {
        STGroup templates = new STGroupFile("DOT2.stg");
        ST fileST = templates.getInstanceOf("file");
        fileST.add("gname", "testgraph");
        fileST.add("graph", graph);
        return fileST.render(); // render (eval) template to text
    }
}
