package org.openpbr.service.dto;

import java.io.Serializable;
import java.util.Objects;
import org.openpbr.domain.enumeration.ValueType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Attribute entity. This class is used in AttributeResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /attributes?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AttributeCriteria implements Serializable {
    /**
     * Class for filtering ValueType
     */
    public static class ValueTypeFilter extends Filter<ValueType> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter uid;

    private StringFilter code;

    private StringFilter name;

    private ValueTypeFilter valueType;

    private BooleanFilter isMandatory;

    private BooleanFilter isUnique;

    private IntegerFilter sortOrder;

    private LongFilter optionSetId;

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

    public ValueTypeFilter getValueType() {
        return valueType;
    }

    public void setValueType(ValueTypeFilter valueType) {
        this.valueType = valueType;
    }

    public BooleanFilter getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(BooleanFilter isMandatory) {
        this.isMandatory = isMandatory;
    }

    public BooleanFilter getIsUnique() {
        return isUnique;
    }

    public void setIsUnique(BooleanFilter isUnique) {
        this.isUnique = isUnique;
    }

    public IntegerFilter getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(IntegerFilter sortOrder) {
        this.sortOrder = sortOrder;
    }

    public LongFilter getOptionSetId() {
        return optionSetId;
    }

    public void setOptionSetId(LongFilter optionSetId) {
        this.optionSetId = optionSetId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AttributeCriteria that = (AttributeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(uid, that.uid) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(valueType, that.valueType) &&
            Objects.equals(isMandatory, that.isMandatory) &&
            Objects.equals(isUnique, that.isUnique) &&
            Objects.equals(sortOrder, that.sortOrder) &&
            Objects.equals(optionSetId, that.optionSetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        uid,
        code,
        name,
        valueType,
        isMandatory,
        isUnique,
        sortOrder,
        optionSetId
        );
    }

    @Override
    public String toString() {
        return "AttributeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (uid != null ? "uid=" + uid + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (valueType != null ? "valueType=" + valueType + ", " : "") +
                (isMandatory != null ? "isMandatory=" + isMandatory + ", " : "") +
                (isUnique != null ? "isUnique=" + isUnique + ", " : "") +
                (sortOrder != null ? "sortOrder=" + sortOrder + ", " : "") +
                (optionSetId != null ? "optionSetId=" + optionSetId + ", " : "") +
            "}";
    }

}
