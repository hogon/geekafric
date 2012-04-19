package com.geek.afric.server;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * 
 * @author Mcicheick
 *
 */
public final class PMF {
    private static final PersistenceManagerFactory pmfInstance =
        JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private PMF() {}

    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}