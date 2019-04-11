package back;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

import java.util.List;

import static org.neo4j.driver.v1.Values.parameters;
@Deprecated
public class ConnectorToNeo implements AutoCloseable {
    private final Driver driver;

    public ConnectorToNeo(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

//    public void getData() {
//        GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();
//        GraphDatabaseService graphDb = graphDbFactory.newEmbeddedDatabase(
//                new File("data/cars"));
//        Result result = graphDb.execute(
//                "MATCH (company:Company)-[:owns]-> (car:Car)" +
//                        "WHERE car.make='tesla' and car.model='modelX'" +
//                        "RETURN company.name");
//    }

//    public List<Item> setDataTest() {
//
//    }

    public void printGreeting(final String message) {
        try (Session session = driver.session()) {
            String greeting = session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction tx) {
                    StatementResult result = tx.run("Match (n)->[r]->(m) return r");

                    return result.single().get(0).asString();
                }
            });
            System.out.println(greeting);
        }
    }

   static public void test()  {
        try {
       ConnectorToNeo greeter = new ConnectorToNeo("bolt://localhost:7687", "neo4j", "123");
           greeter.printGreeting("hello, world");
       } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
   }
}
