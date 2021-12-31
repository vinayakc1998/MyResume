package com.app.pojos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;

	@Column(length = 20)
	@NotBlank(message = "Name is required")
	@Length(min = 3, max = 15, message = "Invalid name length")
	private String firstName;

	@Column(length = 20)
	@NotBlank(message = "Name is required")
	@Length(min = 3, max = 15, message = "Invalid name length")
	private String lastName;

	@Column(length = 200)
	@NotBlank(message = "Email is required")
	@Email(message = "Invalid Email Format ")
	private String email;

	@Column(length = 200, nullable = false)
	@NotBlank(message = "Password is required")
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", length = 20)
	private Role role;

	@OneToOne( mappedBy = "user", orphanRemoval = true)
	private LearningLicense learningLicense;

	@OneToOne(mappedBy = "user", orphanRemoval = true)
	private PermanentLicense permanentLicense;

	public User() {
		// TODO Auto-generated constructor stub
	}

	public User(String firstName, String lastName, String email, String password, Role role) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public LearningLicense getLearningLicense() {
		return learningLicense;
	}

	public void setLearningLicense(LearningLicense learningLicense) {
		this.learningLicense = learningLicense;
	}

	public PermanentLicense getPermanentLicense() {
		return permanentLicense;
	}

	public void setPermanentLicense(PermanentLicense permanentLicense) {
		this.permanentLicense = permanentLicense;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
