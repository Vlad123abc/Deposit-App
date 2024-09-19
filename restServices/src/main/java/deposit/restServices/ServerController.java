package deposit.restServices;

import deposit.domain.Package;
import deposit.domain.User;
import deposit.repository.PackageRepository;
import deposit.repository.UserRepository;
import deposit.server.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/deposit")
public class ServerController {
    @Autowired
    private Service service;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PackageRepository packageRepository;

    @RequestMapping(method = RequestMethod.GET, value = "/users")
    public ResponseEntity<?> getAllUsers(){
        System.out.println("Get all users");
        try {
            List<User> users = this.userRepository.getAll();
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/{id}")
    public ResponseEntity<?> getUsersById(@PathVariable String id){
        System.out.println("Get user by id: " + id);
        try {
            User user = this.userRepository.getById(Long.valueOf(id));
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/users")
    public ResponseEntity<?> saveUser(@RequestBody User user){
        System.out.println("Saving user " + user.toString());
        try {
            this.userRepository.save(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/users")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        System.out.println("Updating user " + user.toString());
        try {
            this.userRepository.update(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method= RequestMethod.DELETE, value="/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id){
        System.out.println("Deleting user with id " + id);
        try {
            this.userRepository.delete(Long.valueOf(id));
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/packages")
    public ResponseEntity<List<Package>> getAllPackages() {
        try {
            List<Package> packages = service.getAllPackages();
            return new ResponseEntity<>(packages, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/packages/{id}")
    public ResponseEntity<?> getPackagesById(@PathVariable String id){
        System.out.println("Get package by id: " + id);
        try {
            Package pack = packageRepository.getById(Long.valueOf(id));
            return new ResponseEntity<>(pack, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/packages")
    public ResponseEntity<?> savePackage(@RequestBody Package pack){
        System.out.println("Saving package " + pack.toString());
        try {
            this.packageRepository.save(pack);
            return new ResponseEntity<>(pack, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/packages")
    public ResponseEntity<?> updatePackage(@RequestBody Package pack) {
        System.out.println("Updating package " + pack.toString());
        try {
            this.packageRepository.update(pack);
            return new ResponseEntity<>(pack, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method= RequestMethod.DELETE, value="/packages/{id}")
    public ResponseEntity<?> deletePackage(@PathVariable String id){
        System.out.println("Deleting package with id " + id);
        try {
            this.packageRepository.delete(Long.valueOf(id));
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
