package ultimate.javaee.helloworld.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ultimate.javaee.helloworld.model.Person;
import ultimate.javaee.helloworld.storage.PersonStorage;

public class PersonFilter implements Filter
{

	private transient final Logger	logger	= LoggerFactory.getLogger(getClass());
	private PersonStorage			storage;

	public PersonFilter(PersonStorage storage)
	{
		super();
		this.storage = storage;
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{

		logger.debug("hello world: you called 'get'");

		List<Person> persons = storage.getPersons();

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

		resp.getOutputStream().print(sb.toString());

		resp.setStatus(HttpServletResponse.SC_OK);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{

		logger.debug("hello world: you called 'post'");

		if("add".equalsIgnoreCase(req.getParameter("action")))
		{
			Person p = new Person();
			p.setFirstname(req.getParameter("firstname"));
			p.setLastname(req.getParameter("lastname"));

			boolean added = storage.addPerson(p);

			if(added)
				resp.getOutputStream().println("person '" + p.getFirstname() + " " + p.getLastname() + "' successfully added");
			else
				resp.getOutputStream().println("person '" + p.getFirstname() + " " + p.getLastname() + "' already exists");

			resp.setStatus(HttpServletResponse.SC_OK);
		}
		else if("delete".equalsIgnoreCase(req.getParameter("action")))
		{
			Person p = new Person();
			p.setFirstname(req.getParameter("firstname"));
			p.setLastname(req.getParameter("lastname"));

			boolean deleted = storage.removePerson(p);

			if(deleted)
				resp.getOutputStream().println("person '" + p.getFirstname() + " " + p.getLastname() + "' successfully deleted");
			else
				resp.getOutputStream().println("person '" + p.getFirstname() + " " + p.getLastname() + "' not existing");

			resp.setStatus(HttpServletResponse.SC_OK);
		}
		else
		{
			logger.error("action not supported");
			resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		if("get".equalsIgnoreCase(httpRequest.getMethod()))
			doGet(httpRequest, httpResponse);
		else if("post".equalsIgnoreCase(httpRequest.getMethod()))
			doPost(httpRequest, httpResponse);
		else
		{
			logger.error("unsupported method");
			httpResponse.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
	}

	@Override
	public void destroy()
	{
	}

}
