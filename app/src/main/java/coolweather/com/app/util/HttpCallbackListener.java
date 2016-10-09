package coolweather.com.app.util;

/**
 * Created by 31798 on 2016/10/8.
 */
public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
