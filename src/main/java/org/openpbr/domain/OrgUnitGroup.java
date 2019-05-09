package org.openpbr.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A OrgUnitGroup.
 */
@Entity
@Table(name = "org_unit_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "orgunitgroup")
public class OrgUnitGroup extends IdentifiableEntity implements Serializable {

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

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "org_unit_group_members",
               joinColumns = @JoinColumn(name = "org_unit_group_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "organisation_unit_id", referencedColumnName = "id"))
    private Set<OrganisationUnit> organisationUnits = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "org_unit_group_attribute_values",
        joinColumns = @JoinColumn(name = "org_unit_group_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "attribute_value_id", referencedColumnName = "id"))
    private Set<AttributeValue> attributeValues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public OrgUnitGroup uid(String uid) {
        this.uid = uid;
        return this;
    }

    public OrgUnitGroup code(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public OrgUnitGroup name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public OrgUnitGroup sortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public OrgUnitGroup isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<OrganisationUnit> getOrganisationUnits() {
        return organisationUnits;
    }

    public OrgUnitGroup organisationUnits(Set<OrganisationUnit> organisationUnits) {
        this.organisationUnits = organisationUnits;
        return this;
    }

    public OrgUnitGroup addOrganisationUnits(OrganisationUnit organisationUnit) {
        this.organisationUnits.add(organisationUnit);
       // organisationUnit.getOrgUnitGroups().add(this);
        return this;
    }

    public OrgUnitGroup removeOrganisationUnits(OrganisationUnit organisationUnit) {
        this.organisationUnits.remove(organisationUnit);
     //   organisationUnit.getOrgUnitGroups().remove(this);
        return this;
    }

    public void setOrganisationUnits(Set<OrganisationUnit> organisationUnits) {
        this.organisationUnits = organisationUnits;
    }

    public Set<AttributeValue> getAttributeValues() {
        return attributeValues;
    }

    public OrgUnitGroup attributeValues(Set<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
        return this;
    }

    public OrgUnitGroup addAttributeValues(AttributeValue attributeValue) {
        this.attributeValues.add(attributeValue);
     //   attributeValue.getOrgUnitGroups().add(this);
        return this;
    }

    public OrgUnitGroup removeAttributeValues(AttributeValue attributeValue) {
        this.attributeValues.remove(attributeValue);
     //   attributeValue.getOrgUnitGroups().remove(this);
        return this;
    }

    public void setAttributeValues(Set<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
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
        OrgUnitGroup orgUnitGroup = (OrgUnitGroup) o;
        if (orgUnitGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orgUnitGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrgUnitGroup{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
