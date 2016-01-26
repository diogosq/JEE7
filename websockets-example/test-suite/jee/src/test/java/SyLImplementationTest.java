import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
public class SyLImplementationTest extends BaseTestImplementation {

    private static final Logger log = LoggerFactory.getLogger(SyLImplementationTest.class);
    private static final String WEBSOCKET_URI = "ws://localhost:8080/websockets-war-syl/console";
    private static final String REQUEST_URL = "http://localhost:8080/websockets-war-syl/websockets/insert";

    private static final String QUERY_PARAM_NAME = "log";
    private static final String QUERY_PARAM_VALUE = "10:22:56,694%20INFO%20[org.jboss.weld.deployer]%20(MSC%20service%20thread%201-7)%20WFLYWELD0009:%20Starting%20weld%20service%20for%20deployment%20logger-war-1.0.1-SNAPSHOT.war";

    @Deployment(name = "websockets-war-syl", testable = false)
    public static WebArchive createSylDeployment() {
        final WebArchive war = createDeployment("websockets-war-syl.war");
        war.addPackage("org.dekstroza.websockets.partitioning.impl.synchronizedlist");
        return war;

    }

    @RunAsClient
    @Test
    public void testSingleRequestToMultipleClients() throws Exception {

        singleRequestToMultipleClients(WEBSOCKET_URI, REQUEST_URL, QUERY_PARAM_NAME, QUERY_PARAM_VALUE);
    }

}
