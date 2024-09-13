package deposit.server;

import deposit.domain.Package;
import deposit.domain.User;
import deposit.repository.PackageRepository;
import deposit.repository.UserRepository;
import deposit.service.IObserver;
import deposit.service.IService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Service implements IService {
    private UserRepository userRepository;
    private PackageRepository packageRepository;
    private Map<Long, IObserver> loggedClients;

    public Service(UserRepository userRepository, PackageRepository packageRepository) {
        this.userRepository = userRepository;
        this.packageRepository = packageRepository;
        this.loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void login(String username, String password, IObserver client) throws Exception {
        User user = userRepository.getByUsername(username);
        if (user != null && Objects.equals(user.getPassword(), password)) {
            if(loggedClients.get(user.getId()) != null)
                throw new Exception("User already logged in.");
            loggedClients.put(user.getId(), client);
        }
        else
            throw new Exception("Authentication failed.");
    }

    @Override
    public synchronized void logout(User user, IObserver client) throws Exception {
        IObserver localClient = loggedClients.remove(user.getId());
        if (localClient == null)
            throw new Exception("User " + user.getUsername() + " is not logged in.");
    }

    @Override
    public User getUserByUsername(String username) {
        return this.userRepository.getByUsername(username);
    }

    @Override
    public List<Package> getAllPackages() {
        return this.packageRepository.getAll();
    }
}
