package net.dusense.framework.core.exception.business;

public class ObjectNotFoundException extends BusinessException {

    public ObjectNotFoundException(String m_message) {
        super(m_message);
    }

    public ObjectNotFoundException(String m_message, Throwable m_root) {
        super(m_message, m_root);
    }

    public ObjectNotFoundException(String m_message, String m_details, Throwable m_root) {
        super(m_message, m_details, m_root);
    }
}
