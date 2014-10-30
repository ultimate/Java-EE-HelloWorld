package ultimate.javaee.helloworld.storage.impl;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ultimate.javaee.helloworld.model.Person;

/**
 * This class shows how to write a simple test case with JUnit.
 * 
 * We are testing the functionality of {@link PersonMemoryStorage} here in order to be sure it works as expected
 * 
 * @author ultimate
 */
public class PersonMemoryStorageTest extends TestCase
{
	/**
	 * slf4j Logger-Instance
	 */
	private transient final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * The {@link PersonMemoryStorage} we are testing
	 */
	private PersonMemoryStorage	personStorage;

	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		// initialize the PersonMemoryStorage
		personStorage = new PersonMemoryStorage();
	}

	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception
	{
		// clear the PersonMemoryStorage
		personStorage = null;
		super.tearDown();
	}

	/**
	 * Test the behavior of {@link PersonMemoryStorage#getPersons()}
	 */
	public void testGetPersons() throws Exception
	{
		logger.debug("testing getPersons...");
		
		// check we have the default data in the list
		assertEquals(3, personStorage.getPersons().size());

		// check for the existance of the default Persons
		assertTrue(personStorage.getPersons().contains(new Person("John", "Doe")));
		assertTrue(personStorage.getPersons().contains(new Person("Jane", "Smith")));
		assertTrue(personStorage.getPersons().contains(new Person("Michael", "Miller")));
	}

	/**
	 * Test the behavior of {@link PersonMemoryStorage#addPerson(Person)}
	 */
	public void testAddPerson() throws Exception
	{
		logger.debug("testing addPerson...");
		
		// get an arbitray existing Person from the storage
		assertTrue(personStorage.getPersons().size() > 0);
		Person oldPerson = personStorage.getPersons().get(0);
		// define a new Person to add
		Person newPerson = new Person("Thomas", "Taylor");
		// store the current count of persons
		int personCountBefore = personStorage.getPersons().size();

		// add the new Person
		assertTrue(personStorage.addPerson(newPerson));
		// check if it was added correctly
		assertEquals(personStorage.getPersons().size(), personCountBefore + 1);
		assertTrue(personStorage.getPersons().contains(newPerson));

		// add an existing Person
		assertFalse(personStorage.addPerson(oldPerson));
		// check it hasn't been added
		assertEquals(personStorage.getPersons().size(), personCountBefore + 1);
	}

	/**
	 * Test the behavior of {@link PersonMemoryStorage#removePerson(Person)}
	 */
	public void testRemovePerson() throws Exception
	{
		logger.debug("testing removePerson...");

		// get an arbitray existing Person from the storage
		assertTrue(personStorage.getPersons().size() > 0);
		Person oldPerson = personStorage.getPersons().get(0);
		// define a non-existing Person to remove
		Person newPerson = new Person("Paul", "White");
		// store the current count of persons
		int personCountBefore = personStorage.getPersons().size();

		// remove an existing Person
		assertTrue(personStorage.removePerson(oldPerson));
		// check if it was removed correctly
		assertEquals(personStorage.getPersons().size(), personCountBefore - 1);
		assertFalse(personStorage.getPersons().contains(oldPerson));

		// remove a non-existing Person
		assertFalse(personStorage.removePerson(newPerson));
		// check the list hasn't changed
		assertEquals(personStorage.getPersons().size(), personCountBefore - 1);
	}
}
