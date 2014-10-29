package ultimate.javaee.helloworld.storage;

import java.util.List;

import ultimate.javaee.helloworld.model.Person;

public interface PersonStorage
{
	public boolean addPerson(Person person);

	public boolean removePerson(Person person);

	public List<Person> getPersons();
}
