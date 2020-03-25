package org.processmining.aam.exp.util;

import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;

/**
 * This class computes various complexity metrics of a petrinet. For detailed
 * explanation, see [Mendling, Jan, Hajo A. Reijers, and Jorge Cardoso. "What
 * makes process models understandable?" ].
 * 
 * @author xlu
 * 
 */
public class ExpModelSimplicityUtil {

	public static String[] propertykeys = new String[] { "Size", "NumTrans", "ACD", "CFC", "CNC", "Density" };

	public static void computeAllSimplicities(Petrinet net, ModelQuality quality) {
		quality.addProperty(propertykeys[0], String.valueOf(calculateSize(net)));
		quality.addProperty(propertykeys[1], String.valueOf(calculateSizeTr(net)));
		quality.addProperty(propertykeys[2], String.valueOf(calculateACD(net)));
		quality.addProperty(propertykeys[3], String.valueOf(calculateCFC(net)));
		quality.addProperty(propertykeys[4], String.valueOf(calculateCNC(net)));
		quality.addProperty(propertykeys[5], String.valueOf(calculateDensity(net)));
	}

	public static int calculateSizeTr(Petrinet net) {
		int sum = 0;
		for (Transition t : net.getTransitions()) {
			if (!t.isInvisible()) {
				sum++;
			}
		}
		return sum;
	}

	/**
	 * the number of nodes.
	 * 
	 * @param petrinet
	 * @return
	 */
	public static int calculateSize(Petrinet petrinet) {
		int size = petrinet.getNodes().size();
		return size;
	}

	/**
	 * Control-Flow Complexity (CFC): the sum of all connectors (i.e. a
	 * place/transition followed/preceded by more then two transitions/places)
	 * weighted by their potential combinations of states after a split.
	 * 
	 * @param petrinet
	 * @return
	 */
	public static int calculateCFC(Petrinet petrinet) {
		int size = 0;

		for (PetrinetNode n : petrinet.getNodes()) {
			int input = 0;
			int output = 0;
			if (n instanceof Transition) {
				input += petrinet.getInEdges(n).size();
				output += petrinet.getOutEdges(n).size();
				if (input > 1 || output > 1) {
					size++;
				}
			} else {
				input += petrinet.getInEdges(n).size();
				output += petrinet.getOutEdges(n).size();
				if (input > 1 || output > 1) {
					size += output;
				}
			}
		}
		return size;
	}

	/**
	 * Average Connector Degree (ACD): the average number of nodes a connector
	 * (i.e. a place/transition followed/preceded by more then two
	 * transitions/places) is connected to.
	 * 
	 * @param petrinet
	 * @return
	 */
	public static double calculateACD(Petrinet petrinet) {
		double size = 0.0;

		int gateways = 0;
		for (PetrinetNode n : petrinet.getNodes()) {
			int input = petrinet.getInEdges(n).size();
			int output = petrinet.getOutEdges(n).size();
			int count = input + output;
			if (input > 1 || output > 1) {
				size += count;
				gateways++;
			}
		}

		return size / gateways;
	}

	public static int calculateMCD(Petrinet petrinet) {
		int size = 0;

		for (PetrinetNode n : petrinet.getNodes()) {
			int input = petrinet.getInEdges(n).size();
			int output = petrinet.getOutEdges(n).size();
			int count = input + output;
			if (input > 1 || output > 1) {
				size = Math.max(size, count);
			}
		}

		return size;
	}

	/**
	 * Coefficient of Network Connectivity (CNC): the ratio between arcs and
	 * nodes.
	 * 
	 * @param petrinet
	 * @return
	 */
	public static double calculateCNC(Petrinet petrinet) {
		double arcs = petrinet.getEdges().size();
		double nodes = petrinet.getNodes().size();

		return arcs / nodes;
	}

	/**
	 * Density: the ratio between the actual number of arcs and the maximum
	 * possible number of arcs in any model with the same number of nodes.
	 * 
	 * @param petrinet
	 * @return
	 */
	public static double calculateDensity(Petrinet petrinet) {
		double arcs = petrinet.getEdges().size();
		double nodes = petrinet.getNodes().size();

		return arcs / (nodes * (nodes - 1));
	}

}
