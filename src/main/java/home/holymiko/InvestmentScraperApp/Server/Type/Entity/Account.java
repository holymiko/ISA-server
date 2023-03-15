package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import lombok.Getter;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Role;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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

    private String username;

    private String password;

    private Role role;

    public Account (String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
