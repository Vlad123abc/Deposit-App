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

    @Override
    public void savePackage(String name, String p_from, String p_to, String description, Float weight, Boolean fragile) {
        this.packageRepository.save(new Package(name, p_from, p_to, description, weight, fragile));
        this.notifyPackageSaved(name, p_from, p_to, description, weight, fragile);
    }

    @Override
    public void updatePackage(Package newPackage) {
        this.packageRepository.update(newPackage);
        this.notifyPackageUpdated(newPackage);
    }

    @Override
    public void deletePackage(Long id) {
        this.packageRepository.delete(id);
        this.notifyPackageDeleted(id);
    }

    private void notifyPackageSaved(String name, String p_from, String p_to, String description, Float weight, Boolean fragile) {
        Iterable<User> users = this.userRepository.getAll();
        for(User us : users) {
            IObserver client = loggedClients.get(us.getId());
            if (client != null) {
                try {
                    System.out.println("notifying package saved");
                    client.packageSaved(new Package(name, p_from, p_to, description, weight, fragile));
                }
                catch (Exception e) {
                    System.err.println("Error notifying user " + e);
                }
            }
        }
    }
    private void notifyPackageUpdated(Package newPackage) {
        Iterable<User> users = this.userRepository.getAll();
        for(User us : users) {
            IObserver client = loggedClients.get(us.getId());
            if (client != null) {
                try {
                    System.out.println("notifying package updated");
                    client.packageUpdated(newPackage);
                }
                catch (Exception e) {
                    System.err.println("Error notifying user " + e);
                }
            }
        }
    }
    public void notifyPackageDeleted(Long id) {
        Iterable<User> users = this.userRepository.getAll();
        for(User us : users) {
            IObserver client = loggedClients.get(us.getId());
            if (client != null) {
                try {
                    System.out.println("notifying package deleted");
                    client.packageDeleted(id);
                }
                catch (Exception e) {
                    System.err.println("Error notifying user " + e);
                }
            }
        }
    }
}
