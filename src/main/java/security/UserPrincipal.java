package security;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import model.User;

public class UserPrincipal implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private Long userId;
	private Boolean enabled;
	private Boolean tempUser;

	public Boolean isTempUser() {
		return tempUser;
	}

	public void setTempUser(Boolean tempUser) {
		this.tempUser = tempUser;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	private String userDisplayName;

	private String businessUnit;

	public String getUserDisplayName() {
		return userDisplayName;
	}

	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	private String username;

	@JsonIgnore
	private String email;

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	public UserPrincipal(Long id, String username, String email, String password, String userDisplayName,
			String businessUnit, Collection<? extends GrantedAuthority> authorities, Boolean deleted, Boolean enabled) {
		this.id = id;
		this.userId = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.userDisplayName = userDisplayName;
		this.businessUnit = businessUnit;
		this.authorities = authorities;
		this.enabled = (deleted != null) ? !deleted : true;
		this.tempUser = (enabled != null) ? !enabled : false;
	}

	public static UserPrincipal create(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role))
				.collect(Collectors.toList());

		return new UserPrincipal(user.getId(), user.getUserName(), user.getEmail(), user.getPassword(),
				user.getUserDisplayName(), user.getBusinessUnit(), authorities, user.isDeleted(), user.isEnabled());
	}

	public Long getId() {
		return id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserPrincipal that = (UserPrincipal) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id);
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
