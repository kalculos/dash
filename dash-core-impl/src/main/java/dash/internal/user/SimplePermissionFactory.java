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
        return parseNode0(node,null);
    }

    @Override
    public Permission getNode(String node) {
        return cache.computeIfAbsent(node,this::parseNode);
    }

    @Override
    public Permission registerPermission(String node, String description) {
        node = node.trim().toLowerCase();
        if(cache.containsKey(node)){
            log.warn(node+" is already registered.");
        }
        var perm = parseNode0(node,description);
        cache.put(node,perm);
        return perm;
    }

    private Permission parseNode0(String node, String description){
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
        return new PermImpl(parent,reversed,current,description);
    }
}
