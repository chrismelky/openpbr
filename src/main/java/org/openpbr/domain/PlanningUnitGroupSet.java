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
 * A PlanningUnitGroupSet.
 */
@Entity
@Table(name = "planning_unit_group_set")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "planningunitgroupset")
public class PlanningUnitGroupSet extends IdentifiableEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
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
    @NotNull
    @JoinTable(name = "planning_unit_group_set_members",
               joinColumns = @JoinColumn(name = "planning_unit_group_set_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "planning_unit_group_id", referencedColumnName = "id"))
    private Set<PlanningUnitGroup> planningUnitGroups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlanningUnitGroupSet uid(String uid) {
        this.uid = uid;
        return this;
    }

    public PlanningUnitGroupSet code(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public PlanningUnitGroupSet name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public PlanningUnitGroupSet sortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public PlanningUnitGroupSet isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<PlanningUnitGroup> getPlanningUnitGroups() {
        return planningUnitGroups;
    }

    public PlanningUnitGroupSet planningUnitGroups(Set<PlanningUnitGroup> planningUnitGroups) {
        this.planningUnitGroups = planningUnitGroups;
        return this;
    }

    public PlanningUnitGroupSet addPlanningUnitGroups(PlanningUnitGroup planningUnitGroup) {
        this.planningUnitGroups.add(planningUnitGroup);
        planningUnitGroup.getPlanningUnitGroupSets().add(this);
        return this;
    }

    public PlanningUnitGroupSet removePlanningUnitGroups(PlanningUnitGroup planningUnitGroup) {
        this.planningUnitGroups.remove(planningUnitGroup);
        planningUnitGroup.getPlanningUnitGroupSets().remove(this);
        return this;
    }

    public void setPlanningUnitGroups(Set<PlanningUnitGroup> planningUnitGroups) {
        this.planningUnitGroups = planningUnitGroups;
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
        PlanningUnitGroupSet planningUnitGroupSet = (PlanningUnitGroupSet) o;
        if (planningUnitGroupSet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), planningUnitGroupSet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PlanningUnitGroupSet{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
