///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  FollowsAnalysis.java
// File:             Graphnode.java
// Semester:         Spring 2011
//
// Author:           Erin Rasmussen
// CS Login:         rasmusse
// Lecturer's Name:  Beck Hasti
// Lab Section:      Lecture 2
//////////////////////////// 80 columns wide //////////////////////////////////
import java.util.*;
/**
 * This class represents the individual graphnodes of the graph. Each node
 * holds a string which is the name of a user in a twitter network and which
 * other graphnodes the node is connected to.
 *
 * <p>Bugs: none known
 *
 * @author Erin Rasmussen
 */
public class Graphnode implements Comparable<Graphnode> {
	private TreeSet<String> successors;
	private int inDegree;
	private String label;
	Iterator<String> iter;
	private boolean visited;
	private int distance;

	/**
	 *The constructor for each individual graphnode.
	 * @param label the name of the graphnode to be created
	 */
	public Graphnode(String label){
		successors = new TreeSet<String>();
		this.inDegree = 0;
		this.label = label;
		this.visited = false;
		this.distance = 0;
	}

	/**
	 *Sets the visited value of the graphnode to the given boolean value
	 * @param b the true/false value of the node's visited property
	 */
	public void setVisited(boolean b){
		this.visited = b;
	}

	/**
	 * Returns whether a node has been visited or not
	 * @return true if visited already, false if not
	 */
	public boolean getVisited(){
		return visited;
	}

	/**
	 * Sets the distance from the "root" node for the shortestPath method.
	 * @param distance the distance away from the "root" node
	 */
	public void setDistance(int distance){
		this.distance = distance;
	}

	/**
	 *Sets the distance of a node to 0
	 */
	public void resetDistance(){
		distance = 0;
	}

	/**
	 *Returns the distance of a node from a "root" node
	 * @return the node's distance
	 */
	public int getDistance(){
		return distance;
	}

	/**
	 *Adds and edge from the graphnode to the node with the name given
	 * @param label the name of the follower of the user
	 */
	public void addEdge(String label){
		if(!containsSuccessors(label))successors.add(label);
	}

	/**
	 *Returns the graphnode's user name.
	 * @return the name of a user in the graph
	 */
	public String getName(){
		return label;
	}

	/**
	 *Increases the in-degree of a node by one.
	 */
	public void setInDegree(){
		inDegree++;
	}

	/**
	 *Returns the in-degree of a node.
	 * @return the in-degree of a node
	 */
	public int getInDegree(){
		return inDegree;
	}

	/**
	 *Returns the out-degree of a node
	 * @return the out-degree of a node
	 */
	public int getOutDegree(){
		return getSuccessors().size();
	}

	/**
	 *Returns all successors of the node, a TreeSet of followers
	 *of the user.
	 * @return a TreeSet of all followers of the user
	 */
	public TreeSet<String> getSuccessors(){
		return successors;
	}

	/**
	 *Returns true if a user has a certain follower 
	 * @param g the name of the follower to search for
	 * @return true if the user is a follower, false if not
	 */
	public boolean containsSuccessors(String g){
		iter = successors.iterator();
		String tmp;
		while (iter.hasNext()){
			tmp = iter.next();
			if (tmp.equals(g))return true;
		}
		return false;
	}

	@Override
	public int compareTo(Graphnode other) {
		return label.compareTo(other.getName());
	}
}
