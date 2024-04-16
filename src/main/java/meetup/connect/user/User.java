package meetup.connect.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import meetup.connect.meetupevent.MeetUpEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "_user")
public class User implements UserDetails {
  @Id @GeneratedValue private Long id;
  private String name;
  private String email;
  private String password;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL)
  private Set<MeetUpEvent> organizedMeetUpEvents = new HashSet<>();

  @ManyToMany(mappedBy = "attendees")
  private Set<MeetUpEvent> meetUpEvents = new HashSet<>();

  public User(Long id, String name, String email, String password) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
  }

  public User(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
  }

  public User() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(email, user.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public String getUsername() {
    return this.getEmail();
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
    return true;
  }

  public boolean isGmailUser() {
    if(this.getEmail() == null) return false;
    return this.getEmail().contains("@gmail.com");
  }
}
