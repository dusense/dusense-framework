package net.dusense.framework.core.exception.system;

public class AppSystemException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String m_message;

    private String m_details;

    private Throwable m_root;

    public AppSystemException(String m_message) {
        this.m_message = m_message;
    }

    public AppSystemException(String m_message, Throwable m_root) {
        //		this.m_message=m_message;
        //		this.m_root=m_root;
        super(m_message, m_root);
    }

    public AppSystemException(String m_message, String m_details, Throwable m_root) {
        this.m_message = m_message;
        this.m_details = m_details;
        this.m_root = m_root;
    }

    public String getM_details() {
        return m_details;
    }

    public void setM_details(String m_details) {
        this.m_details = m_details;
    }

    public String getM_message() {
        return m_message;
    }

    public void setM_message(String m_message) {
        this.m_message = m_message;
    }

    public Throwable getM_root() {
        return m_root;
    }

    public void setM_root(Throwable m_root) {
        this.m_root = m_root;
    }
}
