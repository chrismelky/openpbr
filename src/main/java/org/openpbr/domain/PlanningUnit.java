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
 * A PlanningUnit.
 */
@Entity
@Table(name = "planning_unit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "planningunit")
public class PlanningUnit extends IdentifiableEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    
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

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToOne
    @JoinColumn()
    private PlanningUnit parent;

    @ManyToMany(mappedBy = "planningUnits")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<PlanningUnitGroup> planningUnitGroups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlanningUnit uid(String uid) {
        this.uid = uid;
        return this;
    }

    public PlanningUnit code(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public PlanningUnit name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public PlanningUnit level(Integer level) {
        this.level = level;
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public PlanningUnit sortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public PlanningUnit isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public PlanningUnit getParent() {
        return parent;
    }

    public PlanningUnit parent(PlanningUnit planningUnit) {
        this.parent = planningUnit;
        return this;
    }

    public void setParent(PlanningUnit planningUnit) {
        this.parent = planningUnit;
    }

    public Set<PlanningUnitGroup> getPlanningUnitGroups() {
        return planningUnitGroups;
    }

    public PlanningUnit planningUnitGroups(Set<PlanningUnitGroup> planningUnitGroups) {
        this.planningUnitGroups = planningUnitGroups;
        return this;
    }

    public PlanningUnit addPlanningUnitGroup(PlanningUnitGroup planningUnitGroup) {
        this.planningUnitGroups.add(planningUnitGroup);
        planningUnitGroup.getPlanningUnits().add(this);
        return this;
    }

    public PlanningUnit removePlanningUnitGroup(PlanningUnitGroup planningUnitGroup) {
        this.planningUnitGroups.remove(planningUnitGroup);
        planningUnitGroup.getPlanningUnits().remove(this);
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
        PlanningUnit planningUnit = (PlanningUnit) o;
        if (planningUnit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), planningUnit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PlanningUnit{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", level=" + getLevel() +
            ", sortOrder=" + getSortOrder() +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
