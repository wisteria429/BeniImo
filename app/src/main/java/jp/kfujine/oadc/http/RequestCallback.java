package jp.kfujine.oadc.http;

/**
 * Created by fuji on 2015/11/22.
 */

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * REST通信のコールバック
 */
public class RequestCallback<T> implements Callback<T> {

    private RequestListener<T> mListener;

    public RequestCallback(RequestListener<T> listener){
        mListener = listener;
    }

    // 正常終了時に呼ばれる
    @Override
    public void success(T object, Response response){
        mListener.onSuccess(object);
    }

    // 異常終了時に呼ばれる
    @Override
    public void failure(RetrofitError error){
        mListener.onFailure(error);
    }
}
