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
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the OrganisationUnit entity. This class is used in OrganisationUnitResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /organisation-units?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrganisationUnitCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter uid;

    private StringFilter code;

    private StringFilter name;

    private IntegerFilter level;

    private LocalDateFilter openingDate;

    private LocalDateFilter closedDate;

    private StringFilter url;

    private BigDecimalFilter latitude;

    private BigDecimalFilter longitude;

    private StringFilter address;

    private StringFilter email;

    private StringFilter phoneNumber;

    private IntegerFilter sortOrder;

    private BooleanFilter isActive;

    private LongFilter parentId;

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

    public IntegerFilter getLevel() {
        return level;
    }

    public void setLevel(IntegerFilter level) {
        this.level = level;
    }

    public LocalDateFilter getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDateFilter openingDate) {
        this.openingDate = openingDate;
    }

    public LocalDateFilter getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(LocalDateFilter closedDate) {
        this.closedDate = closedDate;
    }

    public StringFilter getUrl() {
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public BigDecimalFilter getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimalFilter latitude) {
        this.latitude = latitude;
    }

    public BigDecimalFilter getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimalFilter longitude) {
        this.longitude = longitude;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public LongFilter getParentId() {
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrganisationUnitCriteria that = (OrganisationUnitCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(uid, that.uid) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(level, that.level) &&
            Objects.equals(openingDate, that.openingDate) &&
            Objects.equals(closedDate, that.closedDate) &&
            Objects.equals(url, that.url) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(address, that.address) &&
            Objects.equals(email, that.email) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(sortOrder, that.sortOrder) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(parentId, that.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        uid,
        code,
        name,
        level,
        openingDate,
        closedDate,
        url,
        latitude,
        longitude,
        address,
        email,
        phoneNumber,
        sortOrder,
        isActive,
        parentId
        );
    }

    @Override
    public String toString() {
        return "OrganisationUnitCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (uid != null ? "uid=" + uid + ", " : "") +
                (code != null ? "code=" + code + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (level != null ? "level=" + level + ", " : "") +
                (openingDate != null ? "openingDate=" + openingDate + ", " : "") +
                (closedDate != null ? "closedDate=" + closedDate + ", " : "") +
                (url != null ? "url=" + url + ", " : "") +
                (latitude != null ? "latitude=" + latitude + ", " : "") +
                (longitude != null ? "longitude=" + longitude + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (phoneNumber != null ? "phoneNumner=" + phoneNumber + ", " : "") +
                (sortOrder != null ? "sortOrder=" + sortOrder + ", " : "") +
                (isActive != null ? "isActive=" + isActive + ", " : "") +
                (parentId != null ? "parentId=" + parentId + ", " : "") +
            "}";
    }

}
