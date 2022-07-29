package et.keramo.authsvr.service.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FreeIPARequestBody {

    private String method;
    private List<Object> params;

    public FreeIPARequestBody(String method) {
        this.method = method;

        ObjectMapper objectMapper = new ObjectMapper();
        this.params = new ArrayList<>();
        this.params.add(objectMapper.createArrayNode());
        this.params.add(objectMapper.createObjectNode());
    }

    public FreeIPARequestBody userId(String userId) {
        ((ArrayNode) this.params.get(0)).add(userId);
        return this;
    }

    public FreeIPARequestBody password(String userId, String origin, String update) {
        ((ArrayNode) this.params.get(0)).add(userId);

        if (origin != null) {
            ((ObjectNode) this.params.get(1)).put("current_password", origin);
        }
        ((ObjectNode) this.params.get(1)).put("password", update);

        return this;
    }

    public FreeIPARequestBody user(FreeIPAUserDto userDto) {
        this.userId(userDto.getUserId());

        // Exclude
        userDto.setUserId(null);
        userDto.setName(null);
        userDto.setClientId(null);
        userDto.setGroupId(null);

        this.params.set(1, new ObjectMapper().valueToTree(userDto));
        return this;
    }

}
