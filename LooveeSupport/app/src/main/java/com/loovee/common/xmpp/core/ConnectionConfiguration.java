package com.loovee.common.xmpp.core;


import java.io.File;

import javax.net.SocketFactory;


public class ConnectionConfiguration
  implements Cloneable
{
  private String serviceName = "mk";
  private String host;
  private int port;
  private String truststorePath;
  private String truststoreType;
  private String truststorePassword;
  private boolean verifyChainEnabled = false;
  private boolean verifyRootCAEnabled = false;
  private boolean selfSignedCertificateEnabled = false;
  private boolean expiredCertificatesCheckEnabled = false;
  private boolean notMatchingDomainCheckEnabled = false;
  private boolean compressionEnabled = false;
  private boolean saslAuthenticationEnabled = true;
  
  private String username;
  private String password;
  private String resource;
  private boolean sendPresence;

  /**
   * 是否允许重连，当发生错误断开连接的时候
   */
  private boolean reconnectionAllowed = true;
  
  private SocketFactory socketFactory;
  private SecurityMode securityMode = SecurityMode.enabled;



  public ConnectionConfiguration(String host, int port, String serviceName)
  {
    init(host, port, serviceName);
  }

  public ConnectionConfiguration(String host, int port)
  {
    init(host, port, serviceName);
  }

  private void init(String host, int port, String serviceName) {
    this.host = host;
    this.port = port;
    this.serviceName = serviceName;

    String javaHome = System.getProperty("java.home");
    StringBuilder buffer = new StringBuilder();
    buffer.append(javaHome).append(File.separator).append("lib");
    buffer.append(File.separator).append("security");
    buffer.append(File.separator).append("cacerts");
    this.truststorePath = buffer.toString();

    this.truststoreType = "jks";

    this.truststorePassword = "changeit";
  }
  
  
  void setLoginInfo(String username, String password, String resource, boolean sendPresence) {
	  this.username = username;
	  this.password = password;
	  this.resource = resource;
	  this.sendPresence = sendPresence;
  }
  
  

  public String getServiceName()
  {
    return this.serviceName;
  }

  public String getHost()
  {
    return this.host;
  }

  public int getPort()
  {
    return this.port;
  }

  public SecurityMode getSecurityMode()
  {
    return this.securityMode;
  }

  public void setSecurityMode(SecurityMode securityMode)
  {
    this.securityMode = securityMode;
  }

  public String getTruststorePath()
  {
    return this.truststorePath;
  }

  public void setTruststorePath(String truststorePath)
  {
    this.truststorePath = truststorePath;
  }

  public String getTruststoreType()
  {
    return this.truststoreType;
  }

  public void setTruststoreType(String truststoreType)
  {
    this.truststoreType = truststoreType;
  }

  public String getTruststorePassword()
  {
    return this.truststorePassword;
  }

  public void setTruststorePassword(String truststorePassword)
  {
    this.truststorePassword = truststorePassword;
  }

  public boolean isVerifyChainEnabled()
  {
    return this.verifyChainEnabled;
  }

  public void setVerifyChainEnabled(boolean verifyChainEnabled)
  {
    this.verifyChainEnabled = verifyChainEnabled;
  }

  public boolean isVerifyRootCAEnabled()
  {
    return this.verifyRootCAEnabled;
  }

  public void setVerifyRootCAEnabled(boolean verifyRootCAEnabled)
  {
    this.verifyRootCAEnabled = verifyRootCAEnabled;
  }

  public boolean isSelfSignedCertificateEnabled()
  {
    return this.selfSignedCertificateEnabled;
  }

  public void setSelfSignedCertificateEnabled(boolean selfSignedCertificateEnabled)
  {
    this.selfSignedCertificateEnabled = selfSignedCertificateEnabled;
  }

  public boolean isExpiredCertificatesCheckEnabled()
  {
    return this.expiredCertificatesCheckEnabled;
  }

  public void setExpiredCertificatesCheckEnabled(boolean expiredCertificatesCheckEnabled)
  {
    this.expiredCertificatesCheckEnabled = expiredCertificatesCheckEnabled;
  }

  public boolean isNotMatchingDomainCheckEnabled()
  {
    return this.notMatchingDomainCheckEnabled;
  }

  public void setNotMatchingDomainCheckEnabled(boolean notMatchingDomainCheckEnabled)
  {
    this.notMatchingDomainCheckEnabled = notMatchingDomainCheckEnabled;
  }

  public boolean isCompressionEnabled()
  {
    return this.compressionEnabled;
  }

  public void setCompressionEnabled(boolean compressionEnabled)
  {
    this.compressionEnabled = compressionEnabled;
  }

  public boolean isSASLAuthenticationEnabled()
  {
    return this.saslAuthenticationEnabled;
  }

  public void setSASLAuthenticationEnabled(boolean saslAuthenticationEnabled)
  {
    this.saslAuthenticationEnabled = saslAuthenticationEnabled;
  }


  public void setReconnectionAllowed(boolean isAllowed)
  {
    this.reconnectionAllowed = isAllowed;
  }

  public boolean isReconnectionAllowed()
  {
    return this.reconnectionAllowed;
  }

  public void setSocketFactory(SocketFactory socketFactory)
  {
    this.socketFactory = socketFactory;
  }

  public SocketFactory getSocketFactory()
  {
    return this.socketFactory;
  }


  boolean isSendPresence()
  {
    return this.sendPresence;
  }


  public static enum SecurityMode
  {
    required, 

    enabled, 

    disabled;
  }
}
