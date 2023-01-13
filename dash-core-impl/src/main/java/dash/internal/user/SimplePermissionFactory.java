package dash.internal.user;

import dash.internal.user.perm.PermImpl;
import io.ib67.dash.user.IPermissionFactory;
import io.ib67.dash.user.permission.Permission;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SimplePermissionFactory implements IPermissionFactory {
    private final Map<String, Permission> cache = new ConcurrentHashMap<>();

    @Override
    public Permission parseNode(String node) {
        return cache.computeIfAbsent(node,it->registerPermission(it,null));
    }

    @Override
    public Permission registerPermission(String node, String description) {
        node = node.trim();
        if(cache.containsKey(node)){
            log.warn(node+" is already registered.");
        }
        boolean reversed = false;
        String parent;
        String current = null;
        if(node.startsWith("-")){
            reversed = true;
            node = node.substring(1);
        }
        var delimiter = node.lastIndexOf('.');
        if(delimiter == -1){
            parent = node;
        }else{
            parent = node.substring(0,delimiter);
            current = node.substring(delimiter+1);
            if("*".equals(current) || current.isEmpty()){
                current = null;
            }
        }
        var perm = new PermImpl(parent,reversed,current,description);
        cache.put(node,perm);
        return perm;
    }
}
