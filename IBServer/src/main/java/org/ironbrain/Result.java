package org.ironbrain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Result {
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public enum State {OK, ERROR}

    private State res = State.OK;
    private String message;
    private Object data;

    public static Result getError(String message){
        Result result = new Result();
        result.setRes(State.ERROR);
        result.setMessage(message);
        return result;
    }
    public static Result getOk(){
        return new Result();
    }
}
