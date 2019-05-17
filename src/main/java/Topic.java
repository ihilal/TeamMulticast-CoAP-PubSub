import org.eclipse.californium.core.WebLink;

import java.util.ArrayList;
import java.util.Arrays;

public class Topic {

    String name;
    String[] path;
    int ct;

    public Topic(WebLink wl) {
        this.path = wl.getURI().split("(/)");
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

    public Topic(String[] path, int ct){
        this.path = path;
        this.name = path[path.length -1];
        this.ct = ct;
    }

    public ArrayList<Topic> getTopics(Topic topic, ArrayList<Topic> at){

        at.add(topic);

        getTopics(topic.getParent(), at);

        return at;
    }

    public String toString() {
        return "name:  " + this.name + "  path: " + this.path + "  ct: " + this.ct;
    }

    public Topic getParent(){
        ArrayList<String> x = new ArrayList<String>(Arrays.asList(this.path));
        x.remove(x.size() -1);
        Topic parent = new Topic((String[]) x.toArray(), 40);

                return parent;
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
}
