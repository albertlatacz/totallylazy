package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.functions.Curried2;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.regex.Regex;
import com.googlecode.totallylazy.time.Dates;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.util.List;

import static com.googlecode.totallylazy.predicates.Predicates.not;
import static com.googlecode.totallylazy.predicates.Predicates.nullValue;
import static com.googlecode.totallylazy.Sequences.sequence;

public class XPathFunctions {
    @XPathFunction("trim-and-join")
    public static String trimAndJoin(NodeList nodes, String delimiter) {
        return Xml.textContents(nodes).map(Strings.trim()).toString(unescape(delimiter));
    }

    @XPathFunction("string-join")
    public static String stringJoin(NodeList nodes, String delimiter) {
        return Xml.textContents(nodes).toString(unescape(delimiter));
    }

    @XPathFunction("if")
    public static Object IF(NodeList nodeList, Object matched, Object notMatched) {
        return nodeList.getLength() > 0 ? matched : notMatched;
    }

    @XPathFunction("or")
    public static Object or(List<Object> arguments) {
        return sequence(arguments).find(not(nullValue()).and(other -> !(other instanceof NodeList) || ((NodeList) other).getLength() != 0)).getOrNull();
    }

    @XPathFunction("tokenize")
    public static NodeArrayList<Text> tokenize(NodeList input, String pattern) {
        return new NodeArrayList<Text>(Xml.sequence(input).flatMap(split(pattern)));
    }

    @XPathFunction("replace")
    public static NodeArrayList<Text> tokenize(NodeList input, String pattern, String replace) {
        return new NodeArrayList<Text>(Xml.sequence(input).map(replace(pattern, replace)));
    }

    @XPathFunction("time-in-millis")
    public static Long timeInMillis(NodeList dates) {
        Option<String> date = Xml.textContents(dates).headOption();
        return date.map(s -> Dates.parse(s).getTime()).getOrNull();
    }

    @XPathFunction("date-in-millis")
    public static Long dateInMillis(NodeList dates) {
        Option<String> date = Xml.textContents(dates).headOption();
        return date.map(s -> Dates.stripTime(Dates.parse(s)).getTime()).getOrNull();
    }

    private static Function1<Node, Sequence<Text>> split(final String pattern) {
        return node -> Regex.regex(pattern).split(node.getTextContent()).map(createText.apply(node));
    }

    private static Function1<Node, Text> replace(final String pattern, final String replace) {
        return node -> createText(node, node.getTextContent().replaceAll(pattern, replace));
    }

    public static Curried2<Node, String, Text> createText = XPathFunctions::createText;

    public static Text createText(Node nodeInDocument, String text) {return nodeInDocument.getOwnerDocument().createTextNode(text);}

    private static String unescape(String value) {
        return value.replace("\\n", "\n");
    }

}