/**
 * Class that executes Viterbi for hypergraphs.
 * @author swabha
 */
package basicViterbi;

import hypergraph.HypergraphProto.Hyperedge;
import hypergraph.HypergraphProto.Hypergraph;
import hypergraph.HypergraphProto.Vertex;
import java.util.List;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ViterbiMain {

	public static void main(String[] args) throws Exception {
      Hypergraph hypergraph =
          Hypergraph.parseFrom(new FileInputStream(args[0]));
      Viterbi v = new Viterbi(hypergraph);
      List<Hyperedge> derivation = v.run();
      System.out.println(derivation.size());
      System.out.println(v.renderResult(derivation));
	}	
}
