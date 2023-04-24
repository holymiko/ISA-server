package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import lombok.Getter;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Role;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "account")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue
    private long id;
    // TODO Unique
    @Pattern(regexp = "^.{6,}",
            message = "Username must be at least 6 characters long")
    private String username;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$",
            message = "Password must contain at least one lower letter, one upper letter, one number and be at least 6 characters long")
    private String password;
    private Role role;
    @OneToOne(cascade = CascadeType.REMOVE)
    private Person person;

    public Account(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Account(String username, String password, Role role, Person person) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.person = person;
    }
}
