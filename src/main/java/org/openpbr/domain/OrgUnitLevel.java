package org.openpbr.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A OrgUnitLevel.
 */
@Entity
@Table(name = "org_unit_level")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "orgunitlevel")
public class OrgUnitLevel extends IdentifiableEntity implements Serializable {

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

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public OrgUnitLevel uid(String uid) {
        this.uid = uid;
        return this;
    }

    public OrgUnitLevel code(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public OrgUnitLevel name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public OrgUnitLevel level(Integer level) {
        this.level = level;
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
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
        OrgUnitLevel orgUnitLevel = (OrgUnitLevel) o;
        if (orgUnitLevel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orgUnitLevel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrgUnitLevel{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", level=" + getLevel() +
            "}";
    }
}
