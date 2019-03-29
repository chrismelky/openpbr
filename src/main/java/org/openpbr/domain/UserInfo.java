package org.openpbr.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import org.openpbr.domain.enumeration.Gender;

/**
 * A UserInfo.
 */
@Entity
@Table(name = "user_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "userinfo")
public class UserInfo extends IdentifiableEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private Long id;

    @Size(max = 160)
    @Column(name = "last_name", length = 160)
    private String lastName;

    @Size(max = 160)
    @Column(name = "first_name", length = 160)
    private String firstName;

    @Size(max = 160)
    @Column(name = "email", length = 160, unique = true)
    private String email;

    @Size(max = 80)
    @Column(name = "phone_number", length = 80)
    private String phoneNumber;

    @Size(max = 160)
    @Column(name = "job_title", length = 160)
    private String jobTitle;

    @Column(name = "introduction")
    private String introduction;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "birth_day")
    private LocalDate birthDay;

    @Size(max = 160)
    @Column(name = "nationality", length = 160)
    private String nationality;

    @Size(max = 160)
    @Column(name = "employer", length = 160)
    private String employer;

    @Column(name = "education")
    private String education;

    @OneToOne(optional = false)    @NotNull

    @MapsId
    @JoinColumn(name = "id")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public String getUid() {
//        return uid;
//    }
//
    public UserInfo uid(String uid) {
        this.uid = uid;
        return this;
    }
//
//    public void setUid(String uid) {
//        this.uid = uid;
//    }
//
//    public String getCode() {
//        return code;
//    }
//
    public UserInfo code(String code) {
        this.code = code;
        return this;
    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }

    public String getLastName() {
        return lastName;
    }

    public UserInfo lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserInfo firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public UserInfo email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserInfo phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public UserInfo jobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getIntroduction() {
        return introduction;
    }

    public UserInfo introduction(String introduction) {
        this.introduction = introduction;
        return this;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Gender getGender() {
        return gender;
    }

    public UserInfo gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public UserInfo birthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
        return this;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public String getNationality() {
        return nationality;
    }

    public UserInfo nationality(String nationality) {
        this.nationality = nationality;
        return this;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getEmployer() {
        return employer;
    }

    public UserInfo employer(String employer) {
        this.employer = employer;
        return this;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getEducation() {
        return education;
    }

    public UserInfo education(String education) {
        this.education = education;
        return this;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public User getUser() {
        return user;
    }

    public UserInfo user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInfo userInfo = (UserInfo) o;
        if (userInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserInfo{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", introduction='" + getIntroduction() + "'" +
            ", gender='" + getGender() + "'" +
            ", birthDay='" + getBirthDay() + "'" +
            ", nationality='" + getNationality() + "'" +
            ", employer='" + getEmployer() + "'" +
            ", education='" + getEducation() + "'" +
            "}";
    }
}
