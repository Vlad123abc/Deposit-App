package deposit.service;

import deposit.domain.Package;

public interface IObserver {
    void packageSaved(Package pack) throws Exception;
    void packageUpdated(Package pack) throws Exception;
    void packageDeleted(Long id) throws Exception;
}
