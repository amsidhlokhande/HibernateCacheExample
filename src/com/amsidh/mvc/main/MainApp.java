package com.amsidh.mvc.main;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.amsidh.mvc.domain.Shop;

public class MainApp {

	public static void main(String[] args) {
		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties()).build();
		SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

		// Second Level Cache And Query Cache
		Session session1 = sessionFactory.openSession();
		session1.beginTransaction();
		// Shop shop1 = (Shop) session1.get(Shop.class, 1);
		Query query1 = session1.createQuery("from Shop where shopId = :shopId");
		query1.setCacheable(true);

		query1.setInteger("shopId", 1);
		Shop shop1 = (Shop) query1.list().get(0);
		System.out.println(shop1.getShopName());
		session1.flush();
		session1.getTransaction().commit();
		session1.close();

		try {
			// Thread.sleep(6000);//This will fire new query to database as
			// cache invalid the data
			Thread.sleep(6000); // This will not fire query to database as cache
								// for the requested data is still live and
								// present
		} catch (Exception e) {
		}

		Session session2 = sessionFactory.openSession();
		session2.beginTransaction();
		// Shop shop2 = (Shop) session2.get(Shop.class, 1);
		Query query2 = session2.createQuery("from Shop where shopId = :shopId");
		query2.setInteger("shopId", 1);
		query2.setCacheable(true);
		Shop shop2 = (Shop) query2.list().get(0);
		System.out.println(shop2.getShopName());
		session2.flush();
		session2.getTransaction().commit();
		session2.close();

		sessionFactory.close();
	}

}
