package deposit.service;

import deposit.domain.User;

public interface IService {
    void login(String username, String password, IObserver client);
    void logout(User user, IObserver client);
}
