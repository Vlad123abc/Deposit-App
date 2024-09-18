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
    public ResponseEntity<?> getPackagesById(@PathVariable String id){
        System.out.println("Get user by id: " + id);
        try {
            User user = this.userRepository.getById(Long.valueOf(id));
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        catch (Exception e) {
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

    @RequestMapping(method = RequestMethod.GET, value = "/packages/{name}")
    public ResponseEntity<?> getPackagesByName(@PathVariable String name){
        System.out.println("Get package by name: " + name);
        try {
            List<Package> packages = service.getAllPackagesByName(name);
            return new ResponseEntity<>(packages, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
