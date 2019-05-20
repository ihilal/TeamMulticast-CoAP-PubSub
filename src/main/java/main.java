import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.WebLink;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.LinkFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class main {

    public static void main(String[] args) throws RuntimeException, IOException, InterruptedException {

        String host = "127.0.0.1";

        /*constructor*/
        PubSub my = new PubSub(host);

        /* ArrayList to store Topics */
        ArrayList<Topic> at = new ArrayList<>();

        //create
        System.out.println(my.create("topic1" ,40,"ps").getCode().name());
        System.out.println(my.create("topic3", 0,"ps").getCode().name());
        System.out.println(my.create("topic4", 0,"ps/topic1").getCode().name());
        System.out.println(my.create( "topicX", 40,"ps").getCode().name());
        System.out.println(my.create( "topicY", 40,"ps/topicX").getCode().name());
        System.out.println(my.create( "topic4", 0,"ps/topicX/topicY").getCode().name());

        //Discover
        Set<WebLink> w = LinkFormat.parse(my.discover().getResponseText());
        Topic.makeArrayList(w, at);
        for (Topic t : at) {
            System.out.println(t.toString());
        }

        System.out.println(my.create("topicmatias", 0,at.get(1).getPath()).getCode().name());
        w = LinkFormat.parse(my.discover().getResponseText());
        Topic.makeArrayList(w, at);
        for (Topic t : at) {
            System.out.println(t.toString());
        }
        System.out.println();
        System.out.println(my.discover().getResponseText());

        Topic tp = at.get(5);
        System.out.println(tp);
        System.out.println(my.remove(tp.getPath()).getCode().name());

        w = LinkFormat.parse(my.discover().getResponseText());
        Topic.makeArrayList(w, at);
        for (Topic t : at) {
            System.out.println(t.toString());
        }
        System.out.println();
        System.out.println(my.discover().getResponseText());
    }
}
