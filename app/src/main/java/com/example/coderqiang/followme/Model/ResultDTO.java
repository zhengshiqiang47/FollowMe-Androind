package com.example.coderqiang.followme.Model;

/**
 * @author: chenyi.zsq
 * @Date: 2022/5/11
 */
public class ResultDTO<T> {

    private boolean success = true;

    private String errorMsg;

    private T data;

    public static <T> ResultDTO<T> generateWithSuccess(T data){
        ResultDTO<T> resultDTO = new ResultDTO<>();
        resultDTO.setData(data);
        return resultDTO;
    }

    public static <T> ResultDTO<T> generateWithFailure(String errorMsg){
        ResultDTO<T> resultDTO = new ResultDTO<>();
        resultDTO.setErrorMsg(errorMsg);
        resultDTO.setSuccess(false);
        return resultDTO;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}

