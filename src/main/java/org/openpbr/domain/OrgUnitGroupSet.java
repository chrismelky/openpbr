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
 * A OrgUnitGroupSet.
 */
@Entity
@Table(name = "org_unit_group_set")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "orgunitgroupset")
public class OrgUnitGroupSet extends IdentifiableEntity implements Serializable {

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

    @Column(name = "description")
    private String description;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "org_unit_group_set_members",
               joinColumns = @JoinColumn(name = "org_unit_group_set_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "org_unit_group_id", referencedColumnName = "id"))
    private Set<OrgUnitGroup> orgUnitGroups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public OrgUnitGroupSet uid(String uid) {
        this.uid = uid;
        return this;
    }

    public OrgUnitGroupSet code(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public OrgUnitGroupSet name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public OrgUnitGroupSet description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public OrgUnitGroupSet sortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public OrgUnitGroupSet isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<OrgUnitGroup> getOrgUnitGroups() {
        return orgUnitGroups;
    }

    public OrgUnitGroupSet orgUnitGroups(Set<OrgUnitGroup> orgUnitGroups) {
        this.orgUnitGroups = orgUnitGroups;
        return this;
    }

    public OrgUnitGroupSet addOrgUnitGroups(OrgUnitGroup orgUnitGroup) {
        this.orgUnitGroups.add(orgUnitGroup);
      //  orgUnitGroup.getOrgUnitGroupSets().add(this);
        return this;
    }

    public OrgUnitGroupSet removeOrgUnitGroups(OrgUnitGroup orgUnitGroup) {
        this.orgUnitGroups.remove(orgUnitGroup);
      //  orgUnitGroup.getOrgUnitGroupSets().remove(this);
        return this;
    }

    public void setOrgUnitGroups(Set<OrgUnitGroup> orgUnitGroups) {
        this.orgUnitGroups = orgUnitGroups;
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
        OrgUnitGroupSet orgUnitGroupSet = (OrgUnitGroupSet) o;
        if (orgUnitGroupSet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orgUnitGroupSet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrgUnitGroupSet{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
