package fr.iutinfo.rest;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import org.skife.jdbi.v2.tweak.BeanMapperFactory;

public interface UsersDAO {
    /*@SqlUpdate("CREATE TABLE users\n" + 
    		"(\n" + 
    		"	uno SERIAL PRIMARY KEY,\n" + 
    		"	login VARCHAR(100) NOT NULL,\n" + 
    		"	password VARCHAR(50) NOT NULL,\n" + 
    		"	nom VARCHAR(50) NOT NULL,\n" + 
    		"	prenom VARCHAR(50) NOT NULL,\n" + 
    		"	fonction VARCHAR(100),\n" + 
    		"	cno INT NOT NULL,\n" + 
    		"\n" + 
    		"	CONSTRAINT fk_cno FOREIGN KEY (cno)\n" + 
    		"		REFERENCES corporate(cno),\n" + 
    		"\n" + 
    		"	CONSTRAINT uniq_login UNIQUE (login)\n" + 
    		")")
    void createUserTable();
    
    @SqlUpdate("drop table if exists users")
    void dropUserTable();    
    */
	
    @SqlUpdate("insert into users (login, password, nom, prenom, fonction, cno) values (:login, :password, :nom, :prenom, :fonction, :cno)")
    @GetGeneratedKeys
    int insert(@BindBean() User user);
    
    @SqlQuery("select * from users where name = :nom")
    @RegisterMapperFactory(BeanMapperFactory.class)
    User findByName(@Bind("nom") String nom);
    
    @SqlQuery("select * from users where search like :nom")
    @RegisterMapperFactory(BeanMapperFactory.class)
    List<User> search(@Bind("nom") String nom);
    
    @SqlUpdate("delete from users where uno = :uno")
    void delete(@Bind("uno") int uno);
    
    @SqlQuery("select * from users order by uno")
    @RegisterMapperFactory(BeanMapperFactory.class)
    List<User> all();
    
    @SqlQuery("select * from users where uno = :uno")
    @RegisterMapperFactory(BeanMapperFactory.class)
    User findByUno(@Bind("uno") int uno);
    
    void close();
}