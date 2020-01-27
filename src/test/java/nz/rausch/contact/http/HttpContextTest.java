package nz.rausch.contact.http;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HttpContextTest {
    HttpContext context;


    @Before
    public void setup() {
        context = new MockHttpContext();
    }

    @Test
    public void checkSingleParamReturnsTrueWhenParamExists() {
        Boolean result = context.checkParamExists("name");
        assertEquals(true, result);
    }

    @Test
    public void checkSingleParamReturnsTrueWhenParamDoesntExist() {
        Boolean result = context.checkParamExists("namea");
        assertEquals(false, result);
    }

    @Test
    public void checkMultiParamReturnsTrueWhenAllExist() {
        List<String> params = new ArrayList<>();
        params.add("name");
        params.add("email");

        Boolean result = context.checkParamExists(params);
        assertEquals(true, result);
    }

    @Test
    public void checkMultiParamReturnsFalseWhenOneDoesntExist() {
        List<String> params = new ArrayList<>();
        params.add("name111");
        params.add("email");

        Boolean result = context.checkParamExists(params);
        assertEquals(false, result);
    }

    @Test
    public void checkMultiParamReturnsFalseWhenOneNoneExist() {
        List<String> params = new ArrayList<>();
        params.add("aaa");
        params.add("bbb");

        Boolean result = context.checkParamExists(params);
        assertEquals(false, result);
    }

    private class MockHttpContext extends HttpContext {

        @Override
        public String getIp() {
            return null;
        }

        @Override
        public String getFormParameter(String param) {
            if (param.equals("name") || param.equals("email")) return "test";
            return null;
        }

        @Override
        public HttpContext result(String resultString) {
            return null;
        }

        @Override
        public HttpContext setStatus(Integer status) {
            return null;
        }

        @Override
        public HttpContext contentType(String contentType) {
            return null;
        }

        @Override
        public HttpContext resultJson(String resultString) {
            return null;
        }
    }
}