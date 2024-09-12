package deposit.service;

import deposit.domain.Package;
import deposit.domain.User;

import java.util.List;

public interface IService {
    void login(String username, String password, IObserver client) throws Exception;
    void logout(User user, IObserver client) throws Exception;

    User getUserByUsername(String username) throws Exception;
    List<Package> getAllPackages();
}
