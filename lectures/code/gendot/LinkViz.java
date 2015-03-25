/***
 * Excerpted from "Language Implementation Patterns",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/tpdsl for more book information.
***/

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LinkViz {
    STGroup templates;

    List<Link> links = new ArrayList<>();
    public static class Link {
        String from;
        String to;
        public Link(String from, String to) {this.from = from; this.to = to;}
    }

    public LinkViz() throws IOException {
        templates = new STGroupFile("DOT.stg");
    }

    public void addLink(String from, String to) {
        links.add(new Link(from,to));
    }

    public String toString() {
        ST fileST = templates.getInstanceOf("file");
        fileST.add("gname", "testgraph");
        for (Link x : links) {
			ST edgeST = templates.getInstanceOf("edge");
            edgeST.add("from", x.from);
            edgeST.add("to", x.to);
            fileST.add("edges", edgeST);
        }
        return fileST.render(); // render (eval) template to text
    }
}
