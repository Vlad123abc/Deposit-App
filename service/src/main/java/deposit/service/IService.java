package deposit.service;

import deposit.domain.Package;
import deposit.domain.User;

import java.util.List;

public interface IService {
    void login(String username, String password, IObserver client) throws Exception;
    void logout(User user, IObserver client) throws Exception;

    User getUserByUsername(String username) throws Exception;
    List<Package> getAllPackages() throws Exception;

    void savePackage(String name, String p_from, String p_to, String description, Float weight, Boolean fragile) throws Exception;
    void updatePackage(Package newPackage) throws Exception;
    void deletePackage(Long id) throws Exception;

    List<Package> getAllPackagesByName(String name) throws Exception;
    List<Package> getAllPackagesByFrom(String from) throws Exception;
    List<Package> getAllPackagesByTo(String to) throws Exception;
}
