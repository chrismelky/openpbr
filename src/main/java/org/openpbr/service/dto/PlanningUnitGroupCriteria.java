package org.openpbr.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the PlanningUnitGroup entity. This class is used in PlanningUnitGroupResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /planning-unit-groups?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlanningUnitGroupCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter uid;

    private StringFilter code;

    private StringFilter name;

    private IntegerFilter sortOrder;

    private BooleanFilter isActive;

    private LongFilter planningUnitsId;

    private LongFilter planningUnitGroupSetId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUid() {
        return uid;
    }

    public void setUid(StringFilter uid) {
        this.uid = uid;
    }

    public StringFilter getCode() {
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public IntegerFilter getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(IntegerFilter sortOrder) {
        this.sortOrder = sortOrder;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getPlanningUnitsId() {
        return planningUnitsId;
    }

    public void setPlanningUnitsId(LongFilter planningUnitsId) {
        this.planningUnitsId = planningUnitsId;
    }

    public LongFilter getPlanningUnitGroupSetId() {
        return planningUnitGroupSetId;
    }

    public void setPlanningUnitGroupSetId(LongFilter planningUnitGroupSetId) {
        this.planningUnitGroupSetId = planningUnitGroupSetId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PlanningUnitGroupCriteria that = (PlanningUnitGroupCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(uid, that.uid) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(sortOrder, that.sortOrder) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(planningUnitsId, that.planningUnitsId) &&
            Objects.equals(planningUnitGroupSetId, that.planningUnitGroupSetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        uid,
        code,
        name,
        sortOrder,
        isActive,
        planningUnitsId,
        planningUnitGroupSetId
        );
    }

    @Override
    public String toString() {
        return "PlanningUnitGroupCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (uid != null ? "uid=" + uid + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (sortOrder != null ? "sortOrder=" + sortOrder + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
                (planningUnitsId != null ? "planningUnitsId=" + planningUnitsId + ", " : "") +
                (planningUnitGroupSetId != null ? "planningUnitGroupSetId=" + planningUnitGroupSetId + ", " : "") +
            "}";
    }

}
