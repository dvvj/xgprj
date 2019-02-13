package org.xg.ldap

import java.util

import javax.naming.Context
import javax.naming.ldap.{InitialLdapContext, LdapContext}

object LdapHelpers {

  import org.xg.logging.LogHelpers._
  private[ldap] def createContext(
                                 dn:String,
                                 cred:String,
                                 hostPort:String
                                 ):LdapContext = {
    import java.util.Hashtable

    val ht = new util.Hashtable[String, String]()

    ht.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory")
    ht.put(Context.SECURITY_AUTHENTICATION, "Simple")
    ht.put(Context.SECURITY_PRINCIPAL, dn)
    ht.put(Context.SECURITY_CREDENTIALS, cred)
    ht.put(Context.PROVIDER_URL, hostPort)

    val res = new InitialLdapContext(ht, null)

    log("Context created!")
    res
  }

}
