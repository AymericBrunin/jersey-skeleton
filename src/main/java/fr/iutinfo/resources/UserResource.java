package fr.iutinfo.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import fr.iutinfo.App;
import fr.iutinfo.beans.Feedback;
import fr.iutinfo.beans.User;
import fr.iutinfo.dao.UserDao;
import fr.iutinfo.utils.Utils;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
	private static UserDao dao = App.dbi.open(UserDao.class);

	public UserResource() {}

	/**
	 * Insert l'utilisateur si celui ci est valide.
	 * @param user
	 * @return
	 */
	@POST
	@Path("/register")
	public Feedback createUser(User user) {
		Feedback fb = null;
		try {
			fb = isValidUser(user);
			if(fb.isSuccess()) {

				// on hashe le mdp pour le protéger
				String hashedPassword = Utils.hashMD5(user.getPassword());
				if(hashedPassword == null)
					return new Feedback(false, "An error occurs during hashing");
				user.setPassword(hashedPassword);

				// on insert l'utilisateur dans la bdd.
				dao.insert(user.getName(), user.getPassword(), user.getEmail());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new Feedback(false, "An error occurs during insertion to database");
		}
		return fb;
	}

	/**
	 * Vérifie si l'utilisateur existe déjà (pseudo/email), si il est valide (email correcte, pseudo correct, mdp correct...)
	 * @param u
	 * @return
	 */
	private Feedback isValidUser(User user) {
		// test le pseudo
		if(!user.getName().matches("^[a-zA-Z0-9À-ÿ-]{3,20}$")) {
			return new Feedback(false, "Le pseudo est invalide !");
		}

		// Test si le pseudo existe déjà ou non
		User u = dao.isNameExist(user.getName());
		if(u != null)
			return new Feedback(false, "Le pseudo est déjà utilisé");



		// test si l'email est correcte
		if(!user.getEmail().matches("^[a-zA-Z0-9._-]+@[a-z0-9._-]{2,}\\.[a-z]{2,4}$")) {
			return new Feedback(false, "L'adresse mail est incorrecte");
		}

		// Test si l'email a déjà été utilisée
		u = dao.isEmailExist(user.getEmail());
		if(u != null)
			return new Feedback(false, "L'adresse email est déjà utilisée");

		
		// test si le mdp est correct
		if(user.getPassword().length() < 6) {
			return new Feedback(false, "Le mot de passe doit faire au moins 6 caractères !");
		}

		return new Feedback(true, "Register OK");
	}


	@POST
	@Path("/login")
	public Feedback logUser(User user) {
		String hashedPassword = Utils.hashMD5(user.getPassword());
		if(hashedPassword == null)
			return new Feedback(false, "An error occurs during hashing");
		user.setPassword(hashedPassword);

		try {
			User u = dao.userIsCorrect(user.getName(), user.getPassword());
			if(u == null) 
				return new Feedback(false, "user doesn't exist");
		} catch (Exception e) {
			e.printStackTrace();
			return new Feedback(false, "An error occurs during query to database");
		}
		return new Feedback(true, "Login OK");
	}


	@GET
	@Path("/{id}")
	public User getUser(@PathParam("id") int id) {
		User out = dao.findById(id);
		if (out == null) {
			throw new WebApplicationException(404);
		}
		return out;
	}


	@GET
	public List<User> getUsers() {
		List<User> out = dao.getAll();
		if (out == null) {
			throw new WebApplicationException(404);
		}
		return out;
	}

}