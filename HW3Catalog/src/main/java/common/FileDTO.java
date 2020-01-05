
package common;

import server.model.Account;

import java.io.Serializable;
import java.util.Date;

public interface FileDTO extends Serializable {
    public Account getOwner();

    public String getName();

    public boolean hasPrivateAccess();

    public boolean hasReadPermission();

    public boolean hasWritePermission();

    public long getDimension();

    public Date getCreatedAt();

    public Date getUpdatedAt();



}