///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  FollowsAnalysis.java
// File:             Graph.java
// Semester:         Spring 2011
//
// Author:           Erin Rasmussen
// CS Login:         rasmusse
// Lecturer's Name:  Beck Hasti
// Lab Section:      Lecture 2
//////////////////////////// 80 columns wide //////////////////////////////////
import java.util.*;
/**
 * The Graph class represents a directed graph with string labels for nodes. 
 * Node labels are assumed to be unique and it is assumed that nodes do not 
 * have edges to themselves (i.e., no self edges). 
 *
 * <p>Bugs: none known
 *
 * @author Erin Rasmussen
 */
public class Graph {
	TreeSet<Graphnode> allNodes;
	private List<String> names;		//names stores all nodes in the graph too
	private int numEdges;
	Iterator<Graphnode> iter;
	LinkedList<String> queue;

	/**
	 * Constructs a new empty graph.
	 */
	public Graph(){
		allNodes = new TreeSet<Graphnode>();
		names = new ArrayList<String>();
		numEdges = 0;
	}

	/**
	 * Add to the graph the specified directed edge from the
	 * node with the label label1 to the node with the label label2.
	 * @param label1 the the user node
	 * @param label2 the follower of the user
	 * @return true if operation is performed correctly
	 */
	public boolean addEdge(String label1, String label2){
		if (hasEdge(label1, label2)) return false;
		if (hasNode(label1)){
			if (hasNode(label2)){
				getGraphnode(label1).addEdge(label2);
				getGraphnode(label2).setInDegree();
				setEdges();
				return true;
			}
			else {
				addNode(label2);
				getGraphnode(label1).addEdge(label2);
				getGraphnode(label2).setInDegree();
				setEdges();
				return true;
			}
		}
		return false;

	}

	/**
	 * Adds a node with the given label to the graph. If a node with this label
	 * is already in the graph, the graph is unchanged and false is returned. 
	 * Otherwise (i.e., if there previously had not been a node with this label 
	 * and a new node with this label is added to the graph), true is returned. 
	 * @param label the name of the node to be added to the graph
	 * @return true if operation is performed correctly
	 */

	public boolean addNode(String label){
		if (hasNode(label))return false;
		else{
			Graphnode tmp = new Graphnode(label);
			allNodes.add(tmp);
			names.add(label);
			return true;
		}
	}

	/**
	 * Return a list of node labels in the order they are visited using
	 * a breadth-first search starting at the node with the given label.
	 * @param label the name of the node to be bfs'd
	 * @return a list of node labels in bfs order
	 */
	public List<String> bfs(String label){
		queue = new LinkedList<String>();
		List<String> bfs = new ArrayList<String>();
		Graphnode tmp = getGraphnode(label);
		tmp.setVisited(true);
		queue.addFirst(tmp.getName());
		bfs.add(tmp.getName());
		while (!queue.isEmpty()) {
			String current = queue.removeLast();
			for (String k : getGraphnode(current).getSuccessors()) {
				if (! getGraphnode(k).getVisited()){
					getGraphnode(k).setVisited(true);
					queue.addFirst(k);
					bfs.add(k);
				} // end if k not visited
			} // end for every successor k
		} // end while queue not empty
		iter = allNodes.iterator();
		while (iter.hasNext()){
			Graphnode tmp2 = iter.next();
			tmp2.setVisited(false);
		}
		return bfs;
	}

	/**
	 * Return a list of node labels in the order they are visited using
	 * a depth-first search starting at the node with the given label.
	 * @param label the name of the node to be dfs'd
	 * @return a list of node labels in dfs order
	 */
	public List<String> dfs(String label){
		List<String> dfsList = new ArrayList<String>();
		dfsList.add(label);
		dfs(label, dfsList);
		iter = allNodes.iterator();
		while (iter.hasNext()){
			iter.next().setVisited(false);
		}
		return dfsList;
	}

	/**
	 * An auxiliary method that allows for recursion in the dfs algorithm.
	 * @param label the name of the "root" node for each recursion
	 * @param dfsList the list tracking the order of the dfs
	 */
	private void dfs(String label, List<String> dfsList){
		getGraphnode(label).setVisited(true);
		for (String m : getGraphnode(label).getSuccessors()) {
			if (! getGraphnode(m).getVisited()) {
				dfsList.add(m);
				dfs(m, dfsList);
			}
		}
	}

	/**
	 * Return labels of immediate successors of the given node in alphabetical 
	 * order.
	 * @param label the name of the node to retrieve its successors
	 * @return a list of immediate successors
	 */
	public List<String> getSuccessors(String label){
		List<String> ordered = new ArrayList<String>();
		Graphnode tmp = getGraphnode(label);
		while (tmp.getSuccessors().iterator().hasNext()){
			ordered.add(tmp.getSuccessors().iterator().next());
		}
		return ordered;
	}

	/**
	 *  Return true if and only if the graph contains an edge from the node 
	 *  with the label label1 to the node with the label label2.
	 * @param label1 the user node in the graph
	 * @param label2 the follower of the user
	 * @return true if an edge exists between the two nodes
	 */
	public boolean hasEdge(String label1, String label2){
		if (hasNode(label1)){
			if (hasNode(label2)){
				Graphnode tmp = getGraphnode(label1);
				if (tmp.containsSuccessors(label2))return true;
			}
		}
		return false;
	}

	/**
	 * Return true if and only if a node with the given label is in the graph.
	 * @param label the name of the node to determine it's existance
	 * @return true iff the node is in the graph
	 */
	public boolean hasNode(String label){
		Iterator<String> iter2 = iterator();
		String tmp;
		while (iter2.hasNext()){
			tmp = iter2.next();
			if (tmp.equals(label))return true;
		}
		return false;
	}

	/**
	 * Return true if and only if the graph has size 0 (i.e., no nodes or 
	 * edges).
	 * @return true if graph is empty
	 */
	public boolean isEmpty(){
		return allNodes.size() == 0;
	}

	/**
	 *  Return an iterator over the node labels in the graph.
	 * @return an iterator over the labels of the graph
	 */
	public Iterator<String> iterator(){
		return names.iterator();
	}

	/**
	 * Return the number of edges in the graph.
	 * @return the number of edges in a graph
	 */
	public int numEdges(){
		return numEdges;
	}

	/**
	 * Find the shortest path from a start node to a finish node.
	 * @param startLabel the name of first node in the path
	 * @param finishLabel the name of the last node in the path
	 * @return a list of the shortest path
	 */
	public List<String> shortestPath(String startLabel, String finishLabel){
		queue = new LinkedList<String>();
		List<String> shortestPath = new ArrayList<String>();
		Graphnode tmp = getGraphnode(startLabel);
		tmp.setVisited(true);
		tmp.setDistance(0);
		queue.addFirst(tmp.getName());
		shortestPath.add(tmp.getName());
		while (!queue.isEmpty()) {
			String current = queue.removeLast();
			if (current.equals(finishLabel)){
				shortestPath.add(finishLabel);
				iter = allNodes.iterator();
				while (iter.hasNext()){
					Graphnode tmp2 = iter.next();
					tmp2.setVisited(false);
				}
				return shortestPath;
			}
			for (String k : getGraphnode(current).getSuccessors()) {
				if (!getGraphnode(k).getVisited()){
					getGraphnode(k).setVisited(true);
					getGraphnode(k).setDistance(getGraphnode(current).
							getDistance() + 1);
					queue.addFirst(k);
					shortestPath.add(k);
				} // end if k not visited
			} // end for every successor k
		} // end while queue not empty
		iter = allNodes.iterator();
		while (iter.hasNext()){
			Graphnode tmp2 = iter.next();
			tmp2.setVisited(false);
		}
		return shortestPath;
	}

	/**
	 *  Return the number of nodes in the graph.
	 * @return the number of nodes in a graph
	 */
	public int size(){
		return allNodes.size();
	}


	/**
	 * Returns the graphnode with the given label.
	 * @param g the name of the node to be returned
	 * @return the graphnode with the given label
	 */
	private Graphnode getGraphnode(String g){
		iter = allNodes.iterator();
		Graphnode tmp;
		while (iter.hasNext()){
			tmp = iter.next();
			if (tmp.getName().equals(g))return tmp;
		}
		return null;
	}

	/**
	 *Increases the number of edges by one.
	 */
	private void setEdges(){
		numEdges++;
	}
	
	


}
