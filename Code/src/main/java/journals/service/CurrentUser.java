package journals.service;

import org.springframework.security.core.authority.AuthorityUtils;

import journals.model.Role;
import journals.model.User;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

  private static final long serialVersionUID = 8973071434203037965L;

  private User user;

	public CurrentUser(User user) {
		super(user.getLoginName(), user.getPwd(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public Long getId() {
		return user.getId();
	}

	public Role getRole() {
		return user.getRole();
	}

}