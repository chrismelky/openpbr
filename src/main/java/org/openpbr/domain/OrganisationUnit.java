package org.openpbr.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A OrganisationUnit.
 */
@Entity
@Table(name = "organisation_unit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "organisationunit")
public class OrganisationUnit extends IdentifiableEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;


    @NotNull
    @Size(max = 230)
    @Column(name = "name", length = 230, nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "opening_date")
    private LocalDate openingDate;

    @Column(name = "closed_date")
    private LocalDate closedDate;

    @Column(name = "url")
    private String url;

    @Column(name = "latitude", precision = 10, scale = 2)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 2)
    private BigDecimal longitude;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToOne
    @JoinColumn(unique = true)
    @JsonIgnoreProperties({"isActive","sortOrder",
        "phoneNumber", "email", "address", "longitude", "latitude", "url",
        "closedDate", "openingDate", "uid","code", "createdBy","createdDate", "lastModifiedBy", "lastModifiedDate"})
    private OrganisationUnit parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public OrganisationUnit uid(String uid) {
        this.uid = uid;
        return this;
    }
    
    public OrganisationUnit code(String code) {
        this.code = code;
        return this;
    }
    
    public String getName() {
        return name;
    }

    public OrganisationUnit name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public OrganisationUnit level(Integer level) {
        this.level = level;
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public OrganisationUnit openingDate(LocalDate openingDate) {
        this.openingDate = openingDate;
        return this;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public LocalDate getClosedDate() {
        return closedDate;
    }

    public OrganisationUnit closedDate(LocalDate closedDate) {
        this.closedDate = closedDate;
        return this;
    }

    public void setClosedDate(LocalDate closedDate) {
        this.closedDate = closedDate;
    }

    public String getUrl() {
        return url;
    }

    public OrganisationUnit url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public OrganisationUnit latitude(BigDecimal latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public OrganisationUnit longitude(BigDecimal longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public OrganisationUnit address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public OrganisationUnit email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public OrganisationUnit phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public OrganisationUnit sortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public OrganisationUnit isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public OrganisationUnit getParent() {
        return parent;
    }

    public OrganisationUnit parent(OrganisationUnit organisationUnit) {
        this.parent = organisationUnit;
        return this;
    }

    public void setParent(OrganisationUnit organisationUnit) {
        this.parent = organisationUnit;
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
        OrganisationUnit organisationUnit = (OrganisationUnit) o;
        if (organisationUnit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organisationUnit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrganisationUnit{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", level=" + getLevel() +
            ", openingDate='" + getOpeningDate() + "'" +
            ", closedDate='" + getClosedDate() + "'" +
            ", url='" + getUrl() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", address='" + getAddress() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
