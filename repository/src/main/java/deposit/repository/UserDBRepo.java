package deposit.repository;

import deposit.domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

public class UserDBRepo implements UserRepository {
    private static final Logger logger = LogManager.getLogger();

    private Connection connection = null;

    public UserDBRepo(Connection connection) {
        logger.info("Initializing UserDBRepository");
        this.connection = connection;
        initializeDbIfNeeded();
    }

    private void initializeDbIfNeeded() {
        // create table Users
        //(
        //    id       integer not null
        //        constraint Users_pk
        //            primary key autoincrement,
        //    username TEXT    not null,
        //    password TEXT
        //);

        try {
            final Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Users (id INTEGER, username TEXT, password TEXT, PRIMARY KEY(id))");
        } catch (SQLException e) {
            System.err.println("[ERROR] createSchema : " + e.getMessage());
        }
    }

    @Override
    public User getById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory(connection).openSession()) {
            return session.createSelectionQuery("from User where id =:idU ", User.class)
                    .setParameter("idU", id)
                    .getSingleResultOrNull();
        }
    }

    @Override
    public List<User> getAll() {
        try(Session session = HibernateUtils.getSessionFactory(connection).openSession()) {
            return session.createQuery("from User", User.class).getResultList();
        }
    }

    @Override
    public void save(User user) {
        HibernateUtils.getSessionFactory(connection).inTransaction(session -> session.persist(user));
    }

    @Override
    public void delete(Long id) {
        HibernateUtils.getSessionFactory(connection).inTransaction(session -> {
            User user = session.createQuery("from User where id=?1",User.class).
                    setParameter(1, id).uniqueResult();
            System.out.println("In delete we found this user: " + user);
            if (user != null) {
                session.remove(user);
                session.flush();
            }
        });
    }

    @Override
    public void update(User user) {
        HibernateUtils.getSessionFactory(connection).inTransaction(session -> {
            if (!Objects.isNull(session.find(User.class, user.getId()))) {
                System.out.println("In update we found the user with id: " + user.getId());
                session.merge(user);
                session.flush();
            }
        });
    }

    @Override
    public User getByUsername(String username) {
        try (Session session = HibernateUtils.getSessionFactory(connection).openSession()) {
            return session.createSelectionQuery("from User where username =:usernameU ", User.class)
                    .setParameter("usernameU", username)
                    .getSingleResultOrNull();
        }
    }
}
