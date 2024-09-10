package net.dusense.framework.core.launcher.web;

import java.io.Serializable;

/**
 * Description: [描述该类概要功能介绍]
 *
 * @author:[Fan Yang]
 * @version $Revision$
 */
public class ExceptionRespEntity<T> implements Serializable {
    private static final long serialVersionUID = 7739979424806069482L;
    private String key;
    private String msg;
    private String signature;

    private String requestId;
    private T ext;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public ExceptionRespEntity() {
        this.key = "";
        this.msg = "";
        this.requestId = " ";
    }

    public ExceptionRespEntity(String key) {
        this.key = (key == null ? "" : key);
        this.msg = "";
        this.requestId = " ";
    }

    public ExceptionRespEntity(String key, String msg) {
        this.key = (key == null ? "" : key);
        this.msg = (msg == null ? "" : msg);
        this.requestId = " ";
    }

    public ExceptionRespEntity(String key, String msg, T ext) {
        this.key = (key == null ? "" : key);
        this.msg = (msg == null ? "" : msg);
        this.requestId = " ";
        this.ext = ext;
    }

    public ExceptionRespEntity(String key, String msg, String requestId, T ext) {
        this.key = (key == null ? "" : key);
        this.msg = (msg == null ? "" : msg);
        this.requestId = (requestId == null ? " " : requestId);
        this.ext = ext;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getExt() {
        return ext;
    }

    public void setExt(T ext) {
        this.ext = ext;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
