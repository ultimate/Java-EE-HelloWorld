package ultimate.javaee.helloworld.storage.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ultimate.javaee.helloworld.model.Person;
import ultimate.javaee.helloworld.storage.PersonStorage;

/**
 * This implementation of a {@link PersonStorage} stores the {@link Person}s in an internal
 * {@link List} that is kept in memory. For easier usage this {@link List} is initialized with a few
 * {@link Person}s. All changes made to this {@link PersonStorage} are lost when the application is
 * shutdown and the {@link List} will be reinited with the default values on next startup.
 * 
 * @author ultimate
 */
public class PersonMemoryStorage implements PersonStorage
{
	/**
	 * The list to hold the {@link Person}s
	 */
	private List<Person>	persons;

	/**
	 * Create a new {@link PersonStorage} and initialize the list of {@link Person}s with some
	 * default values.
	 */
	public PersonMemoryStorage()
	{
		persons = new ArrayList<Person>();

		addPerson(new Person("John", "Doe"));
		addPerson(new Person("Jane", "Smith"));
		addPerson(new Person("Michael", "Miller"));
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * ultimate.javaee.helloworld.storage.PersonStorage#addPerson(ultimate.javaee.helloworld.model
	 * .Person)
	 */
	@Override
	public boolean addPerson(Person person)
	{
		if(person == null)
			return false;
		if(persons.contains(person))
			return false;
		return persons.add(person);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * ultimate.javaee.helloworld.storage.PersonStorage#removePerson(ultimate.javaee.helloworld.
	 * model.Person)
	 */
	@Override
	public boolean removePerson(Person person)
	{
		if(person == null)
			return false;
		if(persons.contains(person))
			return persons.remove(person);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see ultimate.javaee.helloworld.storage.PersonStorage#getPersons()
	 */
	@Override
	public List<Person> getPersons()
	{
		// return an unmodifiable version of the person list
		return Collections.unmodifiableList(persons);
	}
}
