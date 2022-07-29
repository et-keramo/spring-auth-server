package et.keramo.authsvr.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Optional;

public class JacksonUtil {

    public static boolean isExist(JsonNode jsonNode, String... keys) {
        return JacksonUtil.find(jsonNode, keys).isPresent();
    }

    public static boolean isTrue(JsonNode jsonNode, String... keys) {
        return JacksonUtil.find(jsonNode, keys).map(JsonNode::asBoolean).orElse(false);
    }

    public static Boolean findBoolean(JsonNode jsonNode, String... keys) {
        return JacksonUtil.find(jsonNode, keys).map(JsonNode::asBoolean).orElse(null);
    }

    public static Integer findInt(JsonNode jsonNode, String... keys) {
        return JacksonUtil.find(jsonNode, keys).map(JsonNode::asInt).orElse(null);
    }

    public static Integer findFirstInt(JsonNode jsonNode, String key) {
        return JacksonUtil.findFirst(jsonNode, key).map(JsonNode::asInt).orElse(null);
    }

    public static String findText(JsonNode jsonNode, String... keys) {
        return JacksonUtil.find(jsonNode, keys).map(JsonNode::asText).orElse(null);
    }

    public static String findFirstText(JsonNode jsonNode, String key) {
        return JacksonUtil.findFirst(jsonNode, key).map(JsonNode::asText).orElse(null);
    }

    public static <T> List<T> findList(JsonNode jsonNode, Class<T> clazz, String... keys) throws Exception {
        Optional<JsonNode> found = JacksonUtil.find(jsonNode, keys);
        if (found.isPresent()) {
            JsonNode result = found.get();

            if (result.isArray()) {
                return new ObjectMapper().convertValue(result, new TypeReference<List<T>>(){});
            } else {
                throw new Exception(String.join(".", keys) + " in JSON Object is not array");
            }
        } else {
            return null;
        }
    }


    private static Optional<JsonNode> find(JsonNode jsonNode, String... keys) {
        JsonNode tempNode = jsonNode;

        for (String key : keys) {
            if (tempNode.hasNonNull(key)) {
                tempNode = tempNode.get(key);
            } else {
                return Optional.empty();
            }
        }
        return Optional.ofNullable(tempNode);
    }

    private static Optional<JsonNode> findFirst(JsonNode jsonNode, String key) {
        if (jsonNode.hasNonNull(key)) {
            return Optional.ofNullable(jsonNode.get(key).get(0));
        } else {
            return Optional.empty();
        }
    }

}
