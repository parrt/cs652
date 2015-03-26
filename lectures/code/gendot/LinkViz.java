/***
 * Excerpted from "Language Implementation Patterns",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/tpdsl for more book information.
***/

import java.util.ArrayList;
import java.util.List;

public class LinkViz {
    public List<Link> links = new ArrayList<>();

    public static class Link {
        public String from;
        public String to;
        public Link(String from, String to) {this.from = from; this.to = to;}
    }

    public void addEdge(String from, String to) {
        links.add(new Link(from,to));
    }
}
