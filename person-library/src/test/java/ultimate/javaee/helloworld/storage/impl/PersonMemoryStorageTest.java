package ultimate.javaee.helloworld.storage.impl;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ultimate.javaee.helloworld.model.Person;
import ultimate.javaee.helloworld.storage.PersonStorage;

public class PersonMemoryStorageTest extends TestCase
{
	private transient final Logger logger = LoggerFactory.getLogger(getClass());

	private PersonStorage	personStorage;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		personStorage = new PersonMemoryStorage();
	}

	@Override
	protected void tearDown() throws Exception
	{
		personStorage = null;
		super.tearDown();
	}

	public void testGetPersons() throws Exception
	{
		logger.debug("testing getPersons...");
		
		assertEquals(3, personStorage.getPersons().size());

		assertTrue(personStorage.getPersons().contains(new Person("John", "Doe")));
		assertTrue(personStorage.getPersons().contains(new Person("Jane", "Smith")));
		assertTrue(personStorage.getPersons().contains(new Person("Michael", "Miller")));
	}

	public void testAddPerson() throws Exception
	{
		logger.debug("testing addPerson...");
		
		assertTrue(personStorage.getPersons().size() > 0);
		Person oldPerson = personStorage.getPersons().get(0);
		Person newPerson = new Person("Thomas", "Taylor");

		int personCountBefore = personStorage.getPersons().size();

		assertTrue(personStorage.addPerson(newPerson));

		assertEquals(personStorage.getPersons().size(), personCountBefore + 1);
		assertTrue(personStorage.getPersons().contains(newPerson));

		assertFalse(personStorage.addPerson(oldPerson));

		assertEquals(personStorage.getPersons().size(), personCountBefore + 1);
	}

	public void testRemovePerson() throws Exception
	{
		logger.debug("testing removePerson...");
		
		assertTrue(personStorage.getPersons().size() > 0);
		Person oldPerson = personStorage.getPersons().get(0);
		Person newPerson = new Person("Paul", "White");

		int personCountBefore = personStorage.getPersons().size();

		assertTrue(personStorage.removePerson(oldPerson));

		assertEquals(personStorage.getPersons().size(), personCountBefore - 1);
		assertFalse(personStorage.getPersons().contains(oldPerson));

		assertFalse(personStorage.removePerson(newPerson));

		assertEquals(personStorage.getPersons().size(), personCountBefore - 1);
	}
}
