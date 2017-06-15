package com.socialnetwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class SoftwareChallenge {
	// list of all the people in the database
	public static ArrayList<Person> people = new ArrayList<>();
	public static HashMap<Person, Integer> peopleMap = new HashMap<>();
	
	public static int parseDB(String db_path) throws IOException {
    	int totLines = 0;
    	
    	// increasing ID associated to each person
    	int id = 0;

    	// variables needed to read file
        File file = new File(db_path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String readLine = "";
        
        while ((readLine = br.readLine()) != null) {
        	totLines++;
//        	System.out.println(readLine);
        	String[] pair = readLine.split(Parameters.PERSON_SPLIT);
        	
        	// get first person
            Person p1 = new Person(pair[0], id); 
            // check if already in list
            Integer p1ID = peopleMap.get(p1);
//            int p1ID = people.indexOf(p1);
//            if(p1ID == -1) {
            if(p1ID == null) {
            	peopleMap.put(p1, id);
            	people.add(p1);
            	p1ID = id;
            	id++;
            }

            // get second person
            Person p2 = new Person(pair[1], id);
            // check if already in list
//            int p2ID = people.indexOf(p2);
            Integer p2ID = peopleMap.get(p2);
            if(p2ID == null) {
            	peopleMap.put(p2, id);
            	people.add(p2);
            	p2ID = id;
            	id++;
            }
        	
            // add to both friend's list
            people.get(p1ID).addFriend(p2ID);
            people.get(p2ID).addFriend(p1ID);
        }
           
        // close stream
        br.close();
        
        return totLines;
	}
	
	public static void printFriends() {
		for(Person p1 : people) {
			System.out.println(p1.toString());
			System.out.println("Friends: ");
			for(Integer id : p1.getFriendsID()) {
				System.out.println("\t" + people.get(id).toString());
			}
			System.out.println("\n");
		}
	}
	
	/**
	 * Performs a breadth-first search in the graph composed by the people in the specified ArrayList.
	 * It starts from the node identified by source and finds the links that separates source from target.
	 * */
	public static int distance(ArrayList<Person> people, Person source, Person target) {
		// queue containing not yet controlled people 
		ArrayDeque<Person> toVisit = new ArrayDeque<>();
		// set containing people analyzed
		HashSet<Person> visited = new HashSet<>();
		visited.add(source);
		toVisit.add(source);
		
		// current person to be analyzed
		Person cur;
		
		while((cur = toVisit.poll()) != null) {
			if(cur.equals(target)) {
				// the target person has been found
//				return cur;
				break;
			}
			// for all the friends of the current person
			for(Integer friendID : cur.getFriendsID()) {
				Person friend = people.get(friendID);
				if(!visited.contains(friend)) {
					// add to queue if not already checked
					friend.setPrevious(cur);
					visited.add(friend);
					toVisit.add(friend);
				}
			}
		}
		
		if(cur == null) 
			// person hasn't been found
			return -1;

		// compute the distance between source and target
		int distance = 0;
        while(cur.getPrevious() != null) {
        	distance++;
        	cur = cur.getPrevious();
        }
        return distance;
	}	

	public static void main(String[] args) {

		// statistics variables
    	long startTime = System.nanoTime();
    	int lines = 0;
    	
    	try {
			lines = parseDB(Parameters.DBFILE_PATH);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        // get statistics
    	long totTime = System.nanoTime() - startTime;
    	double minutes = totTime/6e10;
        System.out.println("\n\nTOT TIME: " + totTime + " (" + minutes + " min)");
        System.out.println("TOT LINES: " + lines);
        System.out.println("TOT PEOPLE: " + people.size());        
    	
        printFriends();

    }
}
