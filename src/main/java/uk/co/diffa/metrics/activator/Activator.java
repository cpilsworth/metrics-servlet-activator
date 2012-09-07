package uk.co.diffa.metrics.activator;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.Servlet;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.yammer.metrics.reporting.HealthCheckServlet;
import com.yammer.metrics.reporting.MetricsServlet;
import com.yammer.metrics.reporting.PingServlet;
import com.yammer.metrics.reporting.ThreadDumpServlet;

public class Activator implements BundleActivator {
	
	private List<ServiceRegistration> registrations;

	public void start(BundleContext context) throws Exception {
		registrations = new ArrayList<ServiceRegistration>();
		registerServlet(context, new MetricsServlet(), "/metrics");
		registerServlet(context, new HealthCheckServlet(), "/healthcheck");
		registerServlet(context, new PingServlet(), "/ping");
		registerServlet(context, new ThreadDumpServlet(), "/threads");
	}

	private void registerServlet(BundleContext context, Servlet servlet, String alias) {
		Hashtable<String, String> props = new Hashtable<String, String>();
		props.put("alias", alias);
		ServiceRegistration registration = context.registerService(Servlet.class.getName(), servlet, props);
		registrations.add(registration);
	}

	public void stop(BundleContext context) throws Exception {
		for (ServiceRegistration registration : registrations) {
			registration.unregister();
		}
		registrations = null;
	}

}