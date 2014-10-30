package ultimate.javaee.helloworld.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ultimate.javaee.helloworld.model.Person;
import ultimate.javaee.helloworld.storage.PersonStorage;

/**
 * This {@link Filter} implements the different actions that are available at the URL this
 * {@link Filter} was mapped to in web.xml. According to the {@link Filter} interface a simple
 * {@link Filter#doFilter(ServletRequest, ServletResponse, FilterChain)} method has to be
 * provided in which we can implement the different methods and/or actions.
 * Of course we could also register additional {@link Filter}s at different URLs in web.xml if
 * desired.
 * 
 * Note: Similar to {@link Filter} you can register {@link Servlet}s with the web.xml. And although
 * Filters are originally intended for filtering and modification of a request we use it here for
 * reasons of simplicity, since spring does not provide a DelegatingServletProxy to map from web.xml
 * to our bean in the application context. (see
 * http://stackoverflow.com/questions/2957165/servlet-vs-filter
 * for more information). If you want to use {@link Servlet}s instead check out my github project
 * syncnapsis
 * where I implemented simple a FilterToBeanProxy and ServletToBeanProxy:
 * https://github.com/ultimate/syncnapsis/tree/master/syncnapsis-core/syncnapsis-core-utils/src/main
 * /java/com/syncnapsis/utils/spring
 * 
 * @author ultimate
 */
public class PersonFilter implements Filter
{
	/**
	 * Logger-Instance
	 */
	private transient final Logger	logger	= LoggerFactory.getLogger(getClass());
	/**
	 * The {@link PersonStorage} used to store/access {@link Person}s.
	 * Since we only refer to the interface here, we do not care which implementation is
	 * injected by the spring context.
	 */
	private PersonStorage			storage;

	/**
	 * Since the PersonFilter requires a {@link PersonStorage} to work it has to be provided
	 * with the constructor.
	 * 
	 * @param storage - the {@link PersonStorage} to use.
	 */
	public PersonFilter(PersonStorage storage)
	{
		super();
		if(storage == null)
			throw new IllegalArgumentException("storage must not be null!");
		this.storage = storage;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		// nothing to do here
		// Note: Filter#init is not delegated by DelegatingFilterProxy
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy()
	{
		// nothing to do here
		// Note: Filter#destroy is not delegated by DelegatingFilterProxy
	}

	/**
	 * 
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		// since we are running in a HTTP Servlet Container we can asume the request and response
		// are of type HttpSerlvet*
		// we need to cast them in order to be able to access all features
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// for reasons of clarity we created methods for doGet and doPost method that are called if
		// the incoming request
		// uses that respective method. All other methods are not supported here.
		// Note: If using HttpServlet as a base you will find this procedure, too.
		if("get".equalsIgnoreCase(httpRequest.getMethod()))
		{
			doGet(httpRequest, httpResponse);
		}
		else if("post".equalsIgnoreCase(httpRequest.getMethod()))
		{
			doPost(httpRequest, httpResponse);
		}
		else
		{
			logger.error("unsupported method");
			httpResponse.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
	}

	/**
	 * Handle HTTP "GET":
	 * Return the list of {@link Person}s from the {@link PersonStorage} as JSON
	 * 
	 * @param req - the incoming {@link HttpServletRequest}
	 * @param resp - the outgoing {@link HttpServletResponse}
	 * @throws IOException - if writing the response fails
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{

		logger.info("hello world: you called 'get'");

		// get the Persons from the PersonStorage
		List<Person> persons = storage.getPersons();

		// hard coded JSON formatting using the built in JSON toString of the Person class
		StringBuilder sb = new StringBuilder();
		sb.append("[\n");
		for(int i = 0; i < persons.size(); i++)
		{
			sb.append("\t");
			sb.append(persons.get(i).toString());
			if(i < persons.size() - 1)
				sb.append(",");
			sb.append("\n");
		}
		sb.append("]");

		// write the result as a JSON string
		resp.getOutputStream().print(sb.toString());
		// set the Http-Status to OK
		resp.setStatus(HttpServletResponse.SC_OK);
	}

	/**
	 * Handle HTTP "POST":
	 * Read the request parameters and add or delete a Person depending on the action.
	 * 
	 * @param req - the incoming {@link HttpServletRequest}
	 * @param resp - the outgoing {@link HttpServletResponse}
	 * @throws IOException - if writing the response fails
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{

		logger.info("hello world: you called 'post'");

		if("add".equalsIgnoreCase(req.getParameter("action")))
		{
			// create a Person Object from the request parameters to pass to the PersonStorage
			Person p = new Person();
			p.setFirstname(req.getParameter("firstname"));
			p.setLastname(req.getParameter("lastname"));

			// attempt to add the Person
			boolean added = storage.addPerson(p);

			// write a short (non-JSON) status message
			if(added)
				resp.getOutputStream().println("person '" + p.getFirstname() + " " + p.getLastname() + "' successfully added");
			else
				resp.getOutputStream().println("person '" + p.getFirstname() + " " + p.getLastname() + "' already exists");

			// set the Http-Status to OK
			resp.setStatus(HttpServletResponse.SC_OK);
		}
		else if("remove".equalsIgnoreCase(req.getParameter("action")))
		{
			// create a Person Object from the request parameters to pass to the PersonStorage
			Person p = new Person();
			p.setFirstname(req.getParameter("firstname"));
			p.setLastname(req.getParameter("lastname"));

			// attempt to remove the Person
			boolean removed = storage.removePerson(p);

			// write a short (non-JSON) status message
			if(removed)
				resp.getOutputStream().println("person '" + p.getFirstname() + " " + p.getLastname() + "' successfully removed");
			else
				resp.getOutputStream().println("person '" + p.getFirstname() + " " + p.getLastname() + "' not existing");

			// set the Http-Status to OK
			resp.setStatus(HttpServletResponse.SC_OK);
		}
		else
		{
			logger.error("action not supported");
			resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
		}
	}

}
