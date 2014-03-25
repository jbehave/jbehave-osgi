package org.jbehave.osgi.core.components;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.jbehave.core.configuration.AnnotationBuilder.InstantiationFailed;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.osgi.core.Constants;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.ComponentContext;

public abstract class AbstractServiceComponent extends AbstractComponent
        implements Comparable<AbstractServiceComponent> {

    private volatile Configuration configuration = null;

    private volatile Bundle ownerBundle = null;

    private String extendeeBundleName = "";

    private String extendeeBundleVersion = "";

    private String extendeeID = "";

    public AbstractServiceComponent() {
    }

    protected void activate(ComponentContext context) {
        super.activate(context);

        initializeServiceProperties(context);
    }

    public int compareTo(AbstractServiceComponent aThat) {
        final int EQUAL = 0;

        // this optimization is usually worthwhile, and can
        // always be added
        if (this == aThat)
            return EQUAL;

        int compare = this.extendeeBundleName
                .compareTo(aThat.extendeeBundleName);
        if (compare != EQUAL)
            return compare;
        compare = this.extendeeBundleVersion
                .compareTo(aThat.extendeeBundleVersion);
        if (compare != EQUAL)
            return compare;
        compare = this.extendeeID.compareTo(aThat.extendeeID);
        if (compare != EQUAL)
            return compare;

        return EQUAL;
    }

    public Configuration configuration() {
        return configuration;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractServiceComponent other = (AbstractServiceComponent) obj;
        if (extendeeBundleName == null) {
            if (other.extendeeBundleName != null) {
                return false;
            }
        } else if (!extendeeBundleName.equals(other.extendeeBundleName)) {
            return false;
        }
        if (extendeeBundleVersion == null) {
            if (other.extendeeBundleVersion != null) {
                return false;
            }
        } else if (!extendeeBundleVersion.equals(other.extendeeBundleVersion)) {
            return false;
        }
        if (extendeeID == null) {
            if (other.extendeeID != null) {
                return false;
            }
        } else if (!extendeeID.equals(other.extendeeID)) {
            return false;
        }
        return true;
    }

    public String getExtendeeBundleName() {
        return extendeeBundleName;
    }

    public String getExtendeeBundleVersion() {
        return extendeeBundleVersion;
    }

    public String getExtendeeID() {
        return extendeeID;
    }

    public Bundle getOwnerBundle() {
        return ownerBundle;
    }

    @SuppressWarnings("unchecked")
    protected <T> T getPropertyValue(Class<T> memberType, String memberName) {

        Object value = getProperties().get(memberName);
        return (T) value;
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> getPropertyValues(Class<T> type, String memberName) {
        ArrayList<T> result = new ArrayList<T>();
        List<T> csvString = (List<T>) getProperties().get(memberName);
        if (csvString == null || csvString.isEmpty()) {
            logWarn("Could not find property with name '" + memberName + "'");
            return result;
        }
        return csvString;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((extendeeBundleName == null) ? 0 : extendeeBundleName
                        .hashCode());
        result = prime
                * result
                + ((extendeeBundleVersion == null) ? 0 : extendeeBundleVersion
                        .hashCode());
        result = prime * result
                + ((extendeeID == null) ? 0 : extendeeID.hashCode());
        return result;
    }

    protected void initializeServiceProperties(ComponentContext context) {
        Object storyBundleNameObj = getProperties().get(
                Constants.EXTENDEE_BUNDLE);
        extendeeBundleName = storyBundleNameObj != null ? (String) storyBundleNameObj
                : "no defined";

        Object storyBundleVersionObj = getProperties().get(
                Constants.EXTENDEE_BUNDLE_VERSION);
        extendeeBundleVersion = storyBundleVersionObj != null ? (String) storyBundleVersionObj
                : "no defined";

        setOwnerBundle(searchOwnerBundle(context.getBundleContext(),
                extendeeBundleName, extendeeBundleVersion));
    }

    protected <T, V extends T> T instanceOf(Class<T> type, Class<V> ofClass) {

        try {

            try {
                Constructor<V> constructor = ofClass
                        .getConstructor(new Class<?>[] { Bundle.class,
                                Configuration.class });
                return constructor.newInstance(getOwnerBundle(),
                        configuration());
            } catch (NoSuchMethodException ns) {
            }
            try {
                Constructor<V> constructor = ofClass
                        .getConstructor(new Class<?>[] { Bundle.class });
                return constructor.newInstance(getOwnerBundle());
            } catch (NoSuchMethodException ns) {
            }
            try {
                Constructor<V> constructor = ofClass
                        .getConstructor(new Class<?>[] { Configuration.class });
                return constructor.newInstance(configuration());
            } catch (NoSuchMethodException ns) {
            }
            try {
                Constructor<V> constructor = ofClass
                        .getConstructor(new Class<?>[] { ClassLoader.class });
                ClassLoader classloader = getOwnerBundle().adapt(
                        BundleWiring.class).getClassLoader();
                return constructor.newInstance(classloader);
            } catch (NoSuchMethodException ns) {
            }
            return ofClass.newInstance();
        } catch (Exception e) {
            // annotationMonitor.elementCreationFailed(ofClass, e);
            throw new InstantiationFailed(ofClass, type, e);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> Class<T> loadClass(String className, Class<T> memberType)
            throws ClassNotFoundException {
        try {
            return (Class<T>) getOwnerBundle().loadClass(className);
        } catch (ClassNotFoundException e) {
            // try another classloader
            try {
                return (Class<T>) getComponentContext().getBundleContext()
                        .getBundle().loadClass(className);
            } catch (ClassNotFoundException e1) {
                logError("Error on loading class " + className, e1);
                throw e1;
            }
        }
    }

    protected void modified(ComponentContext context) {
        initializeServiceProperties(context);
    }

    protected Bundle searchOwnerBundle(BundleContext bundleContext,
            String storyBundleName, String storyBundleVersion) {
        Bundle[] bundles = bundleContext.getBundles();
        for (Bundle bundle : bundles) {
            if (bundle.getSymbolicName().equals(storyBundleName)
                    && bundle.getVersion().toString()
                            .equals(storyBundleVersion)) {
                return bundle;
            }
        }
        return null;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setExtedeeBundleName(String extedeeBundleName) {
        this.extendeeBundleName = extedeeBundleName;
    }

    public void setExtendeeBundleVersion(String extendeeBundleVersion) {
        this.extendeeBundleVersion = extendeeBundleVersion;
    }

    public void setExtendeeID(String extendeeID) {
        this.extendeeID = extendeeID;
    }

    public void setOwnerBundle(Bundle ownerBundle) {
        this.ownerBundle = ownerBundle;
    }

}
