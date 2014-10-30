package ultimate.javaee.helloworld.storage;

import java.util.List;

import ultimate.javaee.helloworld.model.Person;

/**
 * This interface describes the necessary functionality of a {@link Person} storage.
 * By use of an interface we can switch to different implementations by simply updating the
 * application context later without the need to recompile dependent beans.
 * 
 * @author ultimate
 */
public interface PersonStorage
{
	/**
	 * Add a {@link Person} to the storage
	 * 
	 * @param person - the {@link Person} object to add
	 * @return true if the {@link Person} has been added, false otherwise (if existing or null)
	 */
	public boolean addPerson(Person person);

	/**
	 * Remove a {@link Person} from the storage
	 * 
	 * @param person - the {@link Person} object to remove
	 * @return true if the {@link Person} has been removed, false otherwise (if not existing or
	 *         null)
	 */
	public boolean removePerson(Person person);

	/**
	 * Get the list of all {@link Person}s
	 * 
	 * @return the list (may be unmodifiable)
	 */
	public List<Person> getPersons();
}
