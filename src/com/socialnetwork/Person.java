package com.socialnetwork;

import java.util.ArrayList;

public class Person {
	private String name;
	private String surname;
	private final int id;
	private ArrayList<Integer> friendsID = new ArrayList<>();
	
	public Person(String name, String surname, int id) {
		this.name = name;
		this.surname = surname;
		this.id = id;
	}
	
	public Person(String name_surname, int id) {
		String[] s = name_surname.split(Parameters.NAME_SPLIT);
		this.name = s[0];
		this.surname = s[1];
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public void addFriend(Integer id) {
		friendsID.add(id);
	}
	
	public boolean isFriend(Integer id) {
		return friendsID.contains(id);
	}
	
	public int totFriends() {
		return friendsID.size();
	}

	public ArrayList<Integer> getFriendsID() {
		return friendsID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Person [id = " + id + ", name = " + name + ", surname = " + surname + "]";
	}
	
}
