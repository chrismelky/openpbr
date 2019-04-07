package org.openpbr.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import org.openpbr.domain.enumeration.ValueType;

/**
 * A Attribute.
 */
@Entity
@Table(name = "attribute")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "attribute")
public class Attribute extends IdentifiableEntity implements Serializable {

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
    @Enumerated(EnumType.STRING)
    @Column(name = "value_type", nullable = false)
    private ValueType valueType;

    @NotNull
    @Column(name = "is_mandatory", nullable = false)
    private Boolean isMandatory;

    @NotNull
    @Column(name = "is_unique", nullable = false)
    private Boolean isUnique;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @ManyToOne
    @JsonIgnoreProperties("attributes")
    private OptionSet optionSet;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Attribute uid(String uid) {
        this.uid = uid;
        return this;
    }

    public Attribute code(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public Attribute name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public Attribute valueType(ValueType valueType) {
        this.valueType = valueType;
        return this;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    public Boolean isIsMandatory() {
        return isMandatory;
    }

    public Attribute isMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
        return this;
    }

    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public Boolean isIsUnique() {
        return isUnique;
    }

    public Attribute isUnique(Boolean isUnique) {
        this.isUnique = isUnique;
        return this;
    }

    public void setIsUnique(Boolean isUnique) {
        this.isUnique = isUnique;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public Attribute sortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public OptionSet getOptionSet() {
        return optionSet;
    }

    public Attribute optionSet(OptionSet optionSet) {
        this.optionSet = optionSet;
        return this;
    }

    public void setOptionSet(OptionSet optionSet) {
        this.optionSet = optionSet;
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
        Attribute attribute = (Attribute) o;
        if (attribute.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), attribute.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Attribute{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", valueType='" + getValueType() + "'" +
            ", isMandatory='" + isIsMandatory() + "'" +
            ", isUnique='" + isIsUnique() + "'" +
            ", sortOrder=" + getSortOrder() +
            "}";
    }
}
