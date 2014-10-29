package ultimate.javaee.helloworld.storage.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ultimate.javaee.helloworld.model.Person;
import ultimate.javaee.helloworld.storage.PersonStorage;

public class PersonMemoryStorage implements PersonStorage
{

	private List<Person>	persons;

	public PersonMemoryStorage()
	{
		persons = new ArrayList<Person>();

		addPerson(new Person("John", "Doe"));
		addPerson(new Person("Jane", "Smith"));
		addPerson(new Person("Michael", "Miller"));
	}

	@Override
	public boolean addPerson(Person person)
	{
		if(person == null)
			return false;
		if(persons.contains(person))
			return false;
		return persons.add(person);
	}

	@Override
	public boolean removePerson(Person person)
	{
		if(person == null)
			return false;
		if(persons.contains(person))
			return persons.remove(person);
		return false;
	}

	@Override
	public List<Person> getPersons()
	{
		return Collections.unmodifiableList(persons);
	}
}
