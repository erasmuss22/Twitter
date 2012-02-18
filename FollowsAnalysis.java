///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Title:            Program 5, Twitter Graph
// Files:            FollowsAnalysis.java, Graphnode.java, Graph.java
// Semester:         Spring 2011
//
// Author:           Erin Rasmussen ejrasmussen2@wisc.edu
// CS Login:         rasmusse
// Lecturer's Name:  Beck Hasti
// Lab Section:      Lecture 2
//
//
//                   STUDENTS WHO GET HELP FROM ANYONE OTHER THAN THEIR PARTNER
// Credits:          Some code taken from online readings
//////////////////////////// 80 columns wide //////////////////////////////////
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
public class FollowsAnalysis {

	/**
	 * The application part of the graph which calculates certain statistics
	 * about the graph.
	 *
	 * <p>Bugs: large files take a very long time on certain computers for
	 * the "minutes" (Question 7) part of the calculations.
	 *
	 * @author Erin Rasmussen
	 */
	public static void main(String[] args) {
		Graph twitter = new Graph();
		int position = 0;
		if (args.length != 1){
			System.out.println("Usage: java FollowsAnalysis fileName");
			System.exit(0);
		}

		if (!(new File(args[0]).exists())){
			System.out.println("Error: Cannot access input file");
			System.exit(0);
		}

		try {
			File infile = new File(args[0]);
			Scanner in = new Scanner(infile);
			while (in.hasNextLine()){
				position = 0;
				String a = in.nextLine();
				while(a.charAt(position) != ':'){
					position++;
				}
				String user = a.substring(0,position).toLowerCase();
				twitter.addNode(user);
				int j = position + 1;
				for (int i = position + 1; i < a.length(); i++){
					if (a.charAt(i) == ','){
						twitter.addEdge(user, a.substring(j, i).toLowerCase());
						j = i + 1;
					}
					if (i == a.length()-1){
						if (a.charAt(i) == ','){
							if (!a.substring(j, i + 1).toLowerCase().equals(""))
								twitter.addEdge(user, a.substring(j, i + 1)
										.toLowerCase());
						}
						else{
							if (!a.substring(j).toLowerCase().equals(""))
								twitter.addEdge(user, a.substring(j).
										toLowerCase());
						}
					}
				}

			}
		} catch(FileNotFoundException e){
			System.out.println("Error: Cannot access input file");
			System.exit(0);
		} 

		System.out.println("Number of users: " + twitter.size());
		System.out.println("Number of follows links: " + twitter.numEdges());
		System.out.print("DFS visit order: ");
		List<String> dfs = twitter.dfs(twitter.allNodes.first().getName());
		for (int i = 0; i < dfs.size() - 1; i++){
			System.out.print(dfs.get(i) + ",");
		}
		System.out.print(dfs.get(dfs.size() - 1));
		System.out.println();
		System.out.print("BFS visit order: ");
		List<String> bfs = twitter.bfs(twitter.allNodes.first().getName());
		for (int i = 0; i < bfs.size() - 1; i++){
			System.out.print(bfs.get(i) + ",");
		}
		System.out.print(bfs.get(bfs.size() - 1));
		System.out.println();
		System.out.print("Most followers: ");
		TreeSet<Graphnode> mostFollowers = mostFollowers(twitter.allNodes);
		Iterator<Graphnode> iter = mostFollowers.iterator();
		while (iter.hasNext()){
			Graphnode tmp = iter.next();
			if (!iter.hasNext())System.out.print(tmp.getName());
			else System.out.print(tmp.getName() + ",");
		}
		System.out.println();
		System.out.println("No followers: " +  noFollowers(twitter.allNodes));
		System.out.println("Don't follow anyone: " + doesNotFollow
				(twitter.allNodes));
		System.out.println("Mutual followers: " + mutualFollowers
				(twitter.allNodes));
		System.out.print("Receive most tweets: ");
		TreeSet<Graphnode> mostSubscribed = mostSubscribed(twitter.allNodes);
		Iterator<Graphnode> iter2 = mostSubscribed.iterator();
		while (iter2.hasNext()){
			Graphnode tmp = iter2.next();
			if (!iter2.hasNext())System.out.print(tmp.getName());
			else System.out.print(tmp.getName() + ",");
		}
		System.out.println();
		System.out.print("Reach everyone: ");
		TreeSet<Graphnode> reachAll = reachAll(twitter.allNodes, twitter);
		if(reachAll.size() == 0)System.out.println("none");
		else {
			Iterator<Graphnode> iter3 = reachAll.iterator();
			while (iter3.hasNext()){
				Graphnode tmp = iter3.next();
				if (!iter3.hasNext())System.out.print(tmp.getName());
				else System.out.print(tmp.getName() + ",");
			}
			System.out.println();
		}
		System.out.println("Minutes to get tweet: " + minutes(twitter.allNodes,
				twitter));
	}

	/**
	 * Returns a TreeSet of the Graphnodes with the most followers.
	 *
	 * @param allNodes a list of all nodes in the graph
	 * @return a TreeSet of all nodes tied with the most followers
	 */
	public static TreeSet<Graphnode> mostFollowers(TreeSet<Graphnode> 
	allNodes){
		TreeSet<Graphnode> mostFollowers = new TreeSet<Graphnode>();
		mostFollowers.add(allNodes.first());
		int most = allNodes.first().getOutDegree();
		Iterator<Graphnode> iter = allNodes.iterator();
		Graphnode tmp;
		while (iter.hasNext()){
			tmp = iter.next();
			if (tmp.getOutDegree() > most){
				most = tmp.getOutDegree();
				mostFollowers.clear();
				mostFollowers.add(tmp);
			}
			if (tmp.getOutDegree() == most)	mostFollowers.add(tmp);
		}
		return mostFollowers;

	}

	/**
	 * Returns the amount of users who don't have any followers, i.e. they
	 * are just a follower of others.
	 * @param allNodes all nodes in the graph
	 * @return amount of users who have no followers
	 */
	public static int noFollowers(TreeSet<Graphnode> allNodes){
		Iterator<Graphnode> iter = allNodes.iterator();
		int count = 0;
		while (iter.hasNext()){
			if (iter.next().getOutDegree() == 0)count++;
		}
		return count;
	}

	/**
	 *  Returns the amount of users who don't follow anyone
	 *
	 * @param allNodes all nodes in the graph
	 * @return # of users who don't follow anyone
	 */
	public static int doesNotFollow(TreeSet<Graphnode> allNodes){
		Iterator<Graphnode> iter = allNodes.iterator();
		int count = 0;
		while (iter.hasNext()){
			if (iter.next().getInDegree() == 0)count++;
		}
		return count;
	}

	/**
	 * Returns amount of pairs where the users are mutual followers of 
	 * each other.
	 *
	 * @param allNodes all nodes in the graph
	 * @return amount of mutual followers
	 */
	public static int mutualFollowers(TreeSet<Graphnode> allNodes){
		Iterator<Graphnode> iter = allNodes.iterator();
		int count = 0;
		while (iter.hasNext()){
			Graphnode tmp = iter.next();
			Graphnode tmp4 = null;
			Iterator<String> iter2 = tmp.getSuccessors().iterator();
			while (iter2.hasNext()){
				String tmp2 = iter2.next();
				Iterator<Graphnode> iter3 = allNodes.iterator();
				while (iter3.hasNext()){
					Graphnode tmp3 = iter3.next();
					if (tmp2.equals(tmp3.getName()))tmp4 = tmp3;
				}
				Iterator<String> iter4 = tmp4.getSuccessors().iterator();
				while (iter4.hasNext()){
					if (iter4.next().equals(tmp.getName()))	count++;
				}
			}
		}
		return count / 2;	//because everything gets counted twice
	}

	/**
	 * Returns a TreeSet of the users who are the followers of the most
	 * users.
	 * @param allNodes all nodes in the graph
	 * @return a TreeSet of users who follow the most users
	 */
	public static TreeSet<Graphnode> mostSubscribed(TreeSet<Graphnode> 
	allNodes){
		TreeSet<Graphnode> mostFollowers = new TreeSet<Graphnode>();
		mostFollowers.add(allNodes.first());
		int most = allNodes.first().getInDegree();
		Iterator<Graphnode> iter = allNodes.iterator();
		Graphnode tmp;
		while (iter.hasNext()){
			tmp = iter.next();
			if (tmp.getInDegree() > most){
				most = tmp.getInDegree();
				mostFollowers.clear();
				mostFollowers.add(tmp);
			}
			if (tmp.getInDegree() == most)mostFollowers.add(tmp);
		}
		return mostFollowers;

	}

	/**
	 * Returns a TreeSet of the users capable of reaching all users
	 * through retweeting. None is displayed if noone can do this.
	 *
	 * @param allNodes all nodes in the graph
	 * @param twitter the graph to be examines
	 * @return all users who can reach everyone
	 */
	public static TreeSet<Graphnode> reachAll(TreeSet<Graphnode> allNodes, 
			Graph twitter){
		TreeSet<Graphnode> reachAll = new TreeSet<Graphnode>();
		Iterator<Graphnode> iter = allNodes.iterator();
		while (iter.hasNext()){
			Graphnode tmp = iter.next();
			List<String> tmpList = twitter.bfs(tmp.getName());
			if (tmpList.size() == allNodes.size())reachAll.add(tmp);
		}
		return reachAll;
	}

	/**
	 * Returns the amount of minutes it would take for the user with the
	 * most followers to reach all of their followers through the retweets.
	 *
	 * @param allNodes all nodes in the graph
	 * @param twitter the graph to be examined
	 * @return minutes for the tweet to reach all followers
	 */
	public static int minutes(TreeSet<Graphnode> allNodes, Graph twitter){
		Graphnode most = mostFollowers(allNodes).first();
		@SuppressWarnings("unused")
		List<String> shortestPath;
		Iterator<Graphnode> iter = allNodes.iterator();
		int max = 0;
		while (iter.hasNext()){
			Graphnode tmp = iter.next();
			shortestPath = twitter.shortestPath(most.getName(), tmp.getName());
			int intermediate = tmp.getDistance();
			if (intermediate > max)	max = intermediate;
		}
		iter = allNodes.iterator();
		while (iter.hasNext()){
			iter.next().resetDistance();
		}
		return max;
	}

}
