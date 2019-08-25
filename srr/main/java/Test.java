import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liukx on 2019/8/25.
 */
public class Test {

    private static OkHttpClient client = new OkHttpClient();

    private static Test test = new Test();

    private static Map<String,List<String>> loginInfos = new HashMap<String, List<String>>();

    public static void main(String[] args) throws Exception{
//        JSONObject loginRequest = new JSONObject();
//        loginRequest.put("login_name","13330835227");
//        loginRequest.put("password","qwer1234");
//
//        List<String> setCookies = test.login(loginRequest.toJSONString());
//        loginInfos.put(loginRequest.toJSONString(),setCookies);
//
//        test.read(setCookies);
        test.loginAll();

    }

    public  void loginAll()throws Exception{
        File file = new File("d:\\1000 members.csv");
        List<String> phones = FileUtils.readLines(file,"UTF-8");
        for (String phone : phones) {
            JSONObject loginRequest = new JSONObject();
            loginRequest.put("login_name",phone);
            loginRequest.put("password","123456");
            List<String> setCookies = test.login(loginRequest.toJSONString());
            loginInfos.put(loginRequest.toJSONString(),setCookies);
            test.read(setCookies);
        }
        System.out.println(1223333);
    }

    public List<String> login(String jsonString) throws Exception{
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonString);
        Request request = new Request.Builder()
                .url("http://test.imxkj.com/api/user/login")
                .post(body)
                .addHeader("logat", "1")
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "ead66491-f24b-5650-8438-4cd7f31949e6")
                .build();

        Response response = client.newCall(request).execute();
        String resp = response.body().string();
        if(response.code() == 200){
            List<String> setCookies = response.headers("Set-Cookie");
            System.out.println("login success:"+jsonString+":"+resp);
            return setCookies;
        }else {
            System.out.println("login failed:"+jsonString+":"+resp);
            return null;
        }
    }

    public void read(List<String> cookies) throws Exception{
        String logintoken = cookies.get(2).split(";")[0];
        logintoken = StringUtils.substringAfterLast(logintoken,"=");

        Request request = new Request.Builder()
                .url("http://test.imxkj.com/api/member.user/read")
                .get()
                .addHeader("logintoken", logintoken)
                .addHeader("logat", "1")
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "79895c7c-41ab-1559-2abf-af6af5f46359")
                .build();
            //logintoken=54BB50815A0664C2AF1EC2F96EB063D1; expires=Fri, 21-Feb-2020 07:01:36 GMT; Max-Age=15552000; path=/
        Response response = client.newCall(request).execute();
        String resp = response.body().string();
        System.out.println("read success:"+resp);
    }



}
