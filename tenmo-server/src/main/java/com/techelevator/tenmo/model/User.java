package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.techelevator.tenmo.dao.JdbcUserDao;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class User {

   private int id;
   private String username;
   @JsonIgnore // prevent from being sent to client
   private String password;
   @JsonIgnore
   private boolean activated;
   private Set<Authority> authorities = new HashSet<>();

   JdbcUserDao userDao = new JdbcUserDao();

   public User() {
   }

   public User(int id, String username, String password, String authorities) {
      userDao.create(username, password);
      this.id = id;
      this.username = username;
      this.password = password;
      if (authorities != null)
         this.setAuthorities(authorities);
      else
         this.setAuthorities("USER");
      this.activated = true;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public boolean isActivated() {
      return activated;
   }

   public void setActivated(boolean activated) {
      this.activated = activated;
   }

   public Set<Authority> getAuthorities() {
      return authorities;
   }

   public void setAuthorities(Set<Authority> authorities) {
      this.authorities = authorities;
   }

   public void setAuthorities(String authorities) {
      String[] roles = authorities.split(",");
      for (String role : roles) {
         this.authorities.add(new Authority("ROLE_" + role));
      }
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || this.getClass() != o.getClass())
         return false;
      User user = (User) o;
      return this.id == user.id &&
            this.activated == user.activated &&
            Objects.equals(this.username, user.username) &&
            (Objects.equals(this.password, user.getPassword())
                  || new BCryptPasswordEncoder().matches(user.getPassword(), this.password)
                  || new BCryptPasswordEncoder()
                        .matches(this.getPassword(),
                              user.password))
            &&
            (this.authorities.equals(user.authorities));
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, username, password, activated, authorities);
   }

   @Override
   public String toString() {
      return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", activated=" + activated +
            ", authorities=" + authorities +
            '}';
   }
}
