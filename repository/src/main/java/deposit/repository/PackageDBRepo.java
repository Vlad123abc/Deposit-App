package deposit.repository;

import deposit.domain.Package;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;

public class PackageDBRepo implements PackageRepository{
    private static final Logger logger = LogManager.getLogger();

    private Connection connection = null;

    public PackageDBRepo(Connection connection) {
        logger.info("Initializing PackageDBRepo");
        this.connection = connection;
        initializeDbIfNeeded();
    }
    private void initializeDbIfNeeded() {
//        create table Packages (
//                id integer,
//                description varchar(255),
//                fragile boolean,
//        from varchar(255),
//                name varchar(255),
//                to varchar(255),
//                weight float,
//        primary key (id)
//    );

//        create table Packages
//                (
//                        id          INTEGER not null
//        constraint Packages_pk
//        primary key autoincrement,
//                name        TEXT    not null,
//                "from"      TEXT    not null,
//                "to"        TEXT    not null,
//                description TEXT,
//                weight      REAL,
//                fragile     boolean
//);

        // TODO ...
    }

    @Override
    public List<Package> getByName(String name) {
        try (Session session = HibernateUtils.getSessionFactory(connection).openSession()) {
            return session.createSelectionQuery("from Package where name =:nameP ", Package.class)
                    .setParameter("nameP", name)
                    .getResultList();
        }
    }

    @Override
    public Package getById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory(connection).openSession()) {
            return session.createSelectionQuery("from Package where id =:idP ", Package.class)
                    .setParameter("idP", id)
                    .getSingleResultOrNull();
        }
    }

    @Override
    public List<Package> getAll() {
        try(Session session = HibernateUtils.getSessionFactory(connection).openSession()) {
            return session.createQuery("from Package ", Package.class).getResultList();
        }
    }

    @Override
    public void save(Package pack) {
        HibernateUtils.getSessionFactory(connection).inTransaction(session -> session.persist(pack));
    }

    @Override
    public void delete(Long id) {
        HibernateUtils.getSessionFactory(connection).inTransaction(session -> {
            Package pack = session.createQuery("from Package where id=?1",Package.class).
                    setParameter(1, id).uniqueResult();
            System.out.println("In delete we found this package: " + pack);
            if (pack != null) {
                session.remove(pack);
                session.flush();
            }
        });
    }

    @Override
    public void update(Package pack) {
        HibernateUtils.getSessionFactory(connection).inTransaction(session -> {
            if (!Objects.isNull(session.find(Package.class, pack.getId()))) {
                System.out.println("In update we found the package with id: " + pack.getId());
                session.merge(pack);
                session.flush();
            }
        });
    }
}
