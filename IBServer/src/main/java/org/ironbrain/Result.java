package org.ironbrain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Result<T> {
    public State getRes() {
        return res;
    }

    public void setRes(State res) {
        this.res = res;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonIgnore
    public boolean isOk() {
        return res == State.OK;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public enum State {OK, ERROR}

    private State res = State.OK;
    private String message;
    private T data;

    public String getSubRes() {
        return subRes;
    }

    public void setSubRes(String subRes) {
        this.subRes = subRes;
    }

    private String subRes;

    public static Result getError(String message) {
        Result result = new Result();
        result.setRes(State.ERROR);
        result.setMessage(message);
        return result;
    }

    public static Result getOk() {
        return new Result();
    }
}
