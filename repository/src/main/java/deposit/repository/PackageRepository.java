package deposit.repository;

import deposit.domain.Package;

import java.util.List;

public interface PackageRepository extends Repository<Long, Package>{
    List<Package> getByName(String name);
}
