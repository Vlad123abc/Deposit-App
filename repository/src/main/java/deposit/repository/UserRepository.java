package deposit.repository;

import deposit.domain.User;

public interface UserRepository extends Repository<Long, User>{
    User getByUsername(String username);
}
