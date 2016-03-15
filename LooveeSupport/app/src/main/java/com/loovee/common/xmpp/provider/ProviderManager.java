package com.loovee.common.xmpp.provider;

import android.util.Xml;

import com.loovee.common.app.LooveeApplication;
import com.loovee.common.xmpp.packet.IQ;
import com.loovee.common.xmpp.packet.PacketExtension;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProviderManager {
	private static ProviderManager instance;
	private Map<String, Object> extensionProviders = new ConcurrentHashMap<String, Object>();
	private Map<String, Object> iqProviders = new ConcurrentHashMap<String, Object>();
	private Map<String, String> javaBeans = new ConcurrentHashMap<String, String>();

	public static synchronized ProviderManager getInstance() {
		if (instance == null) {
			instance = new ProviderManager();
		}
		return instance;
	}

	public static synchronized void setInstance(ProviderManager providerManager) {
		if (instance != null) {
			throw new IllegalStateException(
					"ProviderManager singleton already set");
		}
		instance = providerManager;
	}

	protected void initialize() {

		InputStream providerStream = null;
		try {
			providerStream = LooveeApplication.instances.getAssets().open("xmpp_providers.xml");
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(
					"http://xmlpull.org/v1/doc/features.html#process-namespaces",
					true);
			parser.setInput(providerStream, "UTF-8");
			int eventType = parser.getEventType();
			do {
				if (eventType == XmlPullParser.START_TAG) {
					if (parser.getName().equals("iqProvider")) {
						parser.next();
						parser.next();
						String elementName = parser.nextText();
						parser.next();
						parser.next();
						String namespace = parser.nextText();
						parser.next();
						parser.next();
						String className = parser.nextText();
						parser.next();
						parser.next();
						String javaBean = parser.nextText();

						String key = getProviderKey(elementName, namespace);
						if (!this.iqProviders.containsKey(key)) {
							try {
								Class provider = Class.forName(className);
								if (IQProvider.class.isAssignableFrom(provider)) {
									this.iqProviders.put(key,provider.newInstance());
									this.javaBeans.put(key, javaBean);
								} else if (IQ.class.isAssignableFrom(provider))
									this.iqProviders.put(key, provider);
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							} catch (InstantiationException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
						}
					} else if (parser.getName().equals("extensionProvider")) {
						parser.next();
						parser.next();
						String elementName = parser.nextText();
						parser.next();
						parser.next();
						String namespace = parser.nextText();
						parser.next();
						parser.next();
						String className = parser.nextText();

						String key = getProviderKey(elementName, namespace);
						if (!this.extensionProviders.containsKey(key)) {
							try {
								Class provider = Class.forName(className);
								if (PacketExtensionProvider.class
										.isAssignableFrom(provider)) {
									this.extensionProviders.put(key,
											provider.newInstance());
								} else if (PacketExtension.class
										.isAssignableFrom(provider)) {
									this.extensionProviders.put(key, provider);
								}
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							} catch (InstantiationException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
						}
					}
				}
				eventType = parser.next();
			} while (eventType != XmlPullParser.END_DOCUMENT);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(providerStream != null) {
					providerStream.close();
				}
			} catch (Exception e) {
			}
		}
	}
	

	public Object getIQProvider(String elementName, String namespace) {
		String key = getProviderKey(elementName, namespace);
		return this.iqProviders.get(key);
	}
	
	public String getJavaBean(String elementName, String namespace) {
		String key = getProviderKey(elementName, namespace);
		return this.javaBeans.get(key);
	}

	public Collection<Object> getIQProviders() {
		return Collections.unmodifiableCollection(this.iqProviders.values());
	}

	public void addIQProvider(String elementName, String namespace,
			Object provider) {
		if ((!(provider instanceof IQProvider))
				&& ((!(provider instanceof Class)) || (!IQ.class
						.isAssignableFrom((Class) provider)))) {
			throw new IllegalArgumentException(
					"Provider must be an IQProvider or a Class instance.");
		}

		String key = getProviderKey(elementName, namespace);
		this.iqProviders.put(key, provider);
	}

	public void removeIQProvider(String elementName, String namespace) {
		String key = getProviderKey(elementName, namespace);
		this.iqProviders.remove(key);
	}

	public Object getExtensionProvider(String elementName, String namespace) {
		String key = getProviderKey(elementName, namespace);
		return this.extensionProviders.get(key);
	}

	public void addExtensionProvider(String elementName, String namespace,
			Object provider) {
		if ((!(provider instanceof PacketExtensionProvider))
				&& (!(provider instanceof Class))) {
			throw new IllegalArgumentException(
					"Provider must be a PacketExtensionProvider or a Class instance.");
		}

		String key = getProviderKey(elementName, namespace);
		this.extensionProviders.put(key, provider);
	}

	public void removeExtensionProvider(String elementName, String namespace) {
		String key = getProviderKey(elementName, namespace);
		this.extensionProviders.remove(key);
	}

	public Collection<Object> getExtensionProviders() {
		return Collections.unmodifiableCollection(this.extensionProviders
				.values());
	}

	private String getProviderKey(String elementName, String namespace) {
		StringBuilder buf = new StringBuilder();
		buf.append("<").append(elementName).append("/><").append(namespace)
				.append("/>");
		return buf.toString();
	}

	private ClassLoader[] getClassLoaders() {
		ClassLoader[] classLoaders = new ClassLoader[2];
		classLoaders[0] = ProviderManager.class.getClassLoader();
		classLoaders[1] = Thread.currentThread().getContextClassLoader();

		List<ClassLoader> loaders = new ArrayList<ClassLoader>();
		for (ClassLoader classLoader : classLoaders) {
			if (classLoader != null) {
				loaders.add(classLoader);
			}
		}
		return (ClassLoader[]) loaders.toArray(new ClassLoader[loaders.size()]);
	}

	private ProviderManager() {
		initialize();
	}
}
