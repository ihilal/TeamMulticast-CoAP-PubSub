import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Token;
import org.eclipse.californium.core.network.RandomTokenGenerator;
import org.eclipse.californium.core.network.config.NetworkConfig;

public class PubSub {

    private static final String SCHEME = "coap";
    private String host;
    private int port;
    private long timeout;
    private NetworkConfig config = NetworkConfig.createStandardWithoutFile();

    public PubSub(String host) {
        this.host = host;
        this.port = 5683;
        this.timeout = 5000;
    }

    public PubSub(String host, int port, long timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    public NetworkConfig getConfig() {
        return config;
    }

    public void setConfig(NetworkConfig config) {
        this.config = config;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /* Returns array of Topic objects and Confirmation Code*/
    public CoapResponse discover() throws  RuntimeException {
        return discover("");
    }

    public CoapResponse discover(String query) throws RuntimeException {
        Request discover = Request.newGet();
        discover.getOptions().setUriPath(".well-known/core?" + query);

        CoapClient client = new CoapClient(SCHEME, this.getHost(), this.getPort());
        client.setTimeout(this.timeout);

        CoapResponse response = client.advanced(discover);

        return response;
    }

    /* Returns topic and Confirmation Code */
    public CoapResponse create(String name, int ct, String... uri) throws RuntimeException {

        CoapClient client = new CoapClient(SCHEME, this.getHost(), this.getPort(), uri);
        client.setTimeout(this.timeout);

        StringBuilder sb = new StringBuilder().append("<").append(name).append(">;ct=").append(ct);
        String payload = sb.toString();

        Request req = Request.newPost();
        req.setPayload(payload);
        req.getOptions().setContentFormat(ct);

        CoapResponse res = client.advanced(req);

        return res;
    }

    /* Returns Confirmation Code */
    public CoapResponse publish(String payload, int ct, String... uri) throws RuntimeException {
        CoapClient client = new CoapClient(SCHEME, this.getHost(), this.getPort(), uri);
        client.setTimeout(this.timeout);

        CoapResponse res = client.put(payload, ct);

        return res;
    }

    /* Returns Content and Confirmation Code */
    public CoapResponse read(String... uri) throws RuntimeException {
        CoapClient client = new CoapClient(SCHEME, this.getHost(), this.getPort(), uri);
        client.setTimeout(this.timeout);

        CoapResponse res = client.get();

        return res;
    }

    /* Returns Confirmation Code */
    public CoapResponse remove(String... uri) throws RuntimeException {

        CoapClient client = new CoapClient(SCHEME, this.getHost(), this.getPort(), uri);
        client.setTimeout(this.timeout);
        CoapResponse res = client.delete();

        return res;
    }

    public class Subscription {
        private CoapClient client;
        private CoapObserveRelation relation;
        private String[] uri;
        private CoapHandler handler;

        //Constructor, does not subscribe
        public Subscription(CoapHandler handler, String... uri) {
            this.uri = uri;
            this.handler = handler;
            this.relation = null;
            this.client = null;
        }

        public CoapHandler getHandler() {
            return handler;
        }

        public void setHandler(CoapHandler handler) {
            this.handler = handler;
        }

        //call this method to subscribe, can use it to subscribe to same topic again
        public void subscribe() throws RuntimeException {

            Request req = new Request(CoAP.Code.GET);

            client = new CoapClient(SCHEME, getHost(), getPort(), uri);
            client.useExecutor();
            client.setTimeout(timeout);

            req.setURI(client.getURI());
            req.setObserve();

            config.set(NetworkConfig.Keys.TOKEN_SIZE_LIMIT, 4);
            RandomTokenGenerator rand = new RandomTokenGenerator(config);
            Token token = rand.createToken(false);
            req.setToken(token);

            relation = client.observe(req, handler);
        }

        //call to unsubscribe
        public void unsubscribe() {
            if (this.relation != null) {
                relation.proactiveCancel();
                int mid = relation.getCurrent().advanced().getMID();
                while (relation.getCurrent().advanced().getMID() == mid) ;
            }
            if (this.client != null)
                client.shutdown();
        }
    }
}
