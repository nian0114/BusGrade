package com.lyk.busgrade;

import android.text.TextUtils;
import android.util.Log;

import com.lyk.busgrade.tools.HttpClientUtils;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaiduApiService {
    private static final String region = "上海";
    private static final String ak = "vWLtxQbUfWpEFxRNXyiGg3jd";

    public static String getDirectionRoutesResponse(String origin, String destination) {
        try {
            StringBuilder url = new StringBuilder("http://api.map.baidu.com/direction/v1?mode=transit")
                    .append("&origin=").append(URLEncoder.encode(origin, "UTF-8"))
                    .append("&destination=").append(URLEncoder.encode(destination, "UTF-8"))
                    .append("&region=").append(URLEncoder.encode(region, "UTF-8"))
                    .append("&origin_region=").append(URLEncoder.encode(region, "UTF-8"))
                    .append("&destination_region=").append(URLEncoder.encode(region, "UTF-8"))
                    .append("&output=json&ak=").append(ak);
            String content = HttpClientUtils.getResponse(url.toString());
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<RoutesScheme> parseDirectionRoutes(String responseString) {
        if (TextUtils.isEmpty(responseString)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNodes = mapper.readValue(responseString, JsonNode.class);
            if (jsonNodes == null || jsonNodes.get("result") == null) {
                return null;
            }
            jsonNodes = mapper.readValue(jsonNodes.get("result"), JsonNode.class);
            if (jsonNodes == null || jsonNodes.get("routes") == null) {
                return null;
            }

            List<RoutesScheme> list = new ArrayList<RoutesScheme>();

            jsonNodes = mapper.readValue(jsonNodes.get("routes"), JsonNode.class);
            for (JsonNode node : jsonNodes) {
                JsonNode schemeNode = mapper.readValue(node.get("scheme"), JsonNode.class);
                if (schemeNode == null || schemeNode.get(0).get("steps") == null) {
                    continue;
                }
                schemeNode = schemeNode.get(0);
                // 路线方案总的信息描述
                RoutesScheme routesScheme = new RoutesScheme();
                routesScheme.setDistance(schemeNode.get("distance").getIntValue());
                routesScheme.setDuration(schemeNode.get("duration").getIntValue());

                JsonNode stepsNode = mapper.readValue(schemeNode.get("steps"), JsonNode.class);
                if (stepsNode == null) {
                    continue;
                }
                // 循环steps，获取每一步的信息
                List<String> vehicleNames = new ArrayList<String>();
                List<SchemeSteps> stepsList = new ArrayList<SchemeSteps>();
                for (JsonNode distanceNode : stepsNode) {
                    distanceNode = distanceNode.get(0);
                    // 每个step对象
                    SchemeSteps steps = new SchemeSteps();
                    steps.setDistance(distanceNode.get("distance").getIntValue());
                    steps.setDuration(distanceNode.get("duration").getIntValue());
                    steps.setType(distanceNode.get("type").getIntValue());
                    steps.setStepInstruction(distanceNode.get("stepInstruction").getTextValue());
                    if (distanceNode.get("sname") != null) {
                        steps.setSname(distanceNode.get("sname").getTextValue());
                    }
                    if (steps.getType() == 5) {
                        // 步行
                        stepsList.add(steps);
                    } else {
                        if (distanceNode.get("vehicle") != null) {
                            JsonNode vehicleNode = mapper.readValue(distanceNode.get("vehicle"), JsonNode.class);
                            if (vehicleNode != null) {
                                //  车辆信息
                                steps.setVehicleEndName(vehicleNode.get("end_name").getTextValue());
                                steps.setVehicleName(vehicleNode.get("name").getTextValue());
                                steps.setVehicleStartName(vehicleNode.get("start_name").getTextValue());
                                steps.setVehicleStopNum(vehicleNode.get("stop_num").getIntValue());
                                steps.setVehicleType(vehicleNode.get("type").getIntValue());

                                vehicleNames.add(steps.getVehicleName());
                                stepsList.add(steps);
                            }
                        }
                    }
                    routesScheme.setSteps(stepsList);
                    routesScheme.setVehicleNames(vehicleNames);
                }
                list.add(routesScheme);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Map<String, List<String>> parseAccuratePosition(String responseString) {
        if (TextUtils.isEmpty(responseString)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNodes = mapper.readValue(responseString, JsonNode.class);
            if (jsonNodes == null || jsonNodes.get("result") == null) {
                return null;
            }
            jsonNodes = mapper.readValue(jsonNodes.get("result"), JsonNode.class);
            if (jsonNodes == null) {
                return null;
            }

            Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
            for (String key : new String[]{"origin", "destination"}) {
                if (jsonNodes.get(key) != null) {
                    // 解析起终点
                    List<String> list = new ArrayList<String>();
                    JsonNode jsonNodeArray = mapper.readValue(jsonNodes.get(key), JsonNode.class);
                    for (JsonNode node : jsonNodeArray) {
                        list.add(node.get("name").getTextValue());
                    }
                    if (list.size() > 0) {
                        resultMap.put(key, list);
                    }
                }
            }
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static List<String> parseNearStations(String responseString) {
        if (TextUtils.isEmpty(responseString)) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readValue(responseString, JsonNode.class);
            if (jsonNode != null
                    && jsonNode.get("status").getIntValue() == 0
                    && jsonNode.get("results") != null) {
                List<String> list = new ArrayList<String>();
                JsonNode nodes = mapper.readValue(jsonNode.get("results"), JsonNode.class);
                for (JsonNode node : nodes) {
                    list.add(node.get("name").getTextValue());
                }
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
