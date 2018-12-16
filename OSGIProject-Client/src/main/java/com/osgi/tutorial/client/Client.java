package com.osgi.tutorial.client;

import com.osgi.tutorial.def.Greeter;
import org.osgi.framework.*;

public class Client implements BundleActivator, ServiceListener {
    private BundleContext bundleContext;
    private ServiceReference serviceReference;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        this.bundleContext = bundleContext;
        try {
            bundleContext.addServiceListener(
                    this, "(objectclass=" + Greeter.class.getName() + ")");
        } catch (InvalidSyntaxException ise) {
            ise.printStackTrace();
        }
    }

    @Override
    public void stop(BundleContext bundleContext) {
        if (serviceReference != null) {
            bundleContext.ungetService(serviceReference);
        }
    }

    @Override
    public void serviceChanged(ServiceEvent serviceEvent) {
        int type = serviceEvent.getType();
        switch (type) {
            case (ServiceEvent.REGISTERED):
                System.out.println("Notification of service registered.");
                serviceReference = serviceEvent
                        .getServiceReference();
                Greeter service = (Greeter) (bundleContext.getService(serviceReference));
                System.out.println(service.sayHiTo("John"));
                break;
            case (ServiceEvent.UNREGISTERING):
                System.out.println("Notification of service unregistered.");
                bundleContext.ungetService(serviceEvent.getServiceReference());
                break;
            default:
                break;
        }
    }
}

