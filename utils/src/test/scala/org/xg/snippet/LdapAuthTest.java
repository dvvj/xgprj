package org.xg.snippet;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;


public class LdapAuthTest {
    public static void main(String[] args) throws Exception {
        //LdapAuthTest ldapContxCrtn = new LdapAuthTest();
        LdapContext ctx = createLdapContext(
          "cn=ag_usr1,ou=Agents,dc=xg-org,dc=example,dc=com",
            "123"
//          "cn=ag_usr1,ou=Agents,dc=testpart,dc=org",
//          "123"
        );
        test1(ctx);
    }

    private static LdapContext createLdapContext(
      String dn,
      String cred
    ) throws Exception {
        LdapContext ctx = null;
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY,  "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "Simple");
        //it can be <domain\\userid> something that you use for windows login
        //it can also be
//            env.put(Context.SECURITY_PRINCIPAL, "uid=admin,ou=system");
//            env.put(Context.SECURITY_CREDENTIALS, "123");
        env.put(Context.SECURITY_PRINCIPAL, dn);
        env.put(Context.SECURITY_CREDENTIALS, cred);
        //in following property we specify ldap protocol and connection url.
        //generally the port is 389
        env.put(Context.PROVIDER_URL, "ldap://localhost:32773");
        ctx = new InitialLdapContext(env, null);
        System.out.println("Context created.");
        return ctx;
    }
    private static void test1(LdapContext ctx) throws Exception {
        SearchControls ctrls = new SearchControls();
        ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> results = ctx.search(
          "ou=Agents,dc=xg-org,dc=example,dc=com",
          "objectClass=person",
          ctrls
        );

        if (!results.hasMore()) {
            throw new AuthenticationException("Principal name not found");
        }

        while (results.hasMore()) {
            SearchResult result = results.next();
            System.out.println("DN: " + result.getNameInNamespace() );

            Attribute memberOf = result.getAttributes().get("memberOf");
            if(memberOf!=null) {
                for(int idx=0; idx<memberOf.size(); idx++) {
                    System.out.println("memberOf: " + memberOf.get(idx).toString() ); // CN=Mygroup,CN=Users,DC=mydomain,DC=com
                    //Attribute att = context.getAttributes(memberOf.get(idx).toString(), new String[]{"CN"}).get("CN");
                    //System.out.println( att.get().toString() ); //  CN part of groupname
                }
            }
        }
    }

}