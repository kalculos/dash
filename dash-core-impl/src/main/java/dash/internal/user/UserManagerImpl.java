package dash.internal.user;

import dash.internal.user.data.ContactData;
import dash.internal.user.data.PermData;
import dash.internal.user.data.TagData;
import dash.internal.user.data.UserData;
import dash.internal.util.ClosableLock;
import dash.internal.util.LongObjectHashMap;
import dash.internal.util.LongObjectMap;
import io.ib67.dash.adapter.IAdapterRegistry;
import io.ib67.dash.tag.Tag;
import io.ib67.dash.user.IPermissionFactory;
import io.ib67.dash.user.IUserManager;
import io.ib67.dash.user.User;
import io.ib67.dash.user.UserBuilder;
import io.ib67.kiwi.Result;
import net.sf.persism.Parameters;
import net.sf.persism.SQL;
import net.sf.persism.Session;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

import static net.sf.persism.SQL.where;

public class UserManagerImpl implements IUserManager {
    private final Session session;
    private final LongObjectMap<User> cache = new LongObjectHashMap<>();
    private final Lock cacheReadLock;
    private final Lock cacheWriteLock;
    private final IPermissionFactory permissionFactory;
    private final IAdapterRegistry adapterRegistry;

    private static final SQL SQL_ID = where(":id = ?");

    public UserManagerImpl(Session session, IPermissionFactory permissionFactory, IAdapterRegistry adapterRegistry) {
        this.session = session;
        this.permissionFactory = permissionFactory;
        this.adapterRegistry = adapterRegistry;
        var lock = new ReentrantReadWriteLock();
        cacheReadLock = lock.readLock();
        cacheWriteLock = lock.writeLock();
    }

    @Override
    public Result<? extends User, ?> findUserById(long id) {
        try (var lock = new ClosableLock(cacheReadLock)) {
            if (cache.containsKey(id)) {
                return Result.ok(cache.get(id));
            }
            var result = session.fetch(UserData.class, SQL_ID, Parameters.params(id));
            if (result == null) return Result.err();
            try (var w = new ClosableLock(cacheWriteLock)) {
                var user = new UserImpl(result.getId(),
                        result.getPermissions().stream().map(PermData::getPerm).map(permissionFactory::parseNode).toList(),
                        result.getKnownContacts());
                for (TagData tag : result.getTags()) {
                    user.addTag(Tag.of(tag.getTag()));
                }
                cache.put(id, user);
                return Result.ok(user);
            }
        }
    }

    @Override
    public User createUser(Consumer<UserBuilder> configurator) {
        var userData = new UserData();
        session.withTransaction(() -> {
            var defaultBuilder = new UserBuilder();
            configurator.accept(defaultBuilder);
            userData.setUserName(defaultBuilder.username());
            userData.setRegisterTime(System.currentTimeMillis());
            session.insert(userData);

            var id = userData.getId();
            userData.setTags(defaultBuilder.tags().stream().map(it -> new TagData(id, it.identifier())).toList());
            userData.setPermissions(defaultBuilder.initialPermissions().stream().map(it -> new PermData(id, it.getNode())).toList());
            userData.setKnownContacts(List.of(new ContactData(id, defaultBuilder.initialContact().getPlatform().getName(), defaultBuilder.initialContact().getIdOnPlatform())));
            session.update(userData);
        });
        return findUserById(userData.getId()).orElseThrow();
    }

    @Override
    public void saveUser(User user) {
        cache.put(user.getId(),user);
        var data = new UserData();
        data.setPermissions(user.getPermissions().stream().map(it->new PermData(user.getId(),it.getNode())).toList());
        data.setKnownContacts(user.getKnownContacts().stream().map(it->new ContactData(user.getId(),it.getPlatform().getName(), it.getIdOnPlatform())).toList());
        data.setUserName(user.getName());
        data.setId(user.getId());
        data.setTags(user.getTags().stream().map(it->new TagData(user.getId(),it.identifier())).toList());
        session.update(data);
    }
}
