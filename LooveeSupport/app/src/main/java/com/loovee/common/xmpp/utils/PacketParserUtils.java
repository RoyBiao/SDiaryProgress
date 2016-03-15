package com.loovee.common.xmpp.utils;

import android.text.TextUtils;

import com.loovee.common.xmpp.annotation.XMLAttr;
import com.loovee.common.xmpp.annotation.XMLContent;
import com.loovee.common.xmpp.annotation.XMLElement;
import com.loovee.common.xmpp.packet.Body;
import com.loovee.common.xmpp.packet.DefaultPacketExtension;
import com.loovee.common.xmpp.packet.Message;
import com.loovee.common.xmpp.packet.Packet;
import com.loovee.common.xmpp.packet.PacketExtension;
import com.loovee.common.xmpp.packet.Presence;
import com.loovee.common.xmpp.packet.XMPPError;
import com.loovee.common.xmpp.provider.PacketExtensionProvider;
import com.loovee.common.xmpp.provider.ProviderManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PacketParserUtils {
    private static final String PROPERTIES_NAMESPACE = "http://www.jivesoftware.com/xmlns/xmpp/properties";

    protected static XmlPullParser mParser;
    private static StringBuffer sb = new StringBuffer();


    /**
     * 解析Message
     *
     * @param parser 解析器
     * @return
     * @throws Exception
     */
    public static Packet parseMessage(XmlPullParser parser) throws Exception {
        mParser = parser;
        Message message = new Message();
        // 获取XML的属性
        String id = getAttValue("id");
        message.setPacketID(id == null ? "ID_NOT_AVAILABLE" : id);
        message.setTo(getAttValue("to"));
        message.setFrom(getAttValue("from"));
        message.setNick(getAttValue("nick"));
        message.setAge(getAttValue("age"));
        message.setSex(getAttValue("sex"));
        message.setAvatar(getAttValue("avatar"));
        message.setTime(getAttValue("time"));
        message.setType(Message.Type.fromString(getAttValue("type")));
        message.setNewstype(getAttValue("newstype"));
        message.setRoomid(getAttValue("roomid"));
        message.setLanguage(getAttValue("xml:lang"));
        message.setAtNick(getAttValue("atNick"));
        message.setAt(getAttValue("at"));
        message.setOperation(getAttValue("operation"));


        // 获取XML的子元素
        boolean done = false;
        String subject = null;
        String thread = null;
        Map<String, Object> properties = null;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                String elementName = parser.getName();
                String namespace = parser.getNamespace();
                if (elementName.equals("body")) {
                    parseBody(message);
                }

                if(elementName.equals("gift")) {
                    message.setGiftId(getAttValue("giftid"));
                    message.setGiftName(getAttValue("name"));
                }

                if (elementName.equals("delay")) {
                    String delay = readText();
                    if (!TextUtils.isEmpty(delay)) {

                    }
                }
                if (elementName.equals("thread")) {
                    if (thread == null) {
                        thread = parser.nextText();
                    }
                }
                if(elementName.equals("subject")) {
                    if(subject == null) {
                        subject = parser.nextText();
                    }
                }
                if (elementName.equals("error")) {
                    message.setError(parseError(parser));
                }

                if ((elementName.equals("properties"))
                        && (namespace.equals(PROPERTIES_NAMESPACE))) {
                    properties = parseProperties(parser);
                }
//                else {
//                    message.addExtension(parsePacketExtension(elementName,
//                            namespace, parser));
//                }

            } else if ((eventType == XmlPullParser.END_TAG)
                    && (parser.getName().equals("message"))) {
                done = true;
            }
        }

        message.setSubject(subject);
        message.setThread(thread);

        if (properties != null) {
            for (String name : properties.keySet()) {
                message.setProperty(name, properties.get(name));
            }
        }
        return message;
    }


    /**
     * 解析body
     *
     * @param message
     * @throws IOException
     * @throws XmlPullParserException
     */
    private static void parseBody(Message message) throws IOException, XmlPullParserException {

        Body body = new Body();
        body.setLen(getAttValue("len"));
        switch (Message.NewsType.fromString(message.getNewstype())) {
            case text: //文本消息
               body.setMessage(readText());
                break;

            case audio: //语音消息
                body.setMessage(readText());
                break;

            case pic://图片消息
                readImg(body);
                break;
            case gift: //礼物
                body.setMessage(readText());
                break;
            default:
                body.setMessage(readText());
                break;
        }
        message.setBody(body);
    }


    private static void readImg(Body body) throws IOException, XmlPullParserException {
        boolean done = false;
        while (!done) {
            int eventType = mParser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (mParser.getName().equals("smallpic")) {
                    body.setSmallpic(readText());
                } else if (mParser.getName().equals("largepic")) {
                    body.setLargepic(readText());
                }
            } else if ((eventType == XmlPullParser.END_TAG)
                    && (mParser.getName().equals("body"))) {
                done = true;
            }
        }
    }

    /**
     * 解析Pressence
     *
     * @param parser 解析器
     * @return
     * @throws Exception
     */
    public static Presence parsePresence(XmlPullParser parser) throws Exception {
        Presence.Type type = Presence.Type.available;
        String typeString = parser.getAttributeValue("", "type");
        if ((typeString != null) && (!typeString.equals(""))) {
            try {
                type = Presence.Type.valueOf(typeString);
            } catch (IllegalArgumentException iae) {
                System.err.println("Found invalid presence type " + typeString);
            }
        }
        Presence presence = new Presence(type);
        presence.setTo(parser.getAttributeValue("", "to"));
        presence.setFrom(parser.getAttributeValue("", "from"));
        String id = parser.getAttributeValue("", "id");
        presence.setPacketID(id == null ? "ID_NOT_AVAILABLE" : id);

        String language = getLanguageAttribute(parser);
        if ((language != null) && (!"".equals(language.trim()))) {
            presence.setLanguage(language);
        }
        presence.setPacketID(id == null ? "ID_NOT_AVAILABLE" : id);

        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                String elementName = parser.getName();
                String namespace = parser.getNamespace();
                if (elementName.equals("status")) {
                    presence.setStatus(parser.nextText());
                } else if (elementName.equals("priority")) {
                    try {
                        int priority = Integer.parseInt(parser.nextText());
                        presence.setPriority(priority);
                    } catch (NumberFormatException nfe) {
                    } catch (IllegalArgumentException iae) {
                        presence.setPriority(0);
                    }
                } else if (elementName.equals("show")) {
                    String modeText = parser.nextText();
                    try {
                        presence.setMode(Presence.Mode.valueOf(modeText));
                    } catch (IllegalArgumentException iae) {
                        System.err.println("Found invalid presence mode "
                                + modeText);
                    }
                } else if (elementName.equals("error")) {
                    presence.setError(parseError(parser));
                } else {
                    Map<String, Object> properties;
                    if ((elementName.equals("properties"))
                            && (namespace.equals(PROPERTIES_NAMESPACE))) {
                        properties = parseProperties(parser);

                        for (String name : properties.keySet()) {
                            presence.setProperty(name, properties.get(name));
                        }
                    } else {
                        presence.addExtension(parsePacketExtension(elementName,
                                namespace, parser));
                    }
                }
            } else if ((eventType == XmlPullParser.END_TAG)
                    && (parser.getName().equals("presence"))) {
                done = true;
            }
        }

        return presence;
    }

    /**
     * 解析属性
     *
     * @param parser 解析器
     * @return
     * @throws Exception
     */
    public static Map<String, Object> parseProperties(XmlPullParser parser)
            throws Exception {
        Map<String, Object> properties = new HashMap<String, Object>();
        while (true) {
            int eventType = parser.next();
            if ((eventType == XmlPullParser.START_TAG)
                    && (parser.getName().equals("property"))) {
                boolean done = false;
                String name = null;
                String type = null;
                String valueText = null;
                Object value = null;
                while (!done) {
                    eventType = parser.next();
                    if (eventType == XmlPullParser.START_TAG) {// <NAME>age</NAME><type
                        // value="int">18<type>
                        String elementName = parser.getName();
                        if (elementName.equals("name")) {
                            name = parser.nextText();
                        } else if (elementName.equals("value")) {
                            type = parser.getAttributeValue("", "type");
                            valueText = parser.nextText();
                        }
                    } else if ((eventType == XmlPullParser.END_TAG)
                            && (parser.getName().equals("property"))) {
                        if ("integer".equals(type)) {
                            value = Integer.valueOf(valueText);
                        } else if ("long".equals(type)) {
                            value = Long.valueOf(valueText);
                        } else if ("float".equals(type)) {
                            value = Float.valueOf(valueText);
                        } else if ("double".equals(type)) {
                            value = Double.valueOf(valueText);
                        } else if ("boolean".equals(type)) {
                            value = Boolean.valueOf(valueText);
                        } else if ("string".equals(type)) {
                            value = valueText;
                        } else if ("java-object".equals(type)) {
                            try {
                                byte[] bytes = StringUtils
                                        .decodeBase64(valueText); // 解码
                                ObjectInputStream in = new ObjectInputStream(
                                        new ByteArrayInputStream(bytes));
                                value = in.readObject();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if ((name != null) && (value != null)) {
                            properties.put(name, value);
                        }
                        done = true;
                    }
                }
            } else {
                if ((eventType == XmlPullParser.END_TAG)
                        && (parser.getName().equals("properties"))) {
                    break;
                }
            }
        }
        return properties;
    }

    /**
     * 解析错误数据
     *
     * @param parser
     * @return
     * @throws Exception
     */
    public static XMPPError parseError(XmlPullParser parser) throws Exception {
        String errorNamespace = "urn:ietf:params:xml:ns:xmpp-stanzas";
        String errorCode = "-1";
        String type = null;
        String message = null;
        String condition = null;
        List<PacketExtension> extensions = new ArrayList<PacketExtension>();

        for (int i = 0; i < parser.getAttributeCount(); i++) {
            if (parser.getAttributeName(i).equals("code")) {
                errorCode = parser.getAttributeValue("", "code");
            }
            if (parser.getAttributeName(i).equals("type")) {
                type = parser.getAttributeValue("", "type");
            }
        }
        boolean done = false;

        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("text")) {
                    message = parser.nextText();
                } else {
                    String elementName = parser.getName();
                    String namespace = parser.getNamespace();
                    if (errorNamespace.equals(namespace)) {
                        condition = elementName;
                    } else {
                        extensions.add(parsePacketExtension(elementName,
                                namespace, parser));
                    }
                }
            } else if ((eventType == XmlPullParser.END_TAG)
                    && (parser.getName().equals("error"))) {
                done = true;
            }

        }

        XMPPError.Type errorType = XMPPError.Type.CANCEL;
        try {
            if (type != null) {
                errorType = XMPPError.Type.valueOf(type
                        .toUpperCase(Locale.ENGLISH));
            }
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }
        return new XMPPError(Integer.parseInt(errorCode), errorType, condition,
                message, extensions);
    }

    public static PacketExtension parsePacketExtension(String elementName,
                                                       String namespace, XmlPullParser parser) throws Exception {
        Object provider = ProviderManager.getInstance().getExtensionProvider(
                elementName, namespace);
        if (provider != null) {
            if ((provider instanceof PacketExtensionProvider)) {
                return ((PacketExtensionProvider) provider)
                        .parseExtension(parser);
            }
        }

        DefaultPacketExtension extension = new DefaultPacketExtension(
                elementName, namespace);
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                String name = parser.getName();
                if (parser.isEmptyElementTag()) {
                    extension.setValue(name, "");
                } else {
                    eventType = parser.next();
                    if (eventType == XmlPullParser.TEXT) {
                        String value = parser.getText();
                        extension.setValue(name, value);
                    }
                }
            } else if ((eventType == XmlPullParser.END_TAG)
                    && (parser.getName().equals(elementName))) {
                done = true;
            }
        }

        return extension;
    }

    /**
     * 获取语言类型
     *
     * @param parser 解析器
     * @return
     */
    private static String getLanguageAttribute(XmlPullParser parser) {
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            String attributeName = parser.getAttributeName(i);
            if (("xml:lang".equals(attributeName))
                    || (("lang".equals(attributeName)) && ("xml".equals(parser
                    .getAttributePrefix(i))))) {
                return parser.getAttributeValue(i);
            }
        }
        return null;
    }

    /**
     * 将对象转化为XML字符串
     *
     * @param obj 传入的实体bean
     * @return 返回生成的XML字符串
     */
    @SuppressWarnings("unchecked")
    public synchronized static String javaBeanToXML(Object obj) {
        StringBuffer buffer = null;
        if (obj == null) {
            throw new NullPointerException("obj is null!");
        }
        try {
            buffer = new StringBuffer();
            Class<?> clazz = obj.getClass();
            XMLElement clazzElement = (XMLElement) clazz
                    .getAnnotation(XMLElement.class);
            String tagName = clazzElement == null
                    || TextUtils.isEmpty(clazzElement.value()) ? StringUtils
                    .toLowerCaseFirstOne(clazz.getSimpleName()) : clazzElement
                    .value();
            buffer.append("<" + tagName);
            List<Field> fields = getParentClassFields(clazz);
            boolean hasContent = false;
            // 属性
            for (Field f : fields) {
                f.setAccessible(true);
                if (f.getAnnotation(XMLContent.class) != null
                        || f.getAnnotation(XMLElement.class) != null) {
                    hasContent = true;
                    continue;
                }
                XMLAttr xmppAttr = f.getAnnotation(XMLAttr.class);
                String key = (xmppAttr == null || TextUtils.isEmpty(xmppAttr
                        .value())) ? f.getName() : xmppAttr.value();
                Object value = f.get(obj);
                if (value == null)
                    continue;
                final String fieldType = f.getType().getName();
                if (isPrimitive(fieldType)) {
                    buffer.append(" " + key + "=" + "\""
                            + String.valueOf(value == null ? "" : value) + "\"");
                } else {
                    throw new IllegalArgumentException(
                            "attr must be basic type!");
                }
            }
            // 内容
            if (hasContent) {
                buffer.append(">");
            } else {
                return buffer.append("/>").toString(); // 解析已经结束
            }
            // 子节点
            for (Field f : fields) {
                f.setAccessible(true);
                if (f.getAnnotation(XMLContent.class) == null
                        || "java.lang.Class".equals(f.getType().getName())) {
                    continue;
                }
                Object value = f.get(obj);
                buffer.append(String.valueOf(value));
                return buffer.append("</" + tagName + ">").toString(); // 解析结束
            }

            for (Field f : fields) {
                f.setAccessible(true);
                XMLElement xmppElement = f.getAnnotation(XMLElement.class);
                if (xmppElement == null) {
                    continue;
                }
                String key = TextUtils.isEmpty(xmppElement.value()) ? f
                        .getName() : xmppElement.value();
                Object value = f.get(obj);
                String fieldType = f.getType().getName();
                if ("java.lang.Class".equals(fieldType))
                    continue;

                if (isPrimitive(fieldType)) { // 基本类型处理
                    if (value == null)
                        continue;
                    buffer.append("<" + key + ">" + String.valueOf(value)
                            + "</" + key + ">");
                } else if (Collection.class.isAssignableFrom(f.getType())) { // 集合的处理
                    Collection<Object> collection = (Collection<Object>) value;

                    if (collection == null)
                        continue; // 忽略为空的集合

                    buffer.append("<" + key + ">");
                    Iterator<Object> iterator = collection.iterator();
                    while (iterator.hasNext()) {
                        buffer.append(javaBeanToXML(iterator.next()));
                    }
                    buffer.append("</" + key + ">");
                } else { // 对象处理
                    if (value == null)
                        continue;
                    buffer.append(javaBeanToXML(value));
                }

            }
            buffer.append("</" + tagName + ">");

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    /**
     * 是否为基本类型
     *
     * @param fieldType
     * @return
     */
    private static boolean isPrimitive(final String fieldType) {
        return fieldType.equals("java.lang.Integer")
                || fieldType.equals("java.lang.Long")
                || fieldType.equals("java.lang.Float")
                || fieldType.equals("java.lang.Double")
                || fieldType.equals("java.lang.String")
                || fieldType.equals("java.lang.Boolean")
                || fieldType.equals("boolean") || fieldType.equals("int")
                || fieldType.equals("long") || fieldType.equals("float")
                || fieldType.equals("double") || fieldType.equals("char");
    }

    /**
     * 将xml转为javabean
     *
     * @param clazz
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public synchronized static <T> T xmlToJavaBean(XmlPullParser parser,
                                                   Class<T> clazz) {
        String tagName = getTagName(clazz);

        T instance = null;
        try {
            List<Field> fields = getParentClassFields(clazz);
            boolean done = false;
            while (!done) {
                int eventType = parser.getEventType();
                String childTagName = parser.getName();
                if (eventType == XmlPullParser.START_TAG
                        && childTagName.equals(tagName)) { // 开始创建实例
                    instance = clazz.newInstance();
                }
                if (eventType == XmlPullParser.START_TAG && instance != null) { // 属性赋值

                    // 属性
                    for (int i = 0; i < parser.getAttributeCount(); i++) {
                        String attrName = parser.getAttributeName(i);
                        String attrValue = parser.getAttributeValue(i);

                        Field field = null;
                        for (Field f : fields) {
                            f.setAccessible(true);
                            XMLAttr xmlAttr = f.getAnnotation(XMLAttr.class);
                            if (xmlAttr != null
                                    && xmlAttr.value().equals(attrName)) {
                                field = getDeclaredFieldIncludeParent(clazz,
                                        f.getName());
                            } else if (f.getName().equals(attrName)) {
                                field = getDeclaredFieldIncludeParent(clazz,
                                        attrName);
                            }
                        }
                        if (field == null) {
                            continue;
                        }
                        assignmentAttr(parser, instance, attrValue, field);

                    }

                    // 内容和子节点
                    Field field = null;

                    for (Field f : fields) {
                        f.setAccessible(true);
                        XMLElement xmlElement = f
                                .getAnnotation(XMLElement.class);
                        if (f.getName().equals(childTagName)) {
                            field = getDeclaredFieldIncludeParent(clazz,
                                    childTagName);
                        } else if (xmlElement != null
                                && xmlElement.value().equals(childTagName)) {
                            field = getDeclaredFieldIncludeParent(clazz,
                                    f.getName());
                        } else if (Collection.class.isAssignableFrom(f
                                .getType())) { // 集合处理
                            Type genericType = f.getGenericType();
                            if (genericType != null) {
                                if (genericType instanceof ParameterizedType) {
                                    ParameterizedType pt = (ParameterizedType) genericType;
                                    Class genericClazz = (Class) pt
                                            .getActualTypeArguments()[0];
                                    if (getTagName(genericClazz).equals(
                                            childTagName)) {
                                        Object object = xmlToJavaBean(parser,
                                                genericClazz);
                                        String getMethodName = "get"
                                                + StringUtils
                                                .toUpercaseFirstOne(f
                                                        .getName());
                                        Method method = getMethodIncludeParent(
                                                clazz, getMethodName,
                                                new Class[]{});
                                        if (method != null) {
                                            Collection<Object> collection = (Collection<Object>) method
                                                    .invoke(instance,
                                                            new Object[]{}); //得到集合
                                            collection.add(object); // 添加到集合中去
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (field != null) {
                        assignment(parser, instance, field);
                    }
                    if (field != null
                            && field.getAnnotation(XMLContent.class) != null
                            && field.getName().equals(
                            StringUtils.toLowerCaseFirstOne(clazz
                                    .getSimpleName()))) {
                        done = true;
                    }
                } else if (eventType == XmlPullParser.END_TAG
                        && childTagName.equals(tagName)) { // 结束赋值
                    done = true;
                }

                // 没有结束，就开始下一个节点
                if (!done) {
                    parser.next();
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return instance;
    }

    private static <T> String getTagName(Class<T> clazz) {
        XMLElement xmppElement = clazz.getAnnotation(XMLElement.class);
        String clazzName = clazz.getSimpleName();
        String tagName = (xmppElement == null || TextUtils.isEmpty(xmppElement
                .value())) ? StringUtils.toLowerCaseFirstOne(clazzName)
                : xmppElement.value();
        return tagName;
    }

    /**
     * 获取字段，包括父类中的字段
     *
     * @param clazz
     * @param name
     * @return
     */
    public static Field getDeclaredFieldIncludeParent(Class<?> clazz,
                                                      String name) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
        }
        if (field == null && clazz.getSuperclass() != null) {
            field = getDeclaredFieldIncludeParent(clazz.getSuperclass(), name);
        }
        return field;
    }

    /**
     * 获取方法，包括父类中的方法
     *
     * @param clazz
     * @param name
     * @param parameterTypes
     * @return
     */
    public static Method getMethodIncludeParent(Class<?> clazz, String name,
                                                Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
        }
        if (method == null && clazz.getSuperclass() != null) {
            method = getMethodIncludeParent(clazz.getSuperclass(), name,
                    parameterTypes);
        }
        return method;
    }

    /**
     * 为实例对象赋值
     *
     * @param instance
     * @param field
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws IOException
     * @throws XmlPullParserException
     * @throws InstantiationException
     */
    @SuppressWarnings("unchecked")
    private synchronized static <T> void assignment(XmlPullParser parser,
                                                    T instance, Field field) throws IllegalAccessException,
            InvocationTargetException, XmlPullParserException, IOException,
            InstantiationException {

        String fieldType = field.getType().getName();
        final String setMethodName = "set"
                + StringUtils.toUpercaseFirstOne(field.getName());
        String getMethodName = "get"
                + StringUtils.toUpercaseFirstOne(field.getName());
        Method setMethod = null;
        try {
            if (fieldType.equals("java.lang.String")) {
                String value = parser.nextText();
                setMethod = getMethodIncludeParent(instance.getClass(),
                        setMethodName, String.class);
                if (setMethod != null) {
                    setMethod.invoke(instance, String.valueOf(value));
                }
            } else if (fieldType.equals("int")
                    || fieldType.equals("java.lang.Integer")) {
                String value = parser.nextText();
                setMethod = getMethodIncludeParent(instance.getClass(),
                        setMethodName, int.class);
                if (setMethod != null) {
                    setMethod.invoke(instance, Integer.valueOf(value));
                }
            } else if (fieldType.equals("long")
                    || fieldType.equals("java.lang.Long")) {
                String value = parser.nextText();
                setMethod = getMethodIncludeParent(instance.getClass(),
                        setMethodName, long.class);
                if (setMethod != null) {
                    setMethod.invoke(instance, Long.valueOf(value));
                }
            } else if (fieldType.equals("double")
                    || fieldType.equals("java.lang.Double")) {
                String value = parser.nextText();
                setMethod = getMethodIncludeParent(instance.getClass(),
                        setMethodName, double.class);
                if (setMethod != null) {
                    setMethod.invoke(instance, Double.valueOf(value));
                }
            } else if (fieldType.equals("float")
                    || fieldType.equals("java.lang.Float")) {
                String value = parser.nextText();
                setMethod = getMethodIncludeParent(instance.getClass(),
                        setMethodName, float.class);
                if (setMethod != null) {
                    setMethod.invoke(instance, Float.valueOf(value));
                }
            } else if (fieldType.equals("boolean")
                    || fieldType.equals("java.lang.Boolean")) {
                String value = parser.nextText();
                value = BooleanFormat(value);
                setMethod = getMethodIncludeParent(instance.getClass(),
                        setMethodName, boolean.class);
                if (setMethod != null) {
                    setMethod.invoke(instance, Boolean.valueOf(value));
                }
            } else if (fieldType.equals("char")
                    || fieldType.equals("java.lang.Character")) {
                String value = parser.nextText();
                setMethod = getMethodIncludeParent(instance.getClass(),
                        setMethodName, char.class);
                if (value != null && value.toCharArray().length == 1) {
                    Character ch = value.charAt(0);
                    if (setMethod != null) {
                        setMethod.invoke(instance, Character.valueOf(ch));
                    }
                }
            } else if (Collection.class.isAssignableFrom(field.getType())) { // 集合
                Method getMethod = getMethodIncludeParent(instance.getClass(),
                        getMethodName, new Class[]{});
                if (getMethod == null)
                    return;
                Collection<Object> collection = (Collection<Object>) getMethod
                        .invoke(instance, new Object[]{});
                if (collection == null) {
                    if (Modifier.isInterface(field.getType().getModifiers())
                            || Modifier.isAbstract(field.getType()
                            .getModifiers())) { // 判断是否为接口或者抽象类
                        if (field.getType().isAssignableFrom(ArrayList.class)) {
                            collection = ArrayList.class.newInstance();
                        }
                        if (field.getType().isAssignableFrom(HashSet.class)) {
                            collection = HashSet.class.newInstance();
                        }
                    } else {
                        collection = (Collection<Object>) field.getType()
                                .newInstance();
                    }
                    setMethod = getMethodIncludeParent(instance.getClass(),
                            setMethodName, field.getType());
                    if (setMethod != null) {
                        setMethod.invoke(instance, collection);
                    }
                }
            } else { //对象
                setMethod = getMethodIncludeParent(instance.getClass(),
                        setMethodName, field.getType());
                Object object = xmlToJavaBean(parser, field.getType());
                if (setMethod != null) {
                    setMethod.invoke(instance, object);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对布尔值进行格式化
     *
     * @param value
     * @return
     */
    private static String BooleanFormat(String value) {
        if (value.equals("yes") || value.equals("1")) {
            value = "true";
        }
        if (value.equals("no") || value.equals("0") || value.equals("")) {
            value = "false";
        }
        return value;
    }

    /**
     * 属性赋值给对象
     *
     * @param parser
     * @param instance
     * @param value
     * @param field
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws XmlPullParserException
     * @throws IOException
     */
    private static <T> void assignmentAttr(XmlPullParser parser, T instance,
                                           String value, Field field) throws IllegalAccessException,
            InvocationTargetException, XmlPullParserException, IOException {

        String fieldType = field.getType().getName();
        String setMethodName = "set"
                + StringUtils.toUpercaseFirstOne(field.getName());
        Method setMethod = null;
        try {
            if (fieldType.equals("java.lang.String")) {
                setMethod = getMethodIncludeParent(instance.getClass(),
                        setMethodName, String.class);
                if (setMethod != null) {
                    setMethod.invoke(instance, String.valueOf(value));
                }
            } else if (fieldType.equals("int")
                    || fieldType.equals("java.lang.Integer")) {
                setMethod = getMethodIncludeParent(instance.getClass(),
                        setMethodName, int.class);
                if (setMethod != null) {
                    setMethod.invoke(instance, Integer.valueOf(value));
                }
            } else if (fieldType.equals("long")
                    || fieldType.equals("java.lang.Long")) {
                setMethod = getMethodIncludeParent(instance.getClass(),
                        setMethodName, long.class);
                if (setMethod != null) {
                    setMethod.invoke(instance, Long.valueOf(value));
                }
            } else if (fieldType.equals("double")
                    || fieldType.equals("java.lang.Double")) {
                setMethod = getMethodIncludeParent(instance.getClass(),
                        setMethodName, double.class);
                if (setMethod != null) {
                    setMethod.invoke(instance, Double.valueOf(value));
                }
            } else if (fieldType.equals("float")
                    || fieldType.equals("java.lang.Float")) {
                setMethod = getMethodIncludeParent(instance.getClass(),
                        setMethodName, float.class);
                if (setMethod != null) {
                    setMethod.invoke(instance, Float.valueOf(value));
                }
            } else if (fieldType.equals("boolean")
                    || fieldType.equals("java.lang.Boolean")) {
                setMethod = getMethodIncludeParent(instance.getClass(),
                        setMethodName, boolean.class);
                if (setMethod != null) {
                    setMethod.invoke(instance, Boolean.valueOf(value));
                }
            } else if (fieldType.equals("char")
                    || fieldType.equals("java.lang.Character")) {
                setMethod = getMethodIncludeParent(instance.getClass(),
                        setMethodName, char.class);
                if (value != null && value.toCharArray().length == 1) {
                    Character ch = value.charAt(0);
                    if (setMethod != null) {
                        setMethod.invoke(instance, Character.valueOf(ch));
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取一个类的所有字段（包括父类的字段）
     *
     * @param clazz
     * @return
     */
    private static List<Field> getParentClassFields(Class<?> clazz) {
        List<Field> list = new ArrayList<Field>();
        list.addAll(Arrays.asList(clazz.getDeclaredFields()));
        if (clazz.getSuperclass() == null || Object.class.getName().equals(clazz.getSuperclass().getName())) {
            return list;
        }
        list.addAll(getParentClassFields(clazz.getSuperclass()));
        return list;
    }

    /**
     * 获取属性内容
     *
     * @param att
     * @return
     */
    public static String getAttValue(String att) {
        if (TextUtils.isEmpty(att)) {
            throw new IllegalArgumentException("att should not null");
        }
        String value = "";
        value = mParser.getAttributeValue(null, att);
        return value;
    }

    /**
     * 读取节点内容
     *
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    public static String readText() throws XmlPullParserException, IOException {
        String result = "";
        if (next() == XmlPullParser.TEXT) {
            result = mParser.getText();
            mParser.nextTag();
        }
        return result;
    }

    protected static int next() throws XmlPullParserException, IOException {
        int ret = mParser.next();
        sb.append((char) ret);
        return ret;
    }

    /**
     * 获取节点所有属性
     *
     * @param tag
     * @return
     * @throws XmlPullParserException
     * @throws IOException
     */
    public ArrayList<String> getAttributes(final String tag)
            throws XmlPullParserException, IOException {
        ArrayList<String> keys = new ArrayList<String>();
        mParser.require(XmlPullParser.START_TAG, null, tag);
        int i = mParser.getAttributeCount();
        for (int j = 0; j < i; j++) {
            keys.add(j, mParser.getAttributeName(j));
        }
        return keys;
    }
}
