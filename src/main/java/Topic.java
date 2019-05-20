import org.eclipse.californium.core.WebLink;

import java.util.ArrayList;
import java.util.Set;

public class Topic {

    String name;
    String[] path;
    int ct;

    public Topic(WebLink wl) {
        this.path = wl.getURI().substring(1).split("(/)");
        this.name = this.path[this.path.length - 1];
        int corchete = wl.toString().indexOf('[');

        try {
            StringBuilder sb = new StringBuilder().append(wl.toString().charAt(corchete + 1)).append(wl.toString().charAt(corchete + 2));
            this.ct = Integer.parseInt(sb.toString());
        } catch (NullPointerException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            StringBuilder sb = new StringBuilder().append(wl.toString().charAt(corchete + 1));
            this.ct = Integer.parseInt(sb.toString());
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
        return "name: " + this.name + "\n   |path: " + this.getPathString() + "\n       |ct: " + this.ct;
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
