package et.keramo.authsvr.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public abstract class AbstractDataService extends RestService {

    public abstract Object list(HttpServletRequest request, HttpServletResponse response) throws Exception;
    public abstract Object get(HttpServletRequest request, HttpServletResponse response, String id) throws Exception;
    public abstract Object add(HttpServletRequest request, HttpServletResponse response, Map<String, Object> data) throws Exception;
    public abstract Object update(HttpServletRequest request, HttpServletResponse response, Map<String, Object> data) throws Exception;
    public abstract Object delete(HttpServletRequest request, HttpServletResponse response, String id) throws Exception;

}
