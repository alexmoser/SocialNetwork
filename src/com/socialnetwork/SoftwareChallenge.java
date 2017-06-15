package com.socialnetwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SoftwareChallenge {

	public static void main(String[] args) {
		// list of all the people in the database
    	ArrayList<Person> people = new ArrayList<>();
    	
    	// statistics variables
    	int lines = 0;
    	long startTime = System.currentTimeMillis();
    	
    	try {
	    	// variables needed to read file
	        File file = new File(Parameters.DBFILE_PATH);
	        BufferedReader br = new BufferedReader(new FileReader(file));
	        String readLine = "";
	        
	        while ((readLine = br.readLine()) != null) {
	        	lines++;
	        	System.out.println(readLine);
	        	String[] pair = readLine.split(Parameters.PERSON_SPLIT);
	        	
	        	// get first person
                Person p1 = new Person(pair[0]); 
                // check if already in list
                if(!people.contains(p1)) {
                	people.add(p1);
                }

                // get second person
                Person p2 = new Person(pair[1]);
                // check if already in list
                if(!people.contains(p2)) {
                	people.add(p2);
                }
	        	
	        }
	        
	        // get statistics
	        System.out.println("\n\nTOT TIME: " + (System.currentTimeMillis() - startTime));
            System.out.println("TOT LINES: " + lines);
            System.out.println("TOT PEOPLE: " + people.size());
	           
	        // close stream
	        br.close();
    	
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    }
}
