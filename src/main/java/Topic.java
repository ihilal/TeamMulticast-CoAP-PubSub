import org.eclipse.californium.core.WebLink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

public class Topic {

    String name;
    String[] path;
    int ct;

    public Topic(WebLink wl) {
        this.path = wl.getURI().substring(1).split("(/)");
        this.name = this.path[this.path.length - 1];
        String corchete = wl.toString().substring(wl.toString().indexOf('[') + 1);
        try {
            this.ct = Integer.parseInt(corchete.substring(0, 1));
        } catch (NullPointerException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            this.ct = Integer.parseInt(corchete.substring(0, 0));
        }
    }

    public Topic(String[] path, int ct) {
        this.path = path;
        this.name = path[path.length - 1];
        this.ct = ct;
    }

    public static void makeArrayList(Set<WebLink> swl, ArrayList<Topic> at) {
        for (WebLink wl : swl) {
            Topic t = new Topic(wl);
            t.getTopics(t, at);
        }
        Collections.sort(at, new TopicComparator());
    }

    public void getTopics(Topic topic, ArrayList<Topic> at) {

        if (topic == null) {
            return;
        }
        boolean flag = false;
        for (Topic x :
                at) {
            if (topic.equals(x)) {
                flag = true;
            }
        }
        if (!flag) {
            at.add(topic);
        }
        getTopics(topic.getParent(), at);
    }

    public String toString() {
        return this.getPathString() + "                 |ct: " + this.ct;
    }

    public Topic getParent() {
        String[] ps = new String[this.path.length - 1];
        for (int i = 0; i < this.path.length - 1; i++) {
            ps[i] = this.path[i];
        }
        if (ps.length != 0) {
            Topic parent = new Topic(ps, 40);
            return parent;
        }
        return null;
    }

    public int getCt() {
        return ct;
    }

    public String getName() {
        return name;
    }

    public String[] getPath() {
        return path;
    }

    public String getPathString() {
        StringBuilder sb = new StringBuilder();
        for (String s : this.getPath()) {
            sb.append(s).append("/");
        }
        String p = sb.toString();
        return p = p.substring(0, p.length() - 1);
    }

    public boolean equals(Topic topic) {
        return (this.getName().equals(topic.getName()) && this.getPathString().equals(topic.getPathString()));
    }

}

class TopicComparator implements Comparator<Topic> {
    public int compare(Topic o1, Topic o2) {
        boolean value1 = o1.getPath().length >= (o2.getPath().length);
        if (!value1) {
            int value2 = o1.getPathString().compareTo(o2.getPathString());
            return value2;
        }
        if (value1) {
            return 1;
        } else {
            return 0;
        }
    }
}
