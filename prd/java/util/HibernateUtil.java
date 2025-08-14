package com.mccmr.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {
   private static final EntityManager entityManager;

   public HibernateUtil() {
   }

   public static EntityManager getEntityManager() {
      return entityManager;
   }

   public static void shutdown() {
      entityManager.close();
   }

   static {
      try {
         EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
         entityManager = emf.createEntityManager();
      } catch (Throwable ex) {
         throw new ExceptionInInitializerError(ex);
      }
   }
}
