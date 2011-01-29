package az.his.ejb;

import javax.ejb.Remote;

@Remote
public interface ContentManager {
    public void persist(Object object);
}
