package common;

import java.io.Serializable;

/**
 * An identifier of a session.
 *
 * This session is referenced on both the client side and the server side.
 *
 * It is logged into a hashmap of users and sessions that allows login tracking
 *
 * @author ReneDescarnage
 */
public class Session implements Serializable {

    public long id;

    public Session(long id) {
        this.id = id;
    }

    public Session() {

    }
}