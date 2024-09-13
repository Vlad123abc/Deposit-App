package deposit.repository;

import deposit.domain.Package;

import java.util.List;

public interface PackageRepository extends Repository<Long, Package>{
    List<Package> getByName(String name);
    List<Package> getByFrom(String p_from);
    List<Package> getByTo(String p_to);
}
