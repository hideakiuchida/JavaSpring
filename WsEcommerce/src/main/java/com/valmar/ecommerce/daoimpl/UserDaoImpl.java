package com.valmar.ecommerce.daoimpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.valmar.ecommerce.dao.AbstractDao;
import com.valmar.ecommerce.dao.UserDao;
import com.valmar.ecommerce.model.Usuario;


@Repository("userDao")
@EnableTransactionManagement
public class UserDaoImpl extends AbstractDao<Integer, Usuario> implements UserDao{
	
	private final static String TB_USER = "USUARIO";

	public int validateUser(String username, String password){
		int userId = 0;
		Query query = getSession().createSQLQuery("SELECT id FROM " + TB_USER + " WHERE correo = :username"
				+ " AND contrasena = :password");
		query.setString("username", username);
		query.setString("password", password);
		@SuppressWarnings("unchecked")
		List<Object[]> results = query.list();
		Object obj = results.get(0);
		userId = Integer.parseInt(obj.toString());
		return userId;
	}

	@Override
	public Usuario getUserById(int userId) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.like("id",(long)userId)); 
		Usuario usuario = (Usuario)criteria.uniqueResult();
		return usuario;
	}
	
	public Usuario findByUsername(String username) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.like("correo", username)); 
		Usuario usuario = (Usuario)criteria.uniqueResult();
		return usuario;
	}

}