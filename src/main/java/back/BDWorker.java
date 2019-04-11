package back;

import com.vaadin.data.HasValue;
import executor.BoltCypherExecutor;
import executor.CypherExecutor;
import org.neo4j.helpers.collection.Iterators;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static org.neo4j.helpers.collection.MapUtil.map;

/**
 * @author mh
 * @since 30.05.12
 */
public class BDWorker {

    private final CypherExecutor cypher;

    public BDWorker(String uri) {
        cypher = createCypherExecutor(uri);
    }


    private CypherExecutor createCypherExecutor(String uri) {
        try {
            String auth = new URL(uri.replace("bolt", "http")).getUserInfo();
            if (auth != null) {
                String[] parts = auth.split(":");
                return new BoltCypherExecutor(uri, parts[0], parts[1]);
            }
            return new BoltCypherExecutor(uri);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid Neo4j-ServerURL " + uri);
        }
    }

    public Map findMovie(String title) {
        if (title == null) return Collections.emptyMap();
        return Iterators.singleOrNull(cypher.query(
                "MATCH (movie:Movie {title:{title}})" +
                        " OPTIONAL MATCH (movie)<-[r]-(person:Person)\n" +
                        " RETURN movie.title as title, collect({name:person.name, job:head(split(lower(type(r)),'_')), role:r.roles}) as cast LIMIT 1",
                map("title", title)));
    }


    @SuppressWarnings("unchecked")
    public Iterable<Map<String, Object>> search(String query) {
        if (query == null || query.trim().isEmpty()) return Collections.emptyList();
        return Iterators.asCollection(cypher.query(
                query,
                map("limit", 1000)
        ));
    }

    public ArrayList<String> getLabels() {
        Iterable<Map<String, Object>> list1 = Iterators.asCollection(cypher.query(
                "match(n) return distinct labels(n)",
                map("limit", 1000)
        ));
        List list3 = new ArrayList<Collection>();
        ArrayList<String> list4 = new ArrayList<String>();
        ArrayList<Map<String, Object>> list2 = (ArrayList<Map<String, Object>>) Utils.makeCollection(list1);
        list2.forEach(element -> list3.add((Collection) element.get("labels(n)"))
        );
        list3.forEach(element -> list4.add(element.toString()));
        return list4;
    }

    public ArrayList<String> getNodesByLabel(String label) {
        Iterable<Map<String, Object>> list1 = Iterators.asCollection(cypher.query(
                "match(n:" +
                        label +
                        ") return n.name",
                map("limit", 1000)
        ));
        ArrayList<Map<String, Object>> list2 = (ArrayList<Map<String, Object>>) Utils.makeCollection(list1);
        ArrayList<Set> list3 = new ArrayList<>();
        ArrayList<String> list4 = new ArrayList<String>();
        list2.forEach(element -> list3.add((Set)element.values())
        );
        list3.forEach(element -> element.forEach(el->{
            list4.add(el.toString());
        }));
        return list4;
    }

    public ArrayList<String> getParametres(String label, String node) {
        Iterable<Map<String, Object>> list1 = Iterators.asCollection(cypher.query(
                "match(n:" +
                        label+
                        "{name : \'" +
                        node +
                        "\'}) -[a: Имеет_параметр]-(b) return b.name",
                map("limit", 1000)
        ));
        ArrayList<Map<String, Object>> list2 = (ArrayList<Map<String, Object>>) Utils.makeCollection(list1);
        ArrayList<Set> list3 = new ArrayList<>();
        ArrayList<String> list4 = new ArrayList<String>();
        list2.forEach(element -> list3.add((Set)element.values())
        );
        list3.forEach(element -> element.forEach(el->{
            list4.add(el.toString());
        }));
        return list4;
    }

    public void createObjects(ArrayList<Item> items) {
        items.forEach(item -> {
            cypher.query(
                    "match (d) where d.name = \"" +
                            item.getName()+
                                    "\" " +
                            "create (a: Экземпляр {name: \"Злой\", type: \"Агент\", Сытость: 10, Вес: 30, Рост: 120})\n" +
                            "merge (a)-[e:Экземпляр]->(d)",
                    map("limit", 1000)
            );
        });

    }
    private String createQuery(HashMap<String,String> parametres){
        String query = "create (a: Экземпляр {";
        query+="name: \"Злой\",";
        for(Map.Entry entry : parametres.entrySet()) {
            query+= entry.getKey().toString()+ ": \""+ entry.getValue().toString()+ "\", ";
        }
        return query+=query.substring(0,query.lastIndexOf(','))+ "})";
    }


    @SuppressWarnings("unchecked")
    public Map<String, Object> graph(int limit) {
        Iterator<Map<String, Object>> result = cypher.query(
                "MATCH (m:Movie)<-[:ACTED_IN]-(a:Person) " +
                        " RETURN m.title as movie, collect(a.name) as cast " +
                        " LIMIT {limit}", map("limit", limit));
        List nodes = new ArrayList();
        List rels = new ArrayList();
        int i = 0;
        while (result.hasNext()) {
            Map<String, Object> row = result.next();
            nodes.add(map("title", row.get("movie"), "label", "movie"));
            int target = i;
            i++;
            for (Object name : (Collection) row.get("cast")) {
                Map<String, Object> actor = map("title", name, "label", "actor");
                int source = nodes.indexOf(actor);
                if (source == -1) {
                    nodes.add(actor);
                    source = i++;
                }
                rels.add(map("source", source, "target", target));
            }
        }
        return map("nodes", nodes, "links", rels);
    }
}
