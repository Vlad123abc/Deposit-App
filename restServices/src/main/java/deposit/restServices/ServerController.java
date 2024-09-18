package deposit.restServices;

import deposit.domain.Package;
import deposit.domain.User;
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

    @RequestMapping(method = RequestMethod.GET, value = "/users/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username){
        System.out.println("Get user by username: " + username);
        try {
            User user = service.getUserByUsername(username);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
