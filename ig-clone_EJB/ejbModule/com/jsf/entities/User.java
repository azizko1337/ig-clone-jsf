package com.jsf.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private Timestamp createdAt;

	private String email;

	@Lob
	private String firstName;

	@Lob
	private String lastName;

	@Lob
	private String nickname;

	@Lob
	private String password;

	private Timestamp updatedAt;

	//bi-directional many-to-one association to Comment
	@OneToMany(mappedBy="user")
	private List<Comment> comments;

	//bi-directional many-to-one association to Follow
	@OneToMany(mappedBy="user1")
	private List<Follow> follows1;

	//bi-directional many-to-one association to Follow
	@OneToMany(mappedBy="user2")
	private List<Follow> follows2;

	//bi-directional many-to-one association to Like
	@OneToMany(mappedBy="user")
	private List<Like> likes;

	//bi-directional many-to-one association to Post
	@OneToMany(mappedBy="user")
	private List<Post> posts;

	//bi-directional many-to-one association to UserRole
	@OneToMany(mappedBy="user")
	private List<UserRole> userRoles;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="createdBy")
	private User user1;

	//bi-directional many-to-one association to User
	@OneToMany(mappedBy="user1")
	private List<User> users1;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="updatedBy")
	private User user2;

	//bi-directional many-to-one association to User
	@OneToMany(mappedBy="user2")
	private List<User> users2;

	public User() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<Comment> getComments() {
		return this.comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Comment addComment(Comment comment) {
		getComments().add(comment);
		comment.setUser(this);

		return comment;
	}

	public Comment removeComment(Comment comment) {
		getComments().remove(comment);
		comment.setUser(null);

		return comment;
	}

	public List<Follow> getFollows1() {
		return this.follows1;
	}

	public void setFollows1(List<Follow> follows1) {
		this.follows1 = follows1;
	}

	public Follow addFollows1(Follow follows1) {
		getFollows1().add(follows1);
		follows1.setUser1(this);

		return follows1;
	}

	public Follow removeFollows1(Follow follows1) {
		getFollows1().remove(follows1);
		follows1.setUser1(null);

		return follows1;
	}

	public List<Follow> getFollows2() {
		return this.follows2;
	}

	public void setFollows2(List<Follow> follows2) {
		this.follows2 = follows2;
	}

	public Follow addFollows2(Follow follows2) {
		getFollows2().add(follows2);
		follows2.setUser2(this);

		return follows2;
	}

	public Follow removeFollows2(Follow follows2) {
		getFollows2().remove(follows2);
		follows2.setUser2(null);

		return follows2;
	}

	public List<Like> getLikes() {
		return this.likes;
	}

	public void setLikes(List<Like> likes) {
		this.likes = likes;
	}

	public Like addLike(Like like) {
		getLikes().add(like);
		like.setUser(this);

		return like;
	}

	public Like removeLike(Like like) {
		getLikes().remove(like);
		like.setUser(null);

		return like;
	}

	public List<Post> getPosts() {
		return this.posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public Post addPost(Post post) {
		getPosts().add(post);
		post.setUser(this);

		return post;
	}

	public Post removePost(Post post) {
		getPosts().remove(post);
		post.setUser(null);

		return post;
	}

	public List<UserRole> getUserRoles() {
		return this.userRoles;
	}

	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public UserRole addUserRole(UserRole userRole) {
		getUserRoles().add(userRole);
		userRole.setUser(this);

		return userRole;
	}

	public UserRole removeUserRole(UserRole userRole) {
		getUserRoles().remove(userRole);
		userRole.setUser(null);

		return userRole;
	}

	public User getUser1() {
		return this.user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public List<User> getUsers1() {
		return this.users1;
	}

	public void setUsers1(List<User> users1) {
		this.users1 = users1;
	}

	public User addUsers1(User users1) {
		getUsers1().add(users1);
		users1.setUser1(this);

		return users1;
	}

	public User removeUsers1(User users1) {
		getUsers1().remove(users1);
		users1.setUser1(null);

		return users1;
	}

	public User getUser2() {
		return this.user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public List<User> getUsers2() {
		return this.users2;
	}

	public void setUsers2(List<User> users2) {
		this.users2 = users2;
	}

	public User addUsers2(User users2) {
		getUsers2().add(users2);
		users2.setUser2(this);

		return users2;
	}

	public User removeUsers2(User users2) {
		getUsers2().remove(users2);
		users2.setUser2(null);

		return users2;
	}

}