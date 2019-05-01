package org.openpbr.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * A PlanningUnitGroup.
 */
@Entity
@Table(name = "planning_unit_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "planningunitgroup")
public class PlanningUnitGroup extends IdentifiableEntity implements Serializable {

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
    @JoinTable(name = "planning_unit_group_members",
               joinColumns = @JoinColumn(name = "planning_unit_group_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "planning_unit_id", referencedColumnName = "id"))
    private Set<PlanningUnit> planningUnits = new HashSet<>();

    @ManyToMany(mappedBy = "planningUnitGroups")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<PlanningUnitGroupSet> planningUnitGroupSets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlanningUnitGroup uid(String uid) {
        this.uid = uid;
        return this;
    }

    public PlanningUnitGroup code(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public PlanningUnitGroup name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public PlanningUnitGroup sortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public PlanningUnitGroup isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<PlanningUnit> getPlanningUnits() {
        return planningUnits;
    }

    public PlanningUnitGroup planningUnits(Set<PlanningUnit> planningUnits) {
        this.planningUnits = planningUnits;
        return this;
    }

    public PlanningUnitGroup addPlanningUnits(PlanningUnit planningUnit) {
        this.planningUnits.add(planningUnit);
        planningUnit.getPlanningUnitGroups().add(this);
        return this;
    }

    public PlanningUnitGroup removePlanningUnits(PlanningUnit planningUnit) {
        this.planningUnits.remove(planningUnit);
        planningUnit.getPlanningUnitGroups().remove(this);
        return this;
    }

    public void setPlanningUnits(Set<PlanningUnit> planningUnits) {
        this.planningUnits = planningUnits;
    }

    public Set<PlanningUnitGroupSet> getPlanningUnitGroupSets() {
        return planningUnitGroupSets;
    }

    public PlanningUnitGroup planningUnitGroupSets(Set<PlanningUnitGroupSet> planningUnitGroupSets) {
        this.planningUnitGroupSets = planningUnitGroupSets;
        return this;
    }

    public PlanningUnitGroup addPlanningUnitGroupSet(PlanningUnitGroupSet planningUnitGroupSet) {
        this.planningUnitGroupSets.add(planningUnitGroupSet);
        planningUnitGroupSet.getPlanningUnitGroups().add(this);
        return this;
    }

    public PlanningUnitGroup removePlanningUnitGroupSet(PlanningUnitGroupSet planningUnitGroupSet) {
        this.planningUnitGroupSets.remove(planningUnitGroupSet);
        planningUnitGroupSet.getPlanningUnitGroups().remove(this);
        return this;
    }

    public void setPlanningUnitGroupSets(Set<PlanningUnitGroupSet> planningUnitGroupSets) {
        this.planningUnitGroupSets = planningUnitGroupSets;
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
        PlanningUnitGroup planningUnitGroup = (PlanningUnitGroup) o;
        if (planningUnitGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), planningUnitGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PlanningUnitGroup{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", sortOrder=" + getSortOrder() +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
