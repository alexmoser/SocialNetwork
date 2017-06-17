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
	 * Computes the distance from the person specified as parameter and the root.
	 * @param Person foundPerson: is the person, must have the "previous" field set in order to compute distance
	 * @return the distance between the person and the root
	 * */
	public static int distance(Person foundPerson) {
		if(foundPerson == null) 
			// person hasn't been found
			return -1;

		// compute the distance between source and target
		int distance = 0;
        while(foundPerson.getPrevious() != null) {
        	distance++;
        	foundPerson = foundPerson.getPrevious();
        }
        return distance;
	}
	
	/**
	 * Computes the minimum distance, as number of friends, that separates the two people specified.
	 * It exploits the breadth-first searching algorithm. 
	 * @param Person source: is the first person
	 * @param Person target: is the second person
	 * @return the distance between source and target, in terms of friends.
	 * */
	public static int distance(Person source, Person target) {
		Person last = bfSearch(source, target);
		
		if(last == null) 
			// person hasn't been found
			return -1;

		// compute the distance between source and target
		int distance = 0;
        while(last.getPrevious() != null) {
        	distance++;
        	last = last.getPrevious();
        }
        return distance;
	}	
	
	/**
	 * It searches the specified target person, starting from the specified source person.
	 * It exploits graph theory, by searching the graph that represents the people connections according 
	 * to the breadth-first algorithm. It starts from source and scans all its neighbour first, same for
	 * the next iteration and so on.
	 * @param Person source: is the search starting point
	 * @param Person target: is the target of the search
	 * @return the target Person, with the field "previous" set, in order to find the path towards the source
	 * */
	public static Person bfSearch(Person source, Person target) {
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
				return cur;
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
		return null;
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
    	// round to 2nd decimal
    	double minutes = (double) Math.round(totTime/6e10 * 100d) / 100d;
        System.out.println("\n\nTOT TIME: " + totTime + "ns (" + minutes + " min)");
        System.out.println("TOT LINES: " + lines);
        System.out.println("TOT PEOPLE: " + people.size());        
    	
//        printFriends();
        
        // the database has been parsed, wait for user queries or exit command
        System.out.println("Database created!\n");
        while(true) {
	        try{
		        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		        System.out.println("\nInsert first person in the format NAME_SURNAME (or 'q' to exit)");
		        String s = br.readLine();
		        if(s.equals("q")) break;
		        Person p1 = new Person(s);
		        System.out.println("Insert second person in the format NAME_SURNAME (or 'q' to exit)");
		        s = br.readLine();
		        if(s.equals("q")) break;
		        Person p2 = new Person(s);
		
		        // check if people exist in DB
		        Integer p1ID = peopleMap.get(p1);
		        if(p1ID == null) {
		        	System.err.println("First person inserted does not exist inside the database!");
		        	continue;
		        }
		        Integer p2ID = peopleMap.get(p2);
		        if(p2ID == null) {
		        	System.err.println("Second person inserted does not exist inside the database!");
		        	continue;
		        }

		        Person p = bfSearch(people.get(p1ID), people.get(p2ID));
//		        int distance = distance(people.get(p1ID), people.get(p2ID));
		        int distance = distance(p);
		        if(distance == -1)
		        	System.out.println("There is no way to connect the two people");
		        else {
		        	System.out.println("Distance between the two people is: " + distance);
		        	System.out.println("Path followed: ");
		        	int i = 0;
		        	while(p.getPrevious() != null) {
		        		System.out.println(i + ". " + p.toString());
		        		i++;
		        		p = p.getPrevious();
		        	}
	        		System.out.println(i + ". " + p.toString());
		        }
		       
		        
	        }
	        catch(IOException e) {
	        	e.printStackTrace();
	        }
        }
        
        System.out.println("closing..");
    }
}
