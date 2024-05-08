package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;

    // TODO Email validation
    private String email;
    // TODO Phone validation
    private Long phone;

    public Person(String firstName, String middleName, String lastName, String email, Long phone) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

}
