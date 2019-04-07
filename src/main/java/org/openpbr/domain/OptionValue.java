package org.openpbr.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A OptionValue.
 */
@Entity
@Table(name = "option_value")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "optionvalue")
public class OptionValue extends IdentifiableEntity implements Serializable {

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

    @ManyToOne
    @JsonIgnoreProperties("optionValues")
    private OptionSet optionSet;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OptionValue uid(String uid) {
        this.uid = uid;
        return this;
    }


    public OptionValue code(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public OptionValue name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public OptionValue sortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public OptionSet getOptionSet() {
        return optionSet;
    }

    public OptionValue optionSet(OptionSet optionSet) {
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
        OptionValue optionValue = (OptionValue) o;
        if (optionValue.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), optionValue.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OptionValue{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", sortOrder=" + getSortOrder() +
            "}";
    }
}
