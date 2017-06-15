package com.socialnetwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SoftwareChallenge {
	// list of all the people in the database
	public static ArrayList<Person> people = new ArrayList<>();
	
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
            int p1ID = people.indexOf(p1);
            if(p1ID == -1) {
            	people.add(p1);
            	p1ID = id;
            	id++;
            }

            // get second person
            Person p2 = new Person(pair[1], id);
            // check if already in list
            int p2ID = people.indexOf(p2);
            if(p2ID == -1) {
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
        System.out.println("\n\nTOT TIME: " + totTime + " (" + totTime/60000000 + " min)");
        System.out.println("TOT LINES: " + lines);
        System.out.println("TOT PEOPLE: " + people.size());        
    	
    }
}
