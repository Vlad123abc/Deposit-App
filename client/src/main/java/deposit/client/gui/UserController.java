package deposit.client.gui;

import deposit.domain.Package;
import deposit.domain.User;
import deposit.service.IObserver;
import deposit.service.IService;

public class UserController implements IObserver {
    private IService service;
    private User user;

    public void init_controller(IService service, User user) {
        this.service = service;
        this.user = user;
    }

    @Override
    public void packageSaved(Package pack) throws Exception {

    }

    @Override
    public void packageUpdated(Package pack) throws Exception {

    }

    @Override
    public void packageDeleted(Long id) throws Exception {

    }
}
