package net.dusense.framework.core.exception.business;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String m_message;

    private String m_details;

    private Throwable m_root;

    public BusinessException(String m_message) {
        super(m_message);
        this.m_message = m_message;
    }

    public BusinessException(String m_message, Throwable m_root) {
        super(m_message, m_root);
    }

    public BusinessException(String m_message, String m_details, Throwable m_root) {
        this.m_message = m_message;
        this.m_details = m_details;
        this.m_root = m_root;
    }
}
