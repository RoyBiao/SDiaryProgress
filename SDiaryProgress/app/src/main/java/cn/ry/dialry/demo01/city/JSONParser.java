package cn.ry.dialry.demo01.city;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ruibiao on 16-4-9.
 */
public class JSONParser {
    public ArrayList<String> province_list_code = new ArrayList<String>();
    public ArrayList<String> city_list_code = new ArrayList<String>();

    public List<Cityinfo> getJSONParserResult(String JSONString, String key) {
        List<Cityinfo> list = new ArrayList<Cityinfo>();
        JsonObject result = new JsonParser().parse(JSONString)
                .getAsJsonObject().getAsJsonObject(key);

        Iterator<?> iterator = result.entrySet().iterator();
        while (iterator.hasNext()) {
            @SuppressWarnings("unchecked")
            Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) iterator
                    .next();
            Cityinfo cityinfo = new Cityinfo();

            cityinfo.setCity_name(entry.getValue().getAsString());
            cityinfo.setId(entry.getKey());
            province_list_code.add(entry.getKey());
            list.add(cityinfo);
        }
        System.out.println(province_list_code.size());
        return list;
    }

    public HashMap<String, List<Cityinfo>> getJSONParserResultArray(
            String JSONString, String key) {
        HashMap<String, List<Cityinfo>> hashMap = new HashMap<String, List<Cityinfo>>();
        JsonObject result = new JsonParser().parse(JSONString)
                .getAsJsonObject().getAsJsonObject(key);

        Iterator<?> iterator = result.entrySet().iterator();
        while (iterator.hasNext()) {
            @SuppressWarnings("unchecked")
            Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>) iterator
                    .next();
            List<Cityinfo> list = new ArrayList<Cityinfo>();
            JsonArray array = entry.getValue().getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                Cityinfo cityinfo = new Cityinfo();
                cityinfo.setCity_name(array.get(i).getAsJsonArray().get(0)
                        .getAsString());
                cityinfo.setId(array.get(i).getAsJsonArray().get(1)
                        .getAsString());
                city_list_code.add(array.get(i).getAsJsonArray().get(1)
                        .getAsString());
                list.add(cityinfo);
            }
            hashMap.put(entry.getKey(), list);
        }
        return hashMap;
    }
}
