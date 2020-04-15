package huskymaps.searching;

import autocomplete.Autocomplete;
import autocomplete.DefaultTerm;
import autocomplete.Term;
import huskymaps.graph.Node;
import huskymaps.graph.StreetMapGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @see Searcher
 */
public class DefaultSearcher extends Searcher {
    private Autocomplete autoBS;
    private StreetMapGraph stmGraph;

    private HashMap<String, Node> nodes = new HashMap<>();
    private HashMap<String, List<String>> names = new HashMap<>();

    public DefaultSearcher(StreetMapGraph graph) {
        this.stmGraph = graph;
        List<Term> tArr = new ArrayList<>();
        for (Node node : stmGraph.allNodes()) {
            if (node.name() != null) {
                if (!names.containsKey(node.name())) {
                    names.put(node.name(), new ArrayList<>());
                    nodes.put(node.name(), node);
                    Term eachTerm = createTerm(node.name(), node.importance());
                    tArr.add(eachTerm);
                }
                names.get(node.name()).add(node.name());
            }
        }
        Term[] termsArr = new Term[tArr.size()];
        termsArr = tArr.toArray(termsArr);
        autoBS = createAutocomplete(termsArr);

    }

    @Override
    protected Term createTerm(String name, int weight) {
        return new DefaultTerm(name, weight);
    }

    @Override
    protected Autocomplete createAutocomplete(Term[] termsArray) {
        return new Autocomplete(termsArray);
    }

    @Override
    public List<String> getLocationsByPrefix(String prefix) {
        List<String> prefixLoc = new ArrayList<>();
        for (Term loc : autoBS.findMatchesForPrefix(prefix)) {
            prefixLoc.add(loc.query());
        }
        return prefixLoc;
    }

    @Override
    public List<Node> getLocations(String locationName) {
        List<Node> nodeLoc = new ArrayList<>();
        for (String vtx : names.get(locationName)) {
            nodeLoc.add(nodes.get(vtx));
        }
        return nodeLoc;
    }
}
