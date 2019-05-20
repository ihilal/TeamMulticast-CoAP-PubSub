import org.eclipse.californium.core.WebLink;
import org.eclipse.californium.core.coap.LinkFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class main {

    public static void main(String[] args) throws RuntimeException, IOException, InterruptedException {

        String host = "127.0.0.1";
//        BasicConfigurator.configure();//for logger

        /*constructor*/
        PubSub my = new PubSub(host);

        //create
        System.out.println(my.create("ps", "topic1", 40).getCode().name());
        System.out.println(my.create("ps", "topic3", 0).getCode().name());
        System.out.println(my.create("ps/topic1", "topic4", 0).getCode().name());
        System.out.println(my.create("ps", "topicX", 40).getCode().name());
        System.out.println(my.create("ps/topicX", "topicY", 40).getCode().name());
        System.out.println(my.create("ps/topicX/topicY", "topic4", 0).getCode().name());

        //Discover
        Set<WebLink> w = LinkFormat.parse(my.discover().getResponseText());
        ArrayList<Topic> at = new ArrayList<>();
        Topic.makeArrayList(w, at);
        for (Topic t : at) {
            System.out.println(t.toString());
        }

        System.out.println(my.create(at.get(1).getPathString(), "topicmatias", 0).getCode().name());
        w = LinkFormat.parse(my.discover().getResponseText());
        Topic.makeArrayList(w, at);
        for (Topic t : at) {
            System.out.println(t.toString());
        }
        System.out.println();
        System.out.println(my.discover().getResponseText());

    }
}
