package com.loovee.common.xmpp.bean;

import com.loovee.common.xmpp.annotation.XMLAttr;
import com.loovee.common.xmpp.annotation.XMLContent;
import com.loovee.common.xmpp.annotation.XMLElement;

@XMLElement
public class Version {
    @XMLAttr
    private String ver;
    @XMLAttr
    private String url;
    @XMLContent
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
