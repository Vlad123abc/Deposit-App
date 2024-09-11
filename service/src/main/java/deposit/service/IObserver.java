package deposit.service;

import deposit.domain.Package;

public interface IObserver {
    void packageSaved(Package pack);
    void packageUpdated(Package pack);
    void packageDeleted(Long id);
}
