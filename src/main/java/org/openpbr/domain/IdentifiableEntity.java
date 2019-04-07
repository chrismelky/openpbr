package org.openpbr.domain;

import org.openpbr.common.CodeGenerator;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@MappedSuperclass
public class IdentifiableEntity extends  AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 11, max = 11)
    @Column(name="uid", length = 11, unique = true, nullable = false)
    public String uid = CodeGenerator.generateUid();

    @Size(max = 50)
    @Column(name = "code", length = 50, unique = true)
    public String code;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
